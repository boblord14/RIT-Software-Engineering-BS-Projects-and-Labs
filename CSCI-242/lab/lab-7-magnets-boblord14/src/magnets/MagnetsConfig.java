package magnets;

import backtracking.Configuration;
import test.IMagnetTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The representation of a magnet configuration, including the ability
 * to backtrack and also give information to the JUnit tester.
 *
 * This implements a more optimal pruning strategy in isValid():
 * - Pair checked each time a new cell is populated
 * - Polarity checked each time a new cell is populated
 * - When last column or row is populated, the pos/neg counts are checked
 *
 * @author RIT CS
 * @author Ethan Patterson
 */
public class MagnetsConfig implements Configuration, IMagnetTest {
    /** a cell that has not been assigned a value yet */
    private final static char EMPTY = '.';
    /** a blank cell */
    private final static char BLANK = 'X';
    /** a positive cell */
    private final static char POS = '+';
    /** a negative cell */
    private final static char NEG = '-';
    /** left pair value */
    private final static char LEFT = 'L';
    /** right pair value */
    private final static char RIGHT = 'R';
    /** top pair value */
    private final static char TOP = 'T';
    /** bottom pair value */
    private final static char BOTTOM = 'B';
    /** and ignored count for pos/neg row/col */
    private final static int IGNORED = -1;

    // add private state here
    private static int rowCount;
    /** number of total rows */
    private static int colCount;
    /** number of total columns*/
    private static int[] posRows;
    /** array of the required positive value count for each row(-1 if no count)*/
    private static int[] posCols;
    /** same as posRows but for columns*/
    private static int[] negRows;
    /** array of the required negative value count for each row(-1 if no count)*/
    private static int[] negCols;
    /** same as negRows but for columns*/
    private static char[][] boardRelations;
    /** 2d array storing the relations of the board(L, R, T, B) in the coresponding coordinates in the array*/
    private char[][] boardState;
    /** 2d array storing the current value of each location on the board(empty, neg, pos, neutral)*/
    private int[] cursorPos;
    /** 2 value array storing the location of the cursor, [0] is the row, [1] is the column */


    /**
     * Read in the magnet puzzle from the filename.  After reading in, it should display:
     * - the filename
     * - the number of rows and columns
     * - the grid of pairs
     * - the initial config with all empty cells
     *
     * @param filename the name of the file
     * @throws IOException thrown if there is a problem opening or reading the file
     *
     * Just goes ahead and reads the file as given.
     *
     * The first 5 lines dont change as theyre all specific things(size and 4 lines for pos/neg row/col reqs),
     * and are read in as it stands(w/ a for loop iterating over the split up versions of lines 2-5)
     *
     * Whatever remains is relationships for the board, and the number of lines varies, but thankfully we already
     * have the row and column counts so it already tells us how long it is, and can be iterated over with a couple
     * of for loops and populated in the proper data structure accordingly.
     *
     * Then after that it just creates the cursorPos array with size 2 and values 0,0 as this constructor is only used
     * when the file is read in the first time.
     *
     * Additionally, the occasional System.out.println for the required console outputs for the program is used, with
     * various trivial nonimportant ways to print them in the right formatting.
     */
    public MagnetsConfig(String filename) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            System.out.println("File: " + filename);
            // read first line: rows cols
            String[] fields = in.readLine().split("\\s+");
            rowCount = Integer.parseInt(fields[0]);
            colCount = Integer.parseInt(fields[1]);
            System.out.println("Rows: "+rowCount+", Columns: "+colCount);
            // read second line: pos rows
            fields = in.readLine().split("\\s+");
            posRows = new int[fields.length];
            for(int i=0;i< fields.length;i++){
                posRows[i] = Integer.parseInt(fields[i]);
            }
            // read third line: pos cols
            fields = in.readLine().split("\\s+");
            posCols = new int[fields.length];
            for(int i=0;i< fields.length;i++){
                posCols[i] = Integer.parseInt(fields[i]);
            }
            // read fourth line: neg rows
            fields = in.readLine().split("\\s+");
            negRows = new int[fields.length];
            for(int i=0;i< fields.length;i++){
                negRows[i] = Integer.parseInt(fields[i]);
            }
            // read fifth line: pos rows
            fields = in.readLine().split("\\s+");
            negCols = new int[fields.length];
            for(int i=0;i< fields.length;i++){
                negCols[i] = Integer.parseInt(fields[i]);
            }
            // read rest of file: magnet relations
            boardRelations = new char[rowCount][colCount];
            for(int i=0;i<rowCount;i++){
                fields = in.readLine().split("\\s+");
                for(int j=0;j< fields.length;j++){
                    boardRelations[i][j] = fields[j].charAt(0);
                }
            }
            System.out.println("Pairs:");
            for(int i=0;i<rowCount;i++){
                String temp = Arrays.toString(boardRelations[i]);
                temp= temp.replace("[","");
                temp = temp.replace("]","");
                temp = temp.replace(",","");
                temp = temp.trim();
                System.out.println(temp);
            }
            // initialize the empty board state
            // note for states: E is empty, P is positive, N is negative, S is null
            boardState = new char[rowCount][colCount];
            for(int i=0;i<rowCount;i++){
                for(int j=0;j< colCount;j++){
                    boardState[i][j] = EMPTY;
                }
            }
            System.out.println("Initial config:");
            System.out.println(this.toString());
            // initialize cursor location, stores (row, col) of cursor
            cursorPos = new int[2];
            cursorPos[0] = 0;
            cursorPos[1] = -1;
        } // <3 Jim
    }

    /**
     * The copy constructor which advances the cursor, creates a new grid,
     * and populates the grid at the cursor location with val.
     *
     * @param other the board to copy
     * @param val the value to store at new cursor location
     *
     * A couple for loops read in the boardState values from the previous MagnetsConfig into this new one, and then the
     * cursor is set to the old value as well, and the column value is advanced after that, with some if statements serving
     * as wraparounds to increment the row value when the column goes over the limit(and ofc set column back to 0)
     */
    private MagnetsConfig(MagnetsConfig other, char val) {
        boardState = new char[rowCount][colCount];
        for(int i=0;i<rowCount;i++){
            for(int j=0;j< colCount;j++){
                boardState[i][j] = other.getVal(i, j);
            }
        }
        cursorPos = new int[2];
        cursorPos[0] = other.getCursorRow();
        cursorPos[1] = other.getCursorCol();
        cursorPos[1]++;
        if(cursorPos[1]==colCount){
            cursorPos[1] = 0;
            cursorPos[0]++;
        }
        if(cursorPos[0]!=rowCount) {
            boardState[cursorPos[0]][cursorPos[1]] = val;
        }
    }


    /**
     * Generate the successor configs.  For minimal pruning, this should be
     * done in the order: +, - and X.
     *
     * Nothing special here, just basically the same trick as used in project 2-1 with the list of configurations.
     * Although this time the pruning is done in isValid so this just creates all possible successors with no regard to
     * them being valid or not.
     *
     * @return the collection of successors
     */
    @Override
    public List<Configuration> getSuccessors() {
        List<Configuration> successors = new ArrayList<>();
        successors.add(new MagnetsConfig(this, '+'));
        successors.add(new MagnetsConfig(this, '-'));
        successors.add(new MagnetsConfig(this, 'X'));
        return successors;
    }


    /**
     * Checks to make sure a successor is valid or not.  For minimal pruning,
     * each newly placed cell at the cursor needs to make sure its pair
     * is valid, and there is no polarity violation.  When the last cell is
     * populated, all row/col pos/negative counts are checked.
     *
     * This is the big one. Uses a couple different tests to check validity, returning false if it fails any of them.
     *
     * First test is the pairing, making sure that the other value its paired with(the relation from boardRelations)
     * is valid. Neutral goes with neutral, neg with pos(and vice versa), and anything with null. A switch grabs the right
     * relation from the boardRelations array of the current cursor pos value, and then the boardState value of the related
     * spot on the array. For example if the cursor spot's relation is L, this returns the polarity value to the left of it
     * (null, neutral, neg, pos). currentChar also stores the current char at the cursor pos' value. For the test itself,
     * it is just a matter of confirming that the pairChar isn't empty(in which case the test doesn't do anything), and then
     * the different valid pairs listed earlier are happening(and nothing invalid).
     *
     * Next check is the neighbor polarities. This can only work if the current cursorPos' value is negative or positive.
     * These are the checks that say "a positive cannot be next(in all cardinal directions) to another positive" and vice versa.
     * They start by confirming there *is* a value next to the cursor position before running the test, and if so,
     * they confirm that the values are not equal(which is fine since only negative and positive values get this far).
     *
     * After that the checks become a bit more complex, so some additional statistics are built first. These are the
     * positive, negative, and empty counts for both the row and column of the current cursorPos. Simply handled by a for
     * and some more switch/case action, just nigh identical code being repeated twice for both rows and columns.
     *
     * The third check on the list is confirming that the number of negatives or positives in both the row and column of
     * cursor conform to the assigned requirements and no more. This could be broken up into a few more if statements instead of using ||
     * but this is just the method I went with, even if it is a bit more messy. This test confirms that the row/column tested
     * either has no requirements for neg/pos, or the number of neg/pos in it is less than or equal to the required number.
     * The variants for both negative and positive/rows and columns is nigh identical here.
     *
     * The fourth test is the big one that provides massive time saving over just doing the above 3. This test confirms
     * that the number of open spaces left in the board(null spaces that use a '.'), is greater than or equal to the
     * number of positive and negative values waiting to be added in order to satisfy the row conditions. For example,
     * let's say a row needs 2 positive and 3 negative, with a size of 6 slots in total. If two slots are blank/neutral,
     * and three slots are positive, there is only one slot left, which obviously isn't valid as there needs to be 3 negatives in there
     * as well. This is calculated by subtracting the number of required pos or negs in a specific row/column by the number
     * currently in there, which returns the number left to add(or surplus in some cases). Then it is just confirmed that
     * there actually *is* a required count and not just -1 for the required number of pos/negs(and in that case it gets set to 0 as none are needed)
     * After that, the sum of the positive/negative still necessary to add is compared to the number of empty slots left,
     * and if there is less empty slots than the number of pos/negs still waiting to be added, this configuration is invalid.
     *
     * if a configuration passes all 4 tests successfully, it is a valid configuration.
     *
     * This way with the fourth test solves magnets-16.txt in about 0.024 seconds on average.
     * Now that I'm thinking about it, if I get 5 extra credit points for solving it in under 2 minutes,
     * I think I should get 2500 extra credit points if I scale it to the time it does it in.
     *
     * Just kidding though... unless?
     *
     * @return whether this config is valid or not
     */
    @Override
    public boolean isValid() {
        char pairChar = EMPTY;
        switch (boardRelations[cursorPos[0]][cursorPos[1]]) {
            case LEFT -> pairChar = boardState[cursorPos[0]][cursorPos[1]+1];
            case RIGHT -> pairChar = boardState[cursorPos[0]][cursorPos[1]-1];
            case TOP -> pairChar = boardState[cursorPos[0]+1][cursorPos[1]];
            case BOTTOM -> pairChar = boardState[cursorPos[0]-1][cursorPos[1]];
        }
        char currentChar = boardState[cursorPos[0]][cursorPos[1]];
        //paring polarity checks
        if ((pairChar!=EMPTY)&&((currentChar==BLANK&&pairChar!=BLANK)||(currentChar==POS&&pairChar!=NEG)||(currentChar==NEG&&pairChar!=POS))){
            return false;
        //neighbor polarity checks, assuming neighbors dont break bounds of the array
        }
        if(currentChar==POS||currentChar==NEG){
            if(cursorPos[0]!=0){
                if (currentChar==boardState[cursorPos[0]-1][cursorPos[1]]){
                    return false;
                }
            }
            if(cursorPos[0]!=rowCount-1){
                if (currentChar==boardState[cursorPos[0]+1][cursorPos[1]]){
                    return false;
                }
            }
            if(cursorPos[1]!=0){
                if (currentChar==boardState[cursorPos[0]][cursorPos[1]-1]){
                    return false;
                }
            }
            if(cursorPos[1]!=colCount-1){
                if (currentChar==boardState[cursorPos[0]][cursorPos[1]+1]){
                    return false;
                }
            }
        }

        //building current counts
        int posRowCount = 0;
        int negRowCount = 0;
        int emptyRowSlots = 0;
        for (int i=0;i<colCount;i++){
            switch (boardState[cursorPos[0]][i]) {
                case POS -> posRowCount++;
                case NEG -> negRowCount++;
                case EMPTY -> emptyRowSlots++;
            }
        }
        int posColCount = 0;
        int negColCount = 0;
        int emptyColSlots = 0;
        for (int i=0;i<rowCount;i++){
            switch (boardState[i][cursorPos[1]]) {
                case POS -> posColCount++;
                case NEG -> negColCount++;
                case EMPTY -> emptyColSlots++;
            }
        }
        //row/col neg/pos count checks w/non -1, bit overly lengthy and can be split into 2 if statements but this works too
        if(((posRowCount>posRows[cursorPos[0]])&&posRows[cursorPos[0]]!=(-1))||((negRowCount>negRows[cursorPos[0]])&&negRows[cursorPos[0]]!=(-1))){
            return false;
        }
        if(((posColCount>posCols[cursorPos[1]]&&posCols[cursorPos[1]]!=(-1)))||((negColCount>negCols[cursorPos[1]]&&negCols[cursorPos[1]]!=(-1)))){
            return false;
        }
        //num empty slots in row/col less than req number of pos/neg left checks
        int emptyPosRows = (posRows[cursorPos[0]]-posRowCount);
        int emptyNegRows = (negRows[cursorPos[0]]-negRowCount);
        int emptyPosCols = (posCols[cursorPos[1]]-posColCount);
        int emptyNegCols = (negCols[cursorPos[1]]-negColCount);
        if(posRows[cursorPos[0]]==-1){
            emptyPosRows=0;
        }
        if(negRows[cursorPos[0]]==-1){
            emptyNegRows=0;
        }
        if(posCols[cursorPos[1]]==-1){
            emptyPosCols=0;
        }
        if(negCols[cursorPos[1]]==-1){
            emptyNegCols=0;
        }
         if(emptyRowSlots<(emptyPosRows+emptyNegRows)){
            return false;
        }
        if(emptyColSlots<(emptyPosCols+emptyNegCols)){
            return false;
        }
        return true;
    }

    /**
     * this is a pretty quick answer thanks to the lab writeup, as long as the cursor is in the last slot it is the solution,
     * since this is only called after isValid confirms a config is valid.
     * @return if the current configuration is the solution
     */
    @Override
    public boolean isGoal() {
        return (cursorPos[0] == rowCount - 1) && (cursorPos[1] == colCount - 1);
    }

    /**
     * Returns a string representation of the puzzle including all necessary info.
     *
     * I didn't make this, it is untouched since starting the lab.
     *
     * @return the string
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        // top row
        result.append("+ ");
        for (int col = 0; col < getCols(); ++col) {
            result.append(getPosColCount(col) != IGNORED ? getPosColCount(col) : " ");
            if (col < getCols() - 1) {
                result.append(" ");
            }
        }
        result.append(System.lineSeparator());
        result.append("  ");
        for (int col = 0; col < getCols(); ++col) {
            if (col != getCols() - 1) {
                result.append("--");
            } else {
                result.append("-");
            }
        }
        result.append(System.lineSeparator());

        // middle rows
        for (int row = 0; row < getRows(); ++row) {
            result.append(getPosRowCount(row) != IGNORED ? getPosRowCount(row) : " ").append("|");
            for (int col = 0; col < getCols(); ++col) {
                result.append(getVal(row, col));
                if (col < getCols() - 1) {
                    result.append(" ");
                }
            }
            result.append("|").append(getNegRowCount(row) != IGNORED ? getNegRowCount(row) : " ");
            result.append(System.lineSeparator());
        }

        // bottom row
        result.append("  ");
        for (int col = 0; col < getCols(); ++col) {
            if (col != getCols() - 1) {
                result.append("--");
            } else {
                result.append("-");
            }
        }
        result.append(System.lineSeparator());

        result.append("  ");
        for (int col = 0; col < getCols(); ++col) {
            result.append(getNegColCount(col) != IGNORED ? getNegColCount(col) : " ").append(" ");
        }
        result.append(" -").append(System.lineSeparator());
        return result.toString();
    }

    // IMagnetTest

    /**
     * @return number of rows in the puzzle
     */
    @Override
    public int getRows() {
        return rowCount;
    }
    /**
     * @return number of columns in the puzzle
     */
    @Override
    public int getCols() {
        return colCount;
    }
    /**
     * @param row the given row
     * @return number of required positives in a given row
     */
    @Override
    public int getPosRowCount(int row) {
        return posRows[row];
    }
    /**
     * @param col the given column
     * @return number of required positives in a given column
     */
    @Override
    public int getPosColCount(int col) {
        return posCols[col];
    }
    /**
     * @param row the given row
     * @return number of required negatives in a given row
     */
    @Override
    public int getNegRowCount(int row) {
        return negRows[row];
    }
    /**
     * @param col the column
     * @return number of required negatives in a given column
     */
    @Override
    public int getNegColCount(int col) {
        return negCols[col];
    }
    /**
     * @param row the given row
     * @param col the given column
     * @return relation value of a given coordinate(row, column)
     *
     * used to figure out where the pair of this coordinate value is located
     */
    @Override
    public char getPair(int row, int col) {
        return boardRelations[row][col];
    }

    /**
     *
     * @param row the given row
     * @param col the given column
     * @return the polarity value(pos, neg, blank/neutral, empty) for a given coordinate(row, col)
     *
     * used to figure out what the polarity value of a specific coordinate is
     */
    @Override
    public char getVal(int row, int col) {
        return boardState[row][col];
    }

    /**
     * @return cursor's current row
     */
    @Override
    public int getCursorRow() {
        return cursorPos[0];
    }

    /**
     * @return cursor's current column
     */
    @Override
    public int getCursorCol() {
        return cursorPos[1];
    }
}
