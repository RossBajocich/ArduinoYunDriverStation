import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionRunner implements Runnable {

	List<YunSocketConnection> sockets = new ArrayList<YunSocketConnection>();
	AtomicBoolean stop = new AtomicBoolean();
	static ConnectionRunner runner;

	public ConnectionRunner() {
		stop.set(false);
	}

	public void stop(boolean state) {
		if (state) {
			stop.set(true);
		} else {
			stop.set(false);
		}
	}

	@Override
	public void run() {
		while (!stop.get()) {
			for (YunSocketConnection ys : sockets) {
				ys.loop();
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean onClose() {
		if (sockets.isEmpty()) {
			return false;
		}
		for (YunSocketConnection ys : sockets) {
			ys.onClose();
		}
		return true;
	}

	public static ConnectionRunner getRunner() {
		if (runner == null) {
			runner = new ConnectionRunner();
		}
		return runner;
	}

	public YunSocketConnection getSocket(int id) {
		for (YunSocketConnection y : sockets) {
			if (y.getID() == id) {
				return y;
			}
		}
		return null;
	}

	protected void addSocket(YunSocketConnection socket) {
		this.sockets.add(socket);
	}

	public void requestSocket(int id) {
		if (getSocket(id) == null) {
			System.out.println("Socket ID: " + id + " is null! Socket has not been created, creating now...");

			SocketCreator.addSocketRequest(id);
		}
	}

	public static String getIP(int id) {
		switch (id) {
		case 1:
			return "192.168.0.121";
		case 2:
			return "192.168.0.122";
		case 3:
			return "192.168.0.123";
		case 4:
			return "192.168.0.124";
		}

		return "";
	}

	public void EnableAll() {
		for (int i = 0; i < 4; i++) {
			for (YunSocketConnection y : sockets) {
				y.enable(true);
			}
		}
	}

	public void DisableAll() {
		for (int i = 0; i < 2; i++) {
			for (YunSocketConnection y : sockets) {
				y.enable(false);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
