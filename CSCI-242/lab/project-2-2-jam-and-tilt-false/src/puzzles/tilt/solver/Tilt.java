package puzzles.tilt.solver;

import puzzles.common.solver.Configuration;
import puzzles.tilt.model.TiltConfig;
import puzzles.common.solver.Solver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Tilt {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
        } else {
            try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
                String[] fields = in.readLine().split("\\s+");
                int gridLength = Integer.parseInt(fields[0]);
                char[][] initGrid = new char[gridLength][gridLength];
                for (int i = 0; i < gridLength; i++) {
                    String[] newRow = in.readLine().split("\\s+");
                    for (int j = 0; j < gridLength; j++) {
                        initGrid[i][j] = newRow[j].charAt(0);
                    }
                }
                TiltConfig initConfig = new TiltConfig(gridLength, initGrid);
                Solver mainSolve = new Solver();
                System.out.println("File: " + args[0]);
                System.out.println(initConfig);
                LinkedList<Configuration> theList;
                theList = mainSolve.solve(initConfig, true);
                if (theList == null) {
                    System.out.println("No solution");
                } else {
                    for (int i = 0; i < theList.size(); i++) {
                        System.out.println("Step " + i + ": " + (theList.get(i).toString()));
                    }
                }

            }


            catch (IOException e) {
                System.out.println("Something went wrong");
            }

        }


    }
}
