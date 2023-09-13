package puzzles.common.solver;
import java.util.*;

public class Solver {
    private final LinkedList<Configuration> queue;
    private final HashMap<Configuration, Configuration> testConfigs;

    /**
     * Main solver method. Actually a lot simpler than you would think, which is sort of cool but the bulk of the hard
     * stuff is in the configs. Constructor just initializes a hashmap testConfigs and a LinkedList queue.
     */
    public Solver(){
        testConfigs = new HashMap<>();
        queue = new LinkedList<>();
    }

    /**
     * main solve method.
     * Starts off by initializing a few things, a noSolutions boolean flag, and a testOptions counter(total config count,
     * starts at 1 as to account for the initial config option). Adds the initial input to the queue, and puts it in testConfigs.
     *
     * Then creates a Configuration currentest(starts as null for now). A while true(main) used to loop the actual search stuff,
     * only breaking if no solutions or if a solution is found. First starts by trying to set current test to the first queue
     * value and pulling said value off of the queue(if not possible throws NoSuchElementException, sets the noSolutions flag
     * to true, and breaks). After that, getNeighbors is called, followed by a for each loop on the results.
     *
     * Inside the for loop, testOptions gets incremented by 1, and if the configuration is unique(by seeing if testConfig
     * contains the key that is the configuration), and if so, adds it to the queue, then adds it to testConfig with
     * the new config as the key and the parent config as the value. Then comes the isSolution test, which if the solution
     * is found, sets currentTest to the solution config, and terminates the while loop named main. Otherwise, the for each
     * processing the rest of the results from getNeighbors, and the main while loop goes until a solution is found.
     *
     * After the solution is found(stored in currentTest at time of main while loop termination), an arraylist of strings called path
     * is created, which will store the path of values from the solution back up to the initial parent. This begins by
     * adding the solution value to it(in toString format), setting parent equal to the parent config of the current solution,
     * and starting a while loop. The while begins by confirming it isn't null, and if it isn't it adds the toString'd version
     * of parent to path. The value of the parent key is grabbed from testConfigs then, and if it's null(only possible on
     * the initial node of the graph as it was manually added as null at the start of solve), we know we found the full path.
     * Then the arraylist path gets reversed around for ease of use, and the while loop breaks. Otherwise if the initial
     * node isn't found, parent is set to the value of parent as a key in testConfig.
     *
     * After that is just printing. testOptions is the total configs, testConfigs.size() is the total unique configs,
     * if the nosolutions flag is thrown, "no solutions" is printed out instead of the path, otherwise it prints out
     * the values of path as the steps(via a for loop)
     * @param input initial configuration
     */
    public void solve(Configuration input) {
        boolean noSolutions = false;
        int testOptions = 1;
        queue.add(input);
        testConfigs.put(input, null);
        Configuration currentTest = null;
        main:
        while (true) {
            try{
                currentTest = queue.remove();
            } catch (Exception NoSuchElementException){
                noSolutions=true;
                break;
            }
            Collection<Configuration> neighbors = currentTest.getNeighbors();
            for (Configuration i: neighbors){
                testOptions++;
                if (!testConfigs.containsKey(i)){
                    queue.add(i);
                    testConfigs.put(i, currentTest);
                    if (i.isSolution()) {
                        currentTest = i;
                        break main;
                    }
                }
            }
        }
        ArrayList<String> path = new ArrayList<String>();
        path.add(currentTest.toString());
        Configuration parent = testConfigs.get(currentTest);
        while (true) {
            if (parent!=null) {
                path.add(parent.toString());
            }
            if (testConfigs.get(parent) == null) {
                Collections.reverse(path);
                break;
            }
            parent = testConfigs.get(parent);
        }
        System.out.println("Total configs: " + testOptions);
        System.out.println("Unique configs: " + testConfigs.size());
        if (noSolutions) {
            System.out.println("No solution");
        } else{
            for (int i = 0; i < path.size(); i++) {
                System.out.println("Step " + i + ": " + path.get(i));
            }
        }
    }
}
