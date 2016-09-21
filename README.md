# ArduinoYunDriverStation
Driver Station built for a teammate's senior project involving four robots each with a raspberry pi 2 or 3 which controls two Talon motor controllers.

There are three parts to this package, the client- a runnable jar that should be able to be run on any computer, the server- a very simple udp socket server existing on the Wi-Fi enabled raspberry pi which handles all motor control and network communication.

Due to time constraints (written in less than a week), the program is not dynamic for more than four robots, more info in packets than simply enable, disable and motor values. In the future, I hope to make this project into one that could handle a simple game similar to the FCS system in FIRST Tech Challenge.
