package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import controller.Input.TileChanger;
import controller.Input.RoomObserver;
import model.DayNight.DiurnalDayState;
import model.Drops.DropHelper;
import model.Inventory.Drop;
import model.Inventory.Merchant;
import model.Items.Food;
import model.Shrine.MapHistory;
import model.Tiles.*;
import model.map.Map;

public class Room {
    @JsonProperty("width")
    private int width;
    @JsonProperty("height")
    private int height;
    @JsonProperty("start")
    private boolean isStart = false;
    @JsonProperty("goal")
    private boolean isGoal = false;
    @JsonProperty("difficulty")
    private int difficulty;
    @JsonProperty("exits")
    private Boolean[] exits;
    @JsonProperty("description")
    private String description;
    @JsonProperty("tiles")
    private Tile[][] tiles;
    @JsonProperty("player")
    private CharacterTile player;
    @JsonProperty("enemies")
    private List<CharacterTile> enemies;
    @JsonIgnore
    private Map mapper;
    @JsonProperty("playerAtEnd")
    private boolean playerAtEnd = false;
    @JsonIgnore
    private TileChanger[][] observers;
    @JsonIgnore
    private RoomObserver roomObserver;
    @JsonProperty("isShrine")
    private boolean isShrine;

    @JsonProperty("clearedOnTurn")
    private int clearedOnTurn;

    @JsonProperty("killedEnemeies")
    private List<CharacterTile> killedEnemies;

    @JsonIgnore
    private List<NonPlayerCharacter> adjacentEnemies;

    @JsonCreator
    private Room() {

    }

    @JsonIgnore
    public Room(int width, int height, int difficulty, Map mapper, boolean isShrine) {
        this.height = height;
        this.width = width;
        this.difficulty = difficulty;
        this.mapper = mapper;
        this.description = "A room with";
        this.enemies = new ArrayList<>();
        this.exits = new Boolean[4];
        this.tiles = new Tile[width][height];
        this.isShrine = isShrine;
        this.clearedOnTurn = -1;
        this.killedEnemies = new ArrayList<CharacterTile>();
        int[] diff = randomTile(width, height, difficulty);
        int i = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (i < diff.length) {
                    if ((x + 1) * y == diff[i]) {
                        i++;
                        Tile tile = addTile(x, y);
                        this.description += " A " + tile.stringDes() + ", ";
                        this.tiles[x][y] = tile;
                    } else {
                        this.tiles[x][y] = new EmptyTile(x, y);
                    }
                } else {
                    this.tiles[x][y] = new EmptyTile(x, y);
                }
            }
        }
        this.description += " and exits on the";
        this.observers = new TileChanger[width][height];
    }

    @JsonIgnore
    public Room(int width, int height, int difficulty, boolean isStart, boolean isGoal, PlayerCharacter character,
            Map mapper, boolean isShrine) {
        this(width, height, difficulty, mapper, isShrine);
        this.isGoal = isGoal;
        this.isStart = isStart;
        if (this.isStart == true) {
            setPlayer(character);
        }
        this.observers = new TileChanger[width][height];
    }

    private int[] randomTile(int width, int height, int difficulty) {
        Random rand = new Random();
        int[] diff = new int[difficulty+2];
        for (int i = 0; i < difficulty; i++) {
            diff[i] = rand.nextInt(height * width);
        }
        return diff;
    }

    @JsonIgnore
    public List<NonPlayerCharacter> getEnemies() {
        List<NonPlayerCharacter> ret = new ArrayList<>();
        for (CharacterTile cha : enemies) {
            ret.add((NonPlayerCharacter) cha.getCharacter());
        }
        return ret;
    }

    @JsonIgnore
    public List<NonPlayerCharacter> getAdjacentEnemies() {
        List<NonPlayerCharacter> adjacentEnemies = new ArrayList<>();
        for (Tile tile : getAdjacentTiles(getCharacterTile())) {
            if (!(tile instanceof CharacterTile))
                continue;
            adjacentEnemies.add((NonPlayerCharacter) (((CharacterTile) tile).getCharacter()));
        }

        return adjacentEnemies;
    }

    @JsonIgnore
    public boolean isShrine() {
        return isShrine;
    }

    /**
     * Whether or not a player can pray to a shrine
     * 
     * @return True if and only if there are no enemies in the room and the room is
     *         a shrine
     */
    public boolean canPrayAtShrine() {
        return isShrine() && isCleared();
    }

    public void setShrineStatus(boolean isShrine) {
        this.isShrine = isShrine;
    }

    @JsonIgnore
    public boolean isCleared() {
        for (NonPlayerCharacter enemies : getEnemies()) {
            if (enemies.getHealth() > 0)
                return false;
        }
        return true;
    }

    public void testRespawn(int roomCount) {
        if (isCleared() && (roomCount - clearedOnTurn) <= 5) {
            clearedOnTurn = -1;
            Random rand = new Random();
            int i = 0;
            for (CharacterTile deadEnemyTile : killedEnemies) {
                deadEnemyTile.getCharacter().setHealth(rand.nextInt(150-50) + 50);
                tiles[deadEnemyTile.getX()][deadEnemyTile.getY()] = deadEnemyTile;
            }
        }
    }

    private Tile addTile(int x, int y) {
        Random rand = new Random();
        int r = rand.nextInt(11);
        if (r < 3) {
            CharacterTile enemy = new CharacterTile(
                    new NonPlayerCharacter("ogre", "big green guy", false, new DiurnalDayState()), x, y);
            enemies.add(enemy);
            return enemy;
        } else if (r < 5) {
            return new ObstacleTile("chair", x, y);
        } else if (r < 7) {
            Drop chester = new Drop();
            for (int i = 0; i <= rand.nextInt(10); i++) {
                chester.recieveItem(DropHelper.getItem(difficulty));
            }
            chester.recieveGold(rand.nextInt(35 * difficulty +1));
            return new ChestTile(chester, x, y);
        } else if (r < 9) {
            Merchant mech = new Merchant();
            for (int i = 0; i <= rand.nextInt(10); i++) {
                mech.recieveItem(DropHelper.getItem(difficulty));
            }
            mech.recieveGold(rand.nextInt(35 * difficulty +1));
            return new MerchantTile(mech, x, y);
        } else {
            return new TrapTile(5, x, y);
        }

    }

    public void getRoomMap() {
        System.out.println("\n");
        for (int y = 0; y < height; y++) {
            String linebuff = "";
            for (int x = 0; x < width; x++) {
                linebuff = linebuff + tiles[x][y].stringRep() + " ";
            }
            System.out.println(linebuff);
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getString() {
        return description;
    }

    public void setString(String description) {
        this.description = description;
    }

    @JsonIgnore
    public void setPlayer(Character player) {
        this.player = new CharacterTile(player, this.width / 2, this.height / 2);
        this.tiles[width / 2][height / 2] = this.player;
    }

    @JsonIgnore
    public CharacterTile getCharacterTile() {
        return player;
    }

    @JsonIgnore
    public void finishRoomLoad() {
        for (Tile[] row : this.tiles) {
            for (Tile tile : row) {
                if (!(tile instanceof TrapTile))
                    continue;
                ((TrapTile) tile).fixObservers();
            }
        }
        this.observers = new TileChanger[width][height];
    }

    @JsonIgnore
    public Tile[] getAdjacentTiles(Tile center) {
        // starts with top left corner and goes clockwise
        int x = center.getX();
        int y = center.getY();
        int count = 0;
        Tile[] adjacent = new Tile[8];
        for (int i = -1; i < 2; i++) {
            try {
                adjacent[count] = tiles[i + x][y - 1];
            } catch (Exception e) {
                adjacent[count] = null;
            }
            count++;
        }
        try {
            adjacent[count] = tiles[x + 1][y];
        } catch (Exception e) {
            adjacent[count] = null;
        }
        count++;
        for (int i = 1; i >= -1; i--) {
            try {
                adjacent[count] = tiles[i + x][y + 1];
            } catch (Exception e) {
                adjacent[count] = null;
            }
            count++;
        }
        try {
            adjacent[count] = tiles[x - 1][y];
        } catch (Exception e) {
            adjacent[count] = null;
        }
        count++;

        return adjacent;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public boolean getStart() {
        return this.isStart;
    }

    public boolean getGoal() {
        return this.isGoal;
    }

    public void addExit(int wall) { // lets do north south east west for exits, 0-1-2-3 respectively
        exits[wall] = true;
        if (wall == 1) {
            tiles[width / 2][0] = new ExitTile(wall, width / 2, 0);
            this.description += " north,";
        } else if (wall == 3) {
            tiles[width / 2][height - 1] = new ExitTile(wall, width / 2, height - 1);
            this.description += " south,";
        } else if (wall == 0) {
            tiles[0][height / 2] = new ExitTile(wall, 0, height / 2);
            this.description += " west,";
        } else if (wall == 2) {
            tiles[width - 1][height / 2] = new ExitTile(wall, width - 1, height / 2);
            this.description += " east,";
        }
    }

    public boolean getExit(int wall) {
        return exits[wall];
    }

    public String getDescription() {
        return this.description;
    }

    public void setMap(Map mapper) {
        this.mapper = mapper;
    }

    public boolean wallHasExit(int wall) {
        if (exits[wall] == null)
            return false;
        else
            return true;
    }

    public boolean isPlayerAtEnd() {
        return playerAtEnd;
    }

    private void mover(Tile move) {
        int newX = move.getX();
        int newY = move.getY();
        int oldX = player.getX();
        int oldY = player.getY();
        player.setX(newX);
        player.setY(newY);
        tiles[oldX][oldY] = new EmptyTile(oldX, oldY);
        notify(oldY, oldX);
        tiles[newX][newY] = player;
        notify(newY, newX);
        checkAdjacentTrap();
    }

    private void checkAdjacentTrap() {
        Tile adj[] = getAdjacentTiles(player);
        for (Tile tile : adj) {
            if (tile instanceof TrapTile) {
                TrapTile trap = (TrapTile) tile;
                if (trap.getDetected() == false && trap.getAttempt() == false) {
                    trap.notifyDetect();
                }
            }
        }
    }

    public boolean moveCharacter(Direction dir, int turnCounter) {
        if (isCleared()) {
            clearedOnTurn = turnCounter;
        }
        Tile[] adj = getAdjacentTiles(player);
        Tile tileComp;
        if (dir == Direction.UP) {
            tileComp = adj[1];
        } else if (dir == Direction.DOWN) {
            tileComp = adj[5];
        } else if (dir == Direction.LEFT) {
            tileComp = adj[7];
        } else if (dir == Direction.RIGHT) {
            tileComp = adj[3];
        } else {
            return false;
        }

        if (tileComp == null) {
            return false;
        }

        // Break line now we go into actually doing stuff based on what tile
        if (tileComp instanceof EmptyTile) {
            mover(tileComp);
            return true;
        } else if (tileComp instanceof ObstacleTile) {
            return false;
        } else if (tileComp instanceof ChestTile) {
            ChestTile chest = (ChestTile) tileComp;
            if (roomObserver != null) {
                roomObserver.openInventory(player.getCharacter().getInventory(), chest.getInventory());
            } else {
                chest.giveChest(player.getCharacter());
            }
            if (chest.getInventory().getItemCapacityRatio()[0] == 0) {
                mover(tileComp);
            }
            return true;
        } else if (tileComp instanceof CharacterTile) {
            CharacterTile enemeyTile = (CharacterTile) tileComp;
            PlayerCharacter playerChar = (PlayerCharacter) player.getCharacter();
            NonPlayerCharacter enemey = (NonPlayerCharacter) enemeyTile.getCharacter();
            playerChar.attack(enemey);
            if (enemey.getHealth() <= 0) {
                killedEnemies.add(enemeyTile);
                tiles[tileComp.getX()][tileComp.getY()] = new EmptyTile(tileComp.getX(), tileComp.getY());
            }
            System.out.println("attack");
            return true;
        } else if (tileComp instanceof ExitTile) {
            tiles[player.getX()][player.getY()] = new EmptyTile(player.getX(), player.getY());
            ExitTile exit = (ExitTile) tileComp;
            mapper.moveRoom(exit.getExitTowards());
            System.out.println(mapper.getCurrentPlayerRoom().getDescription());
            if (mapper.getCurrentPlayerRoom().getGoal() == true) {
                this.playerAtEnd = true;
            }
            return true;
        } else if (tileComp instanceof TrapTile) {
            TrapTile trap = (TrapTile) tileComp;
            trap.setPlayer(player.getCharacter());
            if (trap.getDetected() == false) {
                trap.notifyTrigger();
                mover(trap);
            } else if (trap.getDisarmed() == false && trap.getDetected() == true) {
                trap.notifyDisarm();
                if (trap.getDisarmed() == false) {
                    tiles[trap.getX()][trap.getY()] = new EmptyTile(trap.getX(), trap.getY());
                }
            } else if (trap.getDisarmed() == true && trap.getDetected() == true) {
                mover(trap);
            }
            return true;
        }

        return false;
    }

    public Tile getTile(int row, int col) {
        return tiles[col][row];
    }

    public void registerObserver(int row, int col, TileChanger observer) {
        if (observers == null){this.observers = new TileChanger[width][height];}
        observers[col][row] = observer;
    }

    public void registerObserver(RoomObserver o) {
        this.roomObserver = o;
    }

    private void notify(int row, int col) {
        if (observers[col][row] != null) {
            observers[col][row].onChange(getTile(row, col));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Room))
            return false;

        Room other = (Room) obj;
        return this.width == other.width
                && this.height == other.height
                && this.isStart == other.isStart
                && this.isGoal == other.isGoal
                && this.difficulty == other.difficulty
                && Arrays.equals(this.exits, other.exits)
                && this.description.equals(other.description)
                && checkTileArrayEquality(other.tiles)
                && ((this.player == null && other.player == null) || this.player.equals(other.player))
                && ((this.enemies == null && other.enemies == null) || this.enemies.equals(other.enemies))
                && ((this.mapper == null && other.mapper == null) || this.mapper.equals(other.mapper))
                && this.playerAtEnd == other.playerAtEnd
                && this.isShrine == other.isShrine
                && this.clearedOnTurn == other.clearedOnTurn
                && ((this.adjacentEnemies == null && other.adjacentEnemies == null) || this.adjacentEnemies.equals(other.adjacentEnemies));
    }

    /**
     * Does a deep equals check on a tiles 2d array against the tiles array in this class
     * @param otherTiles The tiles array being checked
     * @return True if all elements in the array are equal
     */
    public boolean checkTileArrayEquality(Tile[][] otherTiles) {
        if (this.tiles.length != otherTiles.length) {
            return false;
        }


        for (int index = 0; index < tiles.length; index++) {
            Tile[] tiles = this.tiles[index];
            Tile[] other = otherTiles[index];
            if (!Arrays.equals(tiles, other))
                return false;
        }
        return true;
    }


    public void killPlayer(){
        tiles[getCharacterTile().getX()][getCharacterTile().getY()] = new ChestTile(player.getCharacter().getInventory(), getCharacterTile().getX(), getCharacterTile().getY());
    }

    public void prepForSaving(){
        int charXVal = getCharacterTile().getX();
        int charYVal = getCharacterTile().getY();
        tiles[charXVal][charYVal] = new EmptyTile(charXVal, charYVal);
    }


    /**
     * Deep clones the room (meaning every obj is newly instantiated)
     * 
     * @return A copy of this room
     * @throws JsonMappingException    If unable to map to Room object
     * @throws JsonProcessingException If unable to process json
     */
    public Room deepClone() throws JsonMappingException, JsonProcessingException {
        ObjectMapper objMapper = new ObjectMapper();
        Room clone = objMapper.readValue(objMapper.writeValueAsString(this), Room.class);
        clone.mapper = mapper;
        clone.roomObserver = this.roomObserver;
        clone.observers = this.observers;
        clone.finishRoomLoad();
        return clone;
    }
}