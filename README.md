# ArduinoYunDriverStation
Driver Station built for a senior project involving four robots each with an ArdunioYun which controlled two Talon motor controllers.

Due to time constraints (written in less than a week), the program is not dynamic for more than four robots, more info in packets than simply enable, disable and motor values. In the future, I hope to make this project into one that could handle a simple game similar to the FCS system in FIRST Tech Challenge.

There are three parts to this package, the client- a jar that should be able to be run on any computer, the server- a very simple udp socket server existing on the linino side of the ArduinoYun, and the actual arduino sketch which parses and passes the motor values to the servo objects in addition to having enable and disable functionality.
