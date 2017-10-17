

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;


//  GUI for the Gale Shapley vs Hungarian example
// Displays the two animations side by side with a control panel to step forwards and backwards
// created with Eclipse Window Builder

public class HvsGSRun {
	private JFrame frame;
	JTable leftTable1;
	JTable leftTable2;
	JTable originalValues;
	JTable animatedTable;
	Panel originalPanel;
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
	GSvsHSetUp setUp;
	JPanel displayRightPanel;
	private JPanel leftTables;
	JPanel leftTable1Panel;
	JPanel leftTable2Panel;
	JPanel narrationPanel;
	JTextArea narration;
	JTextArea rightNarration;
	private JPanel rightNarrationPanel;
	JPanel animatedPanel;
	 JPanel left1;
	 JPanel middle1;
	 JPanel right1;
	 private JPanel topNav;
	 private JPanel bottomNav;
	 private JLabel lblNewLabel;
	 private JLabel lblNewLabel_1;
	  JLabel lblPrefs1;
	 private JPanel panel;
	 private JPanel panel_1;
	  JLabel lblPrefs2;
	  private JPanel panel_2;
	   JLabel lblPrefs3;
	  private JPanel panel_3;
	   JLabel lblPrefs4;
	   private JButton helpBtn;

	public HvsGSRun() {
		initialize();
		this.frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		BorderLayout borderLayout = (BorderLayout) frame.getContentPane().getLayout();
		borderLayout.setVgap(25);
		frame.setSize(1500,800);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel navPanel = new JPanel();
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
								
								topNav = new JPanel();
								navPanel.add(topNav, BorderLayout.NORTH);
										topNav.setLayout(new BorderLayout(0, 0));
								
										JButton btnNewButton = new JButton("< Back");
										topNav.add(btnNewButton, BorderLayout.WEST);
										btnNewButton.setFont(new Font("Century Gothic", Font.PLAIN, 20));
										btnNewButton.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												//  goes back to set up page
												setUp = new GSvsHSetUp();
												frame.setVisible(false);
											}
										});
										btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
										
												JLabel lblTitle = new JLabel("Gale Shapley vs Hungarian Run");
												topNav.add(lblTitle);
												lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
												lblTitle.setFont(new Font("Century Gothic", Font.PLAIN, 40));
												
												helpBtn = new JButton("Help with this Page");
												helpBtn.addActionListener(new ActionListener() {
													public void actionPerformed(ActionEvent e) {
														InfoPopup info = new InfoPopup();
														info.changeText("At the top of the window is the controls to step forwards and backwards through the algorithm, the play button "
																+ "runs the simulation right through to the end. \n\nEach simulation has a bipartite graph in the middle to show who is "
																+ "engaged to who as in the single simulation, and also the tables like in the single simulations. \n\n"
																+ "Again, values for social welfare and average choices are given at the end which show how the two algorithms differ and "
																+ "allow comparison of the final outcome");
													}
												});
												helpBtn.setFont(new Font("Century Gothic", Font.PLAIN, 20));
												topNav.add(helpBtn, BorderLayout.EAST);
												
												bottomNav = new JPanel();
												navPanel.add(bottomNav, BorderLayout.SOUTH);
												bottomNav.setLayout(new GridLayout(0, 2, 0, 0));
												
												lblNewLabel = new JLabel("Gale Shapley");
												lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
												lblNewLabel.setFont(new Font("Century Gothic", Font.BOLD, 24));
												bottomNav.add(lblNewLabel);
												
												lblNewLabel_1 = new JLabel("Hungarian");
												lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
												lblNewLabel_1.setFont(new Font("Century Gothic", Font.BOLD, 24));
												bottomNav.add(lblNewLabel_1);

		JPanel mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new GridLayout(0, 4, 0, 0));

		JPanel leftPrefsPanel = new JPanel();
		leftPrefsPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		mainPanel.add(leftPrefsPanel);
		leftPrefsPanel.setLayout(new BorderLayout(0, 0));

		leftTables = new JPanel();
		leftPrefsPanel.add(leftTables, BorderLayout.CENTER);
		leftTables.setLayout(new GridLayout(3, 0, 0, 0));
		
		panel = new JPanel();
		leftTables.add(panel);
				panel.setLayout(new BorderLayout(0, 0));
		
				leftTable1Panel = new JPanel();
				panel.add(leftTable1Panel, BorderLayout.CENTER);
				leftTable1Panel.setLayout(new BorderLayout(0, 0));
				
				lblPrefs1 = new JLabel("Prefs");
				lblPrefs1.setFont(new Font("Century Gothic", Font.PLAIN, 20));
				lblPrefs1.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblPrefs1, BorderLayout.NORTH);
		
		panel_1 = new JPanel();
		leftTables.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
				leftTable2Panel = new JPanel();
				panel_1.add(leftTable2Panel, BorderLayout.CENTER);
				leftTable2Panel.setLayout(new BorderLayout(0, 0));
				
				lblPrefs2 = new JLabel("prefs");
				lblPrefs2.setFont(new Font("Century Gothic", Font.PLAIN, 20));
				lblPrefs2.setHorizontalAlignment(SwingConstants.CENTER);
				panel_1.add(lblPrefs2, BorderLayout.NORTH);

		narrationPanel = new JPanel();
		leftTables.add(narrationPanel);
		narrationPanel.setLayout(new BorderLayout(0, 0));

		narration = new JTextArea();
		narration.setWrapStyleWord(true);
		narration.setLineWrap(true);
		narration.setFont(new Font("Century Gothic", Font.PLAIN, 20));
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
		displayRightPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		left1 = new JPanel();
		displayRightPanel.add(left1);
		left1.setLayout(new GridLayout(6, 1, 0, 0));
		
		middle1 = new JPanel();
		displayRightPanel.add(middle1);
		middle1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		right1 = new JPanel();
		displayRightPanel.add(right1);
		right1.setLayout(new GridLayout(6, 1, 0, 0));

		JPanel rightPrefsPanel = new JPanel();
		rightPrefsPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		mainPanel.add(rightPrefsPanel);
		rightPrefsPanel.setLayout(new GridLayout(3, 1, 0, 0));
		
		panel_2 = new JPanel();
		rightPrefsPanel.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		animatedPanel = new JPanel();
		panel_2.add(animatedPanel);
		animatedPanel.setLayout(new BorderLayout(0, 0));
		
		lblPrefs3 = new JLabel("New label");
		lblPrefs3.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		lblPrefs3.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblPrefs3, BorderLayout.NORTH);
		
		panel_3 = new JPanel();
		rightPrefsPanel.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
				originalPanel = new Panel();
				panel_3.add(originalPanel, BorderLayout.CENTER);
				originalPanel.setLayout(new BorderLayout(0, 0));
				
				lblPrefs4 = new JLabel("New label");
				lblPrefs4.setFont(new Font("Century Gothic", Font.PLAIN, 20));
				lblPrefs4.setHorizontalAlignment(SwingConstants.CENTER);
				panel_3.add(lblPrefs4, BorderLayout.NORTH);

		rightNarrationPanel = new JPanel();
		rightPrefsPanel.add(rightNarrationPanel);
		rightNarrationPanel.setLayout(new BorderLayout(0, 0));

		rightNarration = new JTextArea();
		rightNarrationPanel.add(rightNarration);
		rightNarration.setWrapStyleWord(true);
		rightNarration.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		rightNarration.setLineWrap(true);

		JScrollPane scroller2 = 
				new JScrollPane(rightNarration, 
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	


		rightNarrationPanel.add(scroller2);
	}
}
