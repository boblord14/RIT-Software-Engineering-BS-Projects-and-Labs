package puzzles.tilt.model;

// TODO: implement your TiltConfig for the common solver

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class TiltConfig implements Configuration {

    private final int dim;

    private char[][] currentGrid;


    /**
     * Main config for the tilt variant, constructor here
     *
     * @param dim what the side length of the grid is
     * @param currentGrid 2d array that represents what thing is at each location in the grid
     *
     */
    public TiltConfig(int dim, char[][] currentGrid) {
        this.dim = dim;
        this.currentGrid = currentGrid;
    }
    @Override
    public boolean isSolution() {
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                if (currentGrid[i][j] == 'G') {
                    return false;
                }
            }
        }

        return true;
    }
    public int getDim() {return this.dim;}

    public char[][] getGrid() {return this.currentGrid;};

    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighborList = new ArrayList<>();
        char[][] northTilt = new char[dim][dim];
        char[][] eastTilt = new char[dim][dim];
        char[][] southTilt = new char[dim][dim];
        char[][] westTilt = new char[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                char currentVal = this.currentGrid[i][j];
                northTilt[i][j] = currentVal;
                eastTilt[i][j] = currentVal;
                southTilt[i][j] = currentVal;
                westTilt[i][j] = currentVal;
            }

        }
        boolean northValid = true;
        for (int j = 0; j < dim; j++) {
            int stoppingPoint = 0;
            boolean stopAtHole = false;
            for (int i = 0; i < dim; i++) {
                if (northTilt[i][j] == '*') {
                    stoppingPoint = i + 1;
                    stopAtHole = false;
                } else if (northTilt[i][j] == 'B') {
                    if (stopAtHole) {
                        northValid = false;
                        northTilt[i][j] = '.';
                    } else {
                        northTilt[i][j] = '.';
                        northTilt[stoppingPoint][j] = 'B';
                        stoppingPoint = stoppingPoint + 1;
                    }

                } else if (northTilt[i][j] == 'O') {
                    stoppingPoint = i;
                    stopAtHole = true;
                } else if (northTilt[i][j] == 'G') {
                    if (stopAtHole) {
                        northTilt[i][j] = '.';
                    } else {
                        northTilt[i][j] = '.';
                        northTilt[stoppingPoint][j] = 'G';
                        stoppingPoint = stoppingPoint + 1;
                    }
                }

            }

        }
        boolean eastValid = true;
        for (int i = 0; i < dim; i++) {
            int stoppingPoint = dim - 1;
            boolean stopAtHole = false;
            for (int j = dim - 1; j >= 0; j--) {
                if (eastTilt[i][j] == '*') {
                    stoppingPoint = j - 1;
                    stopAtHole = false;
                } else if (eastTilt[i][j] == 'B') {
                    if (stopAtHole) {
                        eastValid = false;
                        eastTilt[i][j] = '.';
                    } else {
                        eastTilt[i][j] = '.';
                        eastTilt[i][stoppingPoint] = 'B';
                        stoppingPoint = stoppingPoint - 1;
                    }

                } else if (eastTilt[i][j] == 'O') {
                    stoppingPoint = j;
                    stopAtHole = true;
                } else if (eastTilt[i][j] == 'G') {
                    if (stopAtHole) {
                        eastTilt[i][j] = '.';
                    } else {
                        eastTilt[i][j] = '.';
                        eastTilt[i][stoppingPoint] = 'G';
                        stoppingPoint = stoppingPoint - 1;
                    }
                }

            }


        }
        boolean southValid = true;
        for (int j = 0; j < dim; j++) {
            int stoppingPoint = dim - 1;
            boolean stopAtHole = false;
            for (int i = dim - 1; i >= 0; i--) {
                if (southTilt[i][j] == '*') {
                    stoppingPoint = i - 1;
                    stopAtHole = false;
                } else if (southTilt[i][j] == 'B') {
                    if (stopAtHole) {
                        southValid = false;
                        southTilt[i][j] = '.';
                    } else {
                        southTilt[i][j] = '.';
                        southTilt[stoppingPoint][j] = 'B';
                        stoppingPoint = stoppingPoint - 1;
                    }

                } else if (southTilt[i][j] == 'O') {
                    stoppingPoint = i;
                    stopAtHole = true;
                } else if (southTilt[i][j] == 'G') {
                    if (stopAtHole) {
                        southTilt[i][j] = '.';
                    } else {
                        southTilt[i][j] = '.';
                        southTilt[stoppingPoint][j] = 'G';
                        stoppingPoint = stoppingPoint - 1;
                    }
                }

            }
        }
        boolean westValid = true;
        for (int i = 0; i < dim; i++) {
            int stoppingPoint = 0;
            boolean stopAtHole = false;
            for (int j = 0; j < dim; j++) {
                if (westTilt[i][j] == '*') {
                    stoppingPoint = j + 1;
                    stopAtHole = false;
                } else if (westTilt[i][j] == 'B') {
                    if (stopAtHole) {
                        westValid = false;
                        westTilt[i][j] = '.';
                    } else {
                        westTilt[i][j] = '.';
                        westTilt[i][stoppingPoint] = 'B';
                        stoppingPoint = stoppingPoint + 1;
                    }

                } else if (westTilt[i][j] == 'O') {
                    stoppingPoint = j;
                    stopAtHole = true;
                } else if (westTilt[i][j] == 'G') {
                    if (stopAtHole) {
                        westTilt[i][j] = '.';
                    } else {
                        westTilt[i][j] = '.';
                        westTilt[i][stoppingPoint] = 'G';
                        stoppingPoint = stoppingPoint + 1;
                    }
                }

            }

        }
        if (northValid) {
            neighborList.add(new TiltConfig(this.dim, northTilt));
        }
        if (southValid) {
            neighborList.add(new TiltConfig(this.dim, southTilt));
        }
        if (eastValid) {
            neighborList.add(new TiltConfig(this.dim, eastTilt));
        }
        if (westValid) {
            neighborList.add(new TiltConfig(this.dim, westTilt));
        }
        return neighborList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TiltConfig that = (TiltConfig) o;
        return Arrays.deepEquals(currentGrid, that.currentGrid);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(currentGrid);
    }

    @Override
    public String toString() {
        String printable = "\n";
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                printable += this.currentGrid[i][j];
                if (j < this.dim - 1) {
                    printable += " ";
                } else if (i < this.dim - 1) {
                    printable += "\n";
                }
            }
        }
        return printable;
    }
}
