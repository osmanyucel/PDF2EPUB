import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.stream.Collectors;

import javax.swing.*;

public class NoteCreator extends JFrame
{
	int id=1;
	public JPanel topPanel;
	public JPanel bottomPanel;

	public JSpinner spinID;
	public JList<Note> noteContent;
	public DefaultListModel<Note> noteModel;
	public JTextField input;
	public JTextField output;
	public JTextField fileName;
	public JButton notebutton;
	public JScrollPane noteScroll;

	public JTextField noteResult;
	public JScrollPane notesScroll;
	public JTextArea notesArea;

	public JButton btnRef;
	public JButton btnNote;
	public JButton btnDelete;
	public JButton btnFulResult;

	public NoteCreator(){
		//createResultPart();
		this.setLayout(new BorderLayout());
		createTopPanel();createBottomPanel();createCenterPanel();

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(1300,700);
		this.setResizable(false);
		this.setTitle("NoteCreator");
		this.setVisible(true);
	}

	private void createTopPanel()
	{
		topPanel = new JPanel(new BorderLayout());
		spinID = new JSpinner();
		spinID.setMinimumSize(new Dimension(250, 250));
		spinID.setValue(1);
		spinID.addChangeListener(e -> id = Integer.valueOf(spinID.getValue().toString()));

		noteModel = new DefaultListModel<Note>();
		noteContent = new JList<Note>(noteModel);
		noteContent.setSize(500, 500);
		noteContent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		noteContent.setLayoutOrientation(JList.VERTICAL);

		noteScroll = new JScrollPane(noteContent);
		noteScroll.setPreferredSize(new Dimension(250, 80));

		JPanel buttonsPane = new JPanel(new GridLayout(3,1));
		btnRef = new JButton("Referans");
		btnNote = new JButton("Not");
		btnDelete = new JButton("Sil");
		btnRef.addActionListener(e -> output.setText(noteModel.get(noteContent.getSelectedIndex()).getRef()));
		btnNote.addActionListener(e -> output.setText(noteModel.get(noteContent.getSelectedIndex()).getNote()));
		btnDelete.addActionListener(e -> noteModel.remove(noteContent.getSelectedIndex()));
		buttonsPane.add(btnRef);
		buttonsPane.add(btnNote);
		buttonsPane.add(btnDelete);

		this.topPanel.add(spinID,BorderLayout.WEST);
		this.topPanel.add(noteScroll,BorderLayout.CENTER);
		this.topPanel.add(buttonsPane,BorderLayout.EAST);

		this.getContentPane().add(topPanel,BorderLayout.NORTH);
	}
	private void createBottomPanel()
	{
		bottomPanel = new JPanel(new BorderLayout());
		JPanel labels = new JPanel(new GridLayout(3,1));
		labels.add(new Label("File Name"));
		labels.add(new Label("Input"));
		labels.add(new Label("Output"));

		JPanel fields = new JPanel(new GridLayout(3,1));
		fileName = new JTextField(100);
		input = new JTextField(100);
		output = new JTextField(100);
		fields.add(fileName);
		fields.add(input);
		fields.add(output);

		JPanel buttons = new JPanel(new GridLayout(3,1));
		btnFulResult = new JButton("Tum Notlar");
		btnFulResult.addActionListener(e -> notesArea.setText(Collections.list(noteModel.elements()).stream().map(Note::getNote).collect(Collectors.joining("\n"))));
		notebutton = new JButton("Ekle");
		notebutton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				noteModel.addElement(new Note(id,input.getText(),fileName.getText()));
				id++;
				spinID.setValue(id);
			}
		});
		buttons.add(notebutton);
		buttons.add(btnFulResult);


		bottomPanel.add(labels,BorderLayout.WEST);
		bottomPanel.add(fields,BorderLayout.CENTER);
		bottomPanel.add(buttons,BorderLayout.EAST);



		this.getContentPane().add(bottomPanel,BorderLayout.SOUTH);

	}


	private void createCenterPanel()
	{
		noteResult= new JTextField(100);
		this.getContentPane().add(noteResult);

		//		        new ActionListener()
//		{
//
//			@Override
//			public void actionPerformed(ActionEvent e)
//			{
//				String n = "";
//				for(int i =0;i<noteModel.size();i++)
//				{
//					n+= noteModel.getElementAt(i).getNote()+"\n";
//				}
//				System.out.println(n);
//				notesArea.setText(n);
//				;
//
//			}
//		});

		this.notesArea = new JTextArea("",200, 200);
		this.notesScroll = new JScrollPane(notesArea);
		notesScroll.setPreferredSize(new Dimension(300,300));
		notesArea.setPreferredSize(new Dimension(200,200));
		this.getContentPane().add(notesScroll);

	}


}
