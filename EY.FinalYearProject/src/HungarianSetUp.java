import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Button;
import javax.swing.JLabel;
import javax.swing.JList;
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
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JSeparator;

// Set up page for the Hungarian Algorithm

public class HungarianSetUp {

	DefaultTableModel tableModel;
	InfoPopup info;
	private JFrame frame;
	int pairs = 4;
	HungarianMain runHungarian;
	private JTable hungarianPreferencesTable;
	MainPageGUI mainPage;

	public static void main(String[] args) {
		HungarianSetUp window = new HungarianSetUp();
		window.frame.setVisible(true);
	}

	public HungarianSetUp() {
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
		topPanel.setLayout(new GridLayout(3, 1, 0, 0));

		JPanel navConainer = new JPanel();
		topPanel.add(navConainer);
		navConainer.setLayout(new BorderLayout(0, 0));

		JPanel navPanel = new JPanel();
		navConainer.add(navPanel);
		navPanel.setLayout(new BorderLayout(0, 0));

		JPanel left = new JPanel();
		navPanel.add(left, BorderLayout.WEST);
		left.setLayout(new BorderLayout(0, 0));

		JButton btnBack = new JButton("<- Back");
		btnBack.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		left.add(btnBack, BorderLayout.CENTER);  
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

		JLabel lblHungarianSetUp = new JLabel("Hungarian Set-Up");
		lblHungarianSetUp.setFont(new Font("Century Gothic", Font.PLAIN, 40));
		lblHungarianSetUp.setHorizontalAlignment(SwingConstants.CENTER);
		middle.add(lblHungarianSetUp);

		JPanel right = new JPanel();
		navPanel.add(right, BorderLayout.EAST);

		JButton startBTN = new JButton("Start ->");
		startBTN.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		startBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Start pressed, set the variables and begin simulation on next page
				int[][] finalWeighting = new int[pairs][pairs];
				for ( int i = 0; i < pairs; i++){
					for ( int j = 0; j < pairs; j++){
						int cellValue = Integer.parseInt( tableModel.getValueAt(i, j).toString() );
						finalWeighting[i][j] = cellValue;
					}
				}			

				runHungarian = new HungarianMain(finalWeighting, pairs);
				frame.setVisible(false);
				runHungarian.populateTables(); // puts the preferences in the tables
			}
		});
		right.setLayout(new BorderLayout(0, 0));
		right.add(startBTN, BorderLayout.CENTER);

		Panel pairsPanel = new Panel();
		topPanel.add(pairsPanel);
		pairsPanel.setLayout(new BorderLayout(0, 0));

		Panel infoPanel = new Panel();
		pairsPanel.add(infoPanel, BorderLayout.WEST);
		infoPanel.setLayout(new BorderLayout(0, 0));

		JButton infoBtn = new JButton("info");
		infoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info = new InfoPopup();
				info.changeText("Use the selector to the right to choose how many pairs you want to use in the simulation. This can be from 2 to 6");
			}
		});
		infoBtn.setFont(new Font("Century Gothic", Font.PLAIN, 21));
		infoPanel.add(infoBtn, BorderLayout.CENTER);

		JPanel mainPairs = new JPanel();
		FlowLayout flowLayout = (FlowLayout) mainPairs.getLayout();
		flowLayout.setVgap(40);
		mainPairs.setBorder(new LineBorder(Color.LIGHT_GRAY));
		pairsPanel.add(mainPairs);

		Panel middlePairsPanel = new Panel();
		mainPairs.add(middlePairsPanel);
		middlePairsPanel.setLayout(new BorderLayout(0, 0));

		JLabel pairsLabel = new JLabel("Number of Pairs:");
		pairsLabel.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		pairsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		middlePairsPanel.add(pairsLabel);

		Panel pairsSelectorPanel = new Panel();
		mainPairs.add(pairsSelectorPanel);
		pairsSelectorPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JSpinner pairsSpinner = new JSpinner();
		pairsSpinner.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		pairsSpinner.setModel(new SpinnerNumberModel(4, 2, 6, 1));
		pairsSelectorPanel.add(pairsSpinner);

		JPanel tableLabels = new JPanel();
		topPanel.add(tableLabels);
		tableLabels.setLayout(new BorderLayout(0, 0));

		JPanel infopnl = new JPanel();
		tableLabels.add(infopnl, BorderLayout.WEST);
		infopnl.setLayout(new BorderLayout(0, 0));

		JButton infoB = new JButton("info");
		infoB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info = new InfoPopup();
				info.changeText("Below is a table of the preferences, each column is a person, each row is a task. The value in"
						+ " each row is the cost for that person to do that task."
						+ " e.g. the value in column 2, row 3 is how much person 2 costs to do task 3. \n\nThe random button will"
						+ " fill the table with random numbers between 1 and 20.\n\nPlease only use numerical values up to 20.");
			}
		});
		infoB.setFont(new Font("Century Gothic", Font.PLAIN, 21));
		infopnl.add(infoB, BorderLayout.CENTER);

		JLabel lblNewLabel = new JLabel("Input Weighted Preferences");
		lblNewLabel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Century Gothic", Font.PLAIN, 21));
		tableLabels.add(lblNewLabel);
		pairsSpinner.addChangeListener(new ChangeListener() {
			// spinner to select the number of pairs
			public void stateChanged(ChangeEvent e) {
				int newValue = (int) pairsSpinner.getModel().getValue();

				if (pairs > newValue){ // decreased spinner

					//remove rows and columns to match the new number
					tableModel.setColumnCount(newValue);
					for (int i = pairs; i > newValue; i--){
						tableModel.removeRow(i-1);
					}

				}else if (pairs < newValue){ // increased spinner
					
					// add column and rows to match new value
					for ( int i = pairs; i < newValue; i++){

						String newColumnName = Integer.toString(newValue);
						Object[] newData = new Object[i+1];

						for ( int j = 0; j < i+1; j++ ){
							newData[j] = 0;
						}

						tableModel.addRow(newData);
						tableModel.addColumn(newColumnName, newData);
					}
				}
				pairs = newValue;
			}
		});

		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		frame.getContentPane().add(bottomPanel);
		bottomPanel.setLayout(new BorderLayout(0, 0));

		Panel rndPanel = new Panel();
		bottomPanel.add(rndPanel, BorderLayout.WEST);
		rndPanel.setLayout(new BorderLayout(0, 0));

		JButton randomBtn = new JButton("Random");
		randomBtn.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		rndPanel.add(randomBtn, BorderLayout.CENTER);
		randomBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// fill table with random values between 0 and 20
				for ( int i = 0; i < pairs; i++){
					for ( int j = 0; j < pairs; j++){
						Random generator = new Random(); 
						int r = generator.nextInt(20) + 1;
						tableModel.setValueAt(r, i, j);
					}
				}

			}		
		});

		Panel PreferenceSelectPanel = new Panel();
		bottomPanel.add(PreferenceSelectPanel, BorderLayout.CENTER);
		PreferenceSelectPanel.setLayout(new BorderLayout(0, 0));
		
		// initialising the table of preferences
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

		// table properties
		hungarianPreferencesTable = new JTable();
		// this makes the table save data when clicking elsewhere ( the last cell wouldnt update upon clicking 'start', otherwise)
		hungarianPreferencesTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		hungarianPreferencesTable.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		hungarianPreferencesTable.setModel(tableModel);
		hungarianPreferencesTable.setRowHeight(20);
		hungarianPreferencesTable.setFillsViewportHeight(true);

		hungarianPreferencesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		hungarianPreferencesTable.setCellSelectionEnabled(true);
		PreferenceSelectPanel.add(hungarianPreferencesTable);

		JLabel lblNewLabel_1 = new JLabel("People");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Century Gothic", Font.PLAIN, 21));
		PreferenceSelectPanel.add(lblNewLabel_1, BorderLayout.NORTH);

		JPanel leftLabel = new JPanel();
		PreferenceSelectPanel.add(leftLabel, BorderLayout.WEST);
		leftLabel.setLayout(new BorderLayout(0, 0));

		JSeparator separator_3 = new JSeparator();
		leftLabel.add(separator_3, BorderLayout.WEST);
		separator_3.setPreferredSize(new Dimension(7, 2));

		JLabel lblNewLabel_2 = new JLabel("Tasks");
		leftLabel.add(lblNewLabel_2, BorderLayout.CENTER);
		lblNewLabel_2.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel_2.setFont(new Font("Century Gothic", Font.PLAIN, 21));

		JSeparator separator_2 = new JSeparator();
		separator_2.setPreferredSize(new Dimension(7, 2));
		leftLabel.add(separator_2, BorderLayout.EAST);

		JPanel panel = new JPanel();
		bottomPanel.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		JButton eg1 = new JButton("Example Data 1 - Simple");
		eg1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// preset values to fill the table
				pairsSpinner.getModel().setValue(4);
				int[] presetValues = 
					{18,9,13,18,
							4,13,16,15,
							2,16,5,8,
							13,6,19,1};


				loadPresets(presetValues);
			}
		});
		eg1.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		panel_1.add(eg1, BorderLayout.NORTH);

		JButton eg2 = new JButton("Example Data 2 - More Complex");
		eg2.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		eg2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// more presets to fill the table with
				pairsSpinner.getModel().setValue(4);
				int[] presetValues = 
					{5,5,10,6,
							13,13,9,7,
							17,13,8,17,
							15,10,16,1};
				
				loadPresets(presetValues);
			}
		});
		panel_1.add(eg2, BorderLayout.SOUTH);

		JButton presetHelp = new JButton("info");
		presetHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InfoPopup info = new InfoPopup();
				info.changeText("Use the two example buttons to input example data into the table.\n\n"
						+ "Example 1 - Simple data which runs through each step once before finishing\n\n"
						+ "Example 2 - More complex example, this has to repeat some steps a few times to "
						+ "reach the conclusion");
			}
		});
		presetHelp.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		panel.add(presetHelp, BorderLayout.WEST);

	}
	
	// takes the preset values array, and puts them into the table
	public void loadPresets(int[] values){
		int count = 0;

		for(int i = 0; i <4; i ++ ){
			for(int j = 0; j < 4; j ++){
				hungarianPreferencesTable.getModel().setValueAt(values[count], i, j);
				count++;
			}
		}
	}
}
