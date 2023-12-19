package view;

import controller.Input.InventoryObserver;
import controller.Input.RoomChanger;
import controller.Input.RoomObserver;
import controller.Input.TileChanger;
import javafx.application.Application;
import javafx.beans.binding.MapBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Direction;
import model.Game;
import model.PlayerCharacter;
import model.Character;
import model.Room;
import model.Inventory.Drop;
import model.Inventory.Inventory;
import model.Inventory.PlayerInventory;
import model.Items.ItemAttribute;
import model.Save.*;
import model.Tiles.CharacterTile;
import model.Tiles.ChestTile;
import model.Tiles.ExitTile;
import model.Tiles.MerchantTile;
import model.Tiles.ObstacleTile;
import model.Tiles.Tile;
import model.map.Map;

import java.util.ArrayList;
import java.util.HashMap;


public class MUDGUI extends Application implements MUDUI{

    public static final Paint CHARACTERCOLOR = Color.AQUA;
    public static final Paint ENEMYCOLOR = Color.RED;
    public static final Paint DOORCOLOR = Color.BLACK;
    public static final Paint EMPTYCOLOR = Color.color(0,0,0,0);
    public static final Paint CHESTCOLOR = Color.BEIGE;
    public static final Paint OBSTACLECOLOR = Color.GRAY;
    public static final Paint MERCHANTCOLOR = Color.PURPLE;

    public static final int INVENTORYKEYCODE = 73;
    public static final int QUITKEYCODE = 81;
    public static final int STARTGHOSTKEYCODE = 71;

    public static final int INVENTORYCOLCOUNT = 7;

    public static final String[] PREMADEMAPNAMES= {"Harak", "Ithrandil", "Telopomus", "Troft"};

    public static int TILESIZE = 60;
    private Game game;

    private GridPane room;
    private VBox inventories;

    private UserProfile profile;

    private MapProfile map;

    private HashMap<String, PlayerCharacter> userTracker;

    private fileWriter specificFileType;

    private Scene scene;

    private Stage stage;


    public MUDGUI() {
        room = new GridPane();
        room.setPrefSize(TILESIZE * 11, TILESIZE * 11);
        room.setAlignment(Pos.CENTER);
        room.setGridLinesVisible(true);

        inventories = new VBox();
        inventories.setSpacing(TILESIZE / 10);
        inventories.setBackground(new Background(new BackgroundFill(Color.SADDLEBROWN, CornerRadii.EMPTY, Insets.EMPTY)));
        inventories.setAlignment(Pos.CENTER);
        inventories.setBorder(Border.stroke(Color.BLACK));

        scene = new Scene(room);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Paint tileToColor(Tile tile){
        if (tile instanceof CharacterTile){
            if (((CharacterTile)tile).isPC()){
                return CHARACTERCOLOR;
            } else {
                return ENEMYCOLOR;
            }
        } else if (tile instanceof ExitTile){
            return DOORCOLOR;
        } else if(tile instanceof MerchantTile){
            return MERCHANTCOLOR;
        } else if (tile instanceof ChestTile){
            return CHESTCOLOR;
        } else if (tile instanceof ObstacleTile){
            return OBSTACLECOLOR;
        }

        return EMPTYCOLOR;
    }

    public static Tooltip makeCharacterTooltip(Character character){
        String tipText = String.format(
            "name: %s\ndescription: %s\nStats: \n\thealth: %d\n\tdefense: %d\n\tattack: %d",
            character.getName(),
            character.getDescription(),
            character.getHealth(),
            character.getDefense(),
            character.getAttack()
        );
        return new Tooltip(tipText);
    }

    private Button makeTile(int row, int col, Tile tile){
        Button button = new Button();
        button.setBackground(new Background(new BackgroundFill(Color.SADDLEBROWN, CornerRadii.EMPTY, new Insets(1))));

        Rectangle icon = new Rectangle(TILESIZE, TILESIZE, tileToColor(tile));
        game.registerObserver(row, col, new TileChanger(button));

        button.setGraphic(icon);
        if (tile instanceof CharacterTile){
            Character character = ((CharacterTile)tile).getCharacter();
            ;
            button.setTooltip(makeCharacterTooltip(character));
        }

        return button;
    }

    public static Button makeInventoryButton(ItemAttribute item, Inventory thisInventory, Inventory othInventory, PlayerCharacter pc){
        Button button = new Button();
        Tooltip tip = new Tooltip(item.getName() + ": " + item.getDescription());
        Tooltip.install(button, tip);

        Rectangle icon = new Rectangle(TILESIZE, TILESIZE, Color.hsb(item.hashCode() % 360, 0.75, 0.75));
        button.setGraphic(icon);
        button.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY){
                thisInventory.transferItem(item, othInventory);
            } else {
                if (thisInventory instanceof PlayerInventory){
                    item.onUse(pc);
                }
            }
        });

        return button;
    }

    private Pane makeInventoryGrid(Inventory thisInventory, Inventory othInventory){
        HBox fullInv = new HBox();
        GridPane invPane = new GridPane();
        invPane.setAlignment(Pos.TOP_CENTER);
        invPane.setPrefSize(TILESIZE*INVENTORYCOLCOUNT, 2*TILESIZE);
        int col = 0;
        int row = 0;
        for (ItemAttribute item : thisInventory.getInventory()) {
            if (item != null){
                invPane.add(makeInventoryButton(item, thisInventory, othInventory, game.getPlayerCharacter()), col, row);
            }
            if (++col == INVENTORYCOLCOUNT){
                col = 0;
                row++;
            }
        }
        fullInv.setAlignment(Pos.CENTER);   
        fullInv.getChildren().add(invPane);

        Button goldButton = new Button((Integer.toString(thisInventory.getGold())));
        goldButton.setPrefSize(TILESIZE*2, TILESIZE*2);
        goldButton.setOnMouseClicked(event -> {thisInventory.transferGold(thisInventory.getGold(), othInventory);});
        fullInv.getChildren().add(goldButton);

        thisInventory.registerObserver(new InventoryObserver(invPane, goldButton, thisInventory, othInventory, game.getPlayerCharacter()));
        return fullInv;
    }

    private void renderInventory(Inventory othInventory){
        inventories.getChildren().clear();
        inventories.getChildren().add(makeInventoryGrid(othInventory, game.getPlayerInverntory()));
        inventories.getChildren().add(makeInventoryGrid(game.getPlayerInverntory(), othInventory));
    }

    public void renderRoom(){
        room.getChildren().clear();
        Room cur = game.getCurrentPlayerRoom();
        for (int i = 0; i < cur.getHeight(); i++) {
                for (int j = 0; j < cur.getWidth(); j++) {
                    room.add(makeTile(i, j, cur.getTile(i, j)), j, i);
            }
        }
        cur.registerObserver(new RoomObserver(this));
        System.out.println("---------Shrine Debug---------");
        System.out.println(cur.isShrine());
        System.out.println(cur.canPrayAtShrine());
    }

    public void swapScene(Parent newContent){
        stage.getScene().setRoot(newContent);
    }

    @Override
    public void start(Stage givenStage) throws Exception {


        stage = givenStage;
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setTitle("MUD");
        stage.show();

        loginUser();
    }

    public void chooseGamemode() {
        GridPane options = new GridPane(); options.setAlignment(Pos.CENTER);
        Button newGame = new Button(); options.add(newGame, 0, 0); newGame.setText("New Game");
        Button resumeGame = new Button(); options.add(resumeGame, 0, 1); resumeGame.setText("Resume Game");
        Button ghostMode = new Button(); options.add(ghostMode, 1, 0); ghostMode.setText("Ghost Mode");
        Button endless= new Button(); options.add(endless, 1, 1); endless.setText("endless");
        
        Label label = new Label(profile.getWins() + " Wins / " + profile.getLosses() + " Losses");
        options.add(label, 1, 2);
        for (Node button: options.getChildren()) {
            if(button instanceof Button){
                ((Button)button).setPrefSize(TILESIZE * 5, TILESIZE);
            }
        }
        
    


        /**
         * Ethan: for these you should make and setup the game in the designated lamda(), then run the start game function
         */
        newGame.setOnMouseClicked(event -> {
            // start a new game for the user
            game = new Game(false, profile); //start new non endless game
            PlayerCharacter pc = new PlayerCharacter(profile.getName(), profile.getDesc());
            game.setPlayerCharacter(pc);
            game.generateMap();
            startGame(game);
        });

        resumeGame.setOnMouseClicked(event -> {
            // resume the last solo game they saved
            map = specificFileType.loadMapData(profile.getName()+"Map");
            game = new Game(false, profile);
            game.setPlayerCharacter(map.validateAndGetChar(profile));
            game.setMap(map.getMap());
            userTracker = map.getCharDataHashMap();
            startGame(game);
        });

        ghostMode.setOnMouseClicked(event -> {
            selectMap();
        });

        endless.setOnMouseClicked(event -> {
            if(!specificFileType.fileExists("endless")){
                game = new Game(true, profile); //start new endless game
                PlayerCharacter pc = new PlayerCharacter(profile.getName(), profile.getDesc());
                game.setPlayerCharacter(pc);
                game.generateMap();
                startGame(game);
            } else{
                map = specificFileType.loadMapData("endless");
                game = new Game(true, profile);
                Map tempMapStorage = map.getMap();

                PlayerCharacter charCheck = map.validateAndGetChar(profile);
                if(charCheck == null){
                    charCheck = new PlayerCharacter(profile.getName(), profile.getDesc());
                    game.setPlayerCharacter(charCheck);
                    tempMapStorage.joinGame(charCheck);
                }else{
                    game.setPlayerCharacter(charCheck);
                    tempMapStorage.switchToDiffCharacter(charCheck);
                }
                game.setMap(tempMapStorage);
                userTracker = map.getCharDataHashMap();
                startGame(game);
            }
        });

        swapScene(options);
    }

    private void selectMap(){
        specificFileType = new jsonWriter();
        GridPane mapOptions = new GridPane();
        int col = 0;
        int row = 0;
        for (String name : PREMADEMAPNAMES) {
            if (specificFileType.fileExists("premades/" + name)){
                Button button = new Button();
                button.setText(name);
                button.setPrefSize(TILESIZE * 3, TILESIZE);
                button.setOnMouseClicked(event -> {
                    PlayerCharacter ghost = new PlayerCharacter("ghost", "invisible");
                    ghost.setHealth(100000);
                    MapProfile mapProfile = specificFileType.loadMapData("premades/" + name);
                    game = new Game(false, profile);
                    game.setPlayerCharacter(ghost);
                    game.setMap(mapProfile.getMap());
                    game.setIsGhsotMode(true);
                    startGame(game);
                });
                mapOptions.add(button, col, row);
                if (col++ >= 2){col = 0; row++;}
            }
        }
        swapScene(mapOptions);
    }

    private void validateUser(String username){
        VBox fields = new VBox();
        TextField passwordBox = new TextField();
        passwordBox.setPromptText("password");

        fields.getChildren().addAll(passwordBox);
        fields.setAlignment(Pos.CENTER);
        swapScene(fields);
        scene.setOnKeyPressed(KeyEvent -> {
            if (KeyEvent.getCode().getCode() == 10){
                // if the password is right then set profile and then choose gamemode
                // use passwordBox.getText(); to get the password
                UserProfile tempProfile = specificFileType.loadUserProfile(username);
                if(passwordBox.getText().equals(tempProfile.getPass())){
                    this.profile = tempProfile;
                    chooseGamemode();
                }

            }
        });
    }

    private void createUser(String username){
        var wrapper = new Object(){String extension = "json";};
        VBox fields = new VBox();
        TextField passwordBox = new TextField();
        passwordBox.setPromptText("password");
        TextField descriptionBox = new TextField();
        descriptionBox.setPromptText("description");

        HBox extensionButtons= new HBox(); extensionButtons.setAlignment(Pos.CENTER);

        Button XMLButton = new Button("XML");
        Button CSVButton = new Button("CSV");
        Button JSONButton = new Button("JSON");

        XMLButton.setOnMouseClicked(event -> {wrapper.extension = ".xml";}); XMLButton.setPrefSize(TILESIZE *2, TILESIZE);
        CSVButton.setOnMouseClicked(event -> {wrapper.extension = ".csv";}); CSVButton.setPrefSize(TILESIZE *2, TILESIZE);
        JSONButton.setOnMouseClicked(event -> {wrapper.extension = ".json";}); JSONButton.setPrefSize(TILESIZE *2, TILESIZE);

        extensionButtons.getChildren().addAll(XMLButton, CSVButton, JSONButton);


        fields.getChildren().addAll(passwordBox, descriptionBox, extensionButtons);
        fields.setAlignment(Pos.CENTER);
        swapScene(fields);

        scene.setOnKeyPressed(KeyEvent -> {
            if (KeyEvent.getCode().getCode() == 10){
                // when enter is pressed create the user with the desc and pass, access the file type with wrapper.extension
                // the set profile and choose gamemode
                switch (wrapper.extension) {
                    case ".xml" -> specificFileType = new xmlWriter();
                    case ".csv" -> specificFileType = new csvWriter();
                    case ".json" -> specificFileType = new jsonWriter();
                }
                profile = new UserProfile(username, passwordBox.getText(), descriptionBox.getText(),0, 0);
                specificFileType.saveUserProfile(profile);
                chooseGamemode();
            }
        });
    }

    @Override
    public void loginUser() {
        VBox fields = new VBox();
        TextField usernameBox = new TextField();
        usernameBox.setPromptText("username");

        fields.getChildren().addAll(usernameBox);
        fields.setAlignment(Pos.CENTER);
        swapScene(fields);
        scene.setOnKeyPressed(KeyEvent -> {
            if (KeyEvent.getCode().getCode() == 10){
                // this should check if a user already exists: if they do validate 
                //if they dont then create user

                //really ugly function but does the trick. checks all file types for file, if doesnt exist creates user,
                //otherwise run with it, specificFileType is already set right, just validate.
                String username = usernameBox.getText();
                specificFileType = new jsonWriter();
                if(specificFileType.fileExists(username)){
                    validateUser(username);
                }else{
                    specificFileType = new csvWriter();
                    if(specificFileType.fileExists(username)){
                        validateUser(username);
                    }else{
                        specificFileType = new xmlWriter();
                        if(specificFileType.fileExists(username)){
                            validateUser(username);
                        }else{
                            createUser(username);
                        }
                    }
                }

            }
        });    
    }

    @Override
    public void openInventory(Inventory i1, Inventory i2) {
        renderInventory(i2);
        swapScene(inventories);
        scene.setOnKeyPressed(KeyEvent -> {goToRoom();});
    }

    public void goToRoom(){
        swapScene(room);
        scene.setOnKeyPressed(KeyEvent -> {
            if (KeyEvent.getCode().getCode() == INVENTORYKEYCODE && ! game.isGhostMode()){
                this.openInventory(game.getPlayerInverntory(), new Drop());
            } else if (KeyEvent.getCode().getCode() == QUITKEYCODE && ! game.isGhostMode()) {
                game.endGame();
                if (userTracker == null){
                    userTracker = new HashMap<String, PlayerCharacter>();
                }
                userTracker.put(profile.getName(), game.getPlayerCharacter());
                map = new MapProfile(userTracker, game.getMap());
                specificFileType.saveMapData(map);
                chooseGamemode();
                // Ethan: this should be where you save the game
            } else if (KeyEvent.getCode().getCode() == STARTGHOSTKEYCODE && game.isGhostMode()){
                PlayerCharacter pc = new PlayerCharacter(profile.getName(), profile.getDesc());
                game.setPlayerCharacter(pc);
                game.getMap().setCurrentPlayerRoomId(0);
                game.getMap().setPlayerCharacter(pc);
                game.setIsGhsotMode(false);
            } else if(KeyEvent.getCode().getCode() == QUITKEYCODE && game.isGhostMode()) {
                game.setIsGhsotMode(false);
                chooseGamemode();
            }
            
            else {
                game.move(Direction.intToDirection(KeyEvent.getCode().getCode()));
            }
        });
    }

    @Override
    public void startGame(Game game) {
        game.registerObserver(new RoomChanger(this));
        renderRoom();
        goToRoom();
    }
    
}
