
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;

// GUI for the male vs Female animation
// Men first on one side, Women first on the other
// Each side includes two tables of preferences, a narration panel, and a graph

public class GSvsGSRun {
	private JFrame frame;
	JTable leftTable1;
	JTable leftTable2;
	JTable rightTable1;
	JTable rightTable2;
	Panel group1Panel;
	Panel group2Panel;
	JPanel left;
	JPanel right;
	JPanel middle;
	JPanel displayLeftPanel;
	JButton btnForward;
	JButton btnBack;
	boolean nextStep = false;
	String[] columnNames;
	Object [][] group1;
	Object [][] group2;
	Object [][] group3;
	Object [][] group4;
	JButton btnPlay;

	JPanel controls;
	GSvsGSSetUp setUp;
	private JPanel displayRightPanel;
	private JPanel leftTables;
	JPanel leftTable1Panel;
	JPanel leftTable2Panel;
	JPanel rLeft;
	JPanel rMiddle;
	JPanel rRight;
	JPanel narrationPanel;
	JTextArea narration;
	JTextArea rightNarration;
	private JPanel rightNarrationPanel;
	private JPanel panel;
	private JPanel panel_1;
	private JLabel lblGroup1Prefs;
	private JLabel lblGroup2Prefs;
	private JPanel panel_2;
	private JPanel panel_3;
	private JLabel lblGroup3Prefs;
	private JLabel lblGroup4Prefs;
	private JPanel panel_4;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JPanel panel_5;
	private JButton btnNewButton_1;

	public GSvsGSRun() {
		initialize();
		this.frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setSize(1500,800);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel navPanel = new JPanel();
		navPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		frame.getContentPane().add(navPanel, BorderLayout.NORTH);
		navPanel.setLayout(new BorderLayout(0, 0));
		
				controls = new JPanel();
				navPanel.add(controls, BorderLayout.CENTER);
						controls.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				
						btnBack = new JButton("<-");
						btnBack.setFont(new Font("Century Gothic", Font.PLAIN, 20));
						controls.add(btnBack);
						
								btnPlay = new JButton("play");
								btnPlay.setFont(new Font("Century Gothic", Font.PLAIN, 20));
								controls.add(btnPlay);
								
								panel_4 = new JPanel();
								navPanel.add(panel_4, BorderLayout.SOUTH);
								panel_4.setLayout(new GridLayout(0, 2, 0, 0));
								
								lblNewLabel = new JLabel("Men First");
								lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
								lblNewLabel.setFont(new Font("Century Gothic", Font.BOLD, 23));
								panel_4.add(lblNewLabel);
								
								lblNewLabel_1 = new JLabel("Women First");
								lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
								lblNewLabel_1.setFont(new Font("Century Gothic", Font.BOLD, 23));
								panel_4.add(lblNewLabel_1);
								
								panel_5 = new JPanel();
								navPanel.add(panel_5, BorderLayout.NORTH);
										panel_5.setLayout(new BorderLayout(0, 0));
								
										JButton btnNewButton = new JButton("< Back");
										btnNewButton.setFont(new Font("Century Gothic", Font.PLAIN, 20));
										panel_5.add(btnNewButton, BorderLayout.WEST);
										btnNewButton.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												setUp = new GSvsGSSetUp();
												frame.setVisible(false);
											}
										});
										btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
										
												JLabel lblTitle = new JLabel("Gale Shapley vs Gale Shapley Run");
												panel_5.add(lblTitle);
												lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
												lblTitle.setFont(new Font("Century Gothic", Font.PLAIN, 40));
												
												btnNewButton_1 = new JButton("Help with this Page");
												btnNewButton_1.addActionListener(new ActionListener() {
													public void actionPerformed(ActionEvent e) {
														InfoPopup info = new InfoPopup();
					                              		info.changeText("The controls are at the top of the page, use these to step forwards and backwards through the algorithm. "
					                              				+ "\nThe male optimal simulation is on the left, women on the right. They have a narration, tables and "
					                              				+ "bipartite graph like in the single simulation. The end result still gives an average choice value to allow "
					                              				+ "comparison between the two simulations and see how the results can differ depending which group is proposing. ");
					                              	}
												});
												btnNewButton_1.setFont(new Font("Century Gothic", Font.PLAIN, 20));
												panel_5.add(btnNewButton_1, BorderLayout.EAST);

		JPanel mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new GridLayout(0, 4, 0, 0));

		JPanel leftPrefsPanel = new JPanel();
		mainPanel.add(leftPrefsPanel);
		leftPrefsPanel.setLayout(new BorderLayout(0, 0));

		leftTables = new JPanel();
		leftPrefsPanel.add(leftTables, BorderLayout.CENTER);
		leftTables.setLayout(new GridLayout(3, 0, 0, 0));
		
		panel = new JPanel();
		leftTables.add(panel);
				panel.setLayout(new BorderLayout(0, 0));
		
				leftTable1Panel = new JPanel();
				panel.add(leftTable1Panel);
				leftTable1Panel.setLayout(new BorderLayout(0, 0));
				
				lblGroup1Prefs = new JLabel("Male Preferences");
				panel.add(lblGroup1Prefs, BorderLayout.NORTH);
				lblGroup1Prefs.setHorizontalAlignment(SwingConstants.CENTER);
				lblGroup1Prefs.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		
		panel_1 = new JPanel();
		leftTables.add(panel_1);
				panel_1.setLayout(new BorderLayout(0, 0));
		
				leftTable2Panel = new JPanel();
				panel_1.add(leftTable2Panel);
				leftTable2Panel.setLayout(new BorderLayout(0, 0));
				
				lblGroup2Prefs = new JLabel("Female Preferences");
				panel_1.add(lblGroup2Prefs, BorderLayout.NORTH);
				lblGroup2Prefs.setHorizontalAlignment(SwingConstants.CENTER);
				lblGroup2Prefs.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		
				narrationPanel = new JPanel();
				leftTables.add(narrationPanel);
				narrationPanel.setLayout(new BorderLayout(0, 0));
				
						narration = new JTextArea();
						narration.setWrapStyleWord(true);
						narration.setLineWrap(true);
						narration.setFont(new Font("Century Gothic", Font.PLAIN, 19));
						narrationPanel.add(narration);
						
								JScrollPane scroller = 
										new JScrollPane(narration, 
												JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
												JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	
								
								
										narrationPanel.add(scroller);



		displayLeftPanel = new JPanel();
		displayLeftPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		mainPanel.add(displayLeftPanel);
		displayLeftPanel.setLayout(new GridLayout(1, 3, 0, 0));

		left = new JPanel();
		displayLeftPanel.add(left);
		left.setLayout(new GridLayout(6, 1, 0, 0));

		middle = new JPanel();
		displayLeftPanel.add(middle);

		right = new JPanel();
		displayLeftPanel.add(right);
		right.setLayout(new GridLayout(6, 1, 0, 0));

		displayRightPanel = new JPanel();
		displayRightPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		mainPanel.add(displayRightPanel);
		displayRightPanel.setLayout(new GridLayout(0, 3, 0, 0));

		rLeft = new JPanel();
		displayRightPanel.add(rLeft);
		rLeft.setLayout(new GridLayout(6, 1, 0, 0));

		rMiddle = new JPanel();
		displayRightPanel.add(rMiddle);

		rRight = new JPanel();
		displayRightPanel.add(rRight);
		rRight.setLayout(new GridLayout(6, 1, 0, 0));

		JPanel rightPrefsPanel = new JPanel();
		mainPanel.add(rightPrefsPanel);
		rightPrefsPanel.setLayout(new GridLayout(3, 1, 0, 0));
		
		panel_2 = new JPanel();
		rightPrefsPanel.add(panel_2);
				panel_2.setLayout(new BorderLayout(0, 0));
		
				group1Panel = new Panel();
				panel_2.add(group1Panel);
				group1Panel.setLayout(new BorderLayout(0, 0));
				
				lblGroup3Prefs = new JLabel("Female Preferences");
				lblGroup3Prefs.setFont(new Font("Century Gothic", Font.PLAIN, 20));
				lblGroup3Prefs.setHorizontalAlignment(SwingConstants.CENTER);
				panel_2.add(lblGroup3Prefs, BorderLayout.NORTH);
				
				panel_3 = new JPanel();
				rightPrefsPanel.add(panel_3);
						panel_3.setLayout(new BorderLayout(0, 0));
				
						group2Panel = new Panel();
						panel_3.add(group2Panel);
						group2Panel.setLayout(new BorderLayout(0, 0));
						
						lblGroup4Prefs = new JLabel("Male Preferences");
						lblGroup4Prefs.setFont(new Font("Century Gothic", Font.PLAIN, 20));
						lblGroup4Prefs.setHorizontalAlignment(SwingConstants.CENTER);
						panel_3.add(lblGroup4Prefs, BorderLayout.NORTH);
						
								rightNarrationPanel = new JPanel();
								rightPrefsPanel.add(rightNarrationPanel);
								rightNarrationPanel.setLayout(new BorderLayout(0, 0));
								
										rightNarration = new JTextArea();
										rightNarrationPanel.add(rightNarration);
										rightNarration.setWrapStyleWord(true);
										rightNarration.setFont(new Font("Century Gothic", Font.PLAIN, 19));
										rightNarration.setLineWrap(true);
										
												JScrollPane scroller2 = 
														new JScrollPane(rightNarration, 
																JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
																JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	
												
												
														rightNarrationPanel.add(scroller2);


	}

}
