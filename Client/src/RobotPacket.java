import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public class RobotPacket {

	public static enum PacketType {
		MOVE, ENABLE, DISABLE
	}

	final byte BUFFER_LENGTH = 3;

	PacketType type;
	String ip;
	int port;
	byte[] buffer = new byte[BUFFER_LENGTH];

	RobotPacket(PacketType type, String ip, int port){
		this.type = type;
		this.ip = ip;
		this.port = port;
		switch(type){
		case MOVE:
			buffer[0] = 'm';
			break;
		case ENABLE:
			buffer[0] = 'e';
			break;
		case DISABLE:
			buffer[0] = 'd';
			break;
		}
	}

	RobotPacket(byte first, byte second, String ip, int port) {
		this.type = PacketType.MOVE;
		this.ip = ip;
		this.port = port;
		buffer[0] = 'm';
		setData(first, second);
	}

	// sets data into the buffer after the state byte
	public void setData(byte first, byte second) {
		// eventually make this dynamic possibly
		buffer[1] = first;
		buffer[2] = second;
	}

	DatagramPacket getPacket() {
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, new InetSocketAddress(ip, port));

		return packet;
	}

}
