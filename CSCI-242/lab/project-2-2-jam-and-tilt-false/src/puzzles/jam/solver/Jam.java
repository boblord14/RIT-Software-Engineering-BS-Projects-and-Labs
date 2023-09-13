package puzzles.jam.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.model.JamCar;
import puzzles.jam.model.JamConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Jam {
    /** main method. first does an args check, and then once that passes, it mainly handles reading in the file. see the
     * loadBoardFromFile in JamModel, as it is explained more there as these are nigh identical for the most part.
     *
     * Once the file is read in and the initial JamConfig created, the linkedList is created, and the solver is created/ran(with true for stats printouts), with the results going
     * to the created linkedList. if the linkedList has no results, there is no solutions. Otherwise it is printed out since the linkedList is the steps in order for the solve.
     * */
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Usage: java Jam filename");
        } else {
            String line = new String();
            String[] lineVals;
            ArrayList<JamCar> carList= new ArrayList<JamCar>();
            Scanner in = new Scanner(new File(args[0]));
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
            JamConfig initConfig = new JamConfig(carList, rowCount, colCount);
            System.out.println("File: " + args[0]);
            System.out.println(initConfig.toString());
            Solver mainSolve = new Solver();
            LinkedList<Configuration> theList;
            theList = mainSolve.solve(initConfig, true);
            if (theList == null) {
                System.out.println("No solution");
            } else {
                for (int i = 0; i < theList.size(); i++) {
                    System.out.println("Step " + i + ":" + "\n" + (theList.get(i).toString()));
                }
            }

        }
    }
}