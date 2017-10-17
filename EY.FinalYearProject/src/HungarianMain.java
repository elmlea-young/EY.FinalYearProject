import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
/*
 * Runs the algorithm step by step
 */
public class HungarianMain {
	int[][] costMatrix; // the main matrix of weightings used throughout
	final int[][] originalMatrix; // keeps original values, as costMatrix changes
	int numberOfPairs; // number of pairs the user chose
	int lines = 0; // number or lines required to cover all 0s
	int currentR = 0; // current row being manipulated
	int currentC = 0; // current column being manipulated
	int[] verticalLines; // shows which columns have lines drawn on them ( 0 = no line, 1 = lines)
	int[] horizontalLines; // which rows have lines on them
	Object[][] tableData; // data for table model that stays the same
	int minValue = 0; // smallest value
	int minValueIndex = -1; // index of smallest value
	HungarianRun gui; // object for the gui when running
	Object[][] animatedData; // data for table model which changes
	boolean play = false; // play to end
	int step = 1; // which step of the algorithm is next
	CustomTableCellRenderer renderer; // renderer for colouring in table cells
	CustomTableCellRenderer renderer2; // table 2's renderer
	int currentStep = 0;
	int totalSteps = 0;
	int[] reorderedResults; // orders the results to be printable

	//constructor, creates the gui, sets costMatrix values, initialises other values
	public HungarianMain(int[][] weightMatrix, int pairs) {

		gui = new HungarianRun();
		numberOfPairs = pairs;
		renderer = new CustomTableCellRenderer();
		renderer2 = new CustomTableCellRenderer();
		originalMatrix = new int[numberOfPairs][numberOfPairs];

		this.costMatrix = new int[numberOfPairs][numberOfPairs];
		for (int i = 0; i < numberOfPairs; i++) {
			for ( int j = 0; j < numberOfPairs; j++){
				this.costMatrix[i][j] = weightMatrix[i][j];  
				originalMatrix[i][j] = weightMatrix[i][j];
			}
		}   

		// buttons' action listeners
		gui.play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				play = true;
				nextStep();
			}
		});
		gui.backward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// only do if at least one step has been done already
				// clear variables, and play algorithm to the step before
				if ( currentStep>1){
					play = true;
					totalSteps = currentStep;
					currentStep = 0;
					clearEverything();
					nextStep();
				}
			}
		});
		// do next step when forward button pressed
		gui.forward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextStep();
			}
		});
	}

	// resets variables to start again
	public void clearEverything(){

		lines = 0; 
		currentR = 0;
		currentC = 0; 		
		minValue = 0; 
		minValueIndex = -1; 		
		step = 1;
		verticalLines = new int[numberOfPairs];
		horizontalLines = new int[numberOfPairs];

		gui.narrationText.setText("");
		// resets values and cell colours in the table to the original values
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
			gui.narrationText.append("\n");
		}else if ( step == 3){ // gets smallest value in column
			getSmallestInColumn();
		}else if ( step == 4){ // reduces columns by smallest value
			reduceColumn();
			gui.narrationText.append("\n");
		}else if ( step == 5){ // works out the lines required to cover all 0s, and shades them in the table
			resetCells();
			markAcross0s();
			gui.narrationText.append("\n");
		}else if ( step == 6){ // get smallest value not covered by a line
			getSmallestOverall();
			gui.narrationText.append("\n");
		}else if (step == 7){ // alter uncovered, and twice covered values
			finalAlteration();
			gui.narrationText.append("\n");
		}else if (step == 8){ // match the pairs, print final values
			resetCells();
			int[] result = finalAssignment();
			printResults(result);
			averageScore();
			int socialWelfare = getSocialWelfare(result);
			gui.narrationText.append("\nSocial welfare value is: " + socialWelfare);
		} else if ( step == 9){
			// finished, dont increment currentStep every time button pressed
			play = false; // finished so stop playing
			currentStep--;
		}
		// for going backwards. Stops the algorithm at the previous step
		if (currentStep >= totalSteps-1 && ( totalSteps != 0)){
			play = false;
			totalSteps = 0;
		}
		// but if not done and should be playing, then do the next step
		if ( play == true){
			nextStep();
		}
	}

	//  calculates average choice assigned
	public void averageScore(){
		double total = 0;
		int choiceNum = -1;
		int[] weights = new int[numberOfPairs];
		int[] prefs = new int[numberOfPairs];

		for (int i = 0; i < numberOfPairs; i++){
			// get persom's preferences
			for (int j = 0; j < numberOfPairs; j++){
				weights[j] = originalMatrix[j][i];
			}
			// get the order of preferences from weight
			prefs = findPrefsFromWeight(weights);
			for (int j = 0; j < numberOfPairs; j++){
				if (reorderedResults[i] == prefs[j]){
					// add total
					choiceNum = j+1;
					total = total + choiceNum;
				}
			}
		}
		// average, round and print
		total = total/numberOfPairs;
		DecimalFormat df = new DecimalFormat("#.##");
		gui.narrationText.append("\nAverage choice assigned = " + df.format(total));
	}

	// method which takes a list of weighted preferences, and finds the order of preferences
	public int[] findPrefsFromWeight(int[] weights){
		int[] finalPrefs = new int[numberOfPairs];
		int smallest = 100; // initialise
		int index = 0;
		int count = 1;

		// find smallest in list
		for (int i = 0; i < numberOfPairs; i++) {
			if (weights[i] < smallest){
				smallest = weights[i];
				index = i;
			}
		}
		
		//
		while (smallest!=100){
			// put index of smallest value in the next entry (So finalPrefs[0] holds index of smallest, and most desirable, task)
			finalPrefs[count-1] = index;
			weights[index] = 1000; // ignored next time
			count++;
			// initialise values to get next smallest
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

	// prints the final pairs and colours the tables
	public void printResults(int[] result){
		reorderedResults = new int[numberOfPairs];
		gui.narrationText.append("The final pairings are:\n");
		for ( int i = 0; i < numberOfPairs; i++){

			renderer.setRowColor(i, result[i], Color.green);
			gui.animatedTable.getColumnModel().getColumn(result[i]).setCellRenderer(renderer);
			gui.animatedTable.updateUI();
			for (int j = 0; j<numberOfPairs; j++){
				if (result[j] == i){
					gui.narrationText.append("Person " + (result[j]+1) + " paired to task " + (j+1) + "\n");
					reorderedResults[result[j]] = j;
				}
			}
			play = false;
		}
	}

	public void reduceColumn(){ // reduces values in the column by the smallest value
		if(minValue!=0){ // 
			gui.narrationText.append("So all values in this column are decreased by " + minValue + "\n");

			for (int j = 0; j < numberOfPairs; j++) { // decrease all values in column by minValue
				costMatrix[j][currentC] -= minValue;
				gui.animatedTable.getModel().setValueAt(costMatrix[j][currentC], j, currentC);
				// colour cell orange
				renderer.setRowColor(j, currentC, Color.orange);
				gui.animatedTable.getColumnModel().getColumn(currentC).setCellRenderer(renderer);
				gui.animatedTable.updateUI();
			}

		}else{ // there's a 0 in this column, so no alterations required
			gui.narrationText.append("so no action needed\n");	
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

	// adds up the values in the assigned cells to get an overall value for Social Welfare
	// also sets the cells to green while it's here
	public int getSocialWelfare(int[] matchings){
		int totalWelfare = 0;

		for (int i = 0; i < numberOfPairs; i++){
			totalWelfare = totalWelfare+originalMatrix[i][matchings[i]];
			renderer2.setRowColor(i, matchings[i], Color.green);
			gui.originalValues.getColumnModel().getColumn(matchings[i]).setCellRenderer(renderer2);
			gui.originalValues.updateUI();
		}

		return totalWelfare;
	}

	// loops through the column to find the smallest value
	public void getSmallestInColumn(){
		if(currentC == 0){ // reset table cell colour first ( remove the previous orange cell)
			for (int j = 0; j < numberOfPairs; j++) {
				renderer.setRowColor(numberOfPairs-1, j, Color.white);
				gui.animatedTable.getColumnModel().getColumn(j).setCellRenderer(renderer);
				gui.animatedTable.updateUI();
			}
		}

		minValue = 100;
		minValueIndex = -1;
		for (int j = 0; j < numberOfPairs; j++) {
			if (costMatrix[j][currentC] < minValue) { // get smallest value and it's index in the column
				minValue = costMatrix[j][currentC];
				minValueIndex = j;
			}
			if (currentC!=0){ // reset cells of last column to white
				renderer.setRowColor(j, currentC-1, Color.white);
				gui.animatedTable.getColumnModel().getColumn(currentC-1).setCellRenderer(renderer);
				gui.animatedTable.updateUI();
			}
		}
		// add narration, colour in cell yellow, go to reducing the column
		gui.narrationText.append("Smallest value in column " + (currentC+1) + " is " + minValue + "\n");
		renderer.setRowColor(minValueIndex, currentC, Color.yellow);
		gui.animatedTable.getColumnModel().getColumn(currentC).setCellRenderer(renderer);
		gui.animatedTable.updateUI();
		step = 4;
	}

	public void reduceRow(){
		if (minValue !=0){ // reduce all row's values by smallest value, colour them orange
			gui.narrationText.append("So all values in this row are decreased by " + minValue + "\n");
			for (int j = 0; j < numberOfPairs; j++) {
				costMatrix[currentR][j] -= minValue;
				gui.animatedTable.getModel().setValueAt(costMatrix[currentR][j], currentR, j);

				renderer.setRowColor(currentR, j, Color.orange);
				gui.animatedTable.getColumnModel().getColumn(j).setCellRenderer(renderer);
				gui.animatedTable.updateUI();
			}
		}else{ //there's a 0 in the row, no alterations needed.
			gui.narrationText.append("so no action needed\n");	
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

	// go through each row and find the smallest value
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
		gui.narrationText.append("Smallest value in row " + (currentR+1) + " is " + minValue + "\n");
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
			columnNames[i] = Integer.toString(i+1);
		}

		gui.originalValues = new JTable(tableData, columnNames);
		gui.displayTable.add(gui.originalValues, BorderLayout.CENTER);

		gui.animatedTable = new JTable(animatedData, columnNames);
		gui.table1Panel.add(gui.animatedTable, BorderLayout.CENTER);

		// add table labels
		gui.table1Panel.add(gui.animatedTable.getTableHeader(), BorderLayout.NORTH);
		gui.displayTable.add(gui.originalValues.getTableHeader(), BorderLayout.NORTH);

	}

	// matches up each group
	public int[] finalAssignment(){
		int[] rowsAssigned = new int[numberOfPairs]; // which rows have been matched up ( 1 = matched)
		int[] zerosPerRow = new int[numberOfPairs]; // how many zeros are in each row
		int totalZeros = 0; // total number of zeros in the matrix
		Arrays.fill(rowsAssigned, -1); // initialise all values of rows assigned to -1

		// add up zeros in each row
		for (int i = 0; i < numberOfPairs; i++) {
			for ( int j = 0; j < numberOfPairs; j++){
				if ( costMatrix[i][j] == 0){
					zerosPerRow[i]++;
					totalZeros++;
				}
			}
		}

		while (totalZeros != 0){ // do until all are matched up
			int leastZerosRow = -1;// index of row with least 0s;
			int smallestRow = 100; // smallest number in that row

			for (int i = 0; i < numberOfPairs; i++){ // get row with least 0s, and how many that is
				if (zerosPerRow[i] < smallestRow && zerosPerRow[i]!=0){
					leastZerosRow = i; // row index
					smallestRow = zerosPerRow[i]; // least 0s
				}	
			}

			for (int i = 0; i < numberOfPairs; i++){ // find the 0 in the row with the least number of 0s
				if (costMatrix[leastZerosRow][i] == 0 && rowsAssigned[leastZerosRow] == -1){
					rowsAssigned[leastZerosRow] = i; // assign that row to column i
					costMatrix[leastZerosRow][i] = 1; // set the 0 to 1 so it is ignored next time
					for ( int j = 0; j < numberOfPairs; j++){
						if (costMatrix[j][i] == 0){
							costMatrix[j][i] = 1;
						}
					}
				}
				// remove any other zeros in that row so they're ignored too, as that row has already been assigned a column
				if (costMatrix[leastZerosRow][i] == 0 && rowsAssigned[leastZerosRow] != -1){
					costMatrix[leastZerosRow][i] = 1;
				}
			}

			// reset variables to go again with the next row
			Arrays.fill(zerosPerRow, 0);
			totalZeros = 0;

			// update zeros per row and total zeros
			for (int i = 0; i < numberOfPairs; i++) {
				for ( int j = 0; j < numberOfPairs; j++){
					if ( costMatrix[i][j] == 0){
						zerosPerRow[i]++;
						totalZeros++;
					}
				}
			}
		}
		step = 9; // end of the algorithm
		return rowsAssigned; 
	}

	// gets the smallest value in the matrix which isn't covered by a line
	public void getSmallestOverall(){
		minValue = 100;
		int x = 0; //row
		int y = 0; // column
		for (int i = 0; i< numberOfPairs; i++){
			for (int j = 0; j< numberOfPairs; j++){
				if (verticalLines[j] == 0 && horizontalLines[i] == 0){ // cell is not marked
					if (costMatrix[i][j] < minValue && costMatrix[i][j] > 0){ 
						minValue = costMatrix[i][j];
						x = i;
						y = j;
					}
				}
			}
		}
		// narration and colour the cell yellow
		gui.narrationText.append("The smallest uncovered value is " + minValue + "\n");
		renderer.setRowColor(x,y , Color.yellow);
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
				gui.narrationText.append("This " + costMatrix[currentR][currentC] + " is uncovered, so is decreased by " + minValue + "\n");
				costMatrix[currentR][currentC] = costMatrix[currentR][currentC]-minValue;
				gui.animatedTable.getModel().setValueAt(costMatrix[currentR][currentC], currentR, currentC);
				renderer.setRowColor(currentR,currentC , Color.orange);
				gui.animatedTable.getColumnModel().getColumn(currentR).setCellRenderer(renderer);
				gui.animatedTable.updateUI();
				done = true;
			}
			else if (verticalLines[currentC] == 1 && horizontalLines[currentR] == 1){ // cell is marked twice
				// narrate, colour, and change value
				gui.narrationText.append("This " + costMatrix[currentR][currentC] + " is covered by two lines, so is increased by " + minValue + "\n");
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

	public void markAcross0s(){ // the fun bit...
		verticalLines = new int[numberOfPairs]; // which columns have lines
		horizontalLines = new int[numberOfPairs]; // which rows have lines
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

		// add-up zeros in each row
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

			for (int i = 0; i < numberOfPairs; i++){
				// find zero in the row with least zeros, where that row isnt already assigned
				if (copyMatrix[leastZerosRow][i] == 0 && rowsAssigned[leastZerosRow] == -1){
					// assign that row to that column
					rowsAssigned[leastZerosRow] = i;
					copyMatrix[leastZerosRow][i] = 1; // change that 0 to 1 so it's ignored next time
					totalZeros--;
					zerosPerRow[leastZerosRow]--;

					// change any other 0s in that column to 1 as that column is assigned now
					for ( int j = 0; j < numberOfPairs; j++){
						if (copyMatrix[j][i] == 0){
							copyMatrix[j][i] = 1;
							totalZeros--;
							zerosPerRow[j]--;

						}
					}
				}	
				// any 0s in the row which is assigned, change them to 1.
				if (copyMatrix[leastZerosRow][i] == 0 && rowsAssigned[leastZerosRow] != -1){
					copyMatrix[leastZerosRow][i] = 1;
					totalZeros--;
					zerosPerRow[leastZerosRow]--;
				}				
			}

			/* next smallest row must be found. First we check the remaining rows instead of going back to the first row
			 * 
			 */
			if (leastZerosRow == numberOfPairs-1){
				count = 0;
			}else{
				count = leastZerosRow;
			}

			leastZerosRow = -1;// index of row with least 0s;
			smallestRow = 100; // smallest number in that row
			boolean same = true;
			
		//	go from current row to last row. ( if two rows are the same, the next one must be assigned first, not the one at the start)
			for (int k = count; k < numberOfPairs; k++){ // get row with least 0s, and how many that is
				if (zerosPerRow[k] < smallestRow && zerosPerRow[k]!=0){
					leastZerosRow = k; // row index
					smallestRow = zerosPerRow[k]; // least 0s
				}	
			}

			// the go from the first row to see if there's one smaller but not the same.
			for (int k = 0; k < numberOfPairs; k++){ // get row with least 0s, and how many that is
				if (zerosPerRow[k] < smallestRow && zerosPerRow[k]!=0){
					leastZerosRow = k; // row index
					smallestRow = zerosPerRow[k]; // least 0s
				}if (zerosPerRow[k] != smallestRow && ( zerosPerRow[k] !=0)){
					same = false;
				}
			}
			// if all remaining rows have the same number of zeros left... get smallest from the top.
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
		
		// okay, this is the fun bit..
		// work out how to cover the rows and columns with lines
		
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
		gui.narrationText.append("The smallest number of lines required to cover all 0s in the table is " + lines + " -\n");
		gui.narrationText.append(v + " vertical and " + h + " horizontal\n");

		if ( lines < numberOfPairs){
			step = 6; // smallest + alterations
		}else{
			step = 8;// final assignment
		}
	}

	// class which allows colouring in the table cells
	class CustomTableCellRenderer extends DefaultTableCellRenderer {

		Color[][] mapColors; // matrix for each table cell's colour

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
}

// Yaaassssss

