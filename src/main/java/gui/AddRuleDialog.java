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
public class AddRuleDialog extends JDialog implements ActionListener {

	JLabel jlm;
	JTextField jtm;
	JLabel jlf;
	JTextField jtf;
	JLabel jlt;
	JTextField jtt;
	public AddRuleDialog(JFrame parent, String title, String message) {
		super(parent, title, true);
		if (parent != null) {
			Dimension parentSize = parent.getSize(); 
			Point p = parent.getLocation(); 
			setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
		}
		JPanel messagePane = new JPanel(new GridLayout(3,2));
		getContentPane().add(messagePane);
		if(message.contains("===>"))
		{
			String m = message.split("===>")[0];
			String c = message.split("===>")[1];
			jlm = new JLabel("Line");
			jtm = new JTextField(m,30);
			jlf = new JLabel("From");
			jtf = new JTextField(c,30);
			jlt = new JLabel("To");
			jtt = new JTextField(30);
		}
		else
		{
			jlm = new JLabel("Line");
			jtm = new JTextField(message,30);
			jlf = new JLabel("From");
			jtf = new JTextField(30);
			jlt = new JLabel("To");
			jtt = new JTextField(30);
		}
		messagePane.add(jlm);
		messagePane.add(jtm);
		messagePane.add(jlf);
		messagePane.add(jtf);
		messagePane.add(jlt);
		messagePane.add(jtt);


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
		Data.rules.add(new Rule(jtf.getText(), jtt.getText()));
		setVisible(false); 
		dispose(); 
	}
}