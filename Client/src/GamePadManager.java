import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Component.Identifier;

public class GamePadManager implements Runnable {
	List<Controller> found = new ArrayList<Controller>();
	List<GamePad> gamePads = new ArrayList<GamePad>();
	AtomicBoolean stop = new AtomicBoolean();
	static GamePadManager manager;
	boolean oneController = true;

	public GamePadManager() {
		searchForControllers();
		stop.set(false);
		if (!oneController) {
			for (int i = 0; i < found.size(); i++) {
				GamePad g = new GamePad(i + 1);
				gamePads.add(g);
			}
		} else {
			for (int i = 0; i < 4; i++) {
				GamePad g = new GamePad(i + 1);
				gamePads.add(g);
			}
		}
	}

	private void searchForControllers() {
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

		for (int i = 0; i < controllers.length; i++) {
			Controller controller = controllers[i];

			if (controller.getType() == Controller.Type.STICK || controller.getType() == Controller.Type.GAMEPAD
					|| controller.getType() == Controller.Type.WHEEL
					|| controller.getType() == Controller.Type.FINGERSTICK) {
				// Add new controller to the list of all controllers.
				found.add(controller);
			}
		}
	}

	List<Controller> getFoundDevices() {
		return found;
	}

	public static GamePadManager getManager() {
		if (manager == null) {
			manager = new GamePadManager();
		}
		return manager;
	}

	public GamePad getGamePad(int id) {
		for (GamePad g : gamePads) {
			if (g.getID() == id) {
				return g;
			}
		}
		return null;
	}

	public void stop(boolean state) {
		stop.set(state);
	}

	@Override
	public void run() {
		while (!stop.get()) {
			for (GamePad g : gamePads) {
				g.poll();
				// System.out.println("polled " + g.getID());
			}
			// System.out.println("size gamePads " + gamePads.size());
			// TODO: change this value
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Pair viewNextPair(int id) {
		if (getGamePad(id) != null) {
			return getGamePad(id).viewNextPair();
		}
		return new Pair(-420.0, -420.0);
	}

	public Pair getNextPair(int id) {
		return getGamePad(id).getNextPair();
	}

	public Controller getNextAvailableController() {
		for (int x = 0; x < 4; x++) {
			for (Controller c : found) {
				c.poll();
				Component[] components = c.getComponents();
				for (int i = 0; i < components.length; i++) {
					Component component = components[i];
					Identifier componentIdentifier = component.getIdentifier();

					// Axes
					if (!component.isAnalog()) {
						if (componentIdentifier == Component.Identifier.Button.A
								|| componentIdentifier == Component.Identifier.Button.B
								|| componentIdentifier == Component.Identifier.Button._1
								|| componentIdentifier == Component.Identifier.Button.X
								|| componentIdentifier == Component.Identifier.Button.Y) {
							if(component.getPollData() != 0.0f){
								return c;
							}
						}
					}
				}
			}
		}

		return null;
	}

}
