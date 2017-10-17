
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
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
/*
 * Class for running the GS vs H animation. Code is basically the same as the individual examples
 */
public class HvsGSmain { 
	//************** Hungarian Variables **************8
	
	int[][] costMatrix; // the main matrix of weightings used throughout
	final int[][] originalMatrix; // keeps original values, as costMatrix changes
	int numberOfPairs; // number of pairs the user chose
	int lines = 0; // number or lines required to cover all 0s
	int currentR = 0; // current row being manipulated
	int currentC = 0; // current column being manipulated
	int[] verticalLines; // shows which columns have lines drawn on them ( 0 = no line, 1 = lines)
	int[] horizontalLines; // which rows have lines on them
	Object[][] tableData; // data for table model that stays the smae
	int minValue = 0; // smallest value
	int minValueIndex = -1; // index of smallest value
	HvsGSRun gui; // object for the gui when running
	Object[][] animatedData; // data for table modle which changes
	boolean play = false;
	int step = 1; // which step of the algorithm is next
	CustomTableCellRenderer renderer; // renderer for colouring in table cells
	CustomTableCellRenderer renderer2;
	int currentStep = 0; // counts how many steps have happened
	int totalSteps = 0; // used for going backwards
	boolean maleBias = true;
	private JLabel[] labels3; 
	private JLabel[] labels4;
	int[] reorderedResults; // ordered results for easier printing
	ArrayList<DrawLine> linesH; // list of the lines to draw on the graph


	//***************** GS variables ***********************
	static Person[] Men; // list of PErson objects for the men
	String firstGroup;
	String scndGroup;
	static	Person[] Women; // list of perosn objects for the women
	static	int numberOfEngagedGS; // number of pairs engaged
	private JLabel[] labels1;
	private JLabel[] labels2;
	int nextStepGS = 1; // which step to perform next
	int totalStepsGS = 0; // used for backwards
	int currentStepGS = 0; // total number of seps to this point
	int currentManI = 0; // current man being looked at
	int currentWomanI = 0; // current woman being looked at
	int[] currentManPrefs; //  current man's preferences
	int[] currentFPrefs; // current woman'e preferences
	int bestFemale = -1; // index of the woman the current man liked best
	boolean playGS = false; 
	CustomTableCellRenderer rendererGS; // renderers for colouring table cells
	CustomTableCellRenderer renderer2GS;
	ArrayList<DrawLine> linesGS; // list of lines to draw on the graph
	int[] womensAverages; // array of the chocie each woman has been assigned

	//constructor, creates the gui, sets costMatrix values, initialises other values for both animations
	public HvsGSmain(int[][] group1Prefs, int[][] group2Prefs, int pairs, boolean bias) {

		gui = new HvsGSRun();
		maleBias = bias;
		numberOfPairs = pairs;

		this.costMatrix = new int[numberOfPairs][numberOfPairs];
		originalMatrix = new int[numberOfPairs][numberOfPairs];
		gui.columnNames = new String[numberOfPairs]; 
		womensAverages = new int[numberOfPairs];

		Men = new Person[numberOfPairs];
		Women = new Person[numberOfPairs];
		numberOfEngagedGS = 0;
		linesGS = new ArrayList<DrawLine>();
		linesH = new ArrayList<DrawLine>();
		rendererGS = new CustomTableCellRenderer();
		renderer2GS = new CustomTableCellRenderer();	

		if ( maleBias == true){
			firstGroup = "man";
			scndGroup = "woman";
		}else{
			scndGroup = "man";
			firstGroup = "woman";
		}

		// get each list, put in hungarian array
		for (int i = 0; i < numberOfPairs; i++){

			int[] tempPrefs = new int[numberOfPairs];

			if (maleBias == true){

				for (int j = 0; j < numberOfPairs; j++){
					tempPrefs[j] = group1Prefs[j][i];
				}

				gui.columnNames[i] = Integer.toString(i+1);
				for (int j = 0; j < numberOfPairs; j++){
					costMatrix[j][i] = tempPrefs[j];
					originalMatrix[j][i] = costMatrix[j][i];
				}

			}else{

				for (int j = 0; j < numberOfPairs; j++){
					tempPrefs[j] = group2Prefs[j][i];
				}

				for (int j = 0; j < numberOfPairs; j++){
					costMatrix[j][i] = tempPrefs[j];
					originalMatrix[j][i] = costMatrix[j][i];
				}

			}
		}

		renderer = new CustomTableCellRenderer();
		renderer2 = new CustomTableCellRenderer();

		setPrefs(group1Prefs, group2Prefs);
		addLabels();

		// buttons' action listeners
		gui.btnPlay.addActionListener(new ActionListener() {
			// play both animations right to the end
			public void actionPerformed(ActionEvent e) {
				play = true;
				playGS = true;
				nextStep();
				nextStepGS();
			}
		});
		
		gui.btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				// goes to previous step of each animation, if that animation is past the first step
				// clear variables and play animation to the step before
				if (currentStep > 1){
					play = true;
					totalSteps = currentStep;
					currentStep = 0;
					clearEverything();
					nextStep();
				}
				if (currentStepGS > 1){
					playGS = true;
					nextStepGS = 1;
					totalStepsGS = currentStepGS;
					currentStepGS = 0;
					clearEverythingGS();
					nextStepGS();
				}
			}
		});

		gui.btnForward = new JButton("->");
		gui.btnForward.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		gui.btnForward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// play next step of Hungarian, then Gale Shapley (no delay though)
				nextStep();
				nextStepGS();
			}
		});
		gui.controls.add(gui.btnForward);

		// put the preferences in the gui tables
		populateTables();
	}
	
	// ******** Hungarian Methods ***********

	// before playing backwards, clear the variables of the hungarian
	public void clearEverything(){

		// remove any lines on the graph
		gui.middle1.getGraphics().clearRect(0, 0, gui.middle1.getWidth(), gui.middle1.getHeight());
		
		linesH.clear();
		
		lines = 0; 
		currentR = 0;
		currentC = 0; 		
		minValue = 0; 
		minValueIndex = -1; 		
		step = 1;
		verticalLines = new int[numberOfPairs];
		horizontalLines = new int[numberOfPairs];

		gui.rightNarration.setText("");
		// reset the tables to the original values and colour
		for(int i = 0; i < numberOfPairs; i ++){
			verticalLines[i] = 0;
			horizontalLines[i] = 0;
			for(int j = 0; j < numberOfPairs; j ++){
				costMatrix[i][j] = originalMatrix[i][j];
				gui.animatedTable.getModel().setValueAt(costMatrix[i][j], i, j);
				gui.animatedTable.updateUI();

				renderer.setRowColor(i, j, Color.white);
				gui.animatedTable.getColumnModel().getColumn(j).setCellRenderer(renderer);
				gui.animatedTable.updateUI();

				renderer2.setRowColor(i, j, Color.white);
				gui.originalValues.getColumnModel().getColumn(j).setCellRenderer(renderer2);
				gui.originalValues.updateUI();
			}
		}
	}

	//runs correct method depending on the step
	public void nextStep(){
		currentStep++;
		if (step ==  1){ // gets smallest value in row
			getSmallestInRow();
		}else if ( step == 2){ // reduces row by smallest value
			reduceRow();
			gui.rightNarration.append("\n");
		}else if ( step == 3){ // gets smallest value in column
			getSmallestInColumn();
		}else if ( step == 4){ // reduces columns by smallest value
			reduceColumn();
			gui.rightNarration.append("\n");
		}else if ( step == 5){ // works out the lines required to cover all 0s, and shades them in the table
			resetCells();
			markAcross0s();
			gui.rightNarration.append("\n");
		}else if ( step == 6){ // get smallest value not covered by a line
			getSmallestOverall();
			gui.rightNarration.append("\n");
		}else if (step == 7){ // alter uncovered, and twice covered values
			finalAlteration();
			gui.rightNarration.append("\n");
		}else if (step == 8){ // match the pairs, print results
			resetCells();
			int[] result = finalAssignment();
			printResults(result);

			int socialWelfare = getSocialWelfare(result);
			gui.rightNarration.append("\nSocial welfare value is: " + socialWelfare);
			averageScoreH();

		} else if ( step == -1){ // finished, dont need to do anything. but keep current step the same
			play = false;
			currentStep--;
		}
		// play to the step before, so stop playing now
		if (currentStep == totalSteps-1){
			play = false;
			totalSteps = 0;
		}
		// keep playing until finished
		if ( play == true){
			nextStep();
		}
	}

	public void printResults(int[] result){
		// output the pairings and colour the table cells, draw graph lines
		reorderedResults = new int[numberOfPairs];
		gui.rightNarration.append("The final pairings are:\n");
		for ( int i = 0; i < numberOfPairs; i++){
			renderer.setRowColor(i, result[i], Color.green);
			renderer2.setRowColor(i, result[i], Color.green);
			gui.animatedTable.getColumnModel().getColumn(result[i]).setCellRenderer(renderer);
			gui.animatedTable.updateUI();

			gui.originalValues.getColumnModel().getColumn(result[i]).setCellRenderer(renderer2);
			gui.originalValues.updateUI();

			for (int j = 0; j<numberOfPairs; j++){
				if (result[j] == i){
					gui.rightNarration.append(firstGroup + " " + (result[j]+1) + " paired to " + scndGroup + " " + (j+1) + "\n");

					// get the points to draw a line between
					Point p1 =  new Point(0, labels3[result[j]].getY());
					Point p2 = new Point(gui.middle1.getWidth(), labels3[j].getY());
					p1.y = (p1.y + (labels3[j].getHeight() /2));
					p2.y = (p2.y + (labels3[j].getHeight() /2));
					reorderedResults[result[j]] = j;

					linesH.add(new DrawLine(p1, p2, Color.green, 3, result[j], j, true)); 
					paint(gui.middle1.getGraphics(), linesH);
				}
			}
			play = false;
		}
	}
	
	// calculates the average choice assigned to this group
	public void averageScoreH(){

		double total = 0;
		int choiceNum = -1;
		int[] weights = new int[numberOfPairs];
		int[] prefs = new int[numberOfPairs];

		// person by person, get their preferences and find which one was assigned
		for (int i = 0; i < numberOfPairs; i++){
			for (int j = 0; j < numberOfPairs; j++){
				weights[j] = originalMatrix[j][i];
			}
			prefs = findPrefsFromWeight(weights);
			for (int j = 0; j < numberOfPairs; j++){
				if (reorderedResults[i] == prefs[j]){
					choiceNum = j+1;
					total = total + choiceNum;
				}
			}
		}

		// average, round and print
		total = total/numberOfPairs;
		DecimalFormat df = new DecimalFormat("#.##"); 
		gui.rightNarration.append("\nAverage choice assigned = " + df.format(total));
	}

	public void reduceColumn(){ // reduces values in the column by the smallest value
		if(minValue!=0){ // 
			gui.rightNarration.append("So all values in this column are decreased by " + minValue + "\n");

			for (int j = 0; j < numberOfPairs; j++) { // decrease all values in column by minValue
				costMatrix[j][currentC] -= minValue;
				gui.animatedTable.getModel().setValueAt(costMatrix[j][currentC], j, currentC);
				// colour cell orange
				renderer.setRowColor(j, currentC, Color.orange);
				gui.animatedTable.getColumnModel().getColumn(currentC).setCellRenderer(renderer);
				gui.animatedTable.updateUI();
			}

		}else{ // there's a 0 in this column, so no alterations required
			gui.rightNarration.append("so no action needed\n");	
			// reset cell colour
			renderer.setRowColor(minValueIndex, currentC, Color.white);
			gui.animatedTable.getColumnModel().getColumn(currentC).setCellRenderer(renderer);
			gui.animatedTable.updateUI();
		}

		currentC++; // next column
		step = 3; // go back to step 3 ( getting smallest value)
		if (currentC >= numberOfPairs){ // if been through all columns, go to step 5
			step = 5;
			currentC = 0;
		}
	}

	// gets overall social welfare value
	public int getSocialWelfare(int[] matchings){
		int totalWelfare = 0;
		
		// adds up the cells which are assigned
		for (int i = 0; i < numberOfPairs; i++){
			totalWelfare = totalWelfare+originalMatrix[i][matchings[i]];
		}
		return totalWelfare;
	}

	// finds the smallest value in the column
	public void getSmallestInColumn(){
		if(currentC == 0){ // reset table cell colour
			for (int j = 0; j < numberOfPairs; j++) {
				renderer.setRowColor(numberOfPairs-1, j, Color.white);
				gui.animatedTable.getColumnModel().getColumn(j).setCellRenderer(renderer);
				gui.animatedTable.updateUI();
			}
		}

		minValue = 100;
		minValueIndex = -1;
		for (int j = 0; j < numberOfPairs; j++) {
			if (costMatrix[j][currentC] < minValue) { // get smallest value and it's index in column
				minValue = costMatrix[j][currentC];
				minValueIndex = j;
			}
			if (currentC!=0){ // reset cells of last column to white
				renderer.setRowColor(j, currentC-1, Color.white);
				gui.animatedTable.getColumnModel().getColumn(currentC-1).setCellRenderer(renderer);
				gui.animatedTable.updateUI();
			}
		}
		// add narration, colour in cell yellow, go to step 5 - reducing the column
		gui.rightNarration.append("Smallest value in column " + (currentC+1) + " is " + minValue + "\n");
		renderer.setRowColor(minValueIndex, currentC, Color.yellow);
		gui.animatedTable.getColumnModel().getColumn(currentC).setCellRenderer(renderer);
		gui.animatedTable.updateUI();
		step = 4;
	}

	public void reduceRow(){
		if (minValue !=0){ // reduce all row's values by smallest value, colour them orange
			gui.rightNarration.append("So all values in this row are decreased by " + minValue + "\n");
			for (int j = 0; j < numberOfPairs; j++) {
				costMatrix[currentR][j] -= minValue;
				gui.animatedTable.getModel().setValueAt(costMatrix[currentR][j], currentR, j);

				renderer.setRowColor(currentR, j, Color.orange);
				gui.animatedTable.getColumnModel().getColumn(j).setCellRenderer(renderer);
				gui.animatedTable.updateUI();
			}
		}else{ //there's a 0 in the row, no alterations needed.
			gui.rightNarration.append("so no action needed\n");	
			renderer.setRowColor(currentR, minValueIndex, Color.white);
			gui.animatedTable.getColumnModel().getColumn(minValueIndex).setCellRenderer(renderer);
			gui.animatedTable.updateUI();
		}
		currentR++; // do for next row
		step = 1; // get smallest value in row
		if (currentR >= numberOfPairs){ // if all rows done, go to columns
			step = 3;			
			currentR = 0;
		}
	}

	public void getSmallestInRow(){
		minValue = 100;
		minValueIndex = -1;
		for (int j = 0; j < numberOfPairs; j++) {
			if (costMatrix[currentR][j] < minValue ) { // get smallest value in row
				minValue = costMatrix[currentR][j];
				minValueIndex = j;
			}
			if ( currentR!=0){ // reset last row to white
				renderer.setRowColor(currentR-1, j, Color.white);
				gui.animatedTable.getColumnModel().getColumn(j).setCellRenderer(renderer);
				gui.animatedTable.updateUI();
			}
		} // add narration and colour the cell in yellow
		gui.rightNarration.append("Smallest value in row " + (currentR+1) + " is " + minValue + "\n");
		renderer.setRowColor(currentR, minValueIndex, Color.yellow);
		gui.animatedTable.getColumnModel().getColumn(minValueIndex).setCellRenderer(renderer);
		gui.animatedTable.updateUI();
		step = 2; // reduce the row
	}

	// sets the values of each table on the gui
	public void populateTables(){

		String[] columnNames = new String[numberOfPairs];
		tableData = new Object[numberOfPairs][numberOfPairs];
		animatedData = new Object[numberOfPairs][numberOfPairs];

		for ( int i = 0; i < numberOfPairs; i++){
			for ( int j = 0; j < numberOfPairs; j++){
				tableData[i][j] = originalMatrix[i][j];
				animatedData[i][j] = originalMatrix[i][j];
			}
			columnNames[i] = Integer.toString(i);
		}

		// adds column labels to tables
		gui.originalValues = new JTable(tableData, gui.columnNames);
		gui.originalPanel.add(gui.originalValues.getTableHeader(), BorderLayout.NORTH);
		gui.originalPanel.add(gui.originalValues, BorderLayout.CENTER);

		gui.animatedTable = new JTable(animatedData, gui.columnNames);

		gui.animatedPanel.add(gui.animatedTable.getTableHeader(), BorderLayout.NORTH);
		gui.animatedPanel.add(gui.animatedTable, BorderLayout.CENTER);
	}

	// matches up each group
	public int[] finalAssignment(){
		int[] colsAssigned = new int[numberOfPairs]; // which rows have been matched up ( 1 = matched)
		int[] zerosPerCol = new int[numberOfPairs]; // how many zeros are in each row
		int totalZeros = 0; // total number of zeros in the matrix
		Arrays.fill(colsAssigned, -1); // initialise all values of rows assigned to -1

		// add up zeros in each column
		for (int i = 0; i < numberOfPairs; i++) {
			for ( int j = 0; j < numberOfPairs; j++){
				if ( costMatrix[j][i] == 0){
					zerosPerCol[j]++;
					totalZeros++;
				}
			}
		}

		while (totalZeros != 0){ // do until all are matched up
			int leastZerosCol = -1;// index of row with least 0s;
			int smallestRow = 100; // smallest number in that row

			for (int i = 0; i < numberOfPairs; i++){ // get row with least 0s, and how many that is
				if (zerosPerCol[i] < smallestRow && zerosPerCol[i]!=0){
					leastZerosCol = i; // row index
					smallestRow = zerosPerCol[i]; // number of 0s
				}	
			}

			for (int i = 0; i < numberOfPairs; i++){ // find the 0 in the row with the least number of 0s
				if (costMatrix[leastZerosCol][i] == 0 && colsAssigned[leastZerosCol] == -1){
					colsAssigned[leastZerosCol] = i; // assign that row to column i
					costMatrix[leastZerosCol][i] = 1; // set the 0 to 1 so it is ignored next time
					for ( int j = 0; j < numberOfPairs; j++){
						// remove any other 0s in that column, as it's now assigned
						if (costMatrix[j][i] == 0){
							costMatrix[j][i] = 1;
						}
					}
				}
				// remove any other zeros in that row so they're ignored too, as that row has already been assigned a column
				if (costMatrix[leastZerosCol][i] == 0 && colsAssigned[leastZerosCol] != -1){
					costMatrix[leastZerosCol][i] = 1;
				}
			}

			// reset variables to go again with the next row
			Arrays.fill(zerosPerCol, 0);
			totalZeros = 0;

			// update zeros per row and total zeros
			for (int i = 0; i < numberOfPairs; i++) {
				for ( int j = 0; j < numberOfPairs; j++){
					if ( costMatrix[i][j] == 0){
						zerosPerCol[i]++;
						totalZeros++;
					}
				}
			}
		}
		step = -1; // end of the algorithm
		return colsAssigned; 
	}

	// gets the smallest value in the matrix which isn't covered by a line
	public void getSmallestOverall(){
		minValue = 100;
		int x = 0; //row
		int y = 0; // column
		for (int i = 0; i< numberOfPairs; i++){
			for (int j = 0; j< numberOfPairs; j++){
				if (verticalLines[j] == 0 && horizontalLines[i] == 0){ // cell is not marked
					if (costMatrix[i][j] < minValue && costMatrix[i][j] > 0){ // compare to current smallest
						minValue = costMatrix[i][j];
						x = i;
						y = j;
					}
				}
			}
		}
		// narration and colour the cell yellow
		gui.rightNarration.append("The smallest uncovered value is " + minValue + "\n");
		renderer.setRowColor(x, y, Color.yellow);
		gui.animatedTable.getColumnModel().getColumn(y).setCellRenderer(renderer);
		gui.animatedTable.updateUI();

		step = 7; // final alterations
		currentR = 0;
		currentC = 0;
	}

	// smallest value is added to cells which are marked twice, subtracted if unmarked
	public void finalAlteration(){
		boolean done = false; // find a cell to alter before  requiring the user to press the button again
		while ( done == false){
			if (verticalLines[currentC] == 0 && horizontalLines[currentR] == 0){ // cell is not marked
				// narrate, colour, and change table value
				gui.rightNarration.append("This " + costMatrix[currentR][currentC] + " is uncovered, so is decreased by" + minValue + "\n");
				costMatrix[currentR][currentC] = costMatrix[currentR][currentC]-minValue;
				gui.animatedTable.getModel().setValueAt(costMatrix[currentR][currentC], currentR, currentC);
				renderer.setRowColor(currentR,currentC , Color.orange);
				gui.animatedTable.getColumnModel().getColumn(currentR).setCellRenderer(renderer);
				gui.animatedTable.updateUI();
				done = true;
			}
			else if (verticalLines[currentC] == 1 && horizontalLines[currentR] == 1){ // cell is marked twice
				// narrate, colour, and change value
				gui.rightNarration.append("This " + costMatrix[currentR][currentC] + " is covered by two lines, so is increased by " + minValue + "\n");
				costMatrix[currentR][currentC] = costMatrix[currentR][currentC]+minValue;
				gui.animatedTable.getModel().setValueAt(costMatrix[currentR][currentC], currentR, currentC);
				renderer.setRowColor(currentR,currentC , Color.orange);
				gui.animatedTable.getColumnModel().getColumn(currentR).setCellRenderer(renderer);
				gui.animatedTable.updateUI();
				done = true;
			} 
			currentC++; // go to next column
			if ( currentC >= numberOfPairs){ // next row and reset column if it reached the last column
				currentR++;
				currentC = 0;
			}
			if(currentR >= numberOfPairs){ // everything's been checked
				step=5; // go back to marking 0s
				done = true;
			}else{ // repeat for all cells
				step = 7;
			}
			if (currentC == 0 && currentR >= numberOfPairs){ // it's the last cell and no changes were needed -
				done= true; // break the loop
				step = 5; // go to  marking 0s
				if (!(currentStep==totalSteps-1)){
					nextStep(); // do it right away (next button was already pressed but no more alterations needed here)
				}
				}
		}
	}

	public void resetCells(){
		// reset all cells to white
		for ( int i = 0; i < numberOfPairs; i++){
			for (int j = 0; j< numberOfPairs; j++){
				renderer.setRowColor(i, j, Color.white);
				gui.animatedTable.getColumnModel().getColumn(j).setCellRenderer(renderer);
				gui.animatedTable.updateUI();
			}
		}
	}

	// find out how many lines required to cover all 0s. 
	public void markAcross0s(){ // the fun bit
		verticalLines = new int[numberOfPairs]; // which columns do/dont have lines
		horizontalLines = new int[numberOfPairs]; // rows that do/dont have lines
		int[] rowsAssigned = new int[numberOfPairs]; // which rows are assigned, and to which column
		int[] zerosPerRow = new int[numberOfPairs]; // number of zeros in each row
		int[] markedCols = new int[numberOfPairs]; // columns marked for drawing a line
		int[] markedRows = new int[numberOfPairs]; // rows marked for drawling a line
		int[] newlyMarkedCols = new int[numberOfPairs]; // columns marked in the current iteration
		int[] newlyMarkedRows = new int[numberOfPairs]; // rows marked in the current iteration
		int totalZeros = 0; // total number of zeros in the matrix
		int[][] copyMatrix = new int[numberOfPairs][numberOfPairs]; //copy of the matrix to alter, without messing up the original
		Arrays.fill(rowsAssigned, -1); // initialise
		int v = 0; // number of vertical lines
		int h = 0; // number of horizontal lines
		int count = 0;

		// add up zeros in each row
		for (int i = 0; i < numberOfPairs; i++) {
			for ( int j = 0; j < numberOfPairs; j++){
				copyMatrix[i][j] = costMatrix[i][j]; // populate copy matrix while we're here
				if (copyMatrix[i][j] == 0){
					zerosPerRow[i]++;
					totalZeros++;
				}
			}
		}

		int leastZerosRow = -1;// index of row with least 0s;
		int smallestRow = 100; // smallest number in that row

		for (int i = 0; i < numberOfPairs; i++){ // get row with least 0s, and how many that is
			if (zerosPerRow[i] < smallestRow && zerosPerRow[i]!=0){
				leastZerosRow = i; // row index
				smallestRow = zerosPerRow[i]; // least 0s
			}	
		}

		while (totalZeros != 0){ // repeat until all 0s are marked

			for (int i = 0; i < numberOfPairs; i++){ // find the 0 in the row
				if (copyMatrix[leastZerosRow][i] == 0 && rowsAssigned[leastZerosRow] == -1){
					
					rowsAssigned[leastZerosRow] = i;// assign that cell to that column
					copyMatrix[leastZerosRow][i] = 1; // change to 1 so it's ignored next time
					totalZeros--; // reduce because we've found one
					zerosPerRow[leastZerosRow]--; // reduce again

					// check for other 0s in that column
					for ( int j = 0; j < numberOfPairs; j++){
						// ignore and reduce for other zeros, as this column is now assigned
						if (copyMatrix[j][i] == 0){
							copyMatrix[j][i] = 1;
							totalZeros--;
							zerosPerRow[j]--;
						}
					}
				}	// ignore and reduce values if a 0 is found in an assigned row
				if (copyMatrix[leastZerosRow][i] == 0 && rowsAssigned[leastZerosRow] != -1){
					copyMatrix[leastZerosRow][i] = 1;
					totalZeros--;
					zerosPerRow[leastZerosRow]--;
				}
			}

			// Now need to find the next row with the smallest number of 0s. 
			if (leastZerosRow == numberOfPairs-1){ // just checked the last row, so search from the start
				count = 0;
			}else{
				count = leastZerosRow;
			}

			leastZerosRow = -1;// index of row with least 0s;
			smallestRow = 100; // smallest number in that row
			boolean same = true;
			
			// check remaining rows first, before searching from the first row, this is important for some circumstances
			for (int k = count; k < numberOfPairs; k++){ // get row with least 0s, and how many that is
				if (zerosPerRow[k] < smallestRow && zerosPerRow[k]!=0){
					leastZerosRow = k; // row index
					smallestRow = zerosPerRow[k]; // least 0s
				}	
			}

			// Then check from the top to see if there's a smaller one
			for (int k = 0; k < numberOfPairs; k++){ // get row with least 0s, and how many that is
				if (zerosPerRow[k] < smallestRow && zerosPerRow[k]!=0){
					leastZerosRow = k; // row index
					smallestRow = zerosPerRow[k]; // least 0s
				}if (zerosPerRow[k] != smallestRow && ( zerosPerRow[k] !=0)){
					same = false;
				}
			}
			// if two or more rows have the same number of 0s, go with the first one
			if (same == true){
				leastZerosRow = -1;// index of row with least 0s;
				smallestRow = 100; // smallest number in that row
				for (int k = 0; k < numberOfPairs; k++){ // get row with least 0s, and how many that is
					if (zerosPerRow[k] < smallestRow && zerosPerRow[k]!=0){
						leastZerosRow = k; // row index
						smallestRow = zerosPerRow[k]; // least 0s
					}
				}
			}
		}

		// okay, this is the fun bit
		int unassigned = 0; //number of rows that arent assigned a column
		int[] freeRow = new int[numberOfPairs]; // which rows aren't assigned ( 0 = unassigned)
		// initialise them ^^
		for (int i = 0; i < numberOfPairs; i++) {
			if (rowsAssigned[i] == -1){
				unassigned++;
				freeRow[i] = 1;
			}
		}
		if ( unassigned == 0 ){ // all rows are assigned, so number of lines needed = numberOfPairs
			lines = numberOfPairs;
			v = numberOfPairs; // just use vertical lines, could use either tho
			h = 0;
			Arrays.fill(markedCols, 1); // all columns will  have a line
			Arrays.fill(markedRows, 1); // markedRows will be toggled in a min, so this means no row/horizontal lines
		}
		while ( unassigned != 0){ // executed when there's rows that are unassigned

			Arrays.fill(newlyMarkedCols, 0); // initialise with 0s
			Arrays.fill(newlyMarkedRows, 0);

			// mark all rows that don't have an assigned column
			for ( int i = 0; i < numberOfPairs; i ++){
				if ( freeRow[i] == 1){ // 1 = not assigned
					markedRows[i] = 1;
					rowsAssigned[i] = 1; // set it to be assigned so it's ignored next time
					newlyMarkedRows[i] = 1;
				}
			}

			//mark columns with zeros in newly marked rows
			for ( int i = 0; i < numberOfPairs; i ++){
				if ( newlyMarkedRows[i] == 1){
					for ( int j = 0; j < numberOfPairs; j ++){
						if ( costMatrix[i][j] == 0){
							markedCols[j] = 1;
							newlyMarkedCols[j] = 1;
						}
					}
				}
			}

			// mark rows with assignments in newly marked columns
			for ( int i = 0; i < numberOfPairs; i ++){
				if ( newlyMarkedCols[i] == 1){
					for ( int j = 0; j < numberOfPairs; j ++){
						if(rowsAssigned[j] == i){
							markedRows[j] = 1;
							newlyMarkedRows[j] = 1;
						}
					}
				}
			}

			// update values and go again.
			unassigned = 0;
			freeRow = new int[numberOfPairs];
			Arrays.fill(freeRow, 0);
			for (int i = 0; i < numberOfPairs; i++) {
				if (rowsAssigned[i] == -1){
					unassigned++;
					freeRow[i] = 1;
				}
			}
		}

		lines = 0; //number of lines to draw
		v = 0; // number of vertical lines
		h = 0; // number of horizontal lines
		// draw lines through marked columns and unmarked rows
		verticalLines = markedCols; // same thing, just assign
		for (int i = 0; i < numberOfPairs; i++) { // toggle the marked rows array 1 = 0, 0 = 1
			if (verticalLines[i] == 1){
				v++;
			}
			if( markedRows[i] == 0){
				horizontalLines[i] = 1;
				h++;
			}
			if( markedRows[i] == 1){
				horizontalLines[i] = 0;
			}
		}
		// add up how many lines there are in total
		for (int i = 0; i < numberOfPairs; i++) {
			if ( verticalLines[i] == 1 || horizontalLines[i] == 1){
				lines++;
			}
		}

		//colour in cells which are in a column or row that's covered by a line
		for (int i = 0; i < numberOfPairs; i++) {
			for (int j = 0; j < numberOfPairs; j++) {
				if ( verticalLines[j] == 1 || horizontalLines[i] ==1){
					renderer.setRowColor(i, j, Color.blue);
					gui.animatedTable.getColumnModel().getColumn(j).setCellRenderer(renderer);
					gui.animatedTable.updateUI();
				}
			}
		}
		gui.rightNarration.append("The smallest number of lines required to cover all 0s in the table is " + lines + " -\n");
		gui.rightNarration.append(v + " vertical and " + h + " horizontal\n");

		if ( lines < numberOfPairs){
			step = 6; // smallest + alterations
		}else{
			step = 8;// final assignment
		}
	}


	// class which allows colouring in the table cells
	class CustomTableCellRenderer extends DefaultTableCellRenderer {

		Color[][] mapColors; // matrix of each table cell's colour

		public CustomTableCellRenderer() {
			mapColors = new Color[numberOfPairs][numberOfPairs];
		}

		//sent a cell's row and column and which colour to set it to
		public void setRowColor(int row, int column, Color color) {
			mapColors[row][column] = color;
		}

		@Override // does the colour change
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
	// Yaaassssss



	//---------------Gale Shapley Methods-----------------//

	// clear variables for playing backwards
	public void clearEverythingGS(){
		// clear the graph lines
		gui.middle.getGraphics().clearRect(0, 0, gui.middle.getWidth(), gui.middle.getHeight());

		numberOfEngagedGS = 0;
		gui.narration.setText("");

		currentManI = 0;
		currentWomanI = 0;
		bestFemale = -1;

		linesGS.clear();

		// reset table cell colours
		for ( int i = 0; i < numberOfPairs; i++){
			for ( int j = 0; j < numberOfPairs; j++){
				rendererGS.setRowColor(i, j, Color.white);
				gui.leftTable1.getColumnModel().getColumn(j).setCellRenderer(rendererGS);
				gui.leftTable1.updateUI();

				renderer2GS.setRowColor(i, j, Color.white);
				gui.leftTable2.getColumnModel().getColumn(j).setCellRenderer(renderer2GS);
				gui.leftTable2.updateUI();
			}
			// reset partners to none
			Men[i].setPartner(-1);
			Women[i].setPartner(-1);
		}
	}

	// takes two arrays of preferences, and sets the men and women's preferences
	public void setPrefs(int[][] prefs1, int [][]prefs2){

		gui.group1 = new Object[numberOfPairs][numberOfPairs];
		gui.group2 = new Object[numberOfPairs][numberOfPairs];

		// get each list, put in array
		for (int i = 0; i < numberOfPairs; i++){

			int[] tempPrefs = new int[numberOfPairs];
			int[] finalPrefs = new int[numberOfPairs];

			if (maleBias == true){ // men preferences in first groups
				for (int j = 0; j < numberOfPairs; j++){
					tempPrefs[j] = prefs1[j][i];
				}

				// get the order of preferences from the weighting
				finalPrefs = findPrefsFromWeight(tempPrefs);

				// make person with preferences, add to list
				Men[i] = new Person(numberOfPairs);
				Men[i].setPreferences(finalPrefs);
				for (int j = 0; j < numberOfPairs; j++){
					gui.group1[j][i] =((finalPrefs[j])+1);
				}

				for (int j = 0; j < numberOfPairs; j++){
					tempPrefs[j] = prefs2[j][i];
				}

				finalPrefs = findPrefsFromWeight(tempPrefs);

				Women[i] = new Person(numberOfPairs);
				Women[i].setPreferences(finalPrefs);
				for (int j = 0; j < numberOfPairs; j++){
					gui.group2[j][i] =((finalPrefs[j])+1);
				}

			}else{ // women go in the first group, same as above.

				for (int j = 0; j < numberOfPairs; j++){
					tempPrefs[j] = prefs1[j][i];
				}
				finalPrefs = findPrefsFromWeight(tempPrefs);

				Women[i] = new Person(numberOfPairs);
				Women[i].setPreferences(finalPrefs);

				gui.columnNames[i] = Integer.toString(i+1);

				for (int j = 0; j < numberOfPairs; j++){
					gui.group1[j][i] = ((finalPrefs[j])+1);
				}

				for (int j = 0; j < numberOfPairs; j++){
					tempPrefs[j] = prefs2[j][i];
				}
				finalPrefs = findPrefsFromWeight(tempPrefs);

				Men[i] = new Person(numberOfPairs);
				Men[i].setPreferences(finalPrefs);

				gui.columnNames[i] = Integer.toString(i+1);

				for (int j = 0; j < numberOfPairs; j++){
					gui.group2[j][i] = ((finalPrefs[j])+1);
				}
			}
		}

		// make preference tables on the gui
		gui.leftTable1 = new JTable(gui.group1, gui.columnNames);
		gui.leftTable2 = new JTable(gui.group2, gui.columnNames);

		gui.leftTable1Panel.add(gui.leftTable1.getTableHeader(), BorderLayout.NORTH);
		gui.leftTable1Panel.add(gui.leftTable1, BorderLayout.CENTER);

// set labels depending on the bias
		if (maleBias){
			gui.lblPrefs1.setText("Male Ordered Preferences");
			gui.lblPrefs2.setText("Female Ordered Preferences");

			gui.lblPrefs3.setText("Male Preferences - Animated");
			gui.lblPrefs4.setText("Male Preferences - Original");
		}
		else{
			gui.lblPrefs1.setText("Female Ordered Preferences");
			gui.lblPrefs2.setText("Male Ordered Preferences");
			gui.lblPrefs3.setText("Female Preferences - Animated");
			gui.lblPrefs4.setText("Female Preferences - Original");
		}

		gui.leftTable2Panel.add(gui.leftTable2.getTableHeader(), BorderLayout.NORTH);
		gui.leftTable2Panel.add(gui.leftTable2, BorderLayout.CENTER);

	}

	// takes a list of weighted preferences, and outputs th order instead
	public int[] findPrefsFromWeight(int[] weights){
		int[] finalPrefs = new int[numberOfPairs];
		int smallest = 100;
		int index = 0;
		int count = 1;

		// finds smallest value first
		for (int i = 0; i < numberOfPairs; i++) {
			if (weights[i] < smallest){
				smallest = weights[i];
				index = i;
			}
		}

		// puts smallest in next available slot (smallest, most desirable, in slot 0)
		while (smallest!=100){
			finalPrefs[count-1] = index;
			weights[index] = 1000;
			count++;
			index = -1;
			smallest = 100;
			for (int i = 0; i < numberOfPairs; i++) {
				if (weights[i] < smallest){
					index = i;
					smallest = weights[i];
				}
			}
		}
		return finalPrefs;
	}

	// add the group's labels on the graph panel
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
			gui.left1.add(label);
		}

		labels4 = new JLabel[numberOfPairs];
		for (int i = 0; i < numberOfPairs; i++) {
			JLabel label = new JLabel(Integer.toString(i+1));
			label.setHorizontalAlignment(SwingConstants.LEFT);
			labels4[i] = label;
			gui.right1.add(label);
		}
	}

	// does the next step of the gale shapley algorithm
	public void nextStepGS(){

		currentStepGS++;
		switch (nextStepGS) {

		case 1: // get next  man, check he's single
			if ( numberOfEngagedGS < numberOfPairs){
				boolean repeat = true;

				while(repeat==true){
					if (currentManI > numberOfPairs-1){ // go from man 1 again
						currentManI = 0;
					}

					if (Men[currentManI].getPartner() == -1){ // do if man is single
						repeat = false;
						gui.narration.append("\n" + firstGroup + " " + (currentManI+1) + " is single, ");
						currentManPrefs = Men[currentManI].getPreferences(); // get preferences of man i
						nextStepGS = 2;
					} else{
						currentManI++;
						nextStepGS = 1;
					}
				}

			} else{
				nextStepGS = 7; // finished, print results
				nextStepGS();
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

				// colour in cells orange to show proposal
				rendererGS.setRowColor(currentWomanI, currentManI, Color.orange);
				gui.leftTable1.getColumnModel().getColumn(currentManI).setCellRenderer(rendererGS);
				gui.leftTable1.updateUI();

				renderer2GS.setRowColor(newMan, bestFemale, Color.orange);
				gui.leftTable2.getColumnModel().getColumn(bestFemale).setCellRenderer(renderer2GS);
				gui.leftTable2.updateUI();

				//d= draw orange line to show proposal
				Point p1 =  new Point(0, labels1[currentManI].getY());
				Point p2 = new Point(gui.middle.getWidth(), labels2[bestFemale].getY());
				p1.y = (p1.y + (labels1[currentManI].getHeight() /2));
				p2.y = (p2.y + (labels1[currentManI].getHeight() /2));

				linesGS.add(new DrawLine(p1, p2, Color.orange, 3, currentManI, bestFemale, true)); 
				paint(gui.middle.getGraphics(), linesGS);


				if (Women[bestFemale].getPartner() == -1){ // woman is single, so accept proposal
					nextStepGS = 3;
				}
				else { // see who she prefers
					nextStepGS = 4;
				}
			}
			break;

		case 3: // accept proposal.

			gui.narration.append(scndGroup+ " " + (bestFemale+1) + "  is single so accepts " + firstGroup + " " + (currentManI+1) + "'s proposal.\n");
			gui.narration.append(scndGroup+ " " + (bestFemale+1) + "  and " + firstGroup + " " + (currentManI+1) + " are engaged.\n");
			acceptProposal(currentManI, bestFemale, Women[bestFemale].getPartner());
			currentWomanI = 0;
			currentManI++;
			nextStepGS = 1;
			break;

		case 4: // get woman's preferences. see who she prefers ( current or new partner)

			gui.narration.append(scndGroup + " " + (bestFemale+1) + " is engaged ");		 
			currentFPrefs = Women[bestFemale].getPreferences();
			int prefer = preferToCurrentPartner(currentManI, currentFPrefs, Women[bestFemale].getPartner());
			if ( prefer != -1){ // she likes new man best
				nextStepGS = 5;
			}else{ // likes current man best
				nextStepGS = 6;
			}
			break;

		case 5: // she likes the new man best, so accept proposal

			gui.narration.append("but prefers " + firstGroup + " " + (currentManI+1) + ".\n");
			gui.narration.append(scndGroup+ " " + (bestFemale+1) + "  and " + firstGroup + " " + (currentManI+1) + " are engaged.\n");
			acceptProposal(currentManI, bestFemale, Women[bestFemale].getPartner());
			currentWomanI = 0;
			currentManI++;
			nextStepGS = 1;		
			break;

		case 6: // she prefers current man , 

			gui.narration.append("and prefers current " + firstGroup + ". \n");
			removeLine(bestFemale, currentManI);

			// remove orange cells
			rendererGS.setRowColor(currentWomanI, currentManI, Color.white);
			gui.leftTable1.getColumnModel().getColumn(currentManI).setCellRenderer(rendererGS);
			gui.leftTable1.updateUI();

			int newMan = 0;
			for (int i = 0; i < numberOfPairs; i++){
				if ( currentFPrefs[i] == (currentManI)){
					newMan = i;
				}
			}

			renderer2GS.setRowColor(newMan, bestFemale, Color.white);
			gui.leftTable2.getColumnModel().getColumn(bestFemale).setCellRenderer(renderer2GS);
			gui.leftTable2.updateUI();

			currentWomanI++;
			nextStepGS = 2;

			break;

		case 7: // finished, print final results
			printResult();
			socialWelfare();
			averageScores();
			nextStepGS = -1;
			break;
		}
		if ( currentStepGS == totalStepsGS-1){ // stop playing the step before, for backwards
			playGS = false;
			totalStepsGS = 0;
		}

		if ( nextStepGS == -1){ // stop playing, dont update steps everytime
			playGS = false;
			currentStepGS--;
		}
		if ( playGS == true){ // keep playing till the end
			nextStepGS();
		}
	}

	// works out which man the woman prefers
	public int preferToCurrentPartner(int currentMan, int[] currentWoman, int spouse){
		int preference = -1;
		boolean found = false;
		int i = 0;
		// goes through her rpeferences, sees which man is higher up
		while ((i < numberOfPairs) && (found == false)){
			if(currentWoman[i] == spouse){ // prefers spouse
				found = true;
				preference = -1;
			} else if ( currentWoman[i] == currentMan){ // prefers new man
				preference = currentMan;
				found = true;
			}
			i++;
		}
		return preference;
	}

	// change appropriate values to reflect a proposal has been accepted
	public void acceptProposal(int currentMan, int currentWoman, int oldSpouse){

		// change line to green
		Men[currentMan].setPartner(currentWoman);
		changeLineColour(currentWoman, currentMan, Color.green);

		// change cell colours to green
		rendererGS.setRowColor(currentWomanI, currentManI, Color.green);
		gui.leftTable1.getColumnModel().getColumn(currentManI).setCellRenderer(rendererGS);
		gui.leftTable1.updateUI();
		int newMan = 0;
		for (int i = 0; i < numberOfPairs; i++){
			if ( currentFPrefs[i] == (currentManI)){
				newMan = i;
			}
		}
		renderer2GS.setRowColor(newMan, bestFemale, Color.green);
		gui.leftTable2.getColumnModel().getColumn(bestFemale).setCellRenderer(renderer2GS);
		gui.leftTable2.updateUI();
		womensAverages[bestFemale] = newMan;

		if(oldSpouse!=-1){ // if woman was engaged, reset spouse to single
			Men[oldSpouse].setPartner(-1);
			gui.narration.append(firstGroup + " " + (oldSpouse+1) + " is single again.\n");
			removeLine(currentWoman, oldSpouse);

			int womanIndex = 0;
			int[] oldManPrefs = Men[oldSpouse].getPreferences();
			for (int i = 0; i < numberOfPairs; i++){
				if ( oldManPrefs[i] == (currentWoman)){
					womanIndex = i;
				}
			}

			// man cell to white...
			rendererGS.setRowColor(womanIndex, oldSpouse, Color.white);
			gui.leftTable1.getColumnModel().getColumn(oldSpouse).setCellRenderer(rendererGS);
			gui.leftTable1.updateUI();

			// get index of woman's old spouse, change cell to white
			int oldMan = 0;
			for (int i = 0; i < numberOfPairs; i++){
				if ( currentFPrefs[i] == (oldSpouse)){
					oldMan = i;
				}
			}
			renderer2GS.setRowColor(oldMan, bestFemale, Color.white);
			gui.leftTable2.getColumnModel().getColumn(bestFemale).setCellRenderer(renderer2GS);
			gui.leftTable2.updateUI();
		}
		else{ // woman was single, so set her partner. easy
			numberOfEngagedGS++;
		}
		Women[currentWoman].setPartner(currentMan);
	}

	// takes a start and end point, and colour to draw a line on the graph
	public void changeLineColour(int to, int from, Color c){

		for ( int i = 0; i < linesGS.size(); i++){
			DrawLine current = linesGS.get(i);
			if (( current.personFrom == from)&& (current.personTo == to)&& (current.colour != c)){
				linesGS.get(i).colour = c;
			}
		}
		paint(gui.middle.getGraphics(), linesGS);
	}

	public void removeLine(int woman, int spouse){

		// remves a line from the graph
		DrawLine newLine;

		for ( int i = 0; i < linesGS.size(); i++){
			DrawLine current = linesGS.get(i);

			// searched for the line to remove, changed the colour and thickness so it disappears
			if (( current.personFrom == spouse)&& (current.personTo == woman)&& (current.colour != gui.middle.getBackground())){
				newLine = current;
				newLine.colour = gui.middle.getBackground();
				newLine.thickness = 5;
				linesGS.add(newLine);
				linesGS.get(i).addLine = false;
			}
		}
		paint(gui.middle.getGraphics(), linesGS);
	}

	// prints out the final pairs
	public void printResult(){
		gui.narration.append("\nThe final Pairings are:");
		for (int i = 0; i < numberOfPairs; i++){
			gui.narration.append("\n" + firstGroup + " " + (i+1) + " is paired with " + scndGroup + " " + (Men[i].getPartner()+1));
		}
	}
	
	// adds up the value of social welfare using the weight values from the hungarian table, 
	public void socialWelfare(){
		int total = 0;
		int partner = 0;
		
		// find and add the weight for the assigned cells
		for (int i = 0; i < numberOfPairs; i++){
			partner = Men[i].getPartner();
			System.out.println(Men[i].getPartner());
			total = total + (originalMatrix[partner][i]);
			System.out.println(originalMatrix[partner][i]);
			System.out.println("**");
		}
		gui.narration.append("\n\nSocial Welfare value is = " + total);
	}

	// calculate the average choice assigned
	public void averageScores(){
		
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
		
		// do for first group men/women
		double total = 0;
		for (int i = 0; i < numberOfPairs; i++){
			for (int j = 0; j < numberOfPairs; j++){
				int[] prefs = Men[i].getPreferences();
				if (prefs[j] == (Men[i].getPartner())){
					total = total + (j+1);
				}
			}
		}
		
		// average, round, and print out
		total = total/numberOfPairs;
		DecimalFormat df = new DecimalFormat("#.##");
		gui.narration.append("\n\nAverage choice assigned for " + group1 + " = " + df.format(total));
		total = 0;
		
		// then get average for the other group men/women
		for (int i = 0; i < numberOfPairs; i++){
			total = total + womensAverages[i];
			total++;
		}
		
		total = total/numberOfPairs;
		df = new DecimalFormat("#.##");
		gui.narration.append("\nAverage choice assigned for " + group2 + " = " + df.format(total));

	}

	// draws the line on the graph
	public void paint(Graphics g, ArrayList<DrawLine> list) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// draws the lines one by one
		for (int i = 0; i < list.size(); i++){
			DrawLine currentLine = list.get(i);
			if ( currentLine.colour == gui.middle.getBackground()){ // draw the background coloured lines first
				g2.setPaint(currentLine.colour);
				g2.setStroke(new BasicStroke(currentLine.thickness));
				g2.draw(new Line2D.Double(currentLine.start.x, currentLine.start.y, currentLine.end.x, currentLine.end.y));
			}
		}
		
		// then draw  the coloured lines
		for (int i = 0; i < list.size(); i++){

			DrawLine currentLine = list.get(i);

			if (( currentLine.colour != gui.middle.getBackground())&& ( currentLine.addLine == true)){
				g2.setPaint(currentLine.colour);
				g2.setStroke(new BasicStroke(currentLine.thickness));
				g2.draw(new Line2D.Double(currentLine.start.x, currentLine.start.y, currentLine.end.x, currentLine.end.y));
			}
		}
	}
}