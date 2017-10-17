

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Button;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Panel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import java.awt.Color;
/*
 * Set up page for Hungarian vs Galse shapley example
 */
public class GSvsHSetUp {

	DefaultTableModel tableModel;
	DefaultTableModel tableModel2;
	InfoPopup info;
	private JFrame frame;
	int pairs = 4;
	HvsGSmain runHvsGS;
	private JTable group1Prefs;
	MainPageGUI mainPage;
	private JTable group2Prefs;
	JRadioButton femaleRadio;

	public static void main(String[] args) {
		GSvsHSetUp window = new GSvsHSetUp();
		window.frame.setVisible(true);
	}

	public GSvsHSetUp() {
		initialize();
		this.frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setSize(1500,800);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel topPanel = new JPanel();
		frame.getContentPane().add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new GridLayout(3, 1, 0, 0));

		JPanel navPanel = new JPanel();
		topPanel.add(navPanel);
		navPanel.setLayout(new BorderLayout(0, 0));

		JPanel left = new JPanel();
		navPanel.add(left, BorderLayout.WEST);
		left.setLayout(new BorderLayout(0, 0));

		JButton btnBack = new JButton("<- Back");
		btnBack.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		left.add(btnBack, BorderLayout.WEST);  
		btnBack.addActionListener(new ActionListener() {
// back to home page 
			public void actionPerformed(ActionEvent e) {

				mainPage = new MainPageGUI();
				frame.setVisible(false);
			}
		});


		JPanel middle = new JPanel();
		navPanel.add(middle, BorderLayout.CENTER);
		middle.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblHungarianSetUp = new JLabel("Gale Shapley vs Hungarian Set-Up");
		lblHungarianSetUp.setFont(new Font("Century Gothic", Font.PLAIN, 40));
		lblHungarianSetUp.setHorizontalAlignment(SwingConstants.CENTER);
		middle.add(lblHungarianSetUp);

		JPanel right = new JPanel();
		navPanel.add(right, BorderLayout.EAST);
		right.setLayout(new GridLayout(0, 1, 0, 0));

		JButton startBTN = new JButton("Start ->");
		startBTN.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		startBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// go to animation page,
				// set array of final preferences for group 1 
				int[][] finalWeighting = new int[pairs][pairs];
				for ( int i = 0; i < pairs; i++){
					for ( int j = 0; j < pairs; j++){
						int cellValue = Integer.parseInt( tableModel.getValueAt(i, j).toString() );
						finalWeighting[i][j] = cellValue;
					}
				}
				// final preferences for group 2
				int[][] finalWeighting2 = new int[pairs][pairs];
				for ( int i = 0; i < pairs; i++){
					for ( int j = 0; j < pairs; j++){
						int cellValue = Integer.parseInt( tableModel2.getValueAt(i, j).toString() );
						finalWeighting2[i][j] = cellValue;
					}
				}
				// set bias
				boolean bias;

				if (femaleRadio.isSelected()){
					bias = false;
				}
				else{
					bias = true;
				}
				 //create the gui
				runHvsGS = new HvsGSmain(finalWeighting, finalWeighting2, pairs, bias);
				frame.setVisible(false);
			}
		});
		right.add(startBTN);

		Panel pairsPanel = new Panel();
		topPanel.add(pairsPanel);
		pairsPanel.setLayout(new BorderLayout(0, 0));

		Panel infoPanel = new Panel();
		pairsPanel.add(infoPanel, BorderLayout.WEST);
		infoPanel.setLayout(new GridLayout(1, 0, 0, 0));

		JButton infoBtn = new JButton("info");
		infoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info = new InfoPopup();
				info.changeText("Use the selector to the right to choose how many pairs you want to use in the simulation. This can be from 2 to 6");
			}
		});
		infoBtn.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		infoPanel.add(infoBtn);

		Panel middlePairsPanel = new Panel();
		pairsPanel.add(middlePairsPanel, BorderLayout.CENTER);
		middlePairsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel pairsLabel = new JLabel("Number of Pairs:");
		pairsLabel.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		pairsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		middlePairsPanel.add(pairsLabel);

		Panel pairsSelectorPanel = new Panel();
		middlePairsPanel.add(pairsSelectorPanel);
		pairsSelectorPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JSpinner pairsSpinner = new JSpinner();
		pairsSpinner.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		pairsSpinner.setModel(new SpinnerNumberModel(4, 2, 6, 1));
		pairsSelectorPanel.add(pairsSpinner);


		pairsSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				// spinner value for pairs...
				// change number of columns and rows to match new value
				int newValue = (int) pairsSpinner.getModel().getValue();

				if (pairs > newValue){ // decreased spinner

					tableModel.setColumnCount(newValue);
					tableModel2.setColumnCount(newValue);

					for (int i = pairs; i > newValue; i--){
						tableModel.removeRow(i-1);
						tableModel2.removeRow(i-1);
					}

				}else if (pairs < newValue){ // increased spinner

					for ( int i = pairs; i < newValue; i++){

						String newColumnName = Integer.toString(newValue);
						Object[] newData = new Object[i+1];

						for ( int j = 0; j < i+1; j++ ){
							newData[j] = 0;
						}

						tableModel.addRow(newData);
						tableModel.addColumn(newColumnName, newData);

						tableModel2.addRow(newData);
						tableModel2.addColumn(newColumnName, newData);
					}
				}
				pairs = newValue;
			}
		});

		JPanel biasPanel = new JPanel();
		topPanel.add(biasPanel);
		biasPanel.setLayout(new BorderLayout(0, 0));

		JPanel biasLabel = new JPanel();
		biasPanel.add(biasLabel);

		JLabel lblMaleOrFemale = new JLabel("Male or Female bias - ");
		lblMaleOrFemale.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		biasLabel.add(lblMaleOrFemale);

		JPanel biasRight = new JPanel();
		biasLabel.add(biasRight);
		biasRight.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel female = new JPanel();
		biasRight.add(female);

		femaleRadio = new JRadioButton("Female");
		femaleRadio.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		femaleRadio.setHorizontalAlignment(SwingConstants.CENTER);
		female.add(femaleRadio);

		JPanel male = new JPanel();
		biasRight.add(male);

		JRadioButton maleRadio = new JRadioButton("Male");
		maleRadio.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		maleRadio.setHorizontalAlignment(SwingConstants.CENTER);
		maleRadio.setSelected(true);
		male.add(maleRadio);

		JPanel infobtn = new JPanel();
		biasPanel.add(infobtn, BorderLayout.WEST);
		infobtn.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton = new JButton("info");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info = new InfoPopup();
				info.changeText("Select which gender you want the algorithm to be optimal for. ");
			}
		});
		btnNewButton.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		infobtn.add(btnNewButton);

		ButtonGroup bg1 = new ButtonGroup( );
		bg1.add(femaleRadio);
		bg1.add(maleRadio);

		JPanel bottom = new JPanel();
		frame.getContentPane().add(bottom);
		bottom.setLayout(new BorderLayout(0,0));

		JPanel bottomPanel = new JPanel();
		bottom.add(bottomPanel, BorderLayout.CENTER);

		bottomPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));

		// initialise the two tables of preferences
		tableModel = new DefaultTableModel(
				new Object[][] {
					{new Integer(0), new Integer(0), new Integer(0), new Integer(0)},
					{new Integer(0), new Integer(0), new Integer(0), new Integer(0)},
					{new Integer(0), new Integer(0), new Integer(0), new Integer(0)},
					{new Integer(0), new Integer(0), new Integer(0), new Integer(0)},
				},
				new String[] {
						"1", "2", "3", "4"
				}
				);

		tableModel2 = new DefaultTableModel(
				new Object[][] {
					{new Integer(0), new Integer(0), new Integer(0), new Integer(0)},
					{new Integer(0), new Integer(0), new Integer(0), new Integer(0)},
					{new Integer(0), new Integer(0), new Integer(0), new Integer(0)},
					{new Integer(0), new Integer(0), new Integer(0), new Integer(0)},
				},
				new String[] {
						"1", "2", "3", "4"
				}
				);

		bottomPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel leftMain = new JPanel();
		bottomPanel.add(leftMain);
		leftMain.setLayout(new BorderLayout(0, 40));

		JPanel leftTable = new JPanel();
		leftMain.add(leftTable);
		leftTable.setLayout(new BorderLayout(0, 0));

		Panel PreferenceSelectPanel = new Panel();
		leftTable.add(PreferenceSelectPanel);
		PreferenceSelectPanel.setLayout(new BorderLayout(0, 0));

		group1Prefs = new JTable();
		group1Prefs.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		group1Prefs.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		group1Prefs.setModel(tableModel);
		group1Prefs.setRowHeight(20);
		group1Prefs.setFillsViewportHeight(true);

		group1Prefs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		group1Prefs.setCellSelectionEnabled(true);
		PreferenceSelectPanel.add(group1Prefs);

		JLabel lblNewLabel = new JLabel("Men");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		leftTable.add(lblNewLabel, BorderLayout.NORTH);

		JLabel lblNewLabel_1 = new JLabel("Preferences");
		lblNewLabel_1.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel_1.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		leftTable.add(lblNewLabel_1, BorderLayout.WEST);

		JPanel panel = new JPanel();
		leftMain.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel btnpnl = new JPanel();
		panel.add(btnpnl, BorderLayout.WEST);
		btnpnl.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton_1 = new JButton("info");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info = new InfoPopup();
				info.changeText("Below is a table just like the hungarian set up. Fill it with weighted values, low being a smaller cost so more desirable. This"
						+ " time the columns are men and the rows are women. The ordered preferences for the Gale Shapley simulation "
						+ "are calulated from the weighted values input below. The random button will fill the table with values "
						+ "between 1 and 20\n\nPlease only use numerical values up to 20");
			}
		});
		btnNewButton_1.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		btnpnl.add(btnNewButton_1);

		JLabel lblNewLabel_2 = new JLabel("Male Preferences");
		panel.add(lblNewLabel_2);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		
				Panel rndPanel = new Panel();
				leftMain.add(rndPanel, BorderLayout.WEST);
				rndPanel.setLayout(new BorderLayout(0, 0));
				
						JButton randomBtn = new JButton("Random");
						randomBtn.setFont(new Font("Century Gothic", Font.PLAIN, 20));
						rndPanel.add(randomBtn, BorderLayout.CENTER);
						randomBtn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {

								for ( int i = 0; i < pairs; i++){
									for ( int j = 0; j < pairs; j++){
										Random generator = new Random(); 
										int r = generator.nextInt(20) + 1;
										tableModel.setValueAt(r, i, j);
									}
								}

							}		
						});

		JPanel rightMain = new JPanel();
		bottomPanel.add(rightMain);
		rightMain.setLayout(new BorderLayout(0, 40));

		JPanel rightTable = new JPanel();
		rightMain.add(rightTable);
		rightTable.setLayout(new BorderLayout(0, 0));

		JPanel prefsSelect = new JPanel();
		rightTable.add(prefsSelect, BorderLayout.CENTER);
		prefsSelect.setLayout(new BorderLayout(0, 0));

		group2Prefs = new JTable();
		group2Prefs.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		group2Prefs.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		group2Prefs.setModel(tableModel2);
		group2Prefs.setRowHeight(20);
		group2Prefs.setFillsViewportHeight(true);

		group2Prefs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		group2Prefs.setCellSelectionEnabled(true);
		prefsSelect.add(group2Prefs);

		JLabel lblNewLabel_3 = new JLabel("Women");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		rightTable.add(lblNewLabel_3, BorderLayout.NORTH);

		JLabel lblNewLabel_4 = new JLabel("Preferences");
		lblNewLabel_4.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel_4.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		rightTable.add(lblNewLabel_4, BorderLayout.WEST);

		JPanel panel_2 = new JPanel();
		rightMain.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel_2.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton_2 = new JButton("info");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info = new InfoPopup();
				info.changeText("Below is a table just like the hungarian set up. Fill it with weighted values, low being a smaller cost so more desirable. This"
						+ " time the columns are women and the rows are men. The ordered preferences for the Gale Shapley simulation "
						+ "are calulated from the weighted values input below. The random button will fill the table with values "
						+ "between 1 and 20\n\nPlease only use numerical values up to 20");
			}
		});
		btnNewButton_2.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		panel_1.add(btnNewButton_2);

		JLabel lblNewLabel_5 = new JLabel("Female Preferences");
		panel_2.add(lblNewLabel_5);
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		
				JPanel random = new JPanel();
				rightMain.add(random, BorderLayout.WEST);
				random.setLayout(new BorderLayout(0, 0));
				
						JButton rndBtn = new JButton("Random");
						rndBtn.setFont(new Font("Century Gothic", Font.PLAIN, 20));
						random.add(rndBtn);
						rndBtn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								// fill table with random values between 0 and 20
								for ( int i = 0; i < pairs; i++){
									for ( int j = 0; j < pairs; j++){
										Random generator = new Random(); 
										int r = generator.nextInt(20) + 1;
										tableModel2.setValueAt(r, i, j);
									}
								}

							}		
						});

		JPanel panel_3 = new JPanel();
		bottom.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));

		JButton infoPreset = new JButton("info");
		infoPreset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InfoPopup info = new InfoPopup();
				info.changeText("Use the two example buttons to fill the tables with example data\n\n"
						+ "Example 1 - This example gives a different result from each algorithm. "
						+ "it's not too simple, and has some repetition to watch.\n\n"
						+ "Example 2 - both algorithms give the same result.");

			}
		});
		infoPreset.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		panel_3.add(infoPreset, BorderLayout.WEST);

		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		JButton eg1 = new JButton("Example Data 1");
		eg1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// preset values for an example
				pairsSpinner.getModel().setValue(4);
				int[] presetValues = 
					{2,7,17,13,
							4,5,17,10,
							20,10,1,17,
							9,13,14,16,
							
					12,13,8,17,
					3,16,14,1,
					6,19,13,20,
					16,8,16,9};
				
				loadPresets(presetValues);
			}

		});
		eg1.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		panel_4.add(eg1, BorderLayout.NORTH);

		JButton eg2 = new JButton("Example Data 2");
		eg2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pairsSpinner.getModel().setValue(4);
				// another set of example data to use
				int[] presetValues = 
					{14,20,15,6,
							4,9,20,9,
							3,17,4,8,
							20,6,4,19,
							
					16,8,10,7,
					9,8,15,18,
					13,13,5,16,
					13,3,19,15};

				loadPresets(presetValues);
			}
		});
			
		eg2.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		panel_4.add(eg2, BorderLayout.SOUTH);

	}
	
	// takes the array of example data, inputs it into the tables on the screen
	public void loadPresets(int[] values){
		int count = 0;

		for(int i = 0; i <4; i ++ ){
			for(int j = 0; j < 4; j ++){
				group1Prefs.getModel().setValueAt(values[count], i, j);
				count++;
			}
		}
		
		for(int i = 0; i <4; i ++ ){
			for(int j = 0; j < 4; j ++){
				group2Prefs.getModel().setValueAt(values[count], i, j);
				count++;
			}
		}
	}
}
