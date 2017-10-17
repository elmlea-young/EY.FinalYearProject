/*
 * back
 * save deets
 * 
 */
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

// The set-up page for the Gale Shapley Simulation. 
// built in eclipse window builder

public class GaleShapleySetUp {
	private JFrame frame;
	InfoPopup info;
	private int pairs = 4; // number Of pairs
	JPanel group1Main; 
	JPanel group2Main;
	MainPageGUI mainPage;
	JRadioButton rdbtnFemale;
	JRadioButton rdbtnMale;
	ArrayList<JList<String>> preferenceLists = new ArrayList<JList<String>>(); // list of the preference lists

	// lists for the groups' preferences. And the List models
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
		GaleShapleySetUp window = new GaleShapleySetUp();
		window.frame.setVisible(true);
	}

	public GaleShapleySetUp() {
		initialize();
		this.frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setSize(1500,800);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(2, 1));

		JPanel topPanel = new JPanel();
		frame.getContentPane().add(topPanel);
		topPanel.setLayout(new GridLayout(4, 1));

		JPanel navPanel = new JPanel();
		topPanel.add(navPanel);
		navPanel.setLayout(new GridLayout(1, 4, 0, 0));

		// back button. When clicked returns to homepage
		JButton btnBack = new JButton("< Back");
		btnBack.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		btnBack.setHorizontalAlignment(SwingConstants.LEADING);
		navPanel.add(btnBack);
		btnBack.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPage = new MainPageGUI();
				frame.setVisible(false);
			}
		});


		JLabel lblTitle = new JLabel("Gale Shapley Set Up");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Century Gothic", Font.PLAIN, 40));
		navPanel.add(lblTitle);

		// start button sets all the variables that the user chose, and begins the simulation page
		JButton btnStart = new JButton("Start >");
		btnStart.setHorizontalAlignment(SwingConstants.RIGHT);
		btnStart.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		btnStart.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// get which radio button is chosen for the bias.
				boolean bias;

				if (rdbtnFemale.isSelected()){
					bias = false;
				}
				else{
					bias = true;
				}

				// begin simulation page
				GaleShapleyMain runGS = new GaleShapleyMain(pairs, bias);
				frame.setVisible(false);
				runGS.setPrefs(preferenceLists); // sends the order of preferences to the next page
				runGS.addLabels(); // puts labels on the window
			}
		});

		navPanel.add(btnStart);

		JPanel pairsPanel = new JPanel();
		pairsPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		topPanel.add(pairsPanel);
		pairsPanel.setLayout(new BorderLayout(0, 0));

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(null);
		pairsPanel.add(infoPanel, BorderLayout.WEST);
		infoPanel.setLayout(new BorderLayout(0, 0));

		JButton btnPairsInfo = new JButton("info");
		btnPairsInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info = new InfoPopup();
				info.changeText("Use the selector to the right to choose how many pairs you want to use in the simulation. This can be from 2 to 6");
			}
		});
		btnPairsInfo.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		infoPanel.add(btnPairsInfo);

		JPanel pairsMain = new JPanel();
		pairsMain.setBorder(null);
		pairsPanel.add(pairsMain, BorderLayout.CENTER);

		JLabel lblPairs = new JLabel("Number of Pairs -");
		pairsMain.add(lblPairs);
		lblPairs.setFont(new Font("Century Gothic", Font.PLAIN, 20));

		// spinner for choosing the number of pairs. 
		JSpinner spinnerPairs = new JSpinner();
		pairsMain.add(spinnerPairs);
		spinnerPairs.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		spinnerPairs.setModel(new SpinnerNumberModel(4, 2, 6, 1));
		spinnerPairs.addChangeListener(new ChangeListener() {
			// when the number is changed...
			// number of lists and number of values in those lists must reflect the value in the spinner
			public void stateChanged(ChangeEvent e) {
				int newValue = (int) spinnerPairs.getModel().getValue();

				if (pairs > newValue){ // decreased spinner
					for (int i = 0; i < 20; i++){ // loop through all lists
						JList<String> currentList = preferenceLists.get(i);
						DefaultListModel<String> currentModel = (DefaultListModel<String>) currentList.getModel();
						// find and remove highest value in each list
						for ( int k = newValue; k < pairs; k++){ // loop from new value to old. to make sure no numbers are missed out
							for ( int j = 0; j < currentModel.size(); j++){
								if ( Integer.parseInt(currentModel.getElementAt(j)) == k+1){
									currentModel.remove(j);
								}
							}
						}
					}   
					for (int i = newValue; i < pairs; i ++){ // also remove whole lists so the correct number are on the screen
						group1Main.remove(preferenceLists.get(i));
						group2Main.remove(preferenceLists.get((i)+10));
					}

				}else if (pairs < newValue){ // increased spinner
					for (int i = 0; i < 20; i++){
						// add new value to end of all lists
						JList<String> currentList = preferenceLists.get(i);
						DefaultListModel<String> currentModel = (DefaultListModel<String>) currentList.getModel();
						for(int j = pairs; j < newValue; j ++){
							currentModel.addElement(Integer.toString(j+1));
						}

					} 
					for ( int i = pairs; i < newValue; i++){ // add lists to page
						group1Main.add(preferenceLists.get(i));
						group2Main.add(preferenceLists.get((i)+10));
					}
				}
				pairs = newValue; // update new value for pairs
			}
		});

		JPanel biasPanel = new JPanel();
		biasPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		topPanel.add(biasPanel);
		biasPanel.setLayout(new BorderLayout(0, 0));

		JPanel infoPnl = new JPanel();
		biasPanel.add(infoPnl, BorderLayout.WEST);
		infoPnl.setLayout(new BorderLayout(0, 0));

		JButton btnBiasInfo = new JButton("info");
		btnBiasInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info = new InfoPopup();
				info.changeText("The Gale Shapley algorithm works by one group 'proposing' to the other. "
						+ "The group which proposes has the best outcome in the final result by being paired "
						+ "with their best possible choice. While the other group isn't as lucky. So choose whether"
						+ " to have a female or male optimal simulation.");
			}
		});
		btnBiasInfo.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		infoPnl.add(btnBiasInfo);

		ButtonGroup bg1 = new ButtonGroup( ); // radio button group

		JPanel mainBias = new JPanel();
		mainBias.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		biasPanel.add(mainBias, BorderLayout.CENTER);

		JLabel lblBias = new JLabel("Male or Female Bias -");
		mainBias.add(lblBias);
		lblBias.setFont(new Font("Century Gothic", Font.PLAIN, 20));

		JPanel femalePanel = new JPanel();
		mainBias.add(femalePanel);
		femalePanel.setLayout(new GridLayout(0, 1, 0, 0));

		rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		femalePanel.add(rdbtnFemale);

		bg1.add(rdbtnFemale);

		JPanel malePanel = new JPanel();
		mainBias.add(malePanel);
		malePanel.setLayout(new GridLayout(0, 1, 0, 0));

		rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		rdbtnMale.setSelected(true);
		malePanel.add(rdbtnMale);
		bg1.add(rdbtnMale);

		JPanel preferencesPanel = new JPanel();
		preferencesPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		topPanel.add(preferencesPanel);
		preferencesPanel.setLayout(new GridLayout(1, 2, 0, 0));

		JPanel menPanel = new JPanel();
		preferencesPanel.add(menPanel);
		menPanel.setLayout(new BorderLayout(0, 0));

		JPanel menInfoPanel = new JPanel();
		menPanel.add(menInfoPanel, BorderLayout.WEST);
		menInfoPanel.setLayout(new BorderLayout(0, 0));

		JButton menInfo = new JButton("info");
		menInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info = new InfoPopup();
				info.changeText("Below is a table of the men's preferences, each column is a man, and the order which the numbers"
						+ " are in is his preferences. e.g. the number at the top is the woman he likes best, the number at the "
						+ "bottom is the one he likes least. \n\n Drag and drop these numbers into the order you want, or use the 'Random'"
						+ " button to shuffle the tables.");
			}
		});
		menInfo.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		menInfoPanel.add(menInfo, BorderLayout.WEST);

		JLabel menLabel = new JLabel("Men Preferences");
		menLabel.setHorizontalAlignment(SwingConstants.CENTER);
		menLabel.setFont(new Font("Century Gothic", Font.PLAIN, 21));
		menPanel.add(menLabel, BorderLayout.CENTER);

		JPanel womenPanel = new JPanel();
		preferencesPanel.add(womenPanel);
		womenPanel.setLayout(new BorderLayout(0, 0));

		JPanel womenInfoPanel = new JPanel();
		womenPanel.add(womenInfoPanel, BorderLayout.WEST);
		womenInfoPanel.setLayout(new BorderLayout(0, 0));

		JButton womenInfo = new JButton("info");
		womenInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info = new InfoPopup();
				info.changeText("Below is a table of the women's preferences, each column is a woman, and the order which the numbers"
						+ " are in are her preferences. e.g. the number at the top is the man she likes best, the number at the "
						+ "bottom is the one she likes least. \n\n Drag and drop these numbers into the order you want, or use the 'Random'"
						+ " button to shuffle the tables.");
			}
		});
		womenInfo.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		womenInfoPanel.add(womenInfo);

		JLabel womenLabel = new JLabel("Women Preferences");
		womenLabel.setFont(new Font("Century Gothic", Font.PLAIN, 21));
		womenLabel.setHorizontalAlignment(SwingConstants.CENTER);
		womenPanel.add(womenLabel);

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
		group1Main.setLayout(new GridLayout(1, 11, 0, 0));

		JButton btnGroup1Random = new JButton("Random");
		btnGroup1Random.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		btnGroup1Random.addActionListener(new ActionListener() {
			// randomize the values in the lists
			public void actionPerformed(ActionEvent e) {
				randomize(0);
			}
		});
		group1Main.add(btnGroup1Random);

		// make the preference lists, add to the group of lists
		// This also enables them to be dragable 
		group1ListModel1 = createStringListModel();
		group1List1 = new JList<String>(group1ListModel1);
		MyMouseAdaptor myMouseAdaptor = new MyMouseAdaptor();
		group1List1.addMouseListener(myMouseAdaptor);
		group1List1.addMouseMotionListener(myMouseAdaptor);
		group1Main.add(group1List1);
		preferenceLists.add(group1List1);

		group1ListModel2 = createStringListModel();
		group1List2 = new JList<String>(group1ListModel2);
		MyMouseAdaptor myMouseAdaptor2 = new MyMouseAdaptor();
		group1List2.addMouseListener(myMouseAdaptor2);
		group1List2.addMouseMotionListener(myMouseAdaptor2);
		group1Main.add(group1List2);
		preferenceLists.add(group1List2);

		group1ListModel3 = createStringListModel();
		group1List3 = new JList<String>(group1ListModel3);
		MyMouseAdaptor myMouseAdaptor3 = new MyMouseAdaptor();
		group1List3.addMouseListener(myMouseAdaptor3);
		group1List3.addMouseMotionListener(myMouseAdaptor3);
		group1Main.add(group1List3);
		preferenceLists.add(group1List3);

		group1ListModel4 = createStringListModel();
		group1List4 = new JList<String>(group1ListModel4);
		MyMouseAdaptor myMouseAdaptor4 = new MyMouseAdaptor();
		group1List4.addMouseListener(myMouseAdaptor4);
		group1List4.addMouseMotionListener(myMouseAdaptor4);
		group1Main.add(group1List4);
		preferenceLists.add(group1List4);

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
		group2Main.setLayout(new GridLayout(1, 11, 0, 0));
		group2Panel.add(group2Main, BorderLayout.CENTER);

		JButton btnGroup2Random = new JButton("Random");
		btnGroup2Random.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		btnGroup2Random.addActionListener(new ActionListener() {
			// randomise the other group's values
			public void actionPerformed(ActionEvent e) {
				randomize(10);
			}
		});
		group2Main.add(btnGroup2Random);

		group2ListModel1 = createStringListModel();
		group2List1 = new JList<String>(group2ListModel1);
		MyMouseAdaptor myMouseAdaptor11 = new MyMouseAdaptor();
		group2List1.addMouseListener(myMouseAdaptor11);
		group2List1.addMouseMotionListener(myMouseAdaptor11);
		group2Main.add(group2List1);
		preferenceLists.add(group2List1);

		group2ListModel2 = createStringListModel();
		group2List2 = new JList<String>(group2ListModel2);
		MyMouseAdaptor myMouseAdaptor12 = new MyMouseAdaptor();
		group2List2.addMouseListener(myMouseAdaptor12);
		group2List2.addMouseMotionListener(myMouseAdaptor12);
		group2Main.add(group2List2);
		preferenceLists.add(group2List2);

		group2ListModel3 = createStringListModel();
		group2List3 = new JList<String>(group2ListModel3);
		MyMouseAdaptor myMouseAdaptor13 = new MyMouseAdaptor();
		group2List3.addMouseListener(myMouseAdaptor13);
		group2List3.addMouseMotionListener(myMouseAdaptor13);
		group2Main.add(group2List3);
		preferenceLists.add(group2List3);

		group2ListModel4 = createStringListModel();
		group2List4 = new JList<String>(group2ListModel4);
		MyMouseAdaptor myMouseAdaptor14 = new MyMouseAdaptor();
		group2List4.addMouseListener(myMouseAdaptor14);
		group2List4.addMouseMotionListener(myMouseAdaptor14);
		group2Main.add(group2List4);
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

		JPanel presetButtons = new JPanel();
		bottom.add(presetButtons, BorderLayout.SOUTH);
		presetButtons.setLayout(new BorderLayout(0, 0));

		JButton helpbtn = new JButton("Info");
		helpbtn.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		helpbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InfoPopup info = new InfoPopup();
				info.changeText("Use these buttons to fill the tables with example data to play with.\n\n"
						+ "Example 1 - a slightly complex set of data so the simulation"
						+ " shows some back and forth between proposals\n\nExample 2 - Male bias selected - Shows "
						+ "that women can still have the best outcome in certain situations.\nFemale bias selected - "
						+ "same end result as male bias\n\n"
						+ "Both examples use groups of 4 men and women.");
			}
		});
		presetButtons.add(helpbtn, BorderLayout.WEST);

		JPanel panel_1 = new JPanel();
		presetButtons.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		JButton example1 = new JButton("Example data 1");
		example1.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		panel_1.add(example1, BorderLayout.NORTH);

		JButton example2 = new JButton("Example data 2");
		example2.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		panel_1.add(example2, BorderLayout.SOUTH);
		example2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// when example data selected, change spinner to 4, as that's the number of pairs it uses
				spinnerPairs.getModel().setValue(4);
				// Array of the preferences for all people
				String[] presetValues = { 
						"2","1","3","4",
						"1","3","4","2",
						"3","2","4","1",
						"1","2","4","3",
						"1","3","2","4",
						"4","3","1","2",
						"3","1","4","2",
						"4","2","3","1"
				};
				// set the preferences to the lists
				loadPresets(presetValues);
			} 
		});
		example1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spinnerPairs.getModel().setValue(4);

				// difference set of data
				String[] presetValues = { 
						"1","2","4","3",
						"2","1","3","4",
						"2","4","3","1",
						"4","2","1","3",
						"1","3","2","4",
						"4","3","2","1",
						"4","3","1","2",
						"1","3","4","2"
				};
				// set the values to the lists
				loadPresets(presetValues);

			}
		});
	}

	// mixes up the preference lists
	public void randomize(int index){

		for ( int i = index; i < (pairs+index); i++){ // go through each list and shuffle the values
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

	// allows the drag and drop feature on the lists
	// moves the chosen value to the position it's dragged to. And moves the other values to reflect this
	private class MyMouseAdaptor extends MouseInputAdapter {
		private boolean mouseDragging = false;
		private int dragSourceIndex;
		JList<String> currentList;
		DefaultListModel<String> currentListModel;

		@Override
		public void mousePressed(MouseEvent e) {
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

	// creates the model for each list
	private DefaultListModel<String> createStringListModel() {
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for (int i = 1; i<=pairs; i++) {
			listModel.addElement(Integer.toString(i));
		}
		return listModel;
	}

	// takes an array of preferences and sets the lists to this order
	public void loadPresets(String[] values){
		int count = 0; // which index of the preset values array is next
		for(int i = 0; i < 4; i ++){ // only uses 4 pairs
			JList<String> currentList;
			DefaultListModel<String> currentModel;

			// get list i
			currentList = preferenceLists.get(i);
			currentModel = (DefaultListModel<String>) currentList.getModel();
			// first clear it
			currentModel.clear();
			// then populate it with the correct values from the array
			for(int j = 0; j < 4; j++){
				currentModel.addElement(values[count]);
				count++;
			}
		}

		for(int i = 10; i < 14; i ++){ // same again but for the second group 
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
