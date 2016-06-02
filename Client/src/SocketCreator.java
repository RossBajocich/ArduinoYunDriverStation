public class SocketCreator {
	
	public static void addSocketRequest(int id) {		
		System.out.println("Starting connection for id " + id);
		YunSocketConnection socket = new YunSocketConnection(ConnectionRunner.getIP(id),
				GamePadManager.getManager().getGamePad(id), id);
		if (socket.connect()) {
			ConnectionRunner.getRunner().addSocket(socket);
			System.out.println("Connection(" + socket.toString() + " added for id " + id);
		} else {
			System.out.println("Connection failed for id " + id);
		}
	}

}
