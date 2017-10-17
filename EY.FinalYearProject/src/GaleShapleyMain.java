import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

// Performs the steps of the gale Shapley algorithm, and changes the interface to reflect this.
public class GaleShapleyMain extends JPanel{

	static Person[] Men; // array of person objects for the Men group
	String firstGroup; // used in printing text, depends which gender the bias is set to
	String scndGroup;
	static	Person[] Women; // person objects for the women's group
	static int numberOfPairs = 1;
	static	int numberOfEngaged; // how many pairs are engaged
	static GaleShapleyRun gui;
	private JLabel[] labels1;
	private JLabel[] labels2;
	int nextStep = 1; // keeps track of the next step to run
	int totalSteps = 0; // used for going backwards
	int currentStep = 0; // increments every time the 'next' button is pressed ( 
	int currentManI = 0; // index of the man currently being observed
	int currentWomanI = 0; // index of woman being observed
	int[] currentManPrefs; // the preferences of the current Man
	int[] currentFPrefs; // preferences of current Woman
	int bestFemale = -1; // will hold current man's top woman.
	boolean maleBias = false;
	boolean play = false; // when true, all steps are played, it doesn't wait for next button press
	CustomTableCellRenderer renderer; // renderer for changing colour of table cells
	CustomTableCellRenderer renderer2;
	ArrayList<DrawLine> lines; // list of the lines required for the graph
	int[] womensAverages; // list of which preference the women were assigned - used to calculate average

	//---------------Methods-----------------//

	public GaleShapleyMain(int pairs, boolean bias){
		// initialises variables and objects
		numberOfPairs = pairs; // value from set up page spinner
		Men = new Person[numberOfPairs];
		Women = new Person[numberOfPairs];
		numberOfEngaged = 0;
		gui = new GaleShapleyRun(); // start the gui
		lines = new ArrayList<DrawLine>();
		renderer = new CustomTableCellRenderer();
		renderer2 = new CustomTableCellRenderer();
		maleBias = bias;
		womensAverages = new int[numberOfPairs];

		// set strings + labels based on the bias
		if ( maleBias == true){
			firstGroup = "man";
			scndGroup = "woman";

			gui.lblGroup1.setText("Male Preferences");
			gui.lblGroup2.setText("Female Preferences");
		}else{
			scndGroup = "man";
			firstGroup = "woman";

			gui.lblGroup1.setText("Female Preferences");
			gui.lblGroup2.setText("Male Preferences");
		}

		gui.btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// play button pressed. Do all steps until finished
				play = true;
				nextStep();
			}
		});

		gui.btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// backwards step button pressed
				/* only do action if at least one step has been performed already
				 * sets total steps to the number of steps performed to this point, 
				 * then plays the algorithm from the start, to the step before 
				 */
				if (currentStep > 1){
					play = true;
					nextStep = 1;
					totalSteps = currentStep;
					currentStep = 0;
					clearEverything(); //
					nextStep();
				}
			}
		});
	}

	public void clearEverything(){
		// resets all variables for a fresh run.

		// clears the graph lines
		gui.middle.getGraphics().clearRect(0, 0, gui.middle.getWidth(), gui.middle.getHeight());
		numberOfEngaged = 0;
		gui.narration.setText(""); // clear text

		currentManI = 0;
		currentWomanI = 0;
		bestFemale = -1;

		lines.clear();
		lines = new ArrayList<DrawLine>();

		// clear the table cell colours
		for ( int i = 0; i < numberOfPairs; i++){
			for ( int j = 0; j < numberOfPairs; j++){
				renderer.setRowColor(i, j, Color.white);
				gui.group1Table.getColumnModel().getColumn(j).setCellRenderer(renderer);

				renderer2.setRowColor(i, j, Color.white);
				gui.group2Table.getColumnModel().getColumn(j).setCellRenderer(renderer2);
			}

			Men[i].setPartner(-1);
			Women[i].setPartner(-1);
		}
		gui.group1Table.updateUI();
		gui.group2Table.updateUI();
	}

	public void setPrefs(ArrayList<JList<String>> prefs){
		// takes the array of preference lists, and puts them into the tables on the gui

		gui.group1 = new Object[numberOfPairs][numberOfPairs];
		gui.group2 = new Object[numberOfPairs][numberOfPairs];
		gui.columnNames = new String[numberOfPairs];

		// get each list, put in array, make a person with those preferences, add person to Men/women array
		for (int i = 0; i < 20; i++){

			JList<String> list = prefs.get(i);
			int[] tempPrefs = new int[numberOfPairs];

			for (int j = 0; j < numberOfPairs; j++){
				tempPrefs[j] = (Integer.parseInt(list.getModel().getElementAt(j))-1);
			}
			if (maleBias == true){ // men are put in first set of lists ( lists 0 - 9)
				if (i < numberOfPairs){
					Men[i] = new Person(numberOfPairs);
					Men[i].setPreferences(tempPrefs);
					gui.columnNames[i] = Integer.toString(i+1);
					for (int j = 0; j < numberOfPairs; j++){
						gui.group1[j][i] =( 1+ (tempPrefs[j]));
					}
				}
				else if ((i > 9)&& ( i < (10 + numberOfPairs))){ // women go in second set ( list 10 - 20)
					Women[(i - 10)] = new Person(numberOfPairs);
					Women[(i - 10)].setPreferences(tempPrefs);

					for (int j = 0; j < numberOfPairs; j++){
						gui.group2[j][(i-10)] = ( 1+ (tempPrefs[j]));
					}
				}
			}else{ // Women bias - so as above, but women are put in first set of lists, men in second set

				if ((i > 9)&& ( i < (10 + numberOfPairs))){
					Men[(i - 10)] = new Person(numberOfPairs);
					Men[(i - 10)].setPreferences(tempPrefs);

					for (int j = 0; j < numberOfPairs; j++){
						gui.group1[j][(i-10)] =( 1+ (tempPrefs[j]));
					}
				}
				else if (i < numberOfPairs){
					Women[i] = new Person(numberOfPairs);
					Women[i].setPreferences(tempPrefs);

					gui.columnNames[i] = Integer.toString(i+1);

					for (int j = 0; j < numberOfPairs; j++){
						gui.group2[j][i] = ( 1+ (tempPrefs[j]));
					}
				}
			}
		}

		// make the gui tables, set properties
		gui.group1Table = new JTable(gui.group1, gui.columnNames);
		gui.group2Table = new JTable(gui.group2, gui.columnNames);

		gui.table1Panel.add(gui.group1Table,  BorderLayout.CENTER);
		gui.table2Panel.add(gui.group2Table,  BorderLayout.CENTER);

		gui.table1Panel.add(gui.group2Table.getTableHeader(), BorderLayout.NORTH);
		gui.table2Panel.add(gui.group1Table.getTableHeader(), BorderLayout.NORTH);

		gui.group1Panel.add(gui.table1Panel, BorderLayout.CENTER);
		gui.group2Panel.add(gui.table2Panel, BorderLayout.CENTER);

	}

	public void addLabels(){
		// places the graph labels on the middle section

		labels1 = new JLabel[numberOfPairs];
		for (int i = 0; i < numberOfPairs; i++) {
			JLabel label = new JLabel(Integer.toString(i+1));
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			labels1[i] = label;
			gui.left.add(label);
		}

		labels2 = new JLabel[numberOfPairs];
		for (int i = 0; i < numberOfPairs; i++) {
			JLabel label = new JLabel(Integer.toString(i+1));
			label.setHorizontalAlignment(SwingConstants.LEFT);
			labels2[i] = label;
			gui.right.add(label);
		}

		// adds forward button to screen, when clicked the nextStep is done
		gui.btnForward = new JButton("->");
		gui.btnForward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextStep();
			}
		});
		gui.controls.add(gui.btnForward);
	}

	public void nextStep(){
		// The main method of the animation. Each case is a step of the algorithm, 
		//the correct step is done when the 'next' button is pressed

		currentStep++;
		switch (nextStep) {

		case 1: // get next  man, check he's single
			if ( numberOfEngaged < numberOfPairs){
				boolean repeat = true;

				while(repeat==true){
					if (currentManI > numberOfPairs-1){ // go from man 1 again
						currentManI = 0;
					}

					if (Men[currentManI].getPartner() == -1){ // do if man is single
						repeat = false;
						// print that he's single, and get his preferences
						gui.narration.append("\n" + firstGroup + " " + (currentManI+1) + " is single, ");
						currentManPrefs = Men[currentManI].getPreferences(); // get preferences of man i
						nextStep = 2;
					} else{ // current man not single, so go to the next one
						currentManI++;
						nextStep = 1;
					}
				}

			} else{ // all are engaged
				nextStep = 7; // finished, print results
				nextStep();
			}
			break;

		case 2: // man is single, get choices and propose
			if (currentWomanI < numberOfPairs){
				bestFemale = currentManPrefs[currentWomanI]; //get man's top choice
				gui.narration.append("so proposes to " + scndGroup+ " " + (bestFemale+1) + ".\n");

				// gets the index of the man proposing, in the Woman's preferences, 
				//so the cell can be highlighted
				currentFPrefs = Women[bestFemale].getPreferences();
				int newMan = 0;
				for (int i = 0; i < numberOfPairs; i++){
					if ( currentFPrefs[i] == (currentManI)){
						newMan = i;
					}
				}
				// highlights the woman which man 1 proposed to in his list
				renderer.setRowColor(currentWomanI, currentManI, Color.orange);
				gui.group1Table.getColumnModel().getColumn(currentManI).setCellRenderer(renderer);
				gui.group1Table.updateUI();
				// highlights the man which proposed to her, in her list
				renderer2.setRowColor(newMan, bestFemale, Color.orange);
				gui.group2Table.getColumnModel().getColumn(bestFemale).setCellRenderer(renderer2);
				gui.group2Table.updateUI();

				// gets start and end points to draw a line between the two graph labels
				// y coord is the ycoord of the man/woman's label
				Point p1 =  new Point(0, labels1[currentManI].getY());
				Point p2 = new Point(gui.middle.getWidth(), labels2[bestFemale].getY());
				p1.y = (p1.y + (labels1[currentManI].getHeight() /2));
				p2.y = (p2.y + (labels1[currentManI].getHeight() /2));

				// draw the line using the two points
				lines.add(new DrawLine(p1, p2, Color.orange, 3, currentManI, bestFemale, true)); 
				paintComponent(gui.middle.getGraphics());

				if (Women[bestFemale].getPartner() == -1){ // woman is single
					nextStep = 3; // accept proposal step
				}
				else { //woman is taken
					nextStep = 4; // check her preferences for next step
				}
			}
			break;

		case 3: // accept proposal.
			// print narration
			gui.narration.append(scndGroup+ " " + (bestFemale+1) + "  is single so accepts " + firstGroup + " " + (currentManI+1) + "'s proposal.\n");
			gui.narration.append(scndGroup+ " " + (bestFemale+1) + "  and " + firstGroup + " " + (currentManI+1) + " are engaged.\n");
			// sets the man and woman's partners
			acceptProposal(currentManI, bestFemale, Women[bestFemale].getPartner());
			// reset values to go again
			currentWomanI = 0;
			currentManI++;
			nextStep = 1;
			break;

		case 4: // get woman's preferences

			gui.narration.append(scndGroup + " " + (bestFemale+1) + " is engaged ");		 
			currentFPrefs = Women[bestFemale].getPreferences();
			// find out if she prefers new or current man
			int prefer = preferToCurrentPartner(currentManI, currentFPrefs, Women[bestFemale].getPartner());
			if ( prefer != -1){
				nextStep = 5; // she prefers new man
			}else{
				nextStep = 6; // prefers current man
			}
			break;

		case 5:
			// woman prefers new man, so accept his proposal

			gui.narration.append("but prefers " + firstGroup + " " + (currentManI+1) + ".\n");
			gui.narration.append(scndGroup+ " " + (bestFemale+1) + "  and " + firstGroup + " " + (currentManI+1) + " are engaged.\n");
			acceptProposal(currentManI, bestFemale, Women[bestFemale].getPartner());
			currentWomanI = 0;
			currentManI++;
			nextStep = 1;		
			break;

		case 6:
			// woman prefers current Man, 
			gui.narration.append("and prefers current " + firstGroup + " \n");
			// remove the orange proposal line between them
			removeLine(bestFemale, currentManI);
			// reset the orange cells to white
			renderer.setRowColor(currentWomanI, currentManI, Color.white);
			gui.group1Table.getColumnModel().getColumn(currentManI).setCellRenderer(renderer);
			gui.group1Table.updateUI();

			int newMan = 0;
			for (int i = 0; i < numberOfPairs; i++){
				if ( currentFPrefs[i] == (currentManI)){
					newMan = i;
				}
			}
			// reset cells to white
			renderer2.setRowColor(newMan, bestFemale, Color.white);
			gui.group2Table.getColumnModel().getColumn(bestFemale).setCellRenderer(renderer2);
			gui.group2Table.updateUI();

			currentWomanI++;
			nextStep = 2; // man proposes to next woman

			break;

		case 7:
			// finished. so print final results
			printResult();
			averageScores();
			currentStep--; // dont update once finished
			nextStep = 8;
			break;
		case 8:
			// dont keep updating this when button is pressed
			currentStep--;
			break;
		}
		// stops the algorithm playing at the step before ( to go backwards one step)
		if ( currentStep == totalSteps-1){
			play = false;
			totalSteps = 0;
		}

		if ( nextStep == 8){ // animation finished, so stop running
			play = false;
		} 

		if ( play == true){ // not finished yet, so keep playing
			nextStep();
		}
	}

	// returns a value to show if the woman prefers the new man to her current partner
	public int preferToCurrentPartner(int currentMan, int[] currentWoman, int spouse){
		int preference = -1;
		boolean found = false;
		int i = 0;
		// searches her preferences, to see if her spouse or the new man is higher in her list
		while ((i < numberOfPairs) && (found == false)){
			if(currentWoman[i] == spouse){
				found = true; // stop the search
				preference = -1; // she prefers her spouse
			} else if ( currentWoman[i] == currentMan){
				preference = currentMan;
				found = true; // stop the search
			}
			i++;
		}
		return preference;
	}

	public void acceptProposal(int currentMan, int currentWoman, int oldSpouse){
		// sets and resets values to show a proposal has been accepted

		// change line to green between the two
		Men[currentMan].setPartner(currentWoman);
		changeLineColour(currentWoman, currentMan, Color.green);

		// change cell colours to green
		renderer.setRowColor(currentWomanI, currentManI, Color.green);
		gui.group1Table.getColumnModel().getColumn(currentManI).setCellRenderer(renderer);
		gui.group1Table.updateUI();
		int newMan = 0;
		for (int i = 0; i < numberOfPairs; i++){
			if ( currentFPrefs[i] == (currentManI)){
				newMan = i;
			}
		}
		renderer2.setRowColor(newMan, bestFemale, Color.green);
		gui.group2Table.getColumnModel().getColumn(bestFemale).setCellRenderer(renderer2);
		gui.group2Table.updateUI();
		womensAverages[bestFemale] = newMan;

		if(oldSpouse!=-1){ // if woman was engaged, reset her spouse to single
			Men[oldSpouse].setPartner(-1);
			gui.narration.append(firstGroup + " " + (oldSpouse+1) + " is single again.\n");
			// remove the line between them
			removeLine(currentWoman, oldSpouse);

			// get index of the woman in her spouse's list
			int womanIndex = 0;
			int[] oldManPrefs = Men[oldSpouse].getPreferences();
			for (int i = 0; i < numberOfPairs; i++){
				if ( oldManPrefs[i] == (currentWoman)){
					womanIndex = i;
				}
			}
			// set that cell to white...
			renderer.setRowColor(womanIndex, oldSpouse, Color.white);
			gui.group1Table.getColumnModel().getColumn(oldSpouse).setCellRenderer(renderer);
			gui.group1Table.updateUI();
			// get index of woman's old spouse, change cell to white
			int oldMan = 0;
			for (int i = 0; i < numberOfPairs; i++){
				if ( currentFPrefs[i] == (oldSpouse)){
					oldMan = i;
				}
			}
			renderer2.setRowColor(oldMan, bestFemale, Color.white);
			gui.group2Table.getColumnModel().getColumn(bestFemale).setCellRenderer(renderer2);
			gui.group2Table.updateUI();
		}
		else{ // woman was single, so just set her partner. easy
			numberOfEngaged++;
		}
		Women[currentWoman].setPartner(currentMan);
	}

	public void changeLineColour(int to, int from, Color c){
		// change the colour of the line from one point to another from orange to green

		// find the orange line in the list, and set it's colour to green
		for ( int i = 0; i < lines.size(); i++){
			DrawLine current = lines.get(i);
			if (( current.personFrom == from)&& (current.personTo == to)&& (current.colour != c)){
				lines.get(i).colour = c;
			}
		}
		paintComponent(gui.middle.getGraphics());
	}

	public void removeLine(int woman, int spouse){
		// draws over the line to remove in the background colour...
		DrawLine newLine;

		for ( int i = 0; i < lines.size(); i++){
			DrawLine current = lines.get(i);
			// find the line to remove, sset the colour to the abckground colour, and increase thickness to it's completely covered
			if (( current.personFrom == spouse)&& (current.personTo == woman)&& (current.colour != gui.middle.getBackground())){
				newLine = current;
				newLine.colour = gui.middle.getBackground();
				newLine.thickness = 5;
				lines.add(newLine);
				lines.get(i).addLine = false;
			}
		}
		paintComponent(gui.middle.getGraphics());
	}

	public void printResult(){
		// outputs the final pairs in the narration box
		gui.narration.append("\nThe final Pairings are:");
		for (int i = 0; i < numberOfPairs; i++){
			gui.narration.append("\n" + firstGroup + " " + (i+1) + " is paired with " + scndGroup + " " + (Men[i].getPartner()+1));
		}
	}

	public void averageScores(){
		// first group first
		String group1;
		String group2;

		if (maleBias){
			group1 = "men";
			group2 = "women";
		}
		else{
			group1 = "women";
			group2 = "men";
		}
		// gets the position of the partner in the man's list, add them all together
		double total = 0;
		for (int i = 0; i < numberOfPairs; i++){
			for (int j = 0; j < numberOfPairs; j++){
				int[] prefs = Men[i].getPreferences();
				if (prefs[j] == (Men[i].getPartner())){
					total = total + (j+1);
				}
			}
		}
		// average, round, and print
		total = total/numberOfPairs;
		DecimalFormat df = new DecimalFormat("#.##");
		gui.narration.append("\n\nAverage choice assigned for " + group1 + " = " + df.format(total));

		total = 0;

		//second group - sum of the values in the average array
		for (int i = 0; i < numberOfPairs; i++){
			total = total + womensAverages[i];
		}
		total = total + numberOfPairs;
		// average, round, and print
		total = total/numberOfPairs;
		gui.narration.append("\nAverage choice assigned for " + group2 + " = " + df.format(total));

	}

	// the line stuff...


	@Override
	public void paintComponent(Graphics g) {
		// takes the list of lines, and draws them. First drawing line sin the background shade, then the coloured lines

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		for (int i = 0; i < lines.size(); i++){
			DrawLine currentLine = lines.get(i);

			if ( currentLine.colour == gui.middle.getBackground()){
				// draw these lines first to stop them going over the coloured ones, and having gaps
				g2.setPaint(currentLine.colour);
				g2.setStroke(new BasicStroke(currentLine.thickness));
				g2.draw(new Line2D.Double(currentLine.start.x, currentLine.start.y, currentLine.end.x, currentLine.end.y));
			}
		}
		// go through list again and print coloured lines
		for (int i = 0; i < lines.size(); i++){

			DrawLine currentLine = lines.get(i);

			if (( currentLine.colour != gui.middle.getBackground())&& ( currentLine.addLine == true)){
				g2.setPaint(currentLine.colour);
				g2.setStroke(new BasicStroke(currentLine.thickness));
				g2.draw(new Line2D.Double(currentLine.start.x, currentLine.start.y, currentLine.end.x, currentLine.end.y));
			}
		}

	}

	// handles the colouring of table cells
	class CustomTableCellRenderer extends DefaultTableCellRenderer {

		private Color[][] mapColors; // array of cell's colours

		public CustomTableCellRenderer() {
			mapColors = new Color[numberOfPairs][numberOfPairs];
		}

		// given x and y, set that cell's new colour
		public void setRowColor(int row, int column, Color color) {
			this.mapColors[row][column] = color;
		}

		@Override
		// change the cells colours on the screen
		public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
			Component cell = super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, 1);
			Color color = mapColors[row][column];
			if (color != null) {
				cell.setBackground(color);
			} else {
				cell.setBackground(Color.white);
			}
			return cell;
		}
	}
}