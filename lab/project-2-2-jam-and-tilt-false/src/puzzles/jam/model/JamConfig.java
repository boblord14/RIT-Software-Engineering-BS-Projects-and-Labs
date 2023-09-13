package puzzles.jam.model;

import puzzles.common.solver.Configuration;
import puzzles.jam.solver.Jam;
import puzzles.water.WaterConfig;

import java.util.*;

/** standard configuration implementation for the solver */
public class JamConfig implements Configuration {
    /** cars holds  all the jamcars that were mandated by the file */
    ArrayList<JamCar> cars;
    /** rows is how many rows the board has */
    int rows;
    /** cols is how many cols the board has */
    int cols;

    /** constructor here takes in the variables described above as parameters, and just puts them on the "this." versions
     * of them */
    public JamConfig(ArrayList<JamCar> cars, int rows, int cols){
    this.cars=cars;
    this.rows =rows;
    this.cols=cols;

    }

    /** configuration interface method, checks if the last car in the arraylist(always the car with the X that must
     * be moved) is in the last column as that is considered solved. just returns true or false based on that*/
    @Override
    public boolean isSolution() {
        return cars.get(cars.size() - 1).getEndPos()[1] == (cols - 1);
    }

    /** 2d array representation of the gameboard so it is easier to process empty spaces and conflicts
     *
     * just creates a 2d array, fills it all with "." which is the symbol for empty spaces here, and then iterates over
     * cars to use getOccupiedSpaces from it, and replace those spots in the 2d array with the car's name char by iterating
     * over the results of getOccupiedSpaces
     * */
    private String[][] representAsArray(){
        String[][] visualArray = new String[rows][cols];
        for(String[] oneDArray : visualArray){
            Arrays.fill(oneDArray, ".");
        }
        for (JamCar tempCar : cars){
            int[][] arrayCoords = tempCar.getOccupiedSpaces();
            for(int[] coord : arrayCoords){
                visualArray[coord[0]][coord[1]] = Character.toString(tempCar.getName());
            }
        }
        return visualArray;
    }

    /** handles getting neighbors, this method was garbage to write, lots of mandatory repetition
     *
     * first creates the typical neighborList arraylist of configs for handling the actual results, and then does representAsArray
     * to get a represntation of the game board to work with here.
     *
     * then, the next segment is done for every car in order to process every possible move:
     *
     * first, isVertical is checked to differentiate between testing rows and cols. the difference between the horizontal and vertical is just
     * doing either rows or cols(often changing a 1 to a 0 or vice versa to switch between them).
     *
     * the actual work inside is done by for loops. the first for loop tests the positions in front of the car, checking if theyre
     * empty(represnted again by a "."), and if so creating a new config for neighborlist via the generateNewChanged method(described later)
     *
     * if a spot isn't empty, it stops looking for them(via a break) as you cant have a valid spot past an invalid one here.
     *
     * after this, it does the same thing but backwards from the car instead of in front of it. nearly same thing but using endpos and i++ instead of startpos and i--
     *
     * again, after this is the else for the earlier isVertical that does the same thing but testing cols instead of rows. just same thing again really.
     *
     *when its all done, neighborlist is returned.
     * */
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighborList = new ArrayList<>();
        String[][] workableArray = representAsArray();
        for (JamCar car : cars){//for every car we have
            if(car.isVertical()){//if vertical
                for(int i= car.getStartPos()[0]-1;i>=0;i--){//test spots between start and border
                    if(Objects.equals(workableArray[i][car.getStartPos()[1]], ".")){//if valid generate new config for every free spot, else stop trying
                        neighborList.add(new JamConfig(generateNewChanged(new JamCar(car.getName(), i, car.getStartPos()[1], ((i-1)+car.getCarLength()), car.getEndPos()[1])), rows, cols));
                    }
                    else break;
                    }//cool now do it again for the other direction
                for(int i= car.getEndPos()[0]+1;i<rows;i++){//test spots between start and border
                    if(Objects.equals(workableArray[i][car.getStartPos()[1]], ".")){//if valid generate new config for every free spot, else stop trying
                        neighborList.add(new JamConfig(generateNewChanged(new JamCar(car.getName(), ((i+1)-car.getCarLength()), car.getStartPos()[1], i, car.getEndPos()[1])), rows, cols));
                    }
                    else break;
                }
            }else{//cool now do it again for horizontal cars now
                for(int i= car.getStartPos()[1]-1;i>=0;i--){//test spots between start and border
                    if(Objects.equals(workableArray[car.getStartPos()[0]][i], ".")){//if valid generate new config for every free spot, else stop trying
                        neighborList.add(new JamConfig(generateNewChanged(new JamCar(car.getName(),  car.getStartPos()[0], i, car.getEndPos()[0], ((i-1)+car.getCarLength()))), rows, cols));
                    }
                    else break;
                }//cool now do it again for the other direction
                for(int i= car.getEndPos()[1]+1;i<cols;i++){//test spots between start and border
                    if(Objects.equals(workableArray[car.getStartPos()[0]][i], ".")){//if valid generate new config for every free spot, else stop trying
                        neighborList.add(new JamConfig(generateNewChanged(new JamCar(car.getName(), car.getStartPos()[0], ((i+1)-car.getCarLength()), car.getEndPos()[0], i)), rows, cols));
                    }
                    else break;
                }
            }
        }

        return neighborList;
    }

    /** this method creates the new arraylist for the new configs. it gets given a new jamcar with the updated locations determined in getNeighbors
     *
     * first it iterates over all the cars in cars, creating new copies of the cars to be put in tempCarList
     *
     * also, it checks if the name of the car equals the shfted one's name, and if so adds the shifted one to tempCarList instead
     * */
    private ArrayList<JamCar> generateNewChanged(JamCar shifted){
        ArrayList<JamCar> tempCarList = new ArrayList<>();
        for(JamCar p : cars) {
            if (p.getName()==shifted.getName()){
                tempCarList.add(shifted);
            }else{
                tempCarList.add(new JamCar(p.getName(), p.getStartPos()[0], p.getStartPos()[1], p.getEndPos()[0], p.getEndPos()[1]));
            }

        }
        return tempCarList;
    }
    /** getter method for returning cars */
    public ArrayList<JamCar> getCars(){
        return cars;
    }

    /** mandatory overridden equals method, compares the hashcodes(see overridden hashcode method below) */
    @Override
    public boolean equals(Object other) {
        return (this.hashCode()==other.hashCode());
    }

    /** mandatory overridden hashcode method, returns the hashcode of this.toString(see the overridden toString method below) */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /** toString'd version of representAsArray, takes each line from it, trims off some garbage, and adds it to a string with a \n on the end.
     * does this for each line and returns the end result.,
     * */
    @Override
    public String toString() {
        String[][] workableArray = representAsArray();
        String printableArray = "";
        for(String[] oneDArray2 : workableArray){
            String temp1D = Arrays.toString(oneDArray2);
            temp1D = temp1D.replace("[","");
            temp1D = temp1D.replace("]","");
            temp1D = temp1D.replace(",","");
            temp1D = (temp1D + "\n");
            printableArray = (printableArray+temp1D);
        }

        return printableArray;
    }

    /** getter method for representAsArray */
    public String[][] getWorkableArray(){
        return representAsArray();
    }
}
