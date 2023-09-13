package puzzles.strings;

import puzzles.common.solver.Solver;

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
            mainSolve.solve(initConfig);
        }
    }
}
