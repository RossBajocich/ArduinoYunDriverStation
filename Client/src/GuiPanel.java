import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GuiPanel extends JPanel {
	int id;
	JButton connect;
	JTextField left, right, status;
	JPanel buttonPanel;

	public GuiPanel(final int id) {
		this.id = id;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 2));
		
		Color bg = this.getBackground();
		switch (id) {
		case 1:
			bg = Color.red;
			break;
		case 2:
			bg = Color.yellow;
			break;
		case 3:
			bg = Color.blue;
			break;
		case 4:
			bg = Color.lightGray;
			break;
		}
		this.setBackground(bg);

		connect = new JButton("Connect id " + id);
		connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ConnectionRunner.getRunner().requestSocket(id);
			}
		});
		connect.setPreferredSize(new Dimension(10, 50));
		connect.setBackground(bg);

		JButton registerController = new JButton("RegisterController " + id);
		registerController.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (GamePadManager.getManager().getGamePad(id) != null) {
					GamePadManager.getManager().getGamePad(id)
							.setController(GamePadManager.getManager().getNextAvailableController());
				}
			}
		});
		registerController.setPreferredSize(new Dimension(10, 50));

		JButton disable = new JButton("Disable Bot " + id);
		disable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (ConnectionRunner.getRunner().getSocket(id) != null) {
					ConnectionRunner.getRunner().getSocket(id).enable(false);
				}
			}
		});

		JButton enable = new JButton("Enable Bot " + id);
		enable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (ConnectionRunner.getRunner().getSocket(id) != null) {
					ConnectionRunner.getRunner().getSocket(id).enable(true);
				}
			}
		});

		enable.setPreferredSize(new Dimension(50, 30));
		disable.setPreferredSize(new Dimension(50, 30));

		left = new JTextField();
		left.setEditable(false);
		left.setText("left");
		right = new JTextField();
		right.setEditable(false);
		right.setText("right");

		status = new JTextField("test");
		status.setText("Disconnected");
		status.setPreferredSize(new Dimension(10, 15));
		status.setForeground(Color.red);
		status.setEditable(false);
		status.setFont(new Font(Font.SANS_SERIF, 50, 15));

		this.setVisible(true);
		buttonPanel.add(connect);
		buttonPanel.add(registerController);
		buttonPanel.add(enable);
		buttonPanel.add(disable);
		this.add(buttonPanel);
		this.add(left);
		this.add(right);
		this.add(status);

	}

	public void update() {
		if (GamePadManager.getManager().getGamePad(id) != null) {
			int formatLeft = GamePadManager.getManager().getGamePad(id).getNormalizedLeft();

			int formatRight = GamePadManager.getManager().getGamePad(id).getNormalizedRight();

			left.setText("" + formatLeft);
			right.setText("" + formatRight);
		} else {

		}
		if (ConnectionRunner.getRunner().getSocket(id) != null) {
			String temp = ConnectionRunner.getRunner().getSocket(id).getState();
			if (!temp.contains("Disconnected")) {
				status.setForeground(Color.green);
			} else {
				status.setForeground(Color.red);
			}
			if (status.getText() != temp) {
				status.setText(temp);
			}
		}
	}

	public void setLeftText(String txt) {
		left.setText(txt);
	}

	public void setRightText(String txt) {
		right.setText(txt);
	}
}
