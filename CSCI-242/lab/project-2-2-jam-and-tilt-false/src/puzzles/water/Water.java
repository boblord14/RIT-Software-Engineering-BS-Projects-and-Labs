package puzzles.water;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Ethan Patterson
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     *
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     *
     * This is a bit more complex to setup than strings as the amount of buckets(and therefore args) varies.
     *
     * First, a 2d array initBuckets is built, with the first value being the size, and the second being the current
     * amount of water in it(starts with 0 of course).
     * After that the config and the solver are created the same way as they were in the strings variant. Then the initial
     * console output is done, by creating an array of just the bucket sizes and spitting that out with Arrays.toString(arraygoeshere).
     *
     * And after that the solve method is called and that takes control of everything.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(
                    ("Usage: java Water amount bucket1 bucket2 ...")
            );
        } else {
            int[][] initBuckets = new int[args.length-1][2];
            for(int i=1;i<args.length;i++){
                initBuckets[i-1][0]=Integer.parseInt(args[i]);
                initBuckets[i-1][1]=0;

            }
            WaterConfig initConfig = new WaterConfig(Integer.parseInt(args[0]), initBuckets);
            Solver mainSolve = new Solver();
            int[] temp = new int[args.length-1];
            for(int i=1;i< args.length;i++){
                temp[i-1]=Integer.parseInt(args[i]);
            }
            System.out.println("Amount: " + args[0] + ", Buckets: " + Arrays.toString(temp));
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

