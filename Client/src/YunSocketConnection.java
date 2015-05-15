import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DecimalFormat;

public class YunSocketConnection {
	Socket socket;
	OutputStream out;
	String ip = "";
	private static final int PORT = 8888;
	GamePad gamePad;
	int id;
	private boolean connected, enabled = false;
	String status;

	public YunSocketConnection(String ip, GamePad gamePad, int id) {
		this.ip = ip;
		this.gamePad = gamePad;
		this.id = id;
	}

	public boolean connect() {
		if (socket == null || !socket.isConnected()) {
			try {
				socket = new Socket(ip, PORT);
				out = socket.getOutputStream();
				enable(true);
				out.flush();
				connected = true;
				return true;
			} catch (IOException e) {
				// e.printStackTrace();
				System.out.println("Connect failed! for id " + id + " at ip " + ip + ":" + PORT);
				connected = false;
				return false;
			}
		} else {
			return true;
		}
	}

	public void loop() {
		if (connected && socket.isConnected() && enabled) {
			DecimalFormat df = new DecimalFormat("###");
			String formatLeft = String.format("%03d", GamePadManager.getManager().getGamePad(id).getNormalizedLeft());
			String formatRight = String.format("%03d", GamePadManager.getManager().getGamePad(id).getNormalizedRight());

			String current = formatLeft + formatRight + "\n";
			try {
				System.out.println("sending " + current + " to " + ip + ":" + PORT);
				out.write(current.getBytes());
				out.flush();
			} catch (IOException e) {
				connected = false;
				e.printStackTrace();
			}
		} else {
			/*
			 * System.out.print("connected: " + connected); if (socket != null)
			 * { System.out.print(" socket connected: " + socket.isConnected() +
			 * "\n"); } else { System.out.print("&& socket is null\n"); }
			 */
		}
	}

	public void onClose() {
		enable(false);
		enable(false);
		enable(false);
		if (socket != null) {
			try {
				socket.shutdownOutput();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int getID() {
		return id;
	}
	
	public String getState(){
		if(connected){
			status = "Connected ";
		}else{
			status = "Disconnected ";
		}
		if(enabled){
			status += " Enabled";
		}else{
			status += " Disabled";
		}
		return status;
	}

	public void enable(boolean state) {
		enabled = state;
		try {
			String val = "";
			if (state) {
				val = "E";
			} else {
				val = "D";
			}
			if (socket != null && socket.isConnected()) {
				for (int i = 0; i < 3; i++) {
					out.write(val.getBytes());
					out.flush();
				}
			}
			System.out.println("sending " + val + " to " + ip + ":" + PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
