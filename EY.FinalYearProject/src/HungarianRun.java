import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Animation page of hungarian
public class HungarianRun {
	private JFrame frame;
	InfoPopup info;
	HungarianSetUp setUp;
	JButton forward;
	JTextArea narrationText; // where narration text appears
	JTable originalValues; // table of original values, doens't change
	JPanel displayTable;
	JPanel narration;
	JButton play;
	JButton backward;
	JTable animatedTable; // table of values which change
	JPanel middle;
	JPanel table1Panel;

	public HungarianRun() {
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

		JPanel backPanel = new JPanel();
		navPanel.add(backPanel, BorderLayout.WEST);
		backPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JButton btnBack = new JButton("<- Back");
		btnBack.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// back to set up
				setUp = new HungarianSetUp();
				frame.setVisible(false);
			}
		});
		backPanel.add(btnBack);

		JPanel titlePanel = new JPanel();
		navPanel.add(titlePanel, BorderLayout.CENTER);
		titlePanel.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblTitle = new JLabel("Hungarian Run");
		lblTitle.setFont(new Font("Century Gothic", Font.PLAIN, 40));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(lblTitle);
		
		JButton btnNewButton = new JButton("Help with this Page");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info = new InfoPopup();
				info.changeText("In the top left is the control panel, use these buttons to step forwards and backwards through the simulation. The play button"
						+ " will run the simulation through to the end.\n\nThe white panel on the left is the narration area, text will appear for each step to "
						+ "explain what's happening. \n\nThe middle panel is the table which will change it's values to show each step. The cells will also "
						+ "change colour to show which are currently being used, or changed. \n\nOn the right is the original table of preferences "
						+ "these values dont change, but at the end a cell in each column and row will be highlighted green to show that's the task assigned "
						+ "to that person, and what it's weight was.\n\nAt the end a value for 'average choice assigned' will be given. This is an average of the"
						+ " assigned choices e.g. if all people got their first choice task, the average would be 1.\n\nThe value for social welfare is also"
						+ " given. This is the sum of the assigned tasks weights (total of all cells highlighted green)");
			}
		});
		btnNewButton.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		navPanel.add(btnNewButton, BorderLayout.EAST);

		JPanel mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new GridLayout(1, 3, 0, 0));

		JPanel left = new JPanel();
		mainPanel.add(left);
		left.setLayout(new BorderLayout(0, 0));

		JPanel controls = new JPanel();
		left.add(controls, BorderLayout.NORTH);
		controls.setLayout(new GridLayout(1, 0, 0, 0));

		//Step backwards
		backward = new JButton("<-");
		backward.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		controls.add(backward);
		// play to end
		play = new JButton("Play");
		play.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		controls.add(play);
		// step forwards
		forward = new JButton("->");
		forward.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		controls.add(forward);

		narration = new JPanel();
		left.add(narration, BorderLayout.CENTER);
		narration.setLayout(new GridLayout(0, 1, 0, 0));

		narrationText = new JTextArea();
		narrationText.setWrapStyleWord(true);
		narrationText.setLineWrap(true);
		narrationText.setEditable(false);
		narrationText.setFont(new Font("Century Gothic", Font.PLAIN, 20));

		JScrollPane scroller = 
				new JScrollPane(narrationText, 
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	


		narration.add(scroller);

		middle = new JPanel();
		mainPanel.add(middle);
		middle.setLayout(new BorderLayout(0, 50));
		
		JLabel lblTable1 = new JLabel("Demonstration Table");
		lblTable1.setHorizontalAlignment(SwingConstants.CENTER);
		lblTable1.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		middle.add(lblTable1, BorderLayout.NORTH);
		
		JPanel middleMain = new JPanel();
		middle.add(middleMain, BorderLayout.CENTER);
		middleMain.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("People");
		lblNewLabel.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		middleMain.add(lblNewLabel, BorderLayout.NORTH);
		
		table1Panel = new JPanel();
		middleMain.add(table1Panel, BorderLayout.CENTER);
		table1Panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("Tasks");
		lblNewLabel_1.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel_1.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		middleMain.add(lblNewLabel_1, BorderLayout.WEST);

		JPanel tablePanel = new JPanel();
		mainPanel.add(tablePanel);
		tablePanel.setLayout(new BorderLayout(0, 50));
		
		JLabel lblTable2 = new JLabel("Original Values");
		tablePanel.add(lblTable2, BorderLayout.NORTH);
		lblTable2.setHorizontalAlignment(SwingConstants.CENTER);
		lblTable2.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		
		JPanel main = new JPanel();
		tablePanel.add(main, BorderLayout.CENTER);
		main.setLayout(new BorderLayout(0, 0));
		
				displayTable = new JPanel();
				main.add(displayTable, BorderLayout.CENTER);
				displayTable.setLayout(new BorderLayout(0, 0));
				
				JLabel lblNewLabel_2 = new JLabel("People");
				lblNewLabel_2.setFont(new Font("Century Gothic", Font.PLAIN, 20));
				lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
				main.add(lblNewLabel_2, BorderLayout.NORTH);
				
				JLabel lblNewLabel_3 = new JLabel("Tasks");
				lblNewLabel_3.setVerticalAlignment(SwingConstants.TOP);
				lblNewLabel_3.setFont(new Font("Century Gothic", Font.PLAIN, 20));
				main.add(lblNewLabel_3, BorderLayout.WEST);


	}

}
