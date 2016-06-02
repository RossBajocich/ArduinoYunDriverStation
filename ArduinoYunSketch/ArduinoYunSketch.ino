#include <Servo.h>
#include <Bridge.h>
#include <YunServer.h>
#include <YunClient.h>

Servo leftMotor;
Servo rightMotor;
bool enabled = false;
char buff[3];

YunServer server;

void setup() {
  Bridge.begin();
}

void loop() {

  Bridge.get("data", buff, 3);

  if (buff[0] == 'e' || buff[0] == 'm') {
    enable();
  } else if (buff[0] == 'd') {
      disable();
  } else {
    if (enabled) {
      telePeriodic(buff);
    }
  }

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

void telePeriodic(char buff[3]) {
  int left = (((double)buff[1] / 127.0) * 90.0) + 90.0;
  int right = (((double)buff[2] / 127.0) * 90.0) + 90.0;

  leftMotor.write(left);
  rightMotor.write(right);
}
