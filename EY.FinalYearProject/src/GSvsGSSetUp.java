
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;

// Class for the set up page of the male vs female Gale Shapley example
public class GSvsGSSetUp {

	private JFrame frame;
	private int pairs = 4;
	JPanel group1Main;
	JPanel group2Main;
	JPanel listsPanel;
	JPanel lists2Panel;
	MainPageGUI mainPage;
	GSvsGSMain gsRun;
	JRadioButton rdbtnFemale;
	JRadioButton rdbtnMale;
	ArrayList<JList<String>> preferenceLists = new ArrayList<JList<String>>();

	// group 1 preference lists and models
	JList<String> group1List1;
	DefaultListModel<String> group1ListModel1;
	JList<String> group1List2;
	DefaultListModel<String> group1ListModel2;
	JList<String> group1List3;
	DefaultListModel<String> group1ListModel3;
	JList<String> group1List4;
	DefaultListModel<String> group1ListModel4;
	JList<String> group1List5;
	DefaultListModel<String> group1ListModel5;
	JList<String> group1List6;
	DefaultListModel<String> group1ListModel6;
	JList<String> group1List7;
	DefaultListModel<String> group1ListModel7;
	JList<String> group1List8;
	DefaultListModel<String> group1ListModel8;
	JList<String> group1List9;
	DefaultListModel<String> group1ListModel9;
	JList<String> group1List10;
	DefaultListModel<String> group1ListModel10;

	JList<String> group2List1;
	DefaultListModel<String> group2ListModel1;
	JList<String> group2List2;
	DefaultListModel<String> group2ListModel2;
	JList<String> group2List3;
	DefaultListModel<String> group2ListModel3;
	JList<String> group2List4;
	DefaultListModel<String> group2ListModel4;
	JList<String> group2List5;
	DefaultListModel<String> group2ListModel5;
	JList<String> group2List6;
	DefaultListModel<String> group2ListModel6;
	JList<String> group2List7;
	DefaultListModel<String> group2ListModel7;
	JList<String> group2List8;
	DefaultListModel<String> group2ListModel8;
	JList<String> group2List9;
	DefaultListModel<String> group2ListModel9;
	JList<String> group2List10;
	DefaultListModel<String> group2ListModel10;

	public static void main(String[] args) {
		GSvsGSSetUp window = new GSvsGSSetUp();
		window.frame.setVisible(true);
	}

	public GSvsGSSetUp() {
		initialize();
		this.frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setSize(1500,800);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JPanel topPanel = new JPanel();
		frame.getContentPane().add(topPanel);
		topPanel.setLayout(new GridLayout(3, 1));

		JPanel navPanel = new JPanel();
		topPanel.add(navPanel);
		navPanel.setLayout(new BorderLayout(0, 0));

		JButton btnBack = new JButton("< Back");
		btnBack.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		btnBack.setHorizontalAlignment(SwingConstants.LEADING);
		navPanel.add(btnBack, BorderLayout.WEST);
		btnBack.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// go back to main page
				mainPage = new MainPageGUI();
				frame.setVisible(false);
			}
		});

		JLabel lblTitle = new JLabel("Gale Shapley vs Gale Shapley Set Up");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Century Gothic", Font.PLAIN, 40));
		navPanel.add(lblTitle, BorderLayout.CENTER);

		JButton btnStart = new JButton("Start >");
		btnStart.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		btnStart.addActionListener(new ActionListener() {
			// go to animation page, and set variables
			public void actionPerformed(ActionEvent e) {

				GSvsGSMain runGS = new GSvsGSMain(pairs);
				frame.setVisible(false);
				runGS.setPrefs(preferenceLists); // sets the table's with the prefs from here
				runGS.addLabels();
			}
		});

		navPanel.add(btnStart, BorderLayout.EAST);

		JPanel pairsPanel = new JPanel();
		pairsPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		topPanel.add(pairsPanel);
		pairsPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		pairsPanel.add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));

		JButton btnPairsInfo = new JButton("info");
		btnPairsInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InfoPopup info = new InfoPopup();
				info.changeText("Number of pairs you want to use. Change the number selector on the right.");
			}
		});
		btnPairsInfo.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		panel.add(btnPairsInfo);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(45);
		pairsPanel.add(panel_1, BorderLayout.CENTER);

		JLabel lblPairs = new JLabel("Number of Pairs -");
		panel_1.add(lblPairs);
		lblPairs.setFont(new Font("Century Gothic", Font.PLAIN, 20));

		JSpinner spinnerPairs = new JSpinner();
		panel_1.add(spinnerPairs);
		spinnerPairs.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		spinnerPairs.setModel(new SpinnerNumberModel(4, 2, 6, 1));

		JPanel panel_2 = new JPanel();
		topPanel.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel group1Top = new JPanel();
		panel_2.add(group1Top);
		group1Top.setLayout(new BorderLayout(0, 0));

		JButton btnGroup1Info = new JButton("info");
		btnGroup1Info.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InfoPopup info = new InfoPopup();
				info.changeText("Below is a table of the men's preferences, each column is a man, and the order which the numbers"
						+ " are in are his preferences. e.g. the number at the top is the woman he likes best, the number at the "
						+ "bottom is the one he likes least. \n\n Drag and drop these numbers into the order you want, or use the 'Random'"
						+ " button to shuffle the tables.");
			}
		});
		btnGroup1Info.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		btnGroup1Info.setHorizontalAlignment(SwingConstants.LEFT);
		group1Top.add(btnGroup1Info, BorderLayout.WEST);

		JLabel lblMalePreferences = new JLabel("Male Preferences -");
		lblMalePreferences.setHorizontalAlignment(SwingConstants.CENTER);
		lblMalePreferences.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		group1Top.add(lblMalePreferences);

		JPanel group2Top = new JPanel();
		panel_2.add(group2Top);
		group2Top.setLayout(new BorderLayout(0, 0));

		JButton btnGroup2Info = new JButton("info");
		btnGroup2Info.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InfoPopup info = new InfoPopup();
				info.changeText("Below is a table of the women's preferences, each column is a woman, and the order which the numbers"
						+ " are in, are her preferences. e.g. the number at the top is the man she likes best, the number at the "
						+ "bottom is the one she likes least. \n\n Drag and drop these numbers into the order you want, or use the 'Random'"
						+ " button to shuffle the tables.");
			}
		});
		btnGroup2Info.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		btnGroup2Info.setHorizontalAlignment(SwingConstants.LEFT);
		group2Top.add(btnGroup2Info, BorderLayout.WEST);

		JLabel lblFemalePreferences = new JLabel("Female Preferences -");
		lblFemalePreferences.setHorizontalAlignment(SwingConstants.CENTER);
		lblFemalePreferences.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		group2Top.add(lblFemalePreferences);
		spinnerPairs.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				// spinner as before... select number of pairs
				// edit lists so they match the new number
				int newValue = (int) spinnerPairs.getModel().getValue();
				if (pairs > newValue){ // decreased spinner
					for (int i = 0; i < 20; i++){
						JList<String> currentList = preferenceLists.get(i);
						DefaultListModel<String> currentModel = (DefaultListModel<String>) currentList.getModel();
						// find and remove highest value in list
						for ( int k = newValue; k < pairs; k++){
							for ( int j = 0; j < currentModel.size(); j++){

								if ( Integer.parseInt(currentModel.getElementAt(j)) == k+1){
									currentModel.remove(j);
								}
							}
						}
					}   
					for (int i = newValue; i < pairs; i ++){
						listsPanel.remove(preferenceLists.get(i));
						lists2Panel.remove(preferenceLists.get((i)+10));
					}

				}else if (pairs < newValue){ // increased spinner
					for (int i = 0; i < 20; i++){

						// add to end of all lists
						JList<String> currentList = preferenceLists.get(i);
						DefaultListModel<String> currentModel = (DefaultListModel<String>) currentList.getModel();
						for(int j = pairs; j < newValue; j ++){
							currentModel.addElement(Integer.toString(j+1));
						}

					} 
					for ( int i = pairs; i < newValue; i++){
						listsPanel.add(preferenceLists.get(i));
						lists2Panel.add(preferenceLists.get((i)+10));
					}
				}
				pairs = newValue;
			}
		});

		JPanel bottom = new JPanel();
		frame.getContentPane().add(bottom);
		bottom.setLayout(new BorderLayout(0,0));

		JPanel bottomPanel = new JPanel();
		bottom.add(bottomPanel, BorderLayout.CENTER);
		bottomPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel group1Panel = new JPanel();
		bottomPanel.add(group1Panel);
		group1Panel.setLayout(new BorderLayout(0, 0));

		group1Main = new JPanel();
		group1Panel.add(group1Main, BorderLayout.CENTER);

		group1ListModel1 = createStringListModel();
		MyMouseAdaptor myMouseAdaptor = new MyMouseAdaptor();
		group1Main.setLayout(new BorderLayout(0, 0));

		listsPanel = new JPanel();
		group1Main.add(listsPanel, BorderLayout.CENTER);
		listsPanel.setLayout(new GridLayout(1, 6, 0, 0));

		group1List1 = new JList<String>(group1ListModel1);
		listsPanel.add(group1List1);
		group1List1.addMouseListener(myMouseAdaptor);
		group1List1.addMouseMotionListener(myMouseAdaptor);
		preferenceLists.add(group1List1);

		group1ListModel2 = createStringListModel();
		MyMouseAdaptor myMouseAdaptor2 = new MyMouseAdaptor();
		group1List2 = new JList<String>(group1ListModel2);
		listsPanel.add(group1List2);
		group1List2.addMouseListener(myMouseAdaptor2);
		group1List2.addMouseMotionListener(myMouseAdaptor2);
		preferenceLists.add(group1List2);

		group1ListModel3 = createStringListModel();
		MyMouseAdaptor myMouseAdaptor3 = new MyMouseAdaptor();
		group1List3 = new JList<String>(group1ListModel3);
		listsPanel.add(group1List3);
		group1List3.addMouseListener(myMouseAdaptor3);
		group1List3.addMouseMotionListener(myMouseAdaptor3);
		preferenceLists.add(group1List3);

		group1ListModel4 = createStringListModel();
		MyMouseAdaptor myMouseAdaptor4 = new MyMouseAdaptor();
		group1List4 = new JList<String>(group1ListModel4);
		listsPanel.add(group1List4);
		group1List4.addMouseListener(myMouseAdaptor4);
		group1List4.addMouseMotionListener(myMouseAdaptor4);
		preferenceLists.add(group1List4);

		JButton btnGroup1Random = new JButton("Random");
		btnGroup1Random.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		btnGroup1Random.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// shuffle the preferences in the lists
				randomize(0);
			}
		});
		group1Main.add(btnGroup1Random, BorderLayout.WEST);

		group1ListModel5 = createStringListModel();
		group1List5 = new JList<String>(group1ListModel5);
		MyMouseAdaptor myMouseAdaptor5 = new MyMouseAdaptor();
		group1List5.addMouseListener(myMouseAdaptor5);
		group1List5.addMouseMotionListener(myMouseAdaptor5);
		preferenceLists.add(group1List5);

		group1ListModel6 = createStringListModel();
		group1List6 = new JList<String>(group1ListModel6);
		MyMouseAdaptor myMouseAdaptor6 = new MyMouseAdaptor();
		group1List6.addMouseListener(myMouseAdaptor6);
		group1List6.addMouseMotionListener(myMouseAdaptor6);
		preferenceLists.add(group1List6);

		group1ListModel7 = createStringListModel();
		group1List7 = new JList<String>(group1ListModel7);
		MyMouseAdaptor myMouseAdaptor7 = new MyMouseAdaptor();
		group1List7.addMouseListener(myMouseAdaptor7);
		group1List7.addMouseMotionListener(myMouseAdaptor7);
		preferenceLists.add(group1List7);

		group1ListModel8 = createStringListModel();
		group1List8 = new JList<String>(group1ListModel8);
		MyMouseAdaptor myMouseAdaptor8 = new MyMouseAdaptor();
		group1List8.addMouseListener(myMouseAdaptor8);
		group1List8.addMouseMotionListener(myMouseAdaptor8);
		preferenceLists.add(group1List8);

		group1ListModel9 = createStringListModel();
		group1List9 = new JList<String>(group1ListModel9);
		MyMouseAdaptor myMouseAdaptor9 = new MyMouseAdaptor();
		group1List9.addMouseListener(myMouseAdaptor9);
		group1List9.addMouseMotionListener(myMouseAdaptor9);
		preferenceLists.add(group1List9);

		group1ListModel10 = createStringListModel();
		group1List10 = new JList<String>(group1ListModel10);
		MyMouseAdaptor myMouseAdaptor10 = new MyMouseAdaptor();
		group1List10.addMouseListener(myMouseAdaptor10);
		group1List10.addMouseMotionListener(myMouseAdaptor10);
		preferenceLists.add(group1List10);

		JPanel group2Panel = new JPanel();
		bottomPanel.add(group2Panel);
		group2Panel.setLayout(new BorderLayout(0, 0));

		group2Main = new JPanel();
		group2Panel.add(group2Main, BorderLayout.CENTER);

		group2ListModel1 = createStringListModel();
		MyMouseAdaptor myMouseAdaptor11 = new MyMouseAdaptor();
		group2Main.setLayout(new BorderLayout(0, 0));

		group2ListModel2 = createStringListModel();
		MyMouseAdaptor myMouseAdaptor12 = new MyMouseAdaptor();

		group2ListModel3 = createStringListModel();
		MyMouseAdaptor myMouseAdaptor13 = new MyMouseAdaptor();

		group2ListModel4 = createStringListModel();
		MyMouseAdaptor myMouseAdaptor14 = new MyMouseAdaptor();

		JButton btnGroup2Random = new JButton("Random");
		btnGroup2Random.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		btnGroup2Random.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Shuffle the group's preferences
				randomize(10);
			}
		});
		group2Main.add(btnGroup2Random, BorderLayout.WEST);
		
		// make them drag-and-drop-abble
		lists2Panel = new JPanel();
		group2Main.add(lists2Panel, BorderLayout.CENTER);
		lists2Panel.setLayout(new GridLayout(1, 6, 0, 0));
		group2List1 = new JList<String>(group2ListModel1);
		lists2Panel.add(group2List1);
		group2List1.addMouseListener(myMouseAdaptor11);
		group2List1.addMouseMotionListener(myMouseAdaptor11);
		preferenceLists.add(group2List1);
		group2List2 = new JList<String>(group2ListModel2);
		lists2Panel.add(group2List2);
		group2List2.addMouseListener(myMouseAdaptor12);
		group2List2.addMouseMotionListener(myMouseAdaptor12);
		preferenceLists.add(group2List2);
		group2List3 = new JList<String>(group2ListModel3);
		lists2Panel.add(group2List3);
		group2List3.addMouseListener(myMouseAdaptor13);
		group2List3.addMouseMotionListener(myMouseAdaptor13);
		preferenceLists.add(group2List3);
		group2List4 = new JList<String>(group2ListModel4);
		lists2Panel.add(group2List4);
		group2List4.addMouseListener(myMouseAdaptor14);
		group2List4.addMouseMotionListener(myMouseAdaptor14);
		preferenceLists.add(group2List4);

		group2ListModel5 = createStringListModel();
		group2List5 = new JList<String>(group2ListModel5);
		MyMouseAdaptor myMouseAdaptor15 = new MyMouseAdaptor();
		group2List5.addMouseListener(myMouseAdaptor15);
		group2List5.addMouseMotionListener(myMouseAdaptor15);
		preferenceLists.add(group2List5);

		group2ListModel6 = createStringListModel();
		group2List6 = new JList<String>(group2ListModel6);
		MyMouseAdaptor myMouseAdaptor16 = new MyMouseAdaptor();
		group2List6.addMouseListener(myMouseAdaptor16);
		group2List6.addMouseMotionListener(myMouseAdaptor16);
		preferenceLists.add(group2List6);

		group2ListModel7 = createStringListModel();
		group2List7 = new JList<String>(group2ListModel7);
		MyMouseAdaptor myMouseAdaptor17 = new MyMouseAdaptor();
		group2List7.addMouseListener(myMouseAdaptor17);
		group2List7.addMouseMotionListener(myMouseAdaptor17);
		preferenceLists.add(group2List7);

		group2ListModel8 = createStringListModel();
		group2List8 = new JList<String>(group2ListModel8);
		MyMouseAdaptor myMouseAdaptor18 = new MyMouseAdaptor();
		group2List8.addMouseListener(myMouseAdaptor18);
		group2List8.addMouseMotionListener(myMouseAdaptor18);
		preferenceLists.add(group2List8);

		group2ListModel9 = createStringListModel();
		group2List9 = new JList<String>(group2ListModel9);
		MyMouseAdaptor myMouseAdaptor19 = new MyMouseAdaptor();
		group2List9.addMouseListener(myMouseAdaptor19);
		group2List9.addMouseMotionListener(myMouseAdaptor19);
		preferenceLists.add(group2List9);

		group2ListModel10 = createStringListModel();
		group2List10 = new JList<String>(group2ListModel10);
		MyMouseAdaptor myMouseAdaptor20 = new MyMouseAdaptor();
		group2List10.addMouseListener(myMouseAdaptor20);
		group2List10.addMouseMotionListener(myMouseAdaptor20);
		preferenceLists.add(group2List10);

		JPanel panel_4 = new JPanel();

		bottom.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new BorderLayout(0, 0));

		JButton presetInfo = new JButton("info");
		presetInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InfoPopup info = new InfoPopup();
				info.changeText("Use these Example buttons to fill the tables with example data\n\n"
						+ "Example 1 - Different outcome for male and female bias\n\n"
						+ "Example 2 - The same outcome regardless of the bias option");

			}
		});
		presetInfo.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		panel_4.add(presetInfo, BorderLayout.WEST);

		JPanel panel_5 = new JPanel();
		panel_4.add(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton_1 = new JButton("Example Data 1");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// fills the tables with these preset values
				spinnerPairs.getModel().setValue(4);

				String[] presetValues = {
						"3","2","1","4",
						"4","3","2","1",
						"4","2","1","3",
						"3","1","4","2",
						"3","2","1","4",
						"2","3","1","4",
						"4","2","1","3",
						"4","1","2","3"
				};
				loadPresets(presetValues);

			}
		});
		btnNewButton_1.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		panel_5.add(btnNewButton_1, BorderLayout.NORTH);

		JButton btnNewButton_2 = new JButton("Example Data 2");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// more preset values to fill the table with
				spinnerPairs.getModel().setValue(4);

				String[] presetValues = {
						"1","2","3","4",
						"1","2","4","3",
						"1","4","2","3",
						"1","4","2","3",
						"1","3","4","2",
						"4","3","2","1",
						"1","4","3","2",
						"3","4","2","1",
				};

				loadPresets(presetValues);

			}
		});
		btnNewButton_2.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		panel_5.add(btnNewButton_2, BorderLayout.SOUTH);
	}

	public void randomize(int index){
// shuffles the preferences in each list
		for ( int i = index; i < (pairs+index); i++){
			JList<String> currentList = preferenceLists.get(i);
			DefaultListModel<String> currentModel = (DefaultListModel<String>) currentList.getModel();
			shuffle(currentModel); 
		}
	}

	// does the shuffling
	static void shuffle(DefaultListModel<String> mdl){
		for(int i=0;i<mdl.size();i++){
			int swapWith = (int)(Math.random()*(mdl.size()-i))+i;
			if(swapWith==i) continue;
			mdl.add(i, mdl.remove(swapWith));
			mdl.add(swapWith, mdl.remove(i+1));
		}
	}

	// allows the drag-drop- feature on the lists
	private class MyMouseAdaptor extends MouseInputAdapter {
		private boolean mouseDragging = false;
		private int dragSourceIndex;
		JList<String> currentList;
		DefaultListModel<String> currentListModel;

		@Override
		public void mousePressed(MouseEvent e) {
			// gets the list and index of the item that's been selected
			if (SwingUtilities.isLeftMouseButton(e)) {

				currentList = (JList<String>)e.getSource();
				currentListModel = (DefaultListModel<String>) currentList.getModel();
				dragSourceIndex = currentList.getSelectedIndex();
				mouseDragging = true;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mouseDragging = false;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			//move the picked up item to the index which it's dropped at.
			if (mouseDragging) {
				int currentIndex = currentList.locationToIndex(e.getPoint());
				if (currentIndex != dragSourceIndex) {
					int dragTargetIndex = currentList.getSelectedIndex();
					String dragElement = currentListModel.get(dragSourceIndex);
					currentListModel.remove(dragSourceIndex);
					currentListModel.add(dragTargetIndex, dragElement);
					dragSourceIndex = currentIndex;
				}
			}
		}
	}

	private DefaultListModel<String> createStringListModel() {
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for (int i = 1; i<=pairs; i++) {
			listModel.addElement(Integer.toString(i));
		}
		return listModel;
	}
	
	// takes the array of values and puts them into the gui tables
	public void loadPresets(String[] values){
		int count = 0;
		for(int i = 0; i < 4; i ++){ // first table
			JList<String> currentList;
			DefaultListModel<String> currentModel;

			currentList = preferenceLists.get(i);
			currentModel = (DefaultListModel<String>) currentList.getModel();

			currentModel.clear();
			for(int j = 0; j < 4; j++){
				currentModel.addElement(values[count]);
				count++;
			}
		}

		for(int i = 10; i < 14; i ++){ // second table
			JList<String> currentList;
			DefaultListModel<String> currentModel;

			currentList = preferenceLists.get(i);
			currentModel = (DefaultListModel<String>) currentList.getModel();

			currentModel.clear();
			for(int j = 0; j < 4; j++){
				currentModel.addElement(values[count]);
				count++;
			}
		}
	}
}
