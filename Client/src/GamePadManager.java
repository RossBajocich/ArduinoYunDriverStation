import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

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

			System.out.println(controller.getType().toString());

			if (controller.getType() == Controller.Type.STICK || controller.getType() == Controller.Type.GAMEPAD) {
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
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Controller getNextAvailableController() {
		for (Controller c : found) {
			System.out.println("Controller " + c.getName());
			c.poll();
			Component[] components = c.getComponents();
			for (int i = 0; i < components.length; i++) {
				Component component = components[i];

				if (!component.isAnalog() && component.getPollData() != 0.0f) {
					if (isRegisterButtonPressed(component.getIdentifier())) {
						return c;
					}
				}
			}
		}

		return null;
	}

	static boolean isRegisterButtonPressed(Identifier comp) {
		System.out.println("component pressed: " + comp.toString());
		
		boolean valid = comp == Component.Identifier.Button.A;

		valid |= comp == Component.Identifier.Button.B;

		valid |= comp == Component.Identifier.Button._1;

		valid |= comp == Component.Identifier.Button._2;

		valid |= comp == Component.Identifier.Button.X;

		valid |= comp == Component.Identifier.Button.Y;

		return valid;
	}

}
