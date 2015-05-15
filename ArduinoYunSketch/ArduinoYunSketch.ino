#include <Servo.h>
#include <YunServer.h>
#include <YunClient.h>

YunServer server;
Servo leftMotor, rightMotor;
bool enabled = false;

void setup() {
  Bridge.begin();
  //Console.begin();

  // Listen for incoming connection only from localhost
  // (no one from the external network could connect)
  //server.listenOnLocalhost();
  //server.begin();
}

void loop() {
  // Get clients coming from server
  //YunClient client = server.accept();

  char buff[256];

  Bridge.get("data", buff, 256);

  if (buff[0] == 'E') {
//    if (Console) {
//      Console.println("Enabled!");
//    }
    enable();
  } else if (buff[0] == 'D') {
//    if (Console) {
//      Console.println("Disabled!");
      disable();
//    }
  } else {
    if (enabled) {
      telePeriodic(buff);
    }
  }

  //  // There is a new client?
  //  if (client) {
  //    // Process request
  //    //process(client);
  //    client.stop();
  //  }

  delay(10); // Poll every 50ms
}

void enable() {
  if (!enabled) {
    leftMotor.attach(9);
    rightMotor.attach(10);
    enabled = true;
  } 
}

void disable() {
  if (enabled) {
    leftMotor.write(90);
    rightMotor.write(90);
    leftMotor.detach();
    rightMotor.detach();
    enabled = false;
  }
}

void telePeriodic(char buff[1024]) {
  int right = 100 * (buff[3] - '0') + 10 * (buff[4] - '0') + (buff[5] - '0');
  int left = 100 * (buff[0] - '0') + 10 * (buff[1] - '0') + (buff[2] - '0');
/*  if (Console) {
    Console.print("rightNum: ");
    Console.print(right);
    Console.print("\tleftNum: ");
    Console.print(left);
    Console.println();
  }*/

  if (left > -1) {
    leftMotor.write(left);
    rightMotor.write(right);
  }
}

void process(YunClient client) {
  // read the command
  String command = client.readStringUntil('/');

  if (command == "stop") {
    leftMotor.write(90);
    rightMotor.write(90);
  } else if (command == "forward") {
    leftMotor.write(150);
    rightMotor.write(150);
  }
}
