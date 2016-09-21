#include <wiringPi.h>
#include <softPwm.h>

//#include <cstring>
#include <iostream>

#include <arpa/inet.h>
#include <netinet/in.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>

//start pwm at zero cycles
#define INITIAL_VALUE 0
#define PWM_RANGE 100

#define BUFLEN 3
//arbitrary, matches client
#define PORT 8888

void kill(char *msg){
	perror(msg);
	exit(1);
}

//measured on the "red robot", possibly need to find better solution
#define ZERO_VALUE -83
#define REVERSE_RANGE (-127 + ZERO_VALUE)
#define FORWARD_RANGE (127 - ZERO_VALUE)

//convert input -127 to 127 into a range of 0 to PWM_RANGE for PWM
//accounts for motor controller zeroing with ZERO_VALUE define above
int normalize(int input){
  //Talon motor controller only starts to reverse at -87 input 
  //which is scaled to a 0 to 100 resulting in approximately 17
  //simply maps piecewise, could be one line but this seems easier to understand.
  if(input < 0){
    input = ZERO_VALUE - (input / 127.0) * REVERSE_RANGE;
  }else{
    input = ZERO_VALUE + (input / 127.0) * FORWARD_RANGE;
  }
  
  //starting map out of negative space into 0to100
  int output = input + 127;
  output = (double)output * ((double)PWM_RANGE)/(127.0*2.0);
  
  //motor controller stops when PWM is exactly equal to zero or full(PWM_RANGE)
  //this is not desireable when 0 needs to reverse the motor not stop
  if(output >= PWM_RANGE)
     output = PWM_RANGE - 1;
  if(output <= 0)
     output = 1;
  
  return output;
}

int main(){
	//two arbitrary GPIO pins on raspberrypi
	//software driven PWM so no need for special pin
	const int left_pin = 18;
	const int right_pin = 17;

	//initialize wiringpi and setup pins for pwm output
	wiringPiSetupGpio();
	softPwmCreate(left_pin, INITIAL_VALUE, PWM_RANGE);
	softPwmCreate(right_pin, INITIAL_VALUE, PWM_RANGE);
	
	struct sockaddr_in si_me;
	struct sockaddr_in si_other;
	int s;
	int slen = sizeof(si_other);
	char buf[BUFLEN];

	if((s=socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)) == -1){
		kill("socket_initialization");
	}
	
	//for testing purposes server has to be killed and reran many times
	//was running into bind issues with addresses already being used
	//this fix could be resolved with client/server handshake but
	//data being transfered is so simple there is no need for handshaking
	int temp = 1;
	//allow bind to address that is already being used (by previous run of server)
	if(setsockopt(s, SOL_SOCKET, SO_REUSEADDR, &temp, sizeof(int)) < 0){
	  kill("setSockOpt");
	}

	memset((char *) &si_me, 0, sizeof(si_me));
	si_me.sin_family = AF_INET;
	si_me.sin_port = htons(PORT);
	si_me.sin_addr.s_addr = htonl(INADDR_ANY);
	if(bind(s, (struct sockaddr *) &si_me, sizeof(si_me))==-1){
		kill("bind");
	}
	
	printf("Entering reading state\n");
	unsigned failedCount = 0;
	
	bool enabled = false;
	
	//don't reallocate every iteration
	int leftPWM = 0;
	int rightPWM = 0;
	
	char state = 'd';
	//values incoming from -127 to 127
	signed char left = 0;
	signed char right = 0;
	
	//run forever, no quit support yet
	while(1){
		if(recvfrom(s, buf, BUFLEN, 0, (struct sockaddr *) &si_other, (socklen_t *) &slen)==-1){
		  failedCount++;
		  if(failedCount > 10){
		    kill("recvFromFailed");
		  }
		}
		state = buf[0];
		left = buf[1];
		right = buf[2];
		
		//initial byte represents robot state
		if(state == 'e' || state == 'm'){
		  enabled = true;
		}else if(state == 'd'){
		  enabled = false;
		}
		
		//map -127 to 127 to 0 to 255 then create ratio from interval and map to 0 to 100
		leftPWM = normalize(left);
		rightPWM = normalize(right);

		//debug printing
		//printf("state %c left %d right %d leftPWM %d rightPWM %d\n",state, left, right, leftPWM, rightPWM);
		
		//finally write out the pwm signal from second and third bytes in packet
		if(enabled){
		  softPwmWrite(left_pin, leftPWM);
		  softPwmWrite(right_pin, rightPWM);
		  printf("left %d, right %d", leftPWM, rightPWM);
		}else{
		  softPwmWrite(left_pin, INITIAL_VALUE);
		  softPwmWrite(right_pin, INITIAL_VALUE);
		}
	}

	return 0;
}
