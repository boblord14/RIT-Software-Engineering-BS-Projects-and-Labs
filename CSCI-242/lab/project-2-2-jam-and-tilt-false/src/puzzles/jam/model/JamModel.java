package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
/** main model for the MVC style gui/ptui work. Large swathes of this were yoinked from lightsout as that had a lot of reusable code
 * for its MVC stuff*/
public class JamModel {
    /** the collection of observers of this model */
    private final List<Observer<JamModel, String>> observers;

    /** the current configuration */
    private JamConfig currentConfig;

    /** public easy access to the current car layout for modification/reading */
    public ArrayList<JamCar> boardState;

    /** internal representation of board state, good for checking empty spaces quickly*/
    private String[][] internalBoardSim;

    /** loaded file name*/
    private File loadedFile;

    /** state for the tileOperation function*/
    private String operationCar;
    /** linkedList for the results of the solve, used by the hint method */
    LinkedList<Configuration> moveList;
    /** constructor, creates the observers linkedList and sets operationCar to null */
    public JamModel(){
        observers = new LinkedList<>();
        operationCar = null;
    }

    /** getter for the dimensions of the board. returns an array that stores rows in 0 and cols in 1 */
    public int[] getDimensions(){
       int[] dimensions = new int[2];
       dimensions[0]= currentConfig.rows;
       dimensions[1]= currentConfig.cols;
       return dimensions;
    }
    /**
     * Attempts to load a board from a given file name. It will announce to the observers if it was loaded successfully or not.
     *
     * taken from lightsout, no real changes
     *
     * @param filename The file to load
     * @return True iff loaded successfully
     */
    public boolean loadBoardFromFile(String filename) {
        return loadBoardFromFile(new File(filename));
    }
    /**
     * Attempts to load a board from a file object. It will announce to the observers if it was loaded successfully or not.
     *
     * taken from lightsout, some changes to make it work right.
     *
     * reads in line by line from a file, initially grabbing row/col/car counts, and then iterates over the rest of the file
     * to read in the cars, and stores them in the carList arraylist.
     *
     * loadedFile is given the file parameter(used to reload later), currentConfig is set to a new config based around the read in data,
     * boardState is the arraylist of cars from it, internalBoardSim is the 2d array representation of the board.
     *
     * after it reads in successfully, it alerts observers, of a success and returns true, if theres an exception it alerts a failure.
     *
     * @param file The file to load
     * @return True if loaded successfully
     */
    public boolean loadBoardFromFile(File file)  {
        try {
            String line = new String();
            String[] lineVals;
            ArrayList<JamCar> carList= new ArrayList<JamCar>();
            Scanner in = new Scanner(file);
            line = in.nextLine();
            lineVals=line.split(" ");
            int rowCount = Integer.parseInt(lineVals[0]);
            int colCount = Integer.parseInt(lineVals[1]);
            int carCount = Integer.parseInt(in.nextLine());
            for (int i=0;i<carCount;i++){
                line = in.nextLine();
                lineVals=line.split(" ");
                JamCar tempCar = new JamCar(lineVals[0].charAt(0), Integer.parseInt(lineVals[1]), Integer.parseInt(lineVals[2]), Integer.parseInt(lineVals[3]), Integer.parseInt(lineVals[4]));
                carList.add(tempCar);
            }
            loadedFile = file;
            currentConfig = new JamConfig(carList, rowCount, colCount);
            boardState = currentConfig.getCars();
            internalBoardSim = currentConfig.getWorkableArray();
            alertObservers("LOADED!" + loadedFile.getName());

            return true;
        }catch (FileNotFoundException e) {
            alertObservers("LOADFAILED!" + file.getName());
            return false; //invalid file
        }
    }
    /** reload the board to the initial state, calls the load from file method with the loadedFile param that stores whatever board was last
     * loaded in, and alerts observers of a reset */
    public boolean reload(){
        boolean temp= loadBoardFromFile(loadedFile);
        alertObservers("RESET");
        return temp;
    }
    /** getter for internalBoardSim */
    public String[][] workableArray(){ return internalBoardSim; }

    /** getter for the occupied spaces for a car of a particular given name, looks for the name and returns the occupied spaces in a 2d array */
    public int[][] getCarSpaces(String name){
        for(JamCar car: boardState){
            if(car.getName()==name.charAt(0)){
                return car.getOccupiedSpaces();
            }
        }
        return null;
    }

    /** checks if game is over, checks the key car thats in the last spot in boardState, and checks if it is in the last column */
    public boolean gameOver() {
        return boardState.get(boardState.size() - 1).getEndPos()[1] == (currentConfig.cols - 1);
    }

    /**
     * handles the tile operations for the model, row and col is the clicked on tile in the controller
     *
     * operationCar is the flag that marks if a car was previously clicked(used for seeing if youre selecting a car to move
     * or selecting a tile to move to)
     *
     * if its null, the car is being selected to move, assuming theres a car there(spits out essentially an error if not).
     * if there is a car there operationCar gets set to that car's name, and the next time tileOperation is called, it knows to
     * do the "is valid" tests and move if possible. if not, the EMPTY message is returned to observers with the row/col.
     *
     * if not null, the car was selected last operation. first it makes sure the square selected is empty, and if not returns
     * the INVALID message with row and col. and resets operationCar to null.
     *
     *else, it gets the car in boardState that is to be moved(via operationCar compared to the name), checks if the move is
     * valid with isValid(detailed later), and if so, commences the move.
     *
     * the move itself is again split by isVertical, and a variable called movement is created, which determines the number of spaces
     * to give to the actual move method by Math.min-ing some absolute value'd subtractions based on the start and end pos.
     *
     * after that an if else determines if it is moving forward or backwards, and the actual JamCar move is called then, given
     * movement(multiplied by -1 for moving backwards). Again, the vertical/horizontal versions are nigh identical.
     *
     * Afterwards a new JamConfig is created based upon the move, and given the row/col data too. Internalboardsim is updated too, and
     * the observers are alerted of the successful move and given the data. else if the move isnt valid, INVALID with the row/col data is passed to observers
     *
     * and at the end operationCar is null again and ready for a new car to be selected.
     * */
    public void tileOperation(int row, int col){
        if(operationCar==null){
            if(Objects.equals(internalBoardSim[row][col], ".")){
                alertObservers("EMPTY!"+row+"!"+col);//must click square with object in it
            } else{
                operationCar = internalBoardSim[row][col];
                alertObservers("MOVE!"+row+"!"+col);
            }
        } else{
            if(!Objects.equals(internalBoardSim[row][col], ".")){//must click empty square
                alertObservers("INVALID!"+row+"!"+col);
                operationCar=null;
            } else{
                for(JamCar car:boardState){
                    if(car.getName()==(operationCar.charAt(0))){//from here on out "car" is the car to move
                        if(isValid(car, row, col)){//good to move
                            int movement;
                            if (car.isVertical()) {
                                movement = Math.min(Math.abs(car.getStartPos()[0] - row), Math.abs(car.getEndPos()[0] - row));
                                if(car.getStartPos()[0]>row){ car.moveCar(movement *(-1));}
                                else{ car.moveCar(movement);}

                            } else {
                                movement = Math.min(Math.abs(car.getStartPos()[1] - col), Math.abs(car.getEndPos()[1] - col));
                                if(car.getStartPos()[1]>col){ car.moveCar(movement*(-1));}
                                else{ car.moveCar(movement);}
                            }
                            currentConfig = new JamConfig(boardState, currentConfig.rows, currentConfig.cols);
                            internalBoardSim = currentConfig.getWorkableArray();
                            alertObservers("GAMESHIFT!"+row+"!"+col);
                        }   else{
                            alertObservers("INVALID!"+row+"!"+col);
                        }
                    }
                }
                operationCar=null;
            }

        }
    }
    /**
     * confirms a valid move. takes in the car to test, the new row, and the new col
     *
     * first grabs the car itself based off of the name, then splits based on isVertical or not,
     * checks to make sure the selected square is in the appropriate lane to move,
     * then splits again based on if the car is moving forward or backward,
     * and then makes sure the path to move is clear
     * */
    private Boolean isValid(JamCar car, int row, int col){
        if (car.isVertical()){
            if(car.getStartPos()[1]!=col){ return false;}
            if(car.getStartPos()[0]>row){//move forward(up)
                for(int i= car.getStartPos()[0]+1;i<=row;i++){
                    if(!Objects.equals(internalBoardSim[i][col], ".")){return false;}
                }
            } else{//move back(down)
                for(int i= car.getEndPos()[0]-1;i>=row;i--){
                    if(!Objects.equals(internalBoardSim[i][col], ".")){return false;}
                }
            }
        } else{
            if(car.getStartPos()[0]!=row){ return false;}
            if(car.getStartPos()[1]<col){//move forward(right)
                for(int i= car.getEndPos()[1]+1;i<=col;i++){
                    if(!Objects.equals(internalBoardSim[row][i], ".")){return false;}
                }
            } else{//move backward(left)
                for(int i= car.getStartPos()[1]-1;i>=col;i--){
                    if(!Objects.equals(internalBoardSim[row][i], ".")){return false;}
                }
            }
        }
        return true;
    }
    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<JamModel, String> observer) {
        this.observers.add(observer);
    }

    /** doHint handles processing hints. uses the previously declared movelist. if movelist is null or the size of it is
     * 0(important if you "unsolve" the puzzle and push hint, it calls the solver, solves it, and stores the end result in moveList.
     * Then the first value is removed as it is just the current position.
     *
     * If moveList is *still* null, that means theres no solutions and the observers are alerted accordingly, otherwise
     * the hint giving part occurs, getting the next config with removeFirst, setting it to currentConfig, setting boardState
     * to getCars of the new currentConfig, and updating internalBoardSim. Observers are then alerted to the successful hint move.
     * */
    public void doHint(){
        if(moveList==null || moveList.size()==0){
            Solver mainSolve = new Solver();
            moveList = mainSolve.solve(currentConfig, false);
            moveList.removeFirst();
        }
        if(moveList == null){
            alertObservers("NOSOLUTIONS");
        }else {
            currentConfig = (JamConfig) moveList.removeFirst();
            boardState = currentConfig.getCars();
            internalBoardSim = currentConfig.getWorkableArray();
            alertObservers("HINTSHIFT");
        }
    }

    /** toString used by the ptui for outputting the model state in the console. very similar to the toString for JamConfig(see that one),
     * just with some extra touchups as the ptui output demands of me.
     *
     * This uses a stringBuilder and not a string though. */
    @Override
    public String toString(){
        StringBuilder printableArray = new StringBuilder("   ");
        for (int i=0; i< currentConfig.cols;i++){
            printableArray.append(i).append(" ");
        }
        printableArray.append("\n  ");
        printableArray.append("--".repeat(Math.max(0, currentConfig.cols)));
        printableArray.append("\n");
        for(int i=0;i< currentConfig.rows;i++){
            String[] oneDArray2 = internalBoardSim[i];
            String temp1D = Arrays.toString(oneDArray2);
            temp1D = temp1D.replace("[","");
            temp1D = temp1D.replace("]","");
            temp1D = temp1D.replace(",","");
            temp1D = (temp1D + " \n");
            printableArray.append(i).append("| ").append(temp1D);
        }

        return printableArray.toString();
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }
}
