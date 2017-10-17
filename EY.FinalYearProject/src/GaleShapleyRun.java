import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// the gui for the gale shapley animation page
// layout and options like fonts and button actions
// made in eclipse window builder

public class GaleShapleyRun {
	private JFrame frame;
	InfoPopup info;
	JTextArea narration; // where the narration text appears
	JTable group1Table; // group 1 prefs
	JTable group2Table; // group 2 prefs
	Panel group1Panel;
	Panel group2Panel;
	JPanel left;
	JPanel right;
	JPanel middle;
	JPanel displayPanel;
	JButton btnForward;
	JButton btnBack;

	boolean nextStep = false;
	String[] columnNames;
	Object [][] group1; // table of preferences for one group
	Object [][] group2; // preferences for the other group
	JButton btnPlay;

	JPanel controls;
	GaleShapleySetUp setUp;
	JLabel lblGroup1;
	JLabel lblGroup2;
	JPanel table1Panel; // table to display left preferences
	JPanel table2Panel; // display right preferences
	private JButton helpBtn;

	public GaleShapleyRun() {
		initialize();
		this.frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setSize(1500,800);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel navPanel = new JPanel();
		frame.getContentPane().add(navPanel, BorderLayout.NORTH);
		navPanel.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton = new JButton("< Back");
		// goes back to set-up page when clicked
		btnNewButton.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setUp = new GaleShapleySetUp();
				frame.setVisible(false);
			}
		});
		btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		navPanel.add(btnNewButton, BorderLayout.WEST);

		JLabel lblTitle = new JLabel("Gale Shapley Run");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		navPanel.add(lblTitle, BorderLayout.CENTER);
		lblTitle.setFont(new Font("Century Gothic", Font.PLAIN, 40));

		helpBtn = new JButton("Help with this Page");
		helpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info = new InfoPopup();
				info.changeText("Use the buttons on the left to step forwards and backwards through the simulation,"
						+ " the play button runs the algorithm through to the end.\n\nThe white panel on the left is where the"
						+ " narration text is displayed to explain each step as it happens. \n\nThe middle panel is the bipartite graph, "
						+ "This shows which person is proposing to who with an orange line, if the proposal is accepted the line turns "
						+ "green. \n\nThe right panel shows each group's table of preferences. Table cells are highlighted as someone proposes"
						+ " or accepts a proposal. e.g. If man 1 proposes to woman 3, the '3' in Man 1's column will be highlighted orange. As"
						+ " will the '1' in woman 3's column. If a proposal is accepted, they turn green to show they are engaged.\n\nA value for "
						+ "average choice assigned will be given at the end. This is an average of the assigned choices e.g. if all Men"
						+ " got their first choice, the average would be 1.");
			}
		});
		helpBtn.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		navPanel.add(helpBtn, BorderLayout.EAST);

		JPanel mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new GridLayout(0, 3, 0, 0));

		JPanel narrationPanel = new JPanel();
		mainPanel.add(narrationPanel);
		narrationPanel.setLayout(new BorderLayout(0, 0));

		controls = new JPanel();
		narrationPanel.add(controls, BorderLayout.NORTH);
		controls.setLayout(new GridLayout(1, 3, 0, 0));

		btnBack = new JButton("<-");
		btnBack.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		controls.add(btnBack);

		btnPlay = new JButton("play");
		btnPlay.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		controls.add(btnPlay);

		narration = new JTextArea();
		narration.setWrapStyleWord(true);
		narration.setLineWrap(true);
		narration.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		narration.setEditable(false);
		narrationPanel.add(narration, BorderLayout.CENTER);

		//  adds a scroller to the side of the text Area.
		JScrollPane scroller2 = 
				new JScrollPane(narration, 
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	
		narrationPanel.add(scroller2);

		displayPanel = new JPanel();
		mainPanel.add(displayPanel);
		displayPanel.setLayout(new GridLayout(1, 3, 0, 0));

		left = new JPanel();
		displayPanel.add(left);
		left.setLayout(new GridLayout(6, 1, 0, 0));

		middle = new JPanel();
		displayPanel.add(middle);

		right = new JPanel();
		displayPanel.add(right);
		right.setLayout(new GridLayout(6, 1, 0, 0));

		JPanel prefsPanel = new JPanel();
		mainPanel.add(prefsPanel);
		prefsPanel.setLayout(new GridLayout(2, 1, 0, 0));

		group1Panel = new Panel();
		prefsPanel.add(group1Panel);
		group1Panel.setLayout(new BorderLayout(0, 0));

		lblGroup1 = new JLabel("New label");
		lblGroup1.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		lblGroup1.setHorizontalAlignment(SwingConstants.CENTER);
		group1Panel.add(lblGroup1, BorderLayout.NORTH);

		table1Panel = new JPanel();
		group1Panel.add(table1Panel, BorderLayout.CENTER);
		table1Panel.setLayout(new BorderLayout(0, 0));

		group2Panel = new Panel();
		prefsPanel.add(group2Panel);
		group2Panel.setLayout(new BorderLayout(0, 0));

		lblGroup2 = new JLabel("New label");
		lblGroup2.setHorizontalAlignment(SwingConstants.CENTER);
		lblGroup2.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		group2Panel.add(lblGroup2, BorderLayout.NORTH);

		table2Panel = new JPanel();
		group2Panel.add(table2Panel, BorderLayout.CENTER);
		table2Panel.setLayout(new BorderLayout(0, 0));
	}
}
