import java.util.concurrent.ArrayBlockingQueue;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;

public class GamePad {
	double left, right;
	ArrayBlockingQueue<Pair> data = new ArrayBlockingQueue<Pair>(100);
	int id;
	Controller controller = null;

	public GamePad(int id) {
		this.id = id;
	} // ayy lmao

	public void poll() {
		if (controller == null) {
			//controller = GamePadManager.getManager().getNextAvailableController();
		}
		if (controller != null) {
			// Go trough all components of the controller.
			controller.poll();
			Component[] components = controller.getComponents();
			for (int i = 0; i < components.length; i++) {
				Component component = components[i];
				Identifier componentIdentifier = component.getIdentifier();

				// Axes
				if (component.isAnalog()) {
					float axisValue = component.getPollData();
					// Y axis
					if (componentIdentifier == Component.Identifier.Axis.Y) {
						left = (double) axisValue;
						// System.out.println("left: " + axisValue);
						continue; // Go to next component.
					}

					if (componentIdentifier == Component.Identifier.Axis.RZ) {
						right = (double) axisValue;
						// System.out.println("right: " + axisValue);
						continue; // Go to next component.
					}
				}
			}
			// data.offer(new Pair(left, right));
			// System.out.println("offered Left:" + data.peek().getLeft() + "
			// right " + data.peek().getRight());
		}
	}
	
	public int getNormalizedLeft() {
		return normalize(left);
	}

	public int getNormalizedRight() {
		return normalize(right);
	}

	private int normalize(double value) {
		value++;
		double temp = 2 - value;
		int val = (int) (temp * 90);

		return val;
	}

	public Pair getNextPair() {
		return data.poll();
	}

	public int getID() {
		return id;
	}

	public Pair viewNextPair() {
		return data.peek();
	}

	public void setController(Controller c) {
		this.controller = c;
		System.out.println("controller set to " + c);
	}
}
