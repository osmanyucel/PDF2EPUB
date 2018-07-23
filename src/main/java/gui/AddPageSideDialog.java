package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import engine.Data;
import engine.Rule;
public class AddPageSideDialog extends JDialog implements ActionListener {

	JLabel sideLabel;
	JTextField sideField;
	public AddPageSideDialog(JFrame parent, String title, String message) {
		super(parent, title, true);
		if (parent != null) {
			Dimension parentSize = parent.getSize(); 
			Point p = parent.getLocation(); 
			setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
		}
		JPanel messagePane = new JPanel(new GridLayout(3,2));
		getContentPane().add(messagePane);

			sideLabel = new JLabel("Line");
			sideField = new JTextField(message,30);

		messagePane.add(sideLabel);
		messagePane.add(sideField);
		sideField.setText(message);


		JPanel buttonPane = new JPanel();
		JButton button = new JButton("Add"); 
		buttonPane.add(button); 
		button.addActionListener(this);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack(); 
		setVisible(true);
	}

	public static JFrame generateFrame()
	{
		return new JFrame();
	}
	public void actionPerformed(ActionEvent e) {
		Data.pageSide.add(sideField.getText());
		setVisible(false); 
		dispose(); 
	}
}