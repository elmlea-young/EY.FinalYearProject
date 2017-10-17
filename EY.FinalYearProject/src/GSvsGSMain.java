import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
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

// runs the GS vs GS steps
// Variables and steps as in the singular GS animation example
public class GSvsGSMain {

	static Person[] Men; // array or Person objects, for the Men
	String firstGroup;
	String scndGroup;
	static	Person[] Women; // array of women
	static int numberOfPairs = 1;
	static	int numberOfEngaged; // how many are paired
	static GSvsGSRun gui;
	private JLabel[] labels1; // labels for the graphs
	private JLabel[] labels2;
	private JLabel[] labels3;
	private JLabel[] labels4;
	int nextStep = 1; // next step for the first animation
	int totalSteps = 0;
	int currentStep = 0;
	int currentManI = 0; 
	int currentWomanI = 0;
	int[] currentManPrefs;
	int[] currentFPrefs;
	int bestFemale = -1;
	boolean play = false;
	CustomTableCellRenderer renderer; // cell renderers for this animation
	CustomTableCellRenderer renderer2;
	ArrayList<DrawLine> lines;

	// --------------- second animation variables ----------------- //

	static Person[] Men2;
	static	Person[] Women2;
	static	int numberOfEngaged2;
	int nextStep2 = 1;
	int totalSteps2 = 0;
	int currentStep2 = 0;
	int currentManI2 = 0;
	int currentWomanI2 = 0;
	int[] currentManPrefs2;
	int[] currentFPrefs2;
	int bestFemale2 = -1;
	boolean play2 = false;
	CustomTableCellRenderer renderer3;
	CustomTableCellRenderer renderer4;
	ArrayList<DrawLine> lines2;
	int[] womensAverages;
	int[] mensAverages;
	int[] womensAverages2;

	//---------------Methods-----------------//

	public GSvsGSMain(int pairs){
		// initialise variables and objects
		numberOfPairs = pairs;
		Men = new Person[numberOfPairs];
		Women = new Person[numberOfPairs];

		Men2 = new Person[numberOfPairs];
		Women2 = new Person[numberOfPairs];

		womensAverages = new int[numberOfPairs];
		mensAverages = new int[numberOfPairs];
		womensAverages2 = new int[numberOfPairs];

		numberOfEngaged = 0;
		numberOfEngaged2 = 0;
		gui = new GSvsGSRun();

		lines = new ArrayList<DrawLine>();
		lines2 = new ArrayList<DrawLine>();

		renderer = new CustomTableCellRenderer();
		renderer2 = new CustomTableCellRenderer();
		renderer3 = new CustomTableCellRenderer();
		renderer4 = new CustomTableCellRenderer();

		gui.btnPlay.addActionListener(new ActionListener() {
			// play to the end of both animations
			public void actionPerformed(ActionEvent e) {
				play = true;
				play2 = true;
				nextStep();
				nextStep2();
			}
		});

		gui.btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// goes one step backwards in each animation by playing to the step before.
				
				if (currentStep > 1){ // only do if first animation had already done a step
					//reset variables
					play = true;
					nextStep = 1;
					totalSteps = currentStep;
					currentStep = 0;
					
					clearEverything();
					nextStep();
					
				}
				if (currentStep2 > 1){ //same for second animation
					
					play2 = true;
					nextStep2 = 1;
					totalSteps2 = currentStep2;
					currentStep2 = 0;
					
					clearEverything2();
					nextStep2();
					
				}
			}
		});
	}

	public void clearEverything(){
		// reset all variables of first algorithm to go from the start again
		numberOfEngaged = 0;
		gui.narration.setText("");

		currentManI = 0;
		currentWomanI = 0;
		bestFemale = -1;

		// clear the graph lines
		lines.clear();
		gui.middle.getGraphics().clearRect(0, 0, gui.middle.getWidth(), gui.middle.getHeight());

		// clear table colours
		for ( int i = 0; i < numberOfPairs; i++){
			for ( int j = 0; j < numberOfPairs; j++){
				renderer.setRowColor(i, j, Color.white);
				gui.leftTable1.getColumnModel().getColumn(j).setCellRenderer(renderer);
				gui.leftTable1.updateUI();

				renderer2.setRowColor(i, j, Color.white);
				gui.leftTable2.getColumnModel().getColumn(j).setCellRenderer(renderer2);
				gui.leftTable2.updateUI();
			}

			Men[i].setPartner(-1);
			Women[i].setPartner(-1);
		}
	}

	public void clearEverything2(){ // same for second animation
		numberOfEngaged2 = 0;
		gui.rightNarration.setText("");

		currentManI2 = 0;
		currentWomanI2 = 0;
		bestFemale2 = -1;

		lines2.clear();
		gui.rMiddle.getGraphics().clearRect(0, 0, gui.rMiddle.getWidth(), gui.rMiddle.getHeight());

		for ( int i = 0; i < numberOfPairs; i++){
			for ( int j = 0; j < numberOfPairs; j++){

				renderer3.setRowColor(i, j, Color.white);
				gui.rightTable1.getColumnModel().getColumn(j).setCellRenderer(renderer3);
				gui.rightTable1.updateUI();

				renderer4.setRowColor(i, j, Color.white);
				gui.rightTable2.getColumnModel().getColumn(j).setCellRenderer(renderer4);
				gui.rightTable2.updateUI();
			}

			Men2[i].setPartner(-1);
			Women2[i].setPartner(-1);
		}
	}

	
	// sets preference values for both groups using the parameters from the set-up page
	public void setPrefs(ArrayList<JList<String>> prefs){

		gui.group1 = new Object[numberOfPairs][numberOfPairs];
		gui.group2 = new Object[numberOfPairs][numberOfPairs];
		gui.group3 = new Object[numberOfPairs][numberOfPairs];
		gui.group4 = new Object[numberOfPairs][numberOfPairs];
		gui.columnNames = new String[numberOfPairs];

		// get each list of preferences, put in array
		for (int i = 0; i < 20; i++){

			JList<String> list = prefs.get(i);
			int[] tempPrefs = new int[numberOfPairs];

			for (int j = 0; j < numberOfPairs; j++){
				tempPrefs[j] = (Integer.parseInt(list.getModel().getElementAt(j))-1);
			}
			
			// make person with the preferences, and add them to the group
			if (i < numberOfPairs){
				Men[i] = new Person(numberOfPairs);
				Men[i].setPreferences(tempPrefs);
				Women2[i] = new Person(numberOfPairs);
				Women2[i].setPreferences(tempPrefs);
				gui.columnNames[i] = Integer.toString(i+1);
				for (int j = 0; j < numberOfPairs; j++){
					gui.group1[j][i] =( 1+ (tempPrefs[j]));
				}
			}
			else if ((i > 9)&& ( i < (10 + numberOfPairs))){
				Women[(i - 10)] = new Person(numberOfPairs);
				Women[(i - 10)].setPreferences(tempPrefs);
				Men2[(i - 10)] = new Person(numberOfPairs);
				Men2[(i - 10)].setPreferences(tempPrefs);

				for (int j = 0; j < numberOfPairs; j++){
					gui.group2[j][(i-10)] = ( 1+ (tempPrefs[j]));
				}
			}	
		}
		// put the preferences in the gui tables
		for (int i = 0; i < numberOfPairs; i++){
			for (int j = 0; j < numberOfPairs; j++){
				gui.group3[i][j] = gui.group2[i][j];
				gui.group4[i][j] = gui.group1[i][j];
			}
		}

		gui.rightTable1 = new JTable(gui.group3, gui.columnNames);
		gui.rightTable2 = new JTable(gui.group4, gui.columnNames);

		gui.group1Panel.add(gui.rightTable1, BorderLayout.CENTER);
		gui.group1Panel.add(gui.rightTable1.getTableHeader(), BorderLayout.NORTH);
		gui.rightTable1.setFillsViewportHeight(true);

		gui.group2Panel.add(gui.rightTable2, BorderLayout.CENTER);
		gui.group2Panel.add(gui.rightTable2.getTableHeader(), BorderLayout.NORTH);
		gui.rightTable2.setFillsViewportHeight(true);

		gui.leftTable1 = new JTable(gui.group1, gui.columnNames);
		gui.leftTable2 = new JTable(gui.group2, gui.columnNames);

		gui.leftTable1Panel.add(gui.leftTable1, BorderLayout.CENTER);
		gui.leftTable1Panel.add(gui.leftTable1.getTableHeader(), BorderLayout.NORTH);
		gui.leftTable1.setFillsViewportHeight(true);

		gui.leftTable2Panel.add(gui.leftTable2, BorderLayout.CENTER);
		gui.leftTable2Panel.add(gui.leftTable2.getTableHeader(), BorderLayout.NORTH);
		gui.leftTable2.setFillsViewportHeight(true);

	}

	// adds the numbers onto the screen for the graph sections
	public void addLabels(){

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

		labels3 = new JLabel[numberOfPairs];
		for (int i = 0; i < numberOfPairs; i++) {
			JLabel label = new JLabel(Integer.toString(i+1));
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			labels3[i] = label;
			gui.rLeft.add(label);
		}

		labels4 = new JLabel[numberOfPairs];
		for (int i = 0; i < numberOfPairs; i++) {
			JLabel label = new JLabel(Integer.toString(i+1));
			label.setHorizontalAlignment(SwingConstants.LEFT);
			labels4[i] = label;
			gui.rRight.add(label);
		}

		gui.btnForward = new JButton("->");
		gui.btnForward.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		gui.btnForward.addActionListener(new ActionListener() {
			// adds forwards button to controls
			public void actionPerformed(ActionEvent e) {
				// does the next step for both animations
				nextStep();
				nextStep2();
			}
		});
		gui.controls.add(gui.btnForward);
	}

	public void nextStep(){ // performs the next step of the animation

		firstGroup = "man";
		scndGroup = "woman";

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
						gui.narration.append("\n" + firstGroup + " " + (currentManI+1) + " is single, ");
						currentManPrefs = Men[currentManI].getPreferences(); // get preferences of man i
						nextStep = 2;
					} else{
						currentManI++;
						nextStep = 1;
					}
				}

			} else{
				nextStep = 7; // finished, print results
				nextStep();
			}
			break;

		case 2: // man is single, get choices and propose

			if (currentWomanI < numberOfPairs){
				bestFemale = currentManPrefs[currentWomanI]; //get man's top choice
				gui.narration.append("so proposes to " + scndGroup+ " " + (bestFemale+1) + ".\n");

				currentFPrefs = Women[bestFemale].getPreferences();
				int newMan = 0;
				for (int i = 0; i < numberOfPairs; i++){
					if ( currentFPrefs[i] == (currentManI)){
						newMan = i;
					}
				}
				// colour table cells orange
				renderer.setRowColor(currentWomanI, currentManI, Color.orange);
				gui.leftTable1.getColumnModel().getColumn(currentManI).setCellRenderer(renderer);
				gui.leftTable1.updateUI();

				renderer2.setRowColor(newMan, bestFemale, Color.orange);
				gui.leftTable2.getColumnModel().getColumn(bestFemale).setCellRenderer(renderer2);
				gui.leftTable2.updateUI();
				
				// draw line between the man and woman
				Point p1 =  new Point(0, labels1[currentManI].getY());
				Point p2 = new Point(gui.middle.getWidth(), labels2[bestFemale].getY());
				p1.y = (p1.y + (labels1[currentManI].getHeight() /2));
				p2.y = (p2.y + (labels1[currentManI].getHeight() /2));

				lines.add(new DrawLine(p1, p2, Color.orange, 3, currentManI, bestFemale, true)); 
				paint(gui.middle.getGraphics(), true);

				if (Women[bestFemale].getPartner() == -1){ // woman is single, so accept proposal
					nextStep = 3;
				}
				else {
					nextStep = 4;
				}
			}
			break;

		case 3: // accept proposal.

			gui.narration.append(scndGroup+ " " + (bestFemale+1) + "  is single so accepts " + firstGroup + " " + (currentManI+1) + "'s proposal.\n");
			gui.narration.append(scndGroup+ " " + (bestFemale+1) + "  and " + firstGroup + " " + (currentManI+1) + " are engaged.\n");
			acceptProposal(currentManI, bestFemale, Women[bestFemale].getPartner(), true);
			currentWomanI = 0;
			currentManI++;
			nextStep = 1;
			break;

		case 4: // get woman's preferences

			gui.narration.append(scndGroup + " " + (bestFemale+1) + " is engaged ");		 
			currentFPrefs = Women[bestFemale].getPreferences();
			int prefer = preferToCurrentPartner(currentManI, currentFPrefs, Women[bestFemale].getPartner());
			if ( prefer != -1){ // prefers new man
				nextStep = 5;
			}else{ // prefers current partner
				nextStep = 6;
			}
			break;

		case 5: // she prefers new man, so accept his proposal

			gui.narration.append("but prefers " + firstGroup + " " + (currentManI+1) + ".\n");
			gui.narration.append(scndGroup+ " " + (bestFemale+1) + "  and " + firstGroup + " " + (currentManI+1) + " are engaged.\n");
			acceptProposal(currentManI, bestFemale, Women[bestFemale].getPartner(), true);
			currentWomanI = 0;
			currentManI++;
			nextStep = 1;		
			break;

		case 6: //prefers current man

			// narrate, colour table cells
			gui.narration.append("and prefers current " + firstGroup + ". \n");
			removeLine(bestFemale, currentManI, true);

			renderer.setRowColor(currentWomanI, currentManI, Color.white);
			gui.leftTable1.getColumnModel().getColumn(currentManI).setCellRenderer(renderer);
			gui.leftTable1.updateUI();

			int newMan = 0;
			for (int i = 0; i < numberOfPairs; i++){
				if ( currentFPrefs[i] == (currentManI)){
					newMan = i;
				}
			}

			renderer2.setRowColor(newMan, bestFemale, Color.white);
			gui.leftTable2.getColumnModel().getColumn(bestFemale).setCellRenderer(renderer2);
			gui.leftTable2.updateUI();

			currentWomanI++;
			nextStep = 2;

			break;

		case 7: // finished, print results
			printResult();
			averageScore1();
			currentStep--;
			nextStep = 8;
			break;
			
		case 8: // dont increment current step every time button is pressed once finished
			currentStep--;
			break;
		}
		// stop playing animation the step before 
		if ( currentStep == totalSteps-1){
			play = false;
			totalSteps = 0;
		}

		// finished, so stop playing
		if ( nextStep == 8){ 
			play = false;
		}

		// not finished playing, so go again
		if (play == true){
			nextStep();
		}
	}
	
	public void nextStep2(){ // Same as above but steps for the second animation...
		
		// ** in this case, the Women are proposing first, even though the language indicated it's the men. The women's preferences
		// were put into the Men2[] to keep the algorithm the same

		firstGroup = "man";
		scndGroup = "woman";
		
		currentStep2++;
		
		// **********************steps for second animation :/ **************************

		switch (nextStep2) {
		case 1: // get next  man, check he's single
			if ( numberOfEngaged2 < numberOfPairs){
				boolean repeat = true;

				while(repeat==true){
					if (currentManI2 > numberOfPairs-1){ // go from man 1 again
						currentManI2 = 0;
					}

					if (Men2[currentManI2].getPartner() == -1){ // do if man is single
						repeat = false;
						gui.rightNarration.append("\n" + scndGroup + " " + (currentManI2+1) + " is single, ");
						currentManPrefs2 = Men2[currentManI2].getPreferences(); // get preferences of man i
						nextStep2 = 2;
					} else{
						currentManI2++;
						nextStep2 = 1;
					}
				}

			} else{
				nextStep2 = 7; // finished, print results
				nextStep2();
			}
			break;

		case 2: // man is single, get choices and propose

			if (currentWomanI2 < numberOfPairs){
				bestFemale2 = currentManPrefs2[currentWomanI2]; //get man's top choice
				gui.rightNarration.append("so proposes to " + firstGroup+ " " + (bestFemale2+1) + ".\n");

				currentFPrefs2 = Women2[bestFemale2].getPreferences();
				int newMan = 0;
				for (int i = 0; i < numberOfPairs; i++){
					if ( currentFPrefs2[i] == (currentManI2)){
						newMan = i;
					}
				}

				renderer3.setRowColor(currentWomanI2, currentManI2, Color.orange);
				gui.rightTable1.getColumnModel().getColumn(currentManI2).setCellRenderer(renderer3);
				gui.rightTable1.updateUI();

				renderer4.setRowColor(newMan, bestFemale2, Color.orange);
				gui.rightTable2.getColumnModel().getColumn(bestFemale2).setCellRenderer(renderer4);
				gui.rightTable2.updateUI();

				Point p1 =  new Point(0, labels3[currentManI2].getY());
				Point p2 = new Point(gui.rMiddle.getWidth(), labels4[bestFemale2].getY());
				p1.y = (p1.y + (labels3[currentManI2].getHeight() /2));
				p2.y = (p2.y + (labels3[currentManI2].getHeight() /2));

				lines2.add(new DrawLine(p1, p2, Color.orange, 3, currentManI2, bestFemale2, true)); 
				paint(gui.rMiddle.getGraphics(), false);


				if (Women2[bestFemale2].getPartner() == -1){ // woman is single, so accept proposal
					nextStep2 = 3;
				}
				else {
					nextStep2 = 4;
				}
			}
			break;

		case 3: // accept proposal.

			gui.rightNarration.append(firstGroup+ " " + (bestFemale2+1) + "  is single so accepts " + scndGroup + " " + (currentManI2+1) + "'s proposal.\n");
			gui.rightNarration.append(firstGroup+ " " + (bestFemale2+1) + "  and " + scndGroup + " " + (currentManI2+1) + " are engaged.\n");
			acceptProposal(currentManI2, bestFemale2, Women2[bestFemale2].getPartner(), false);
			currentWomanI2 = 0;
			currentManI2++;
			nextStep2 = 1;
			break;

		case 4: // get woman's preferences

			gui.rightNarration.append(firstGroup + " " + (bestFemale2+1) + " is engaged ");		 
			currentFPrefs2 = Women2[bestFemale2].getPreferences();
			int prefer = preferToCurrentPartner(currentManI2, currentFPrefs2, Women2[bestFemale2].getPartner());
			if ( prefer != -1){
				nextStep2 = 5;
			}else{
				nextStep2 = 6;
			}
			break;

		case 5: // She prefers new man

			gui.rightNarration.append("but prefers " + scndGroup + " " + (currentManI2+1) + ".\n");
			gui.rightNarration.append(firstGroup+ " " + (bestFemale2+1) + "  and " + scndGroup + " " + (currentManI2+1) + " are engaged.\n");
			acceptProposal(currentManI2, bestFemale2, Women2[bestFemale2].getPartner(), false);
			currentWomanI2 = 0;
			currentManI2++;
			nextStep2 = 1;		
			break;

		case 6: // she prefers current man

			gui.rightNarration.append("and prefers current " + scndGroup + ". \n");
			removeLine(bestFemale2, currentManI2, false);

			renderer3.setRowColor(currentWomanI2, currentManI2, Color.white);
			gui.rightTable1.getColumnModel().getColumn(currentManI2).setCellRenderer(renderer3);
			gui.rightTable1.updateUI();

			int newMan = 0;
			for (int i = 0; i < numberOfPairs; i++){
				if ( currentFPrefs2[i] == (currentManI2)){
					newMan = i;
				}
			}

			renderer4.setRowColor(newMan, bestFemale2, Color.white);
			gui.rightTable2.getColumnModel().getColumn(bestFemale2).setCellRenderer(renderer4);
			gui.rightTable2.updateUI();

			currentWomanI2++;
			nextStep2 = 2;

			break;

		case 7: // finished, print results
			
			printResult1();
			averageScore2();
			currentStep2--;
			nextStep2 = 8;
			break;
			
		case 8: // dont keep incrementing
			currentStep2--;
			break;
		}
			
		// stop playing on the step before
		if ( currentStep2 == totalSteps2-1){
			play2 = false;
			totalSteps2 = 0;
		}
		
		// finished so stop playing
		if ( nextStep2 == 8){
			play2 = false;
		}
		
		// playing, so go to next step
		if ( play2 == true){
			nextStep2();
		}
	}

	// returns which man is prefered out of the current spouce and the new man
	public int preferToCurrentPartner(int currentMan, int[] currentWoman, int spouse){
		int preference = -1;
		boolean found = false;
		int i = 0;
		// searched for spouse and new man in the preference list, sees which appears higher up
		while ((i < numberOfPairs) && (found == false)){
			if(currentWoman[i] == spouse){
				found = true;
				preference = -1;
			} else if ( currentWoman[i] == currentMan){
				preference = currentMan;
				found = true;
			}
			i++;
		}
		return preference;
	}

	// change the values of all people involved to show who is now engaged to who
	public void acceptProposal(int currentMan, int currentWoman, int oldSpouse, boolean left){

		if ( left == true){ // method was called by the first animation
			// change line to green
			Men[currentMan].setPartner(currentWoman);
			changeLineColour(currentWoman, currentMan, Color.green, left);

			// change cell colours to green
			renderer.setRowColor(currentWomanI, currentManI, Color.green);
			gui.leftTable1.getColumnModel().getColumn(currentManI).setCellRenderer(renderer);
			gui.leftTable1.updateUI();
			int newMan = 0;
			for (int i = 0; i < numberOfPairs; i++){
				if ( currentFPrefs[i] == (currentManI)){
					newMan = i;
				}
			}
			renderer2.setRowColor(newMan, bestFemale, Color.green);
			gui.leftTable2.getColumnModel().getColumn(bestFemale).setCellRenderer(renderer2);
			gui.leftTable2.updateUI();

			womensAverages[bestFemale] = newMan;

			if(oldSpouse!=-1){ // if woman was engaged, reset spouse to single
				Men[oldSpouse].setPartner(-1);
				gui.narration.append(firstGroup + " " + (oldSpouse+1) + " is single again.\n");
				removeLine(currentWoman, oldSpouse, left);

				int womanIndex = 0;
				int[] oldManPrefs = Men[oldSpouse].getPreferences();
				for (int i = 0; i < numberOfPairs; i++){
					if ( oldManPrefs[i] == (currentWoman)){
						womanIndex = i;
					}
				}

				// man cell to white...
				renderer.setRowColor(womanIndex, oldSpouse, Color.white);
				gui.leftTable1.getColumnModel().getColumn(oldSpouse).setCellRenderer(renderer);
				gui.leftTable1.updateUI();

				// get index of woman's old spouse, change cell to white
				int oldMan = 0;
				for (int i = 0; i < numberOfPairs; i++){
					if ( currentFPrefs[i] == (oldSpouse)){
						oldMan = i;
					}
				}
				renderer2.setRowColor(oldMan, bestFemale, Color.white);
				gui.leftTable2.getColumnModel().getColumn(bestFemale).setCellRenderer(renderer2);
				gui.leftTable2.updateUI();
			}
			else{
				numberOfEngaged++;
			}
			Women[currentWoman].setPartner(currentMan);
			
		}if ( left == false){ // method called by the second animation... same as above but for the second group
			// change line to green
			Men2[currentMan].setPartner(currentWoman);
			changeLineColour(currentWoman, currentMan, Color.green, left);

			// change cell colours to green
			renderer3.setRowColor(currentWomanI2, currentManI2, Color.green);
			gui.rightTable1.getColumnModel().getColumn(currentManI2).setCellRenderer(renderer3);
			gui.rightTable1.updateUI();
			
			womensAverages2[currentManI2] = currentWomanI2;
			
			int newMan = 0;
			for (int i = 0; i < numberOfPairs; i++){
				if ( currentFPrefs2[i] == (currentManI2)){
					newMan = i;
				}
			}
			renderer4.setRowColor(newMan, bestFemale2, Color.green);
			gui.rightTable2.getColumnModel().getColumn(bestFemale2).setCellRenderer(renderer4);
			gui.rightTable2.updateUI();
			mensAverages[bestFemale2] = newMan;

			if(oldSpouse!=-1){ // if woman was engaged, reset spouse to single
				Men2[oldSpouse].setPartner(-1);
				gui.rightNarration.append(scndGroup + " " + (oldSpouse+1) + " is single again.\n");
				removeLine(currentWoman, oldSpouse, left);

				int womanIndex = 0;
				int[] oldManPrefs = Men2[oldSpouse].getPreferences();
				for (int i = 0; i < numberOfPairs; i++){
					if ( oldManPrefs[i] == (currentWoman)){
						womanIndex = i;
					}
				}

				// man cell to white...
				renderer3.setRowColor(womanIndex, oldSpouse, Color.white);
				gui.rightTable1.getColumnModel().getColumn(oldSpouse).setCellRenderer(renderer3);
				gui.rightTable1.updateUI();

				// get index of woman's old spouse, change cell to white
				int oldMan = 0;
				for (int i = 0; i < numberOfPairs; i++){
					if ( currentFPrefs2[i] == (oldSpouse)){
						oldMan = i;
					}
				}
				renderer4.setRowColor(oldMan, bestFemale2, Color.white);
				gui.rightTable2.getColumnModel().getColumn(bestFemale2).setCellRenderer(renderer4);
				gui.rightTable2.updateUI();
			}
			else{
				numberOfEngaged2++;
			}
			Women2[currentWoman].setPartner(currentMan);
		}

	}

	// finds the line from point a to b and changes it's colour
	public void changeLineColour(int to, int from, Color c, boolean left){
		if ( left == true){ // for the first animation
			for ( int i = 0; i < lines.size(); i++){
				DrawLine current = lines.get(i);
				if (( current.personFrom == from)&& (current.personTo == to)&& (current.colour != c)){
					lines.get(i).colour = c;
				}
			}
			paint(gui.middle.getGraphics(), left);

		}
		else if ( left == false){// for second animation
			for ( int i = 0; i < lines2.size(); i++){
				DrawLine current = lines2.get(i);
				if (( current.personFrom == from)&& (current.personTo == to)&& (current.colour != c)){
					lines2.get(i).colour = c;
				}
			}
			paint(gui.rMiddle.getGraphics(), false);
		}
	}

	// draws over old line that's not needed anymore
	public void removeLine(int woman, int spouse, boolean left){

		// adds a new line with a bigger thickness and colour of the background
		DrawLine newLine;
		if ( left == true){ // first
			for ( int i = 0; i < lines.size(); i++){
				DrawLine current = lines.get(i);

				if (( current.personFrom == spouse)&& (current.personTo == woman)&& (current.colour != gui.middle.getBackground())){
					newLine = current;
					newLine.colour = gui.middle.getBackground();
					newLine.thickness = 5;
					lines.add(newLine);
					lines.get(i).addLine = false;
				}
			}

			paint(gui.middle.getGraphics(), left);
		}
		else if ( left == false){ // second
			for ( int i = 0; i < lines2.size(); i++){
				DrawLine current = lines2.get(i);

				if (( current.personFrom == spouse)&& (current.personTo == woman)&& (current.colour != gui.rMiddle.getBackground())){
					newLine = current;
					newLine.colour = gui.rMiddle.getBackground();
					newLine.thickness = 5;
					lines2.add(newLine);
					lines2.get(i).addLine = false;
				}
			}

			paint(gui.rMiddle.getGraphics(), left);
		}
	}

	// prints results of first animation
	public void printResult(){

		gui.narration.append("\nFinal Pairs are: ");

		for (int i = 0; i < numberOfPairs; i++){
			gui.narration.append("\n" + firstGroup + " " + (i+1) + " is paired with " + scndGroup + " " + (Men[i].getPartner()+1));

		}
	}

	// prints results for second
	public void printResult1(){

		gui.rightNarration.append("\nFinal Pairs are: ");

		for (int i = 0; i < numberOfPairs; i++){
			gui.rightNarration.append("\n" + scndGroup + " " + (i+1) + " is paired with " + firstGroup + " " + ((Men2[i].getPartner()+1)));

		}
	}


	// paints the new line onto the correct panel for the graphs
	public void paint(Graphics g, boolean left) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if ( left == true){
			for (int i = 0; i < lines.size(); i++){
				DrawLine currentLine = lines.get(i);
				if ( currentLine.colour == gui.middle.getBackground()){

					g2.setPaint(currentLine.colour);
					g2.setStroke(new BasicStroke(currentLine.thickness));
					g2.draw(new Line2D.Double(currentLine.start.x, currentLine.start.y, currentLine.end.x, currentLine.end.y));
				}

			}

			for (int i = 0; i < lines.size(); i++){

				DrawLine currentLine = lines.get(i);

				if (( currentLine.colour != gui.middle.getBackground())&& ( currentLine.addLine == true)){

					g2.setPaint(currentLine.colour);
					g2.setStroke(new BasicStroke(currentLine.thickness));
					g2.draw(new Line2D.Double(currentLine.start.x, currentLine.start.y, currentLine.end.x, currentLine.end.y));
				}
			}
		}
		else if ( left == false){

			for (int i = 0; i < lines2.size(); i++){
				DrawLine currentLine = lines2.get(i);
				if ( currentLine.colour == gui.rMiddle.getBackground()){

					g2.setPaint(currentLine.colour);
					g2.setStroke(new BasicStroke(currentLine.thickness));
					g2.draw(new Line2D.Double(currentLine.start.x, currentLine.start.y, currentLine.end.x, currentLine.end.y));
				}

			}

			for (int i = 0; i < lines2.size(); i++){

				DrawLine currentLine = lines2.get(i);

				if (( currentLine.colour != gui.rMiddle.getBackground())&& ( currentLine.addLine == true)){

					g2.setPaint(currentLine.colour);
					g2.setStroke(new BasicStroke(currentLine.thickness));
					g2.draw(new Line2D.Double(currentLine.start.x, currentLine.start.y, currentLine.end.x, currentLine.end.y));
				}
			}

		}
	}
	
	// calculates the average score
	public void averageScore1(){
		
		double total = 0;
		for (int i = 0; i < numberOfPairs; i++){
			for (int j = 0; j < numberOfPairs; j++){
				int[] prefs = Men[i].getPreferences();
				if (prefs[j] == (Men[i].getPartner())){
					total = total + (j+1);
				}
			}
		}
		// average, round, print for men
		total = total/numberOfPairs;
		DecimalFormat df = new DecimalFormat("#.##");
		gui.narration.append("\n\nAverage choice assigned for men = " + df.format(total));
		total = 0;
		
		// then same for Women
		for (int i = 0; i < numberOfPairs; i++){
			total = total + womensAverages[i];
			total++;
		}
		
		total = total/numberOfPairs;
		df = new DecimalFormat("#.##");
		gui.narration.append("\nAverage choice assigned for women = " + df.format(total));

	}

	// Second group's averages
	public void averageScore2(){

		// men first
		double total=0;
		for (int i = 0; i < numberOfPairs; i++){
			total = total + womensAverages2[i];
			total++;
		}
		total = total/numberOfPairs;
		DecimalFormat df = new DecimalFormat("#.##");
		gui.rightNarration.append("\n\nAverage choice assigned for women = " + df.format(total));
		total = 0;
		
		// then women
		for (int i = 0; i < numberOfPairs; i++){
			total = total + mensAverages[i];
			total++;
		}
		
		total = total/numberOfPairs;
		df = new DecimalFormat("#.##");
		gui.rightNarration.append("\nAverage choice assigned for men = " + df.format(total));
	}


	// class to change the colours of the table cells
	class CustomTableCellRenderer extends DefaultTableCellRenderer {

		private Color[][] mapColors; // array of the cell's colours

		public CustomTableCellRenderer() {
			this.mapColors = new Color[numberOfPairs][numberOfPairs];
		}

		public void setRowColor(int row, int column, Color color) {
			this.mapColors[row][column] = color; // change a cell's colour
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
			// paint the table cells using the colour array
			Component cell = super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, 1);
			Color color = this.mapColors[row][column];
			if (color != null) {
				cell.setBackground(color);
			} else {
				cell.setBackground(Color.white);
			}
			return cell;
		}
	}
}