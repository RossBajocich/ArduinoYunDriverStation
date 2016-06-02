import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Window extends JFrame /*implements KeyListener*/ {
	static Window window;
	GuiPanel[] gps = new GuiPanel[4];
	JFrame devicesConnected;
	GamePadManager manager = GamePadManager.getManager();
	Thread networkThread, gamePadThread, socketCreatorThread;
	
//	boolean isEnabled = false;

	public Window(String title) {
		super(title);
		setup();
		startThreads();
	}

	private void setup() {
		this.setSize(500, 500);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(2, 2));
		for (int i = 0; i < 4; i++) {
			gps[i] = new GuiPanel(i + 1);
			this.add(gps[i]);
		}

		JFrame controlFrame = new JFrame("All Bot Control");
		controlFrame.setSize(300, 150);
		controlFrame.setLayout(new GridLayout(1, 2));
		JButton disableAll = new JButton("Disable ALL");
		disableAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ConnectionRunner.getRunner().DisableAll();
			}
		});
		JButton enableAll = new JButton("Enable ALL");
		enableAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ConnectionRunner.getRunner().EnableAll();
			}
		});
		enableAll.setBackground(Color.green);
		disableAll.setBackground(Color.red);

		controlFrame.add(enableAll);
		controlFrame.add(disableAll);
		controlFrame.setVisible(true);

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (ConnectionRunner.getRunner().onClose()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.exit(0);
			}
		});

		this.setVisible(true);
	}

	private void updatePanels() {
		for (GuiPanel gp : gps) {
			gp.update();
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void startThreads() {
		networkThread = new Thread(ConnectionRunner.getRunner());
		networkThread.setPriority(Thread.MAX_PRIORITY);
		networkThread.start();
		gamePadThread = new Thread(GamePadManager.getManager());
		gamePadThread.setPriority(Thread.MAX_PRIORITY);
		gamePadThread.start();
	}

	public static Window getWindow() {
		return window;
	}

	public static void main(String args[]) {
		window = new Window("ArduinoYun DriverStation");
		while (true) {
			window.updatePanels();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}