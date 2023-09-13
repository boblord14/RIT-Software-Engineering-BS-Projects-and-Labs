package puzzles.tilt.gui;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.File;


public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    private final static int GRID_SIZE = 500;
    private Button hint;

    private GridPane gridPane;
    private Label msg;
    private TiltModel model;
    private Button[][] tiltGrid;


    // for demonstration purposes
    private Image greenDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green.png"));
    private Image blueDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"blue.png"));
    private Image block = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"block.png"));
    private Image hole = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"hole.png"));

    public void init() {
        this.msg = new Label("Message: -- ");
        this.model = new TiltModel();
        model.addObserver(this);
        String filename = getParameters().getRaw().get(0);
        model.loadGrid(filename);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = sceneBuilder(stage);

        stage.setTitle("Tilt GUI");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private Scene sceneBuilder(Stage stage){
        BorderPane borderPane = new BorderPane();

        FlowPane labelPane = new FlowPane(this.msg);
        labelPane.setAlignment(Pos.TOP_CENTER);
        borderPane.setTop(labelPane);

        gridPane = makeGridPane();
        gridPane.setAlignment(Pos.CENTER);
        borderPane.setCenter(gridPane);
        Button resetGame = new Button("Reset");
        resetGame.setOnAction(event -> model.reset());
        Button loadGame = new Button("Load Game");
        loadGame.setOnAction(event -> loadGrid(stage));
        Button north = new Button("^");
        north.setOnAction(event -> model.move('n'));
        north.setAlignment(Pos.TOP_CENTER);
        Button south = new Button("v");
        south.setOnAction(event -> model.move('s'));
        south.setAlignment(Pos.BOTTOM_CENTER);
        Button east = new Button(">");
        east.setOnAction(event -> model.move('e'));
        east.setAlignment(Pos.CENTER_RIGHT);
        Button west = new Button("<");
        west.setOnAction(event -> model.move('w'));
        west.setAlignment(Pos.CENTER_LEFT);
        this.hint = new Button("Hint");
        this.hint.setOnAction(event -> getHint());
        FlowPane buttonPane = new FlowPane(loadGame, resetGame, hint, north, south, east, west);
        buttonPane.setOrientation(Orientation.VERTICAL);
        buttonPane.setAlignment(Pos.CENTER_RIGHT);
        borderPane.setRight(buttonPane);
        guiUpdate();

        Scene scene = new Scene(borderPane);
        return scene;

    }
    private GridPane makeGridPane() {
        GridPane gridPane = new GridPane();
        this.tiltGrid = new Button[model.getDimension()][model.getDimension()];
        for (int i = 0; i < model.getDimension(); i++) {
            for (int j = 0; j < model.getDimension(); j++){
                tiltGrid[i][j] = new Button();
                tiltGrid[i][j].setMinSize(GRID_SIZE / model.getDimension(), GRID_SIZE / model.getDimension());
                tiltGrid[i][j].setMaxSize(GRID_SIZE / model.getDimension(), GRID_SIZE / model.getDimension());
                gridPane.add(tiltGrid[i][j], j, i);
            }
        }
        greenDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green.png"), GRID_SIZE / model.getDimension(), GRID_SIZE / model.getDimension(), false, false);
        blueDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"blue.png"), GRID_SIZE / model.getDimension(), GRID_SIZE / model.getDimension(), false, false);
        block = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"block.png"), GRID_SIZE / model.getDimension(), GRID_SIZE / model.getDimension(), false, false);
        hole = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"hole.png"), GRID_SIZE / model.getDimension(), GRID_SIZE / model.getDimension(), false, false);
        return gridPane;
    }

    private void getHint(){
        if (!model.gameOver()){
            model.getHint();
        }
        else {
            msg.setText("Already solved!");
        }
    }
    private void guiUpdate(){
        for (int i = 0; i < model.getDimension(); i++){
            for(int j = 0; j < model.getDimension(); j++) {
                if (model.getCurrentConfig().getGrid()[i][j] == 'O') {
                    tiltGrid[i][j].setGraphic(new ImageView(hole));

                } else if (model.getCurrentConfig().getGrid()[i][j] == 'B') {
                    tiltGrid[i][j].setGraphic(new ImageView(blueDisk));
                } else if (model.getCurrentConfig().getGrid()[i][j] == 'G') {
                    tiltGrid[i][j].setGraphic(new ImageView(greenDisk));
                }
                else if (model.getCurrentConfig().getGrid()[i][j] == '*') {
                    tiltGrid[i][j].setGraphic(new ImageView(block));
                }
                else {
                    tiltGrid[i][j].setGraphic(null);
                }
            }
        }
    }

    private void loadGrid(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load a game board.");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/data/tilt"));
        fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            model.loadGrid(selectedFile);
        }
        stage.setScene(sceneBuilder(stage));
        guiUpdate();
    }

    @Override
    public void update(TiltModel model, String message) {
        this.model = model;
        String[] msgData = message.split("!");
        switch (msgData[0]) {
            case "moveComplete" -> {  // move successfully triggered
                guiUpdate();
                break;
            }
            case "hintDone" -> {  // a hint is used, and a solution exists. Will catch if already done.
                msg.setText("Next step!");
                guiUpdate();
                break;
            }
            case "noSolution" -> {  // a hint is attempted, but no solution exists
                msg.setText("No solution exists!");
                break;
            }
            case "invalidMove" -> { // a move that would send a blue slider into the hole is attempted.
                msg.setText("Illegal move. A blue slider will fall through the hole!");
                break;
            }
            case "loaded" -> { //  file load is successful
                msg.setText("Loaded: " + msgData[1]);
                break;
            }
            case "loadFailed" -> { // file load fails
                msg.setText("Failed to load: " + msgData[1]);
                break;
            }
            case "puzzleReset" -> { // reset game board
                msg.setText("Puzzle reset!");
                guiUpdate();
                break;
            }
        }

    }
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
