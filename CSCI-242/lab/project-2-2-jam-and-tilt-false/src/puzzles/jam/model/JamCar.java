package puzzles.jam.model;

/**
 * jamcar is the base "car" for tilt. this is where the raw data for car positions, their length, verticality, etc are stored.
 * often kept in an arraylist of jamcars and worked with from there.
 */
public class JamCar {
    /** name is the car's letter name assigned by the input file */
    private final char Name;
    /** startpos is an array with the length of 2 that stores a single coordinate in row, col form for the front position
     * of the car dictated by the input file */
    private int[] startPos = new int[2]; //row, col
    /** endpos is the same as startpos but for the back of the car not the front */
    private int[] endPos = new int[2]; //row, col
    /** length is how many cells the car takes up(min 2) */
    private final int length;
    /** determines if the car is positioned vertically or horizontally, this little boolean is the source of a whole lot of
     * boring painful repetitive work everywhere */
    private final boolean isVertical; //if vertical moves along rows and col stays the same, vice versa for horizontals

    /** jamcar constructor, gets read in from the file, and the file is formatted in the same way as the parameters are here.
     * names are self explanatory and all link to the vars described above.
     *
     * only interesting thing is the calculations for length and isvertical. they check to see if either the row or the column
     * for the starting and ending positions are the same(which tells me how its orientated), and does isVertical respectively.
     *
     * length is just absolute value of back-front(but based on verticality).
     * */
    public JamCar(char name, int startRow, int startCol, int endRow, int endCol){
        this.Name = name;
        this.startPos[0] = startRow;
        this.startPos[1] = startCol;
        this.endPos[0] = endRow;
        this.endPos[1] = endCol;
        if (startRow == endRow){
            this.length = Math.abs(endCol-startCol)+1;
            this.isVertical=false;
        } else{
            this.length = Math.abs(endRow-startRow)+1;
            this.isVertical=true;
        }

    }
    /** simple getter to return the length */
    public int getCarLength(){return this.length;}
    /** simple getter to return the car name */
    public char getName(){return this.Name;}
    /** simple getter to return the startPos array */
    public int[] getStartPos(){return this.startPos;}
    /** simple getter to return the endpos array */
    public int[] getEndPos(){return this.endPos;}
    /** simple getter to return verticality */
    public boolean isVertical(){return this.isVertical;}

    /** ok now starts the cool methods yay
     *
     * movecar takes one param, spaces, which is how many spaces to move it(negative values move it backwards).
     *
     * first an isVertical test determines if rows or cols change, and then both the startpos and endpos for either row or col(whatever is needed)
     * get changed by spaces.
     *
     * the end result is always a valid move as this method is only called once a move is determined to be valid.
     * */
    public void moveCar(int spaces){
        if (isVertical){
            this.startPos[0] = (this.startPos[0]+spaces);
            this.endPos[0] = (this.endPos[0]+spaces);
        } else{
            this.startPos[1] = (this.startPos[1]+spaces);
            this.endPos[1] = (this.endPos[1]+spaces);
        }
    }
    /** this is a 2d array that returns all the spaces occupied by the car in row, col format
     *
     * the first dimension handles each individual space(thanks to length), and the second dimension handles the actual row, col data.
     *
     * first the 2d array called spaces is created with the info described above, then isVertical splits it based on if
     * rows or cols are being looked at, and then a for loop iterates over every space occupied by the car and stores the values
     * in spaces. after that spaces just gets returned
     * */
    public int[][] getOccupiedSpaces(){
        int[][] spaces = new int[this.length][2]; //stores each coordinate as row, col that it occupies with spaces[0] being row and [1] being col
        if (isVertical){
            for(int i=0;i<this.length;i++){
                spaces[i][0]=(this.startPos[0]+i);
                spaces[i][1]=this.startPos[1];
            }
        } else{
            for(int i=0;i<this.length;i++){
                spaces[i][0]=this.startPos[0];
                spaces[i][1]=(this.startPos[1]+i);

            }
        }
        return spaces;
    }
}
