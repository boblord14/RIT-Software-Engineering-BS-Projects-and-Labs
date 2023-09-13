package puzzles.jam.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.jam.model.JamModel;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;

public class JamGUI extends Application  implements Observer<JamModel, String>  {
    /** The resources directory is located directly underneath the gui package
     *
     * i based mine off of the examples on the website and noticed resources too late, its fine as it is though, dont think i need these in the end*/
    private final static String RESOURCES_DIR = "resources/";

    /** background for red x car */
    private final static Background X_CAR_COLOR = new Background( new BackgroundFill(Color.web("#DF0101"), null, null));
    /** background for empty spaces */
    private static final Background GRAY =
            new Background( new BackgroundFill(Color.GRAY, null, null));
    /** button text font size */
    private final static int BUTTON_FONT_SIZE = 20;
    /** msglabel for getting messages to display */
    private Label msgLabel;
    /** main model */
    private JamModel model;
    /** gridpane used for the gui */
    private GridPane gridPane;
    /** array of the game buttons for the board */
    private static Button[][] buttonArray;
    /** rand used in color generation */
    private Random rand;
    /** hashmap used to store color data for background things */
    private HashMap<String, Background> colorData;
    /** used for the move command the same way the ptui does it */
    int selectionRow = -1;
    /** used for the move command the same way the ptui does it */
    int selectionCol = -1;
    /** initializes the hashmap, the random, and the model, gives msglabel a generic string, adds this as an observer, pops in the filename and calls loadboardfromfile */
    public void init() {
        colorData = new HashMap<>();
        rand = new Random();
        this.msgLabel = new Label(" -- ");
        this.model = new JamModel();
        model.addObserver(this);
        String filename = getParameters().getRaw().get(0);
        model.loadBoardFromFile(filename);
    }
    /** start method. bulk is outsourced to the sceneBuilder method so the scene can be rebuilt on file load.
     * beyond that this just sets the title, does stage things, and displays it.
     * */
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = sceneBuilder(stage);
        stage.setTitle("Jam GUI");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    /** main gui component.
     *
     * nearly identical to lightsout, as parts of it were taken from it.
     *
     * top of the borderpane is the msglabel set to center
     * center of it is the gridpane created via makeGridPane
     * bottom is the reset/load/hint buttons, all rigged up to either model functions or offsourced to other functions here.
     *
     *after all that, guiUpdate is called to give the buttons the appropriate colors, and the scene is created and returned.
     * */
    private Scene sceneBuilder(Stage stage){
        BorderPane borderPane = new BorderPane();

        FlowPane labelPane = new FlowPane(this.msgLabel);
        labelPane.setAlignment(Pos.TOP_CENTER);
        borderPane.setTop(labelPane);

        gridPane = makeGridPane();
        gridPane.setAlignment(Pos.CENTER);
        borderPane.setCenter(gridPane);

        Button resetGame = new Button("Reset");
        resetGame.setOnAction(event -> model.reload());

        Button loadGame = new Button("Load Game");
        loadGame.setOnAction(event -> loadBoard(stage));

        Button hint = new Button("Hint");
        hint.setOnAction(event -> hintProcess());

        FlowPane buttonPane = new FlowPane(loadGame, resetGame, hint);
        buttonPane.setAlignment(Pos.BOTTOM_CENTER);
        borderPane.setBottom(buttonPane);

        guiUpdate();

        Scene scene = new Scene(borderPane);
        return scene;

    }

    /** handles the gridpane of buttons. works similar to lightsout. uses a 2d array to populate a 2d buttonarray with buttons
     * all set to do tileOperation on press at their row/col. beyond that this just does style things to them, and adds them to the
     * gridpane at the row/col, and returns the gridpane */
    private GridPane makeGridPane(){
        GridPane gridPane = new GridPane();
        buttonArray = new Button[model.getDimensions()[0]][model.getDimensions()[1]];
        for(int row=0;row< model.getDimensions()[0];row++){
            for(int col=0;col< model.getDimensions()[1];col++){
                buttonArray[row][col] = new Button();
                buttonArray[row][col].setStyle(
                        "-fx-font-size: " + BUTTON_FONT_SIZE +";" +
                                "-fx-font-weight: bold;");
                buttonArray[row][col].setMinSize(100, 100);

                int finalRow = row;//intellij didnt like this unless i put these in, i dont mind but whatever
                int finalCol = col;
                buttonArray[row][col].setOnAction(event -> model.tileOperation(finalRow, finalCol));
                gridPane.add(buttonArray[row][col], col, row);
            }
        }
        return gridPane;
    }

    /** checks if the game is over, and does hint if its not, else say game is already solved  */
    private void hintProcess(){
        if(!model.gameOver()){
            model.doHint();
        }
        else{
            msgLabel.setText("Already solved!");
        }
    }
    /** gui update, processes the workableArray from the model with 2 for loops, checks if the current row/col is empty(aka is a ".", and if so
     * sets the background to gray and no text.
     *
     * if its not empty, it checks if it is in the colorData hashmap, and if not, adds a new key, value
     * pair to colorData with the car name as the key and a new background set to a random color  as the value. for the random color, red is toned down to not
     * over color the X car. Then the background of that button is set to that color and the text set to that car name
     *
     * if it is already in the hashmap, the background/text is retrieved and set. The X car gets special treatment as its always the same color, and an if statement ensures this
     *
     * */
    private void guiUpdate(){
        String[][] workableArray = model.workableArray();
        for(int row=0;row< model.getDimensions()[0];row++){
            for(int col=0;col< model.getDimensions()[1];col++) {
                if(Objects.equals(workableArray[row][col], ".")){
                    buttonArray[row][col].setBackground(GRAY);
                    buttonArray[row][col].setText("");
                } else if(colorData.containsKey(workableArray[row][col])){
                        buttonArray[row][col].setBackground(colorData.get(workableArray[row][col]));
                        buttonArray[row][col].setText(workableArray[row][col]);
                    } else if(Objects.equals(workableArray[row][col], "X")) {
                    buttonArray[row][col].setBackground(X_CAR_COLOR);
                    buttonArray[row][col].setText("X");
                }
                    else{
                        colorData.put(workableArray[row][col], new Background( new BackgroundFill(Color.rgb((int) (rand.nextInt(255)/1.1), rand.nextInt(255), rand.nextInt(255)), null, null)));
                        buttonArray[row][col].setBackground(colorData.get(workableArray[row][col]));
                        buttonArray[row][col].setText(workableArray[row][col]);
                    }
                }
            }
        }

    /** handles loading a board, takes stage as a param.
     *
     * nearly identical to lightsout's variant, just with a different location and requirement for the file to load.
     *
     * the one key difference at the end is that it sets the scene again with sceneBuilder once a file is loaded in , in order to
     * update the gridpane with the new dimensions and whatnot, then updates the gui to fix the colors. */
    private void loadBoard(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load a game board.");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/data/jam"));
        fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile!=null) {
            model.loadBoardFromFile(selectedFile);
        }
        stage.setScene(sceneBuilder(stage));
        guiUpdate();
    }
    /** main update method.
     * first updates the model
     *
     * gameshift is a successful normal move, checks if that solves it, and if so says a message for it, otherwise says a genric message and updates game board.
     * hintshift is a successful hint solver move, and updates game board/says message.
     * empty is someone picking an empty square with nothing selected, and says message.
     * invalid is someone picking an occupied square with a car selected, and says message.
     * move is the initial car selection, populating selectionRow/Col, and says message.
     * loaded is the file load, and says message.
     * loadfailed is a failed file load, and says message/updates game board.
     * reset is the reset to the initial game board state, and updates game board.
     * nosolutions is when you ask for a hint to an unsolvable puzzle, and says message.
     * */
    @Override
    public void update(JamModel jamModel, String message) {
        this.model=model;
        String[] msgData = message.split("!");
        switch (msgData[0]) {
            case "GAMESHIFT" -> {  // normal move triggered
                if(model.gameOver()){
                    msgLabel.setText("Puzzle is solved, nicely done!");
                }else{
                    msgLabel.setText("Moved from (" + selectionRow + ", " + selectionCol + ") to (" +msgData[1]+ ", "+msgData[2] + ")");
                }

                guiUpdate();
            }
            case "HINTSHIFT" -> {  //process hint move, with catch if solved already
                msgLabel.setText("Next step!");
                guiUpdate();
            }
            case "EMPTY" -> {  //someone initially selected an empty square
                msgLabel.setText("No car at (" + msgData[1] + ", " + msgData[2] + ")");
            }
            case "INVALID" -> { //someone selected a populated square as the second move input
                msgLabel.setText("Can't move from (" + selectionRow + ", " + selectionCol + ") to (" +msgData[1]+ ", "+msgData[2] + ")");
            }
            case "MOVE" -> { //initial move from selecting a car
                msgLabel.setText("Selected (" + msgData[1] + "," + msgData[2] + ")");
                selectionRow = Integer.parseInt(msgData[1]);
                selectionCol = Integer.parseInt(msgData[2]);
            }
            case "LOADED" -> { //successful file load
                msgLabel.setText("Loaded: " + msgData[1]);
            }
            case "LOADFAILED" -> { //failed file load
                msgLabel.setText("Failed to load: " + msgData[1]);
            }
            case "RESET" -> {//reset game board
                msgLabel.setText("Puzzle reset!");
                guiUpdate();
            }
            case "NOSOLUTIONS" -> {//no possible hints
                msgLabel.setText("This puzzle isn't solvable!");
            }
        }
    }

    /** main launch, just launches the app if theres sufficient args for it */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
