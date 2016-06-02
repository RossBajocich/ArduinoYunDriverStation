import java.io.IOException;
import java.net.DatagramSocket;

public class YunSocketConnection {
	DatagramSocket socket;
	String ip = "";
	private static final int PORT = 8888;
	GamePad gamePad;
	int id;
	private boolean connected;
	private boolean enabled = false;
	String status;

	public YunSocketConnection(String ip, GamePad gamePad, int id) {
		this.ip = ip;
		this.gamePad = gamePad;
		this.id = id;
	}

	public boolean connect() {
		if (socket == null || !socket.isConnected()) {
			try {
				socket = new DatagramSocket();
				connected = true;
				
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Connect failed! for id " + id + " at ip " + ip + ":" + PORT);
				connected = false;
				return false;
			}
		} else {
			return true;
		}
	}

	public void loop() {
		if (connected && enabled) {	
			GamePad gamepad = GamePadManager.getManager().getGamePad(id);

			byte left = gamepad.getNormalizedLeft();
			byte right = gamepad.getNormalizedRight();
			
			RobotPacket packet = new RobotPacket(left, right, ip, PORT);
			
			System.out.println("sending " + left + " " + right);
			
			try {
				socket.send(packet.getPacket());
			} catch (IOException e) {
				connected = false;
				e.printStackTrace();
			}
		}
	}

	public void onClose() {
		enable(false);
		
		if (socket != null) {
			socket.close();
		}
	}

	public int getID() {
		return id;
	}
	
	public String getState(){
		//resets status string to connected or disconnected
		if(connected){
			status = "Connected ";
		}else{
			status = "Disconnected ";
		}
		
		//appends enabled/disabled for guipanel textbox
		if(enabled){
			status += " Enabled";
		}else{
			status += " Disabled";
		}
		return status;
	}

	public void enable(boolean state) {
		enabled = state;
		RobotPacket.PacketType type = state ? RobotPacket.PacketType.ENABLE : RobotPacket.PacketType.DISABLE;
		
		RobotPacket packet = new RobotPacket(type, ip, PORT);
		
		try {
			socket.send(packet.getPacket());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
