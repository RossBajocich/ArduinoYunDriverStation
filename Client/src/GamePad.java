import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;

public class GamePad {
	double left, right;
	int id;
	Controller controller = null;

	public GamePad(int id) {
		this.id = id;
	}

	public void poll() {
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
						left = (double) -axisValue;
						// System.out.println("left: " + axisValue);
						continue; // Go to next component.
					}

					if (componentIdentifier == Component.Identifier.Axis.RZ) {
						right = (double) -axisValue;
						// System.out.println("right: " + axisValue);
						continue; // Go to next component.
					}
				}
			}
		}
	}
	
	public byte getNormalizedLeft() {
		return normalize(left);
	}

	public byte getNormalizedRight() {
		return normalize(right);
	}

	private byte normalize(double value) {
		//convert -1 to 1 to Byte.MIN_VALUE to Byte.MAX_VALUE
		byte t = (byte) (value * 127);

		return t;
	}

	public int getID() {
		return id;
	}

	public void setController(Controller c) {
		this.controller = c;
		System.out.println("controller set to " + c);
	}
}
