package puzzles.tilt.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TiltPTUI implements Observer<TiltModel, String> {
    private TiltModel model;

    private Scanner in;

    private boolean gameOn;

    public TiltPTUI(String filename){
        model = new TiltModel();
        model.addObserver(this);
        in = new Scanner(System.in);
        System.out.println("""
                        h(int)              -- hint next move
                        l(oad) filename     -- load new puzzle file
                        t(ilt) {N/S/E/W}    -- tilt the board in the given direction
                        q(uit)              -- quit the game
                        r(eset)             -- reset the current game""");
        model.loadGrid(filename);

    }

    private void gameLoop(){
        String[] data;
        String commandCopy = "";
        while(gameOn) {
            if (in.hasNextLine()) {
                String command = in.nextLine().strip();
                data = command.split(" ");
                commandCopy = data[0];
                if (data[0].equals("q") || data[0].equals("Q")) {
                    gameOn = false;
                    System.exit(0);

                    return;

                } else if (data[0].equals("h") || data[0].equals("H")) {
                    if(!model.gameOver()){
                        model.getHint();
                    }
                    else{
                        System.out.println("Already solved!");
                        System.out.println(model.toString());
                    }

                } else if (data[0].equals("l") || data[0].equals("L")) {
                    model.loadGrid(data[1]);
                } else if (data[0].equals("r") || data[0].equals("R")) {
                    model.reset();
                } else if (data[0].equals("t") || data[0].equals("T")) {
                    try {
                        Scanner s = new Scanner(command);
                        if ((data[1]).length() == 1) {
                            char direction = (data[1]).charAt(0);
                            model.move(direction);
                        } else {
                            System.out.println("direction must be a single character");
                        }

                    } catch (InputMismatchException e) {

                        System.out.println("direction must be N/S/E/W");
                    } catch (NoSuchElementException e) {
                        System.out.println("Must enter tilt direction on the same line.");
                    }
                }
            }
        }


        if (!(commandCopy.isEmpty())){

            System.out.println("""
                        h(int)              -- hint next move
                        l(oad) filename     -- load new puzzle file
                        t(ilt) {N/S/E/W}    -- tilt the board in the given direction
                        q(uit)              -- quit the game
                        r(eset)             -- reset the current game""");
        }
    }
    public void run() {

        while (true) {
            gameOn = true;
            gameLoop(); // gameplay
        }

    }

    @Override
    public void update(TiltModel model, String message) {
        this.model = model;
        String[] msgData = message.split("!");
        switch (msgData[0]) {
            case "moveComplete" -> {  // move successfully triggered
                System.out.println(model.toString());
                break;
            }
            case "hintDone" -> {  // a hint is used, and a solution exists. Will catch if already done.
                System.out.println("Next step!");
                System.out.println(model.toString());
                break;
            }
            case "noSolution" -> {  // a hint is attempted, but no solution exists
                System.out.println("No solution exists!");
                System.out.println(model.toString());
                break;
            }
            case "invalidMove" -> { // a move that would send a blue slider into the hole is attempted.
                System.out.println("Illegal move. A blue slider will fall through the hole!");
                System.out.println(model.toString());
                break;
            }
            case "loaded" -> { //  file load is successful
                System.out.println("Loaded: " + msgData[1]);
                System.out.println(model.toString());
                break;
            }
            case "loadFailed" -> { // file load fails
                System.out.println("Failed to load: " + msgData[1]);
                System.out.println(model.toString());
                break;
            }
            case "puzzleReset" -> { // reset game board
                System.out.println("Puzzle reset!");
                System.out.println(model.toString());
                break;
            }
            case "invalidDirection" -> { // a one character value other than n, N, s, S, e, E, w, W was used.
                System.out.println("Not a direction!");
                System.out.println(model.toString());
                break;
            }

            default ->  System.out.println("""
                    h(int)              -- hint next move
                    l(oad) filename     -- load new puzzle file
                    t(ilt) {N/S/E/W}    -- tilt the board in the given direction
                    q(uit)              -- quit the game
                    r(eset)             -- reset the current game""");
        }

    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltPTUI filename");
        } else {
            TiltPTUI main = new TiltPTUI(args[0]);
            main.run();
        }
    }
}
