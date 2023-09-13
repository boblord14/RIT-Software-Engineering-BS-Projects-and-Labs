package puzzles.tilt.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class TiltModel {
    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();
    public static String LOADED = "loaded!";

    public static String LOAD_FAILED = "loadFailed!";

    public static String NO_SOLUTION = "noSolution";

    public static String INVALID_MOVE = "invalidMove";

    public static String MOVE_COMPLETE = "moveComplete";

    public static String PUZZLE_RESET = "puzzleReset";

    public static String INVALID_DIRECTION = "invalidDirection";

    public static String HINT_DONE = "hintDone";


    /** the current configuration */
    private TiltConfig currentConfig;
    private TiltConfig originalConfig;
    public TiltModel() {

    }
    public boolean loadGrid(String filename) {
        return loadGrid(new File(filename));
    }
    public boolean loadGrid(File file) {
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String[] fields = in.readLine().split("\\s+");
            int gridLength = Integer.parseInt(fields[0]);
            char[][] theGrid = new char[gridLength][gridLength];
            for (int i = 0; i < gridLength; i++) {
                String[] newRow = in.readLine().split("\\s+");
                for (int j = 0; j < gridLength; j++) {
                    theGrid[i][j] = newRow[j].charAt(0);
                }
            }
            currentConfig = new TiltConfig(gridLength, theGrid);
            originalConfig = new TiltConfig(gridLength, theGrid);
            alertObservers(LOADED + file.getName());
            return true;


        }
        catch (IOException e) {
            System.out.println("Invalid filename.");
            alertObservers(LOAD_FAILED + file.getName());
            return false;
        }

    }

    public void move(char direction) {
        switch(direction) {
            case 'N' | 'n':
                char[][] northTilt = new char[getDimension()][getDimension()];
                boolean northValid = true;
                for (int i = 0; i < getDimension(); i++) {
                    for (int j = 0; j < getDimension(); j++) {
                        northTilt[i][j] = this.currentConfig.getGrid()[i][j];
                    }
                }
                for (int l = 0; l < getDimension(); l++) {
                    int stoppingPoint = 0;
                    boolean stopAtHole = false;
                    for (int k = 0; k < getDimension(); k++) {
                        if (northTilt[k][l] == '*') {
                            stoppingPoint = k + 1;
                            stopAtHole = false;
                        } else if (northTilt[k][l] == 'B') {
                            if (stopAtHole) {
                                northValid = false;
                                northTilt[k][l] = '.';
                            } else {
                                northTilt[k][l] = '.';
                                northTilt[stoppingPoint][l] = 'B';
                                stoppingPoint = stoppingPoint + 1;
                            }

                        } else if (northTilt[k][l] == 'O') {
                            stoppingPoint = k;
                            stopAtHole = true;
                        } else if (northTilt[k][l] == 'G') {
                            if (stopAtHole) {
                                northTilt[k][l] = '.';
                            } else {
                                northTilt[k][l] = '.';
                                northTilt[stoppingPoint][l] = 'G';
                                stoppingPoint = stoppingPoint + 1;
                            }
                        }

                    }


                }
                if (northValid) {
                    this.currentConfig = new TiltConfig(getDimension(), northTilt);
                    alertObservers(MOVE_COMPLETE);
                } else {
                    alertObservers(INVALID_MOVE);
                }
                break;
            case 'S' | 's':
                char[][] southTilt = new char[getDimension()][getDimension()];
                boolean southValid = true;
                for (int i = 0; i < getDimension(); i++) {
                    for (int j = 0; j < getDimension(); j++) {
                        southTilt[i][j] = this.currentConfig.getGrid()[i][j];
                    }
                }
                for (int l = 0; l < getDimension(); l++) {
                    int stoppingPoint = getDimension() - 1;
                    boolean stopAtHole = false;
                    for (int k = getDimension() - 1; k >= 0; k--) {
                        if (southTilt[k][l] == '*') {
                            stoppingPoint = k - 1;
                            stopAtHole = false;
                        } else if (southTilt[k][l] == 'B') {
                            if (stopAtHole) {
                                southValid = false;
                                southTilt[k][l] = '.';
                            } else {
                                southTilt[k][l] = '.';
                                southTilt[stoppingPoint][l] = 'B';
                                stoppingPoint = stoppingPoint - 1;
                            }

                        } else if (southTilt[k][l] == 'O') {
                            stoppingPoint = k;
                            stopAtHole = true;
                        } else if (southTilt[k][l] == 'G') {
                            if (stopAtHole) {
                                southTilt[k][l] = '.';
                            } else {
                                southTilt[k][l] = '.';
                                southTilt[stoppingPoint][l] = 'G';
                                stoppingPoint = stoppingPoint - 1;
                            }
                        }

                    }


                }
                if (southValid) {
                    this.currentConfig = new TiltConfig(getDimension(), southTilt);
                    alertObservers(MOVE_COMPLETE);
                } else {
                    alertObservers(INVALID_MOVE);
                }
                break;
            case 'E' | 'e':
                char[][] eastTilt = new char[getDimension()][getDimension()];
                boolean eastValid = true;
                for (int i = 0; i < getDimension(); i++) {
                    for (int j = 0; j < getDimension(); j++) {
                        eastTilt[i][j] = this.currentConfig.getGrid()[i][j];
                    }
                }
                for (int k = 0; k < getDimension(); k++) {
                    int stoppingPoint = getDimension() - 1;
                    boolean stopAtHole = false;
                    for (int l = getDimension() - 1; l >= 0; l--) {
                        if (eastTilt[k][l] == '*') {
                            stoppingPoint = l - 1;
                            stopAtHole = false;
                        } else if (eastTilt[k][l] == 'B') {
                            if (stopAtHole) {
                                eastValid = false;
                                eastTilt[k][l] = '.';
                            } else {
                                eastTilt[k][l] = '.';
                                eastTilt[k][stoppingPoint] = 'B';
                                stoppingPoint = stoppingPoint - 1;
                            }

                        } else if (eastTilt[k][l] == 'O') {
                            stoppingPoint = l;
                            stopAtHole = true;
                        } else if (eastTilt[k][l] == 'G') {
                            if (stopAtHole) {
                                eastTilt[k][l] = '.';
                            } else {
                                eastTilt[k][l] = '.';
                                eastTilt[k][stoppingPoint] = 'G';
                                stoppingPoint = stoppingPoint - 1;
                            }
                        }

                    }


                }
                if (eastValid) {
                    this.currentConfig = new TiltConfig(getDimension(), eastTilt);
                    alertObservers(MOVE_COMPLETE);
                } else {
                    alertObservers(INVALID_MOVE);
                }
                break;
            case 'W' | 'w':
                char[][] westTilt = new char[getDimension()][getDimension()];
                boolean westValid = true;
                for (int i = 0; i < getDimension(); i++) {
                    for (int j = 0; j < getDimension(); j++) {
                        westTilt[i][j] = this.currentConfig.getGrid()[i][j];
                    }
                }
                for (int k = 0; k < getDimension(); k++) {
                    int stoppingPoint = 0;
                    boolean stopAtHole = false;
                    for (int l = 0; l < getDimension(); l++) {
                        if (westTilt[k][l] == '*') {
                            stoppingPoint = l + 1;
                            stopAtHole = false;
                        } else if (westTilt[k][l] == 'B') {
                            if (stopAtHole) {
                                westValid = false;
                                westTilt[k][l] = '.';
                            } else {
                                westTilt[k][l] = '.';
                                westTilt[k][stoppingPoint] = 'B';
                                stoppingPoint = stoppingPoint + 1;
                            }

                        } else if (westTilt[k][l] == 'O') {
                            stoppingPoint = l;
                            stopAtHole = true;
                        } else if (westTilt[k][l] == 'G') {
                            if (stopAtHole) {
                                westTilt[k][l] = '.';
                            } else {
                                westTilt[k][l] = '.';
                                westTilt[k][stoppingPoint] = 'G';
                                stoppingPoint = stoppingPoint + 1;
                            }
                        }

                    }

                }
                if (westValid) {
                    this.currentConfig = new TiltConfig(getDimension(), westTilt);
                    alertObservers(MOVE_COMPLETE);
                } else {
                    alertObservers(INVALID_MOVE);
                }
                break;
            default:
                alertObservers(INVALID_DIRECTION);
        }
    }





    public int getDimension() {
        return this.currentConfig.getDim();
    }

    public void getHint() {
        Solver mainSolve = new Solver();
        LinkedList<Configuration> path = mainSolve.solve(currentConfig, false);
        if (path.get(1) == null) {
            alertObservers(NO_SOLUTION);
        } else {
            currentConfig = (TiltConfig) path.get(1);
            alertObservers(HINT_DONE);
        }
    }

    public void reset() {
        currentConfig = originalConfig;
        alertObservers(PUZZLE_RESET);
    }

    public void quit() {
        System.exit(0);
    }

    public boolean gameOver() {
        for (int i = 0; i < getDimension(); i++) {
            for (int j = 0; j < getDimension(); j++) {
                if (currentConfig.getGrid()[i][j] == 'G') {
                    return false;
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
    public void addObserver(Observer<TiltModel, String> observer) {
        this.observers.add(observer);
    }

    public String toString() {
        String printable = "";
        for (int i = 0; i < getDimension(); i++) {
            for (int j = 0; j < getDimension(); j++) {
                printable += this.currentConfig.getGrid()[i][j];
                if (j < getDimension() - 1) {
                    printable += " ";
                } else if (i < getDimension() - 1) {
                    printable += "\n";
                }
            }
        }
        return printable;
    }

    public TiltConfig getCurrentConfig() {return this.currentConfig;};

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
