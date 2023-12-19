package model;

import controller.Input.InventoryObserver;
import controller.Input.RoomChanger;
import controller.Input.TileChanger;
import model.Inventory.PlayerInventory;
import model.Save.UserProfile;
import model.Shrine.MapHistory;
import model.map.Map;

public class Game {

    private boolean shouldContinue;
    private boolean isDay;
    private int turnCounter;
    private PlayerCharacter playerCharacter;
    private boolean isWon;
    private Map map;
    private boolean isEndless; // flip to true to enable endless mode
    private boolean isGhostMode;
    private UserProfile profile;

    public Game(boolean isEndless, UserProfile profile) {
        shouldContinue = true;
        turnCounter = 0;
        isWon = false;
        this.isEndless = isEndless;
        this.isGhostMode = false;
        this.profile = profile;
    }

    public void generateMap() {
        setMap(new Map(playerCharacter, isEndless));
    }

    public void setPlayerCharacter(PlayerCharacter playerCharacter) {
        this.playerCharacter = playerCharacter;
    }

    public void setMap(Map map) {
        this.map = map;
        this.map.finishMapLoad(this.playerCharacter);
        MapHistory.INSTANCE.setMap(map);
    }

    public Map getMap() {
        return map;
    }

    public Room getCurrentPlayerRoom() {
        return map.getCurrentPlayerRoom();
    }

    public PlayerInventory getPlayerInverntory() {
        return playerCharacter.getInventory();
    }

    public void makeNewCharacter(String name, String des) {
        setPlayerCharacter(new PlayerCharacter(name, des));
    }

    public boolean shouldContinue() {
        return shouldContinue;
    }

    public boolean handleRestoreRevision() {
        if (MapHistory.INSTANCE.getRevisions().size() == 0)
            return false;

        MapHistory.INSTANCE.restoreRevision(0);
        this.playerCharacter = map.getPlayerCharacter();
        return true;
    }

    public void enableEndlessMode(boolean endless) {
        isEndless = endless;
    }

    private String incTurn() {
        String retString = "";
        if (shouldContinue()) {
            if (this.turnCounter % 5 == 0) {
                isDay = !isDay;
                map.getCurrentPlayerRoom().getEnemies().forEach(enemy -> {
                    enemy.changeState(isDay);
                });
                retString = String.format("It has turned to %s!\n", (this.isDay ? "day" : "night"));
            }
            turnCounter++;
        }
        return retString;
    }

    public boolean isDay() {
        return isDay;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public PlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }

    public boolean isGhostMode(){
        return isGhostMode;
    }

    public void setIsGhsotMode(boolean value){
        this.isGhostMode = value;
    }

    public void endGame() {
        shouldContinue = false;
    }

    public void finishMapLoad() {
        map.finishMapLoad(playerCharacter);
    }

    public String[] move(Direction dir) {
        String[] result = new String[1];
        if (this.isGhostMode()){
            if (map.getCurrentPlayerRoom().getExit(dir.getVal())){
                map.moveRoom(dir.getVal());
            }
            return result;
        }
        if (shouldContinue()) {
            boolean didMove = map.getCurrentPlayerRoom().moveCharacter(dir, turnCounter);
            if (didMove) {
                incTurn();

                if(map.getCurrentPlayerRoom().canPrayAtShrine()) {
                    map.getCurrentPlayerRoom().setShrineStatus(false);
                    MapHistory.INSTANCE.storeRevision();
                    System.out.println("Prayed to shrine!");
                    // TODO: Maybe add output string here?
                }
            }
            result = map.doAttacks();
            if (map.getCurrentPlayerRoom().isPlayerAtEnd()) {
                shouldContinue = false;
                isWon = true;
                this.profile.setWins(this.profile.getWins() + 1);
                System.out.println("you won!");
                
            }
            if (playerCharacter.getHealth() <= 0) {
                if(!handleRestoreRevision()){
                    System.out.println("YOU LOST!");
                    getCurrentPlayerRoom().killPlayer();
                    shouldContinue = false;
                    isWon = false;
                    this.profile.setLosses(this.profile.getLosses() + 1);
                } 
            }
        }
        return result;
    }

    public boolean isWon() {
        return isWon;
    }

    public void registerObserver(int row, int col, TileChanger observer) {
        getMap().getCurrentPlayerRoom().registerObserver(row, col, observer);
    }

    public void registerObserver(RoomChanger roomObserver) {
        getMap().registerObserver(roomObserver);
    }

    public void registerObserver(InventoryObserver inventoryObserver) {

    }

}
