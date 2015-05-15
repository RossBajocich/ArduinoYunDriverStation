import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SocketCreator implements Runnable {
	private List<Integer> requests = Collections.synchronizedList(new ArrayList<Integer>());
	private static SocketCreator creator = new SocketCreator();
	int index = 0;

	public static SocketCreator getSocketCreator() {
		return creator;
	}

	public void addSocketRequest(int id) {
		requests.add(id);
	}

	@Override
	public void run() {
		while (true) {
			if (index < requests.size() && !requests.isEmpty()) {
				int id = 0;
				synchronized (requests) {
					if (requests.size() > index) {
						id = requests.get(index);
					}
					index++;
				}
				System.out.println("Starting connection for id " + id);
				YunSocketConnection socket = new YunSocketConnection(ConnectionRunner.getIP(id),
						GamePadManager.getManager().getGamePad(id), id);
				if (socket.connect()) {
					ConnectionRunner.getRunner().addSocket(socket);
				} else {
					System.out.println("Connection failed for id " + id);
				}
			}
		}
	}
}
