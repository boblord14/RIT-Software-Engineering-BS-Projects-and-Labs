package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.LinkedList;

/**
 * Main class for the strings puzzle.
 *
 * @author Ethan Patterson
 */
public class Strings {
    /**
     * Run an instance of the strings puzzle.
     *
     * @param args [0]: the starting string;
     *             [1]: the finish string.
     *
     * The way I have this set up is to create the new stringConfig, and the new solver, and then call the solve method
     * with the config as the parameter.
     *
     * With the println tossed in the middle too
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            StringsConfig initConfig = new StringsConfig(args[0], args[1]);
            Solver mainSolve = new Solver();
            System.out.println("Start: " + args[0] + ", End: " + args[1]);
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
    }
}
