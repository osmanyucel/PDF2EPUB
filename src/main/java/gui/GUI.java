package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import engine.Data;
import engine.Functions;
import engine.Rule;

public class GUI extends JFrame {
	public JPanel pdfPanel;
	public JLabel pdfName;
	public JButton pdfLoad;
	public JButton pdfRead;

	public JMenuBar menu;

	public JPanel rulePanel;

	public JList<Rule> ruleList;
	public JButton ruleApply;
	public JButton ruleAdd;
	public JButton ruleRemove;
	public JButton rulePredict;
	public JScrollPane rulelistScroller;
	public JButton pageSideAdd;

	public JList<String> bookContentArea;
	public JPanel bookContentPane;
	public JScrollPane bookScrollPane;
	private DefaultListModel<String> listModel;
	public JList<String> bookContentRemoveArea;
	public JScrollPane bookRemoveScrollPane;
	private DefaultListModel<String> removeListModel;
	public JList<String> bookContentHeaderArea;
	public JScrollPane bookHeaderScrollPane;
	private DefaultListModel<String> headerListModel;

	public JButton remPageNumButton;
	public JButton remEmptyButton;
	public JButton remShortButton;
	public JButton remSelectButton;
	public JButton remUnSelectButton;
	public JButton remAllMarkedButton;

	public JButton headSelectButton;
	public JButton headUnSelectButton;
	public JButton headPredictButton;
	public JButton headToRemoveButton;
	public JButton addSeperatorButton;

	public JButton saveResultButton;
	public JCheckBox isPoemBook;

	public GUI() throws Exception {
		Functions.initialize();
		generateMenu();
		generatePDFpart();
		generateRulesPart();
		generateBookContentPart();
		this.setLayout(new BorderLayout());
		this.getContentPane().add(pdfPanel);
		this.getContentPane().add(rulePanel, BorderLayout.NORTH);
		this.getContentPane().add(bookContentPane, BorderLayout.CENTER);
		this.getContentPane().add(pdfName, BorderLayout.SOUTH);
		this.setJMenuBar(menu);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(1200, 800);
		this.setResizable(false);
		this.setTitle("PDF to EPub Converter");
		this.setVisible(true);
	}

	private void generateMenu() {
		menu = new JMenuBar();
		JMenu m1 = new JMenu("File");
		menu.add(m1);
		final JMenuItem load = new JMenuItem(Strings.BROWSE);
		final JMenuItem read = new JMenuItem(Strings.READ_PDF);
		m1.add(load);
		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				File f = new File("Z:\\Book\\ATDB");
				fc.setCurrentDirectory(f);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF or TXT Files", "pdf", "txt");
				fc.setFileFilter(filter);
				int returnVal = fc.showOpenDialog(GUI.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					Data.pdfAddress = file.getAbsolutePath();
					Data.isFileSelected = true;
					read.setEnabled(Data.isFileSelected);

				}
				if (Data.isFileSelected) {
					String txt = Data.pdfAddress.substring(Data.pdfAddress.lastIndexOf("\\") + 1);
					pdfName.setText(txt);
				}
			}
		});
		m1.add(read);
		read.setEnabled(false);
		read.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (Data.pdfAddress.endsWith(".pdf"))
						Data.lines = Functions.readLinesFromPDF(Data.pdfAddress);
					if (Data.pdfAddress.endsWith(".txt"))
						Data.lines = Functions.readLinesFromTXT(Data.pdfAddress);

					Data.headerIndexes = new Vector<Integer>();
					Data.removeIndexes = new Vector<Integer>();
					refreshTextArea();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});

		JMenu s = new JMenu("Save");
		JMenuItem s1 = new JMenuItem("Regular Book");
		JMenuItem s2 = new JMenuItem("Poem Book");
		s.add(s1);
		s.add(s2);
		m1.add(s);

		s1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Data.SIIR = false;
				File f = new File("Z:\\Book\\Ready");
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(f);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(GUI.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					Data.resultAddress = file.getAbsolutePath();
					Data.isFolderSelected = true;

				}
				if (Data.isFileSelected) {
					String folderName = Data.pdfAddress.substring(Data.pdfAddress.lastIndexOf('/'),
							Data.pdfAddress.length() - 4);
					folderName = folderName.replaceAll(" ", "");
					Functions.saveResults(Data.resultAddress + folderName);
				}
				Data.isFolderSelected = false;

			}
		});

		s2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Data.SIIR = true;
				File f = new File("Z:\\Book\\Ready");
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(f);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(GUI.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					Data.resultAddress = file.getAbsolutePath();
					System.out.println(Data.resultAddress);
					Data.isFolderSelected = true;

				}
				if (Data.isFileSelected) {
					String folderName = Data.pdfAddress.substring(Data.pdfAddress.lastIndexOf('\\'),
							Data.pdfAddress.length() - 4);
					folderName = folderName.replaceAll(" ", "");
					Functions.saveResults(Data.resultAddress + folderName);
				}
				Data.isFolderSelected = false;
				Data.SIIR = false;
			}
		});

	}

	private void generateBookContentPart() {
		bookContentPane = new JPanel(new GridLayout(1, 5));
		bookContentPane.setPreferredSize(new Dimension(1000, 800));
		removeListModel = new DefaultListModel<String>();
		bookContentRemoveArea = new JList<String>(removeListModel);

		bookRemoveScrollPane = new JScrollPane(bookContentRemoveArea);
		bookContentPane.add(bookRemoveScrollPane);

		JPanel remPane = new JPanel(new GridLayout(6, 1));
		remPageNumButton = new JButton(Strings.REMOVE_NUMBERS);
		remEmptyButton = new JButton(Strings.REMOVE_EMPTY);
		remAllMarkedButton = new JButton(Strings.REMOVE_MARKED);
		remSelectButton = new JButton(Strings.REMOVE_TOREMOVE);
		remUnSelectButton = new JButton(Strings.REMOVE_NOTTOREMOVE);
		remShortButton = new JButton(Strings.REMOVE_SHORT);

		remShortButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < Data.lines.size(); i++) {
					String s = Data.lines.get(i);
					if (s.length() < 5) {
						Data.removeIndexes.add(i);
					} else if (Functions.isWeirdLines(s)) {
						Data.removeIndexes.add(i);
					}
				}
				Collections.sort(Data.removeIndexes);
				refreshTextArea();
			}
		});
		remPageNumButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < Data.lines.size(); i++) {
					String s = Data.lines.get(i);
					if (Functions.isPageNumber(s)) {
						Data.removeIndexes.add(i);
					}
				}
				Collections.sort(Data.removeIndexes);
				refreshTextArea();
			}
		});

		remEmptyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < Data.lines.size(); i++) {
					String s = Data.lines.get(i);
					if (Functions.isEmptyLine(s)) {
						Data.removeIndexes.add(i);
					}
				}
				Collections.sort(Data.removeIndexes);
				refreshTextArea();
			}
		});

		remSelectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] sels = bookContentArea.getSelectedIndices();
				for (int i : sels) {
					if (Data.removeIndexes.contains(i))
						continue;
					Data.removeIndexes.add(i);
					System.out.println(Data.lines.get(i));
				}
				Collections.sort(Data.removeIndexes);
				refreshTextArea();
			}
		});

		remUnSelectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] sels = bookContentRemoveArea.getSelectedIndices();
				for (int j = sels.length - 1; j >= 0; j--) {
					int i = sels[j];
					Data.removeIndexes.remove(i);
					System.out.println(Data.lines.get(i));
				}
				Collections.sort(Data.removeIndexes);
				refreshTextArea();
			}
		});

		remAllMarkedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int j = Data.removeIndexes.size() - 1; j >= 0; j--) {
					int i = Data.removeIndexes.get(j);
					Data.lines.remove(i);
				}
				Data.removeIndexes.removeAllElements();
				Data.headerIndexes.removeAllElements();
				refreshTextArea();
			}
		});

		bookContentRemoveArea.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				int j = bookContentRemoveArea.getSelectedIndex();
				if (j < 0)
					return;
				int i = Data.removeIndexes.get(j);
				if (i < 0)
					return;
				System.out.println(i);
				bookContentArea.setSelectedIndex(i);
				bookContentArea.ensureIndexIsVisible(i);
			}
		});

		remPane.add(remPageNumButton);
		remPane.add(remEmptyButton);
		remPane.add(remShortButton);
		remPane.add(remSelectButton);
		remPane.add(remUnSelectButton);
		remPane.add(remAllMarkedButton);

		bookContentPane.add(remPane);

		listModel = new DefaultListModel<String>();
		bookContentArea = new JList<String>(listModel);
		bookScrollPane = new JScrollPane(bookContentArea);
		bookContentPane.add(bookScrollPane);

		JPanel header = new JPanel(new GridLayout(5, 1));
		headSelectButton = new JButton(Strings.HEAD_TOHEAD);
		headUnSelectButton = new JButton(Strings.HEAD_TONOTHEAD);
		headPredictButton = new JButton(Strings.HEAD_PREDICT);
		headToRemoveButton = new JButton(Strings.HEAD_TOREMOVE);

		headSelectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int[] sels = bookContentArea.getSelectedIndices();
				for (int i : sels) {
					if (Data.headerIndexes.contains(i))
						continue;
					Data.headerIndexes.add(i);
					System.out.println(Data.lines.get(i));
				}
				Collections.sort(Data.headerIndexes);
				refreshTextArea();
			}
		});

		headUnSelectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int[] sels = bookContentHeaderArea.getSelectedIndices();

				for (int j = sels.length - 1; j >= 0; j--) {
					int i = sels[j];
					Data.headerIndexes.remove(i);

				}
				Collections.sort(Data.headerIndexes);
				refreshTextArea();
			}
		});

		headToRemoveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int[] sels = bookContentHeaderArea.getSelectedIndices();
				for (int j = sels.length - 1; j >= 0; j--) {
					int i = sels[j];
					int s = Data.headerIndexes.get(i);
					Data.headerIndexes.remove(i);
					Data.removeIndexes.add(s);
				}
				Collections.sort(Data.headerIndexes);
				Collections.sort(Data.removeIndexes);
				refreshTextArea();
			}
		});
		headPredictButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < Data.lines.size(); i++) {
					String s = Data.lines.get(i);
					if (Functions.isHeader(s)) {
						Data.headerIndexes.add(i);
						System.out.println(Data.lines.get(i));
					}

				}
				Collections.sort(Data.headerIndexes);
				refreshTextArea();
			}
		});

		addSeperatorButton = new JButton(Strings.ADD_SEPERATOR);
		addSeperatorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int i = bookContentArea.getSelectedIndex();
				Data.lines.add(i + 1, "-SePeRaToR-");
				Vector<Integer> rems = new Vector<Integer>();
				for (int j : Data.removeIndexes) {
					if (j < i)
						rems.add(j);
					else
						rems.add(j + 1);
				}
				Data.removeIndexes = rems;

				Vector<Integer> heads = new Vector<Integer>();
				for (int j : Data.headerIndexes) {
					if (j < i)
						heads.add(j);
					else
						heads.add(j + 1);
				}
				Data.headerIndexes = heads;

				refreshTextArea();

			}
		});

		header.add(headSelectButton);
		header.add(headUnSelectButton);
		header.add(headPredictButton);
		header.add(headToRemoveButton);
		header.add(addSeperatorButton);

		bookContentPane.add(header);

		headerListModel = new DefaultListModel<String>();
		bookContentHeaderArea = new JList<String>(headerListModel);
		bookContentHeaderArea.setVisibleRowCount(10);
		bookHeaderScrollPane = new JScrollPane(bookContentHeaderArea);
		bookContentPane.add(bookHeaderScrollPane);
		bookContentHeaderArea.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				int j = bookContentHeaderArea.getSelectedIndex();
				if (j < 0)
					return;
				int i = Data.headerIndexes.get(j);
				if (i < 0)
					return;
				System.out.println(i);
				bookContentArea.setSelectedIndex(i);
				bookContentArea.ensureIndexIsVisible(i);

			}
		});
	}

	private void generateRulesPart() {
		rulePanel = new JPanel();

		ruleList = new JList<Rule>(Data.rules);
		ruleList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		ruleList.setLayoutOrientation(JList.VERTICAL);
		ruleList.setVisibleRowCount(-1);

		rulelistScroller = new JScrollPane(ruleList);
		rulelistScroller.setPreferredSize(new Dimension(250, 80));

		ruleAdd = new JButton(Strings.RULE_ADD);
		ruleAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame f = new JFrame();
				int i = bookContentArea.getSelectedIndex();
				AddRuleDialog al = new AddRuleDialog(f, Strings.RULE_ADD, Data.lines.get(i));

			}
		});
		pageSideAdd = new JButton(Strings.PAGE_SIDE_ADD);
		pageSideAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame f = new JFrame();
				int i = bookContentArea.getSelectedIndex();
				AddPageSideDialog al = new AddPageSideDialog(f, Strings.RULE_ADD, Data.lines.get(i));

			}
		});
		ruleApply = new JButton(Strings.RULE_APPLY);
		ruleApply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<String> newLines = new Vector<String>();
				for (String s : Data.lines) {
					s = Functions.applyRules(s);
					newLines.add(s);
				}
				Data.lines = newLines;
				refreshTextArea();
			}
		});

		ruleRemove = new JButton(Strings.RULE_REMOVE);

		rulePredict = new JButton(Strings.RULE_PREDICT);

		rulePredict.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean done = false;
				for (String s : Data.lines) {
					for (int j = 0; j < s.length(); j++) {
						char c = s.charAt(j);
						if (Data.allowedChars.contains(c))
							continue;
						Data.allowedChars.add(c);
						JFrame f = new JFrame();
						AddRuleDialog al = new AddRuleDialog(f, Strings.RULE_ADD, s + "===>" + c);
						done = true;
						break;
					}
					if (done)
						break;
				}
			}
		});
		JPanel p = new JPanel(new GridLayout(5, 1));
		p.add(ruleApply);
		p.add(ruleAdd);
		p.add(ruleRemove);
		p.add(rulePredict);
		p.add(pageSideAdd);
		rulePanel.add(rulelistScroller);
		rulePanel.add(p);
	}

	private void generatePDFpart() {
		pdfName = new JLabel(Strings.NO_FILE);
		pdfName.setHorizontalAlignment(JLabel.CENTER);
		pdfPanel = new JPanel(new GridLayout(2, 1));
		JPanel p = new JPanel(new GridLayout(1, 2));
		pdfPanel.add(pdfName);
		pdfPanel.add(p);

	}

	private void refreshTextArea() {
		System.out.println("Start");
		listModel.removeAllElements();
		System.out.println("Removed Main");
		for (String l : Data.lines) {
			listModel.addElement(l);
		}
		System.out.println("Added Main");
		headerListModel.removeAllElements();
		System.out.println("Removed HEAD");
		for (int i : Data.headerIndexes) {
			headerListModel.addElement(Data.lines.get(i));
		}
		System.out.println("Added HEAD");
		removeListModel.removeAllElements();
		System.out.println("Removed REM");
		for (int i : Data.removeIndexes) {
			removeListModel.addElement(Data.lines.get(i));
		}
		System.out.println("Added REM");
		System.out.println("end");
	}

}
