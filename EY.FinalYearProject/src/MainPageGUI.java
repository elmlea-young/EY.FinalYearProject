import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JOptionPane.*;

/*
 * Java swing for the main homepage, where one of the four options can be selected.
 *  built in eclipse window builder
 */

public class MainPageGUI {
	private JFrame mainFrame;
	private JPanel gsPanel;
	private JPanel hungarianPanel;
	private JPanel gsVShPanel;
	private JPanel gsVSgsPanel;
	private GaleShapleySetUp gsSetUp;
	private HungarianSetUp hSetUp;
	private GSvsGSSetUp gVgSetUp;
	private GSvsHSetUp GSvHSetUp;
	private InfoPopup info;

	public MainPageGUI(){
		prepareGUI();
	}

	public static void main(String[] args){
		MainPageGUI mainWindow = new MainPageGUI(); 
	}

	private void prepareGUI(){
		mainFrame = new JFrame("Home");
		mainFrame.setSize(1500,800);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.getContentPane().setLayout(new GridLayout(2, 2));

		galeShapleyPanel();
		hungarianPanel();
		gsVShPanel();
		gsVSgsPanel();

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
			}        
		});  

		// create and add each of the four panels
		mainFrame.getContentPane().add(gsPanel);
		mainFrame.getContentPane().add(hungarianPanel);
		mainFrame.getContentPane().add(gsVShPanel);
		mainFrame.getContentPane().add(gsVSgsPanel);
		mainFrame.setVisible(true);  
	}

	private void galeShapleyPanel(){
		// set the layout of the Gale shapley panel. info button, title, description, start button
		gsPanel = new JPanel();
		gsPanel.setSize(400,400);
		gsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		GridLayout gl_gsPanel = new GridLayout(4, 1);
		gsPanel.setLayout(gl_gsPanel);

		JButton infobtn = new JButton("More Info...");
		infobtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				info = new InfoPopup();
				info.changeText("The Gale Shapley algorithm provides a solution to the stable marriage problem, which is finding a stable matching between two groups, "
						+ "where each element gives a list of preferences of who they would want to be paired with from the other group. \n"
						+ "\nHere we will use men and women and each person ranks the opposite sex in order"
						+ " of preference. The algorithm uses these preferences to pair together a man and woman to produce a final result which is 'stable'. "
						+ "A stable matching exists when there are no two people of the opposite sex who would both rather have each other than their current partners. "
						+ "In this example, there will always be an equal number of men and women, so no-one is left out. \n"
						+ "\nThe animation gives a step by step guide to how the algorithm comes to an end result, providing narration for each step, a bipartite graph of who"
						+ " is paired to who, tables of preferences to show which choice each person is assigned to, and an overall value of 'average choice assigned' to "
						+ "indicate how successful the pairing is for each group.\n\n"
						+ "Other uses for the Gale Shapley algorithm include:\n"
						+ "Matching students to schools, and matching medical students to hospital residency positions.");
			}

		});


		infobtn.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout( new BorderLayout(50, 10));

		JLabel gsTitle = new JLabel("Gale Shapley", JLabel.CENTER);
		gsTitle.setFont(new Font("Century Gothic", Font.PLAIN, 40));
		JTextArea gsDescription = new JTextArea();
		gsDescription.setMargin(new Insets(2, 10, 2, 10));
		gsDescription.setBackground(SystemColor.control);
		gsDescription.setWrapStyleWord(true);
		gsDescription.setText("Run through the Gale Shapley algorithm to see how the stable marriage problem is solved."
				+ " Input the preferences for men and women and see who gets paired to who.");
		gsDescription.setLineWrap(true);
		gsDescription.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		JPanel buttonPanel = new JPanel();
		JButton start = new JButton("start");
		start.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		start.setActionCommand("gsStart");
		start.addActionListener(new ButtonClickListener());

		buttonPanel.add(start);
		infoPanel.add(infobtn,BorderLayout.WEST);

		gsPanel.add(infoPanel); 
		gsPanel.add(gsTitle);
		gsPanel.add(gsDescription);
		gsPanel.add(buttonPanel);
	}

	private void hungarianPanel(){

		// hungarian panel. info button, title, description, start button
		hungarianPanel = new JPanel();
		hungarianPanel.setSize(400,400);
		hungarianPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		hungarianPanel.setLayout(new GridLayout(4, 1));

		JButton infoBtn = new JButton("More Info...");
		infoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				info = new InfoPopup();
				info.changeText("The Hungarian algorithm also solves assignment problems, where there's two groups"
						+ " which need to be put into pairs somehow. In this example we use 'people' and 'tasks'"
						+ " for the two groups and each person gives a weighted preference for each task (the lower"
						+ " the weighted value, the Less that person costs to do that task).\n"
						+ "\nThe aim of the Hungarian algorithm is to pair together elements in a way which gives"
						+ " the lowest possible social welfare value. Which is the sum of the 'cost' for each person"
						+ " to do the assigned task. Unlike the Gale Shapley algorithm, a stable matching is not a"
						+ " requirement.\n"
						+ "\nAgain, there is an equal number of tasks and people, there's one task per person, and all"
						+ " tasks must be paired up.\n\nThe animation gives a step by step guide to how the Hungarian"
						+ " algorithm comes to the final result, this includes a narration to explain each step, and "
						+ "a table which highlights the values being changed or considered. The values for average"
						+ " choice assigned, and social welfare are calculated to provide some analysis of the final"
						+ " matching.\n\nOther uses for the Hungarian algorithm include:\n"
						+ "Matching bedrooms in a house with new occupants,"
						+ " each person ranks the rooms in order of preference, and the aim is to provide the highest "
						+ "social welfare value (keep everyone as happy as possible) ");

			}
		});


		infoBtn.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout( new BorderLayout(50, 10));
		JLabel hungarianTitle = new JLabel("Hungarian", JLabel.CENTER);
		hungarianTitle.setFont(new Font("Century Gothic", Font.PLAIN, 40));
		JTextArea hungDescription = new JTextArea();
		hungDescription.setMargin(new Insets(2, 10, 2, 10));
		hungDescription.setBackground(SystemColor.control);
		hungDescription.setLineWrap(true);
		hungDescription.setWrapStyleWord(true);
		hungDescription.setText("View the steps of the Hungarian algorithm to see how it produces matches with the best "
				+ "possible value for social welfare. This example uses 'people' and their weighted preferences towards 'tasks'.");
		hungDescription.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		JButton start = new JButton("start");
		start.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		start.setActionCommand("hungarianStart");
		start.addActionListener(new ButtonClickListener());

		JPanel buttonPanel = new JPanel();

		buttonPanel.add(start);
		infoPanel.add(infoBtn,BorderLayout.EAST);

		hungarianPanel.add(infoPanel);
		hungarianPanel.add(hungarianTitle);
		hungarianPanel.add(hungDescription);
		hungarianPanel.add(buttonPanel);
	}

	private void gsVShPanel(){

		//same again for the third panel
		gsVShPanel = new JPanel();
		gsVShPanel.setSize(400,400);
		gsVShPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		gsVShPanel.setLayout(new GridLayout(4, 1));

		JButton infoBtn = new JButton("More Info...");
		infoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				info = new InfoPopup();
				info.changeText("This is a side-by-side comparison of the animations of the two algorithms as "
						+ "seen in their individual examples. It’s to show how each algorithm comes to its own "
						+ "conclusion about the best matching which is often, but not always, different to each "
						+ "other. This is because each algorithm has different criteria for a successful pairing."
						+ " Values for average choice assigned and social welfare are included for both, which "
						+ "further helps with comparison because most of the time the Hungarian will have a"
						+ " lower social welfare than the Gale Shapley, and higher value for choice assigned (a"
						+ " higher value means a lower choice was assigned).\n\nThe two groups used are men and"
						+ " women as in the Gale Shapley example, so this requires weighted preference values to"
						+ " be input for both male and female groups, and the order of preferences is calculated "
						+ "from this. You also choose which gender the algorithm should be optimal for. ");
			}
		});
		infoBtn.setFont(new Font("Century Gothic", Font.PLAIN, 18));

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout( new BorderLayout(50, 10));
		JLabel gsVShTitle = new JLabel("Gale Shapley vs Hungarian", JLabel.CENTER);
		gsVShTitle.setFont(new Font("Century Gothic", Font.PLAIN, 40));
		JTextArea gsVShDescription = new JTextArea();
		gsVShDescription.setMargin(new Insets(2, 10, 2, 10));
		gsVShDescription.setBackground(SystemColor.control);
		gsVShDescription.setText("Using the same data, the two algorithms can produce different outcomes, this "
				+ "example allows comparison of their results by giving a social welfare value, and average choice"
				+ " value, to easily view how much they differ.");
		gsVShDescription.setWrapStyleWord(true);
		gsVShDescription.setLineWrap(true);
		gsVShDescription.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		JButton start = new JButton("start");
		start.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		start.setActionCommand("gsVShStart");
		start.addActionListener(new ButtonClickListener());

		JPanel buttonPanel = new JPanel();

		buttonPanel.add(start);
		infoPanel.add(infoBtn,BorderLayout.WEST);

		gsVShPanel.add(infoPanel);
		gsVShPanel.add(gsVShTitle);
		gsVShPanel.add(gsVShDescription);
		gsVShPanel.add(buttonPanel);
	}

	private void gsVSgsPanel(){

		// same again for the fourth panel

		gsVSgsPanel = new JPanel();
		gsVSgsPanel.setSize(400,400);
		gsVSgsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		gsVSgsPanel.setLayout(new GridLayout(4, 1));

		JButton infoBtn = new JButton("More Info...");
		infoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				info = new InfoPopup();
				info.changeText("This is a side-by-side comparison of the Gale Shapley algorithm, one runs"
						+ " optimal for men, the other optimal for women. This is to show how different the "
						+ "outcome can be just by changing which group proposes to the other. Average "
						+ "choice results are given for both men and women in both simulations to show "
						+ "the difference. \n\nTo explain the process this still includes a narration, "
						+ "tables, and bipartite graph. As in the singular simulation.");

			}
		});
		infoBtn.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		JPanel infoPanel = new JPanel();

		infoPanel.setLayout( new BorderLayout(50, 10));
		JLabel gsVSgsTitle = new JLabel("Gale Shapley vs Gale Shapley", JLabel.CENTER);
		gsVSgsTitle.setFont(new Font("Century Gothic", Font.PLAIN, 40));
		JTextArea gsVSgsDescription = new JTextArea();
		gsVSgsDescription.setMargin(new Insets(2, 10, 2, 10));
		gsVSgsDescription.setBackground(SystemColor.control);
		gsVSgsDescription.setText("A comparison of the results from the Gale Shapley algorithm when ran "
				+ "from both male and female perspectives. It emphasises how the group which proposes ends"
				+ " up with their best outcome by giving an average choice value for each group. ");
		gsVSgsDescription.setWrapStyleWord(true);
		gsVSgsDescription.setLineWrap(true);
		gsVSgsDescription.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		JButton start = new JButton("start");
		start.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		start.setActionCommand("gsVSgsStart");
		start.addActionListener(new ButtonClickListener());
		JPanel buttonPanel = new JPanel();

		buttonPanel.add(start);
		infoPanel.add(infoBtn,BorderLayout.EAST);

		gsVSgsPanel.add(infoPanel);
		gsVSgsPanel.add(gsVSgsTitle);
		gsVSgsPanel.add(gsVSgsDescription);
		gsVSgsPanel.add(buttonPanel);
	}     

	private class ButtonClickListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			//  called when any of the start buttons are clicked. then Starts the appropriate simulation
			String command = e.getActionCommand();  

			if( command.equals( "gsStart" ))  {
				mainFrame.setVisible(false);
				gsSetUp = new GaleShapleySetUp();
			}
			else if( command.equals( "hungarianStart" ) )  {

				mainFrame.setVisible(false);
				hSetUp = new HungarianSetUp();

			} 
			else if (command.equals("gsVShStart")) {
				mainFrame.setVisible(false);
				GSvHSetUp = new GSvsHSetUp();

			}  
			else if (command.equals("gsVSgsStart")) {

				mainFrame.setVisible(false);
				gVgSetUp = new GSvsGSSetUp();

			} 	
		}		
	}
}