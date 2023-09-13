package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.LightsOutModel;
import model.Observer;

import java.io.File;

/**
 * Manages the GUI's controller and view parts, gets and requests stuff from the model as need be
 */
public class LightOutGUI extends Application implements Observer {

    /**
     * model is the current copy of the model used by this
     */
    private LightsOutModel model;
    /**
     * moveLabel is the label displayed for the move count
     */
    private Label moveLabel;
    /**
     * msgLabel is the label displayed for stuff like winning, successful file load, etc
     */
    private Label msgLabel;
    /**
     * hint is the hint button, created up here as it needs to be turned on and off at times
     */
    private Button hint;
    /**
     * 2d array of the lightsout buttons so they can be accessed and modified at any time
     */
    private static Button[][] buttonArray;
    /**
     * lastHighlight is an array of some stuff relating to an active hint. [0] is the col, [1] is the row, and [2] is a
     * flag thats 1 if the hint highlight outline is active and 0 if it is not
     */
    private int[] lastHighlight = new int[3];
    /**
     * WHITE is a final for the white button background
     */
    private static final Background WHITE =
            new Background( new BackgroundFill(Color.WHITE, null, null));
    /**
     * BLACK is a final for the black button background
     */
    private static final Background BLACK =
            new Background( new BackgroundFill(Color.BLACK, null, null));
    /**
     * BORDER_NORMAL is a final for the normal button border
     */
    private static final Border BORDER_NORMAL =
            new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2.5)));
    /**
     * BORDER_HIGHLIGHT is a final for the highlighted hint button border
     */
    private static final Border BORDER_HIGHLIGHT =
            new Border(new BorderStroke(Color.LIGHTGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3.5)));

    /**
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     *
     * This is the main gui function. first msg/move label are set to -- respectively just to create an initial "nothing here"
     * state when the gui is started.
     *
     * Then comes the borderpane, then a flowpane with move and msglabel aligned top left and right,
     * and that flowpane added to the top of the borderpane
     *
     * After that comes the gridpane, created via makeGridPane(documented later), alligned to center, and added to the
     * center of the borderpane
     *
     * After that, the button controls at the bottom is created, first the new game button which calls model.generateRandomBoard()
     * on press, then the load game button which calls loadBoard(again, documented later), and lastly the this.hint becomes
     * the hint button, calling model.getHint() on press. These three buttons get chucked into a flowpane, which gets
     * alligned to bottom center, and said flowpane becomes the bottom of the borderpane.
     *
     * After that setBoardDisableState(true); gets called, which is documented later as well. It just disables the actual
     * game elements of the board(hints and actual button presses) until you load a game or hit new game.
     *
     * Lastly, the borderPane gets put into a new scene and put into the stage, stage gets called "Lights Out",
     * the window properties are set, and the stage is finally displayed
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.moveLabel = new Label("Moves: -- ");
        this.msgLabel = new Label("Message: -- ");
        BorderPane borderPane = new BorderPane();

        FlowPane labelPane = new FlowPane(this.moveLabel, this.msgLabel);
        this.moveLabel.setAlignment(Pos.TOP_LEFT);
        this.msgLabel.setAlignment(Pos.TOP_RIGHT);
        borderPane.setTop(labelPane);

        GridPane gridPane = makeGridPane();
        gridPane.setAlignment(Pos.CENTER);
        borderPane.setCenter(gridPane);

        Button newGame = new Button("New Game");
        newGame.setOnAction(event ->model.generateRandomBoard());
        Button loadGame = new Button("Load Game");
        loadGame.setOnAction(event -> loadBoard(stage));
        this.hint = new Button("Hint");
        this.hint.setOnAction(event -> model.getHint());
        FlowPane buttonPane = new FlowPane(newGame, loadGame, hint);
        buttonPane.setAlignment(Pos.BOTTOM_CENTER);
        borderPane.setBottom(buttonPane);

        setBoardDisableState(true);

        Scene scene = new Scene(borderPane);
        stage.setTitle("Lights Out");
        stage.setScene(scene);
        stage.setWidth(500);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @return the properly made gridpane
     * This is the function that handles creating the gridpane for the start method.
     *
     * Starts by creating an initial gridpane, and setting buttonArray to the dimensions of the model's board.
     *
     * Then, double for loops lets us put new buttons corresponding to the model's board into buttonArray,
     * and the button dimensions get set.
     *
     * After that row and col are chucked into finalRow and finalCol respectively because thats what intellij told me to do,
     * and then the button's on press action becomes model.toggleTile on finalCol and finalRow.
     *
     * After that the buttonArray[][] slot with the button in it gets put into the gridpane at the corresponding coordinates,
     *
     * At the end, the gridpane is returned after it is fully built by the for loops.
     *
     */
    private GridPane makeGridPane(){
        GridPane gridPane = new GridPane();
        buttonArray = new Button[model.getDimension()][model.getDimension()];
        for(int row=0;row< model.getDimension();row++){
            for(int col=0;col< model.getDimension();col++){
                buttonArray[row][col] = new Button();
                buttonArray[row][col].setBorder(BORDER_NORMAL);
                buttonArray[row][col].setMinSize(100, 100);

                int finalRow = row;//intellij didnt like this unless i put these in, i dont mind but whatever
                int finalCol = col;
                buttonArray[row][col].setOnAction(event -> model.toggleTile(finalCol, finalRow));
                gridPane.add(buttonArray[row][col], col, row);
            }
        }
        return gridPane;
    }

    /**
     * loadBoard is called when the load button is clicked, and just handles creating the file chooser window, and restricting
     * it to the specific .lob file type and the /boards directory.
     *
     * After that, theres a quick check to make sure a file was actually selected(in case someone just clicks the X), and
     * if so, the model's load from file method is called.
     * @param stage the stage from the start method
     */
    private void loadBoard(Stage stage){
        //extra stage garbage for loading board from file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load a game board.");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/boards"));
        fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Text Files", "*.lob"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile!=null) {
            model.loadBoardFromFile(selectedFile);
        }
    }

    /**
     * hintHighlight is just a simple thing used by update to handle the actual highlighting, taking col and row as params
     * and setting the corresponding button in buttonArray's background to the highlight light green color, setting the
     * active highlight flag at lastHighlight[2] to 1 to denote the green highlight is active.
     *
     * @param col the col coordinate to highlight
     * @param row the row coordinate to highlight
     */
    private void hintHighlight(int col, int row){
        buttonArray[row][col].setBorder(BORDER_HIGHLIGHT);
        lastHighlight[0] = col;
        lastHighlight[1] = row;
        lastHighlight[2] = 1;

    }

    /**
     * boardTileUpdate is what updates the visual board button's background based on the states of them in the model.
     *
     * First the active highlight flag over at lastHighlight[2] is checked and disabled if it is active, setting that button's
     * border color to the default instead of the highlight.
     *
     * After that's over 2 for loops parse all tiles in the model, checking if the tile is on and if so setting the background
     * to white, and black if it is not on. As the model handles all the intricacies of the actual button click logic,
     * we just need to check the end result.
     *
     * After that's done, a check if the model says the game is over is done, and if so the board is disabled and the
     * you win message is set in the msgLabel.
     */
    private void boardTileUpdate(){
        if (lastHighlight[2] == 1){
            buttonArray[lastHighlight[1]][lastHighlight[0]].setBorder(BORDER_NORMAL);
            lastHighlight[2]=0;
        }
        for(int row=0;row< model.getDimension();row++){
            for(int col=0;col< model.getDimension();col++){
                if(model.getTile(col, row).isOn()){
                    buttonArray[row][col].setBackground(WHITE);
                }else{
                    buttonArray[row][col].setBackground(BLACK);
                }
            }
        }
        if  (model.gameOver()){
            setBoardDisableState(true);
            this.msgLabel.setText("Message: You win good job, again?");
        }
    }

    /**
     * setBoardDisableState handles enabling and disabling the tile buttons and the hint button if the game isn't being played
     * currently. For example when the gui starts there is no game going on, so you obviously cant request a hint or click a tile.
     *
     * This is just done by setting setDisable on all the buttons in buttonArray and the hint button to whatever
     * the boolean state is. This method is just a less effort way to do such multiple times in the rest of this class.
     *
     * @param state sets isDisabled to true or false based on this value
     */
    private void setBoardDisableState(boolean state){
        this.hint.setDisable(state);
        for(int row=0;row< model.getDimension();row++){
            for(int col=0;col< model.getDimension();col++){
                buttonArray[row][col].setDisable(state);
            }
        }
    }

    /**
     * Creates the initial model, and adds this to an observer to it.
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        this.model = new LightsOutModel();
        model.addObserver(this);
    }

    /**
     * Main that just launches the application and triggers everything else.
     * @param args initial arguments if present, shouldnt be any.
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Update method. First sets the model here to the current state of the model(o), and then does some string manipulation
     * to break up o2 and put it in tempString, which is the message being passed in by the update.
     *
     * a switch is used to determine which message is being passed in, by looking at tempString[0](first part of o2 before
     * the ":", if one is there).
     *
     * If it is "loaded" that means the model's board representation has been fully loaded in
     * and the gui needs to be updated accordingly. This means calling boardTileUpdate() to load in the tile on/off states,
     * setBoardDisableState(false) to enable the buttons to be clickable, the moveLabel to 0 moves, and the msgLabel to
     * a generic game start message.
     *
     * If it is "loadFailed" that means the file load had an issue, so a message is just put in msgLabel stating that.
     * I never actually figured out how to test this case, so I'm assuming the model was built correctly to toss me the correct
     * thing every time for this one.
     *
     * If it is "hint" that means a hint highlight is requested at specific coordinates in tempString[1], which then get
     * pulled out of tempString[1] and passed to hintHighlight in the respective row/col params.
     *
     * If it is none of these, it must be a tile click. In that case, just update moveLabel with the new move count,
     * and call boardTileUpdate to process the board tile on/off changes. Note moveLabel is needs to be the model's moveCount
     * plus 1 because the first move to it is move 0, and this just looks better for an actual count.
     *
     * @param o the object that wishes to inform this object
     *                about something that has happened.
     * @param o2 optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(Object o, Object o2) {
        model = (LightsOutModel) o;
        String[] tempString = o2.toString().split(":");
        switch (tempString[0]) {
            case "loaded" -> {
                boardTileUpdate();
                setBoardDisableState(false);
                moveLabel.setText("Moves: 0 ");
                this.msgLabel.setText("Message: Game started");
            }
            case "loadFailed" -> this.msgLabel.setText("Message: Load from file failed, invalid file");
            case "Hint" -> {
                tempString[1] = tempString[1].replace(" ","");
                String[] coords = tempString[1].split(",");
                hintHighlight(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
            }
            default -> {
                moveLabel.setText("Moves: " + (model.getMoves()+1) + " ");
                boardTileUpdate();
            }
        }
    }
}
