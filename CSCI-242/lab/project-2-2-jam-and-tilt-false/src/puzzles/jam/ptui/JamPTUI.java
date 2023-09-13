package puzzles.jam.ptui;

import puzzles.common.Observer;
import puzzles.jam.model.JamModel;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class JamPTUI implements Observer<JamModel, String> {
    /** stores the main model */
    private JamModel model;
    /** scanner for reading input */
    private Scanner in;
    /** selectionRow handles the last selected row for the selection movement */
    int selectionRow = -1;
    /** same as above but for cols */
    int selectionCol = -1;
    /** tells if the game should be running now */
    public JamPTUI(String fileName){
        model = new JamModel();
        model.addObserver(this);
        in = new Scanner(System.in);
        model.loadBoardFromFile(fileName);
        System.out.println("""
                        h(int)              -- hint next move
                        l(oad) filename     -- load new puzzle file
                        s(elect) r c        -- select cell at r, c
                        q(uit)              -- quit the game
                        r(eset)             -- reset the current game""");
    }


    /** main gameloop
     *
     * data is the data from the scanner. when a line is sent to the scanner it goes there.
     *
     * it is cleaned up and passed to the ifElse. q/h/l/r/s and their variations are the acceptable commands.
     *
     * q terminates the program, hint does a hint assuming its not solved already, and if it is it doesnt do a hint and just says its solved already,
     * l loads a new board based on the given filename, r resets the board to its initial state, and s does the selection for move.
     *
     * S first does a try/catch, and tries to do tileOperation based on the given input. the catch handles the input not being correct and complains accordingly.
     *
     * if someone inputs anything else it just restates the command list
     * */
    private void gameLoop(){
        String[] data;
        while(true) {
            if (in.hasNextLine()) {
                String command = in.nextLine().strip();
                data = command.split(" ");
                if (data[0].equals("q") || data[0].equals("Q") || data[0].equalsIgnoreCase("quit")) {
                    System.exit(0);

                    return;

                } else if (data[0].equals("h") || data[0].equals("H") || data[0].equalsIgnoreCase("hint")) {
                    if(!model.gameOver()){
                        model.doHint();
                    }
                    else{
                        System.out.println("Already solved!");
                        System.out.println(model.toString());
                    }

                } else if (data[0].equals("l") || data[0].equals("L") || data[0].equalsIgnoreCase("load")) {
                    model.loadBoardFromFile(data[1]);
                } else if (data[0].equals("r") || data[0].equals("R") || data[0].equalsIgnoreCase("reset")) {
                    model.reload();
                } else if (data[0].equals("s") || data[0].equals("S") || data[0].equalsIgnoreCase("select")) {
                    try {
                        int x = Integer.parseInt(data[1]);
                        int y = Integer.parseInt(data[2]);
                        model.tileOperation(x, y);

                    } catch (InputMismatchException e) {

                        System.out.println("r and c must be integers");
                    } catch (NoSuchElementException e) {

                        System.out.println("Must enter r and c on one line.");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("r should be between 0 and " + model.getDimensions()[0] + ", and c should be between 0 and " + model.getDimensions()[1]);
                    }
                } else{
                    System.out.println("""
                        h(int)              -- hint next move
                        l(oad) filename     -- load new puzzle file
                        s(elect) r c        -- select cell at r, c
                        q(uit)              -- quit the game
                        r(eset)             -- reset the current game""");
                }
            }
            }
        }
    /** runs the main game loop. unsure if this is necessary or not but im too far in to take it out and pray nothing breaks.  */
    public void run() {

        while (true) {
            gameLoop(); // gameplay
        }

    }


    /** main update data.
     *
     * first updates the model
     *
     * gameshift is a successful normal move, and is printed out with the info/game board.
     * hintshift is a successful hint solver move, and is printed out with the info/game board.
     * empty is someone picking an empty square with nothing selected, and is printed out with the info/game board.
     * invalid is someone picking an occupied square with a car selected, and is printed out with the info/game board.
     * move is the initial car selection, populating selectionRow/Col, and is printed out with the info/game board.
     * loaded is the file load, and is printed out with the info/game board.
     * loadfailed is a failed file load, and is printed out with the info/game board.
     * reset is the reset to the initial game board state, and is printed out with the info/game board.
     * nosolutions is when you ask for a hint to an unsolvable puzzle, and is printed out with the info/game board.
     *
     * default case prints out the command list, even though it should never be called.
     * */
    @Override
    public void update(JamModel model, String msg) {
        this.model=model;
        String[] msgData = msg.split("!");
        switch (msgData[0]) {
            case "GAMESHIFT" -> {  // normal move triggered
                System.out.println("> Moved from (" + selectionRow + ", " + selectionCol + ")  to (" +msgData[1]+ ", "+msgData[2] + ")");
                System.out.println(model.toString());
                return;
            }
            case "HINTSHIFT" -> {  //process hint move, with catch if solved already
                System.out.println("> Next step!");
                System.out.println(model.toString());
                return;
            }
            case "EMPTY" -> {  //someone initially selected an empty square
                System.out.println("> No car at (" + msgData[1] + ", " + msgData[2] + ")");
                System.out.println(model.toString());
                return;
            }
            case "INVALID" -> { //someone selected a populated square as the second move input
                System.out.println("> Can't move from (" + selectionRow + ", " + selectionCol + ")  to (" +msgData[1]+ ", "+msgData[2] + ")");
                System.out.println(model.toString());
            }
            case "MOVE" -> { //initial move from selecting a car
                System.out.println("> Selected (" + msgData[1] + ", " + msgData[2] + ")");
                selectionRow = Integer.parseInt(msgData[1]);
                selectionCol = Integer.parseInt(msgData[2]);
                System.out.println(model.toString());
            }
            case "LOADED" -> { //successful file load
                System.out.println("> Loaded: " + msgData[1]);
                System.out.println(model.toString());
            }
            case "LOADFAILED" -> { //failed file load
                System.out.println("> Failed to load: " + msgData[1]);
                System.out.println(model.toString());
            }
            case "RESET" -> {//reset game board
                System.out.println("Puzzle reset!");
                System.out.println(model.toString());
            }
            case "NOSOLUTIONS" -> {//no possible hints
                System.out.println("> This puzzle isn't solvable!");
                System.out.println(model.toString());
            }
            default ->  System.out.println("""
                    h(int)              -- hint next move
                    l(oad) filename     -- load new puzzle file
                    s(elect) r c        -- select cell at r, c
                    q(uit)              -- quit the game
                    r(eset)             -- reset the current game""");
        }

    }
    /** creates the JamPTUI, and calls the run command with the args */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamPTUI filename");
        } else{
            JamPTUI main = new JamPTUI(args[0]);
            main.run();

        }
    }
}
