package model.map;

import model.PlayerCharacter;
import model.Room;
import model.Shrine.MapHistory;
import model.Shrine.MapRevision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import controller.Input.RoomChanger;

public class Map {
    @JsonProperty("currentPlayerRoomId")
    int currentPlayerRoomId;

    // Key = Room Id
    @JsonProperty("rooms")
    HashMap<Integer, Room> rooms;

    // Key = Room Id, Value = Array of Exit Room Ids, Position is dependent on where
    // the exit is. Use <Direction>.getValue() to get an exit at that location
    @JsonProperty("connections")
    HashMap<Integer, int[]> connections;

    @JsonProperty("playerCharacter")
    PlayerCharacter playerCharacter;

    @JsonIgnore()
    RoomChanger observer;

    @JsonProperty("endless")
    Boolean isEndless;

    MapGen mapBuilder;

    @JsonProperty("roommovecounter")
    int roomMoveCounter;

    public Map(PlayerCharacter playerCharacter, boolean isEndless) {
        this.isEndless = isEndless;
        this.playerCharacter = playerCharacter;
        if(isEndless){
            this.mapBuilder = new EndlessFactory(playerCharacter,this);
        }
        else{
            this.mapBuilder = new PremadeFactory(playerCharacter, this);
        }
        rooms = mapBuilder.getRooms();
        connections = mapBuilder.getConnections();
        roomMoveCounter = 0;
        currentPlayerRoomId = 0;
    }

    // No-argument constructor for JSON
    public Map() {
    }

    public void setPlayerCharacter(PlayerCharacter playerCharacter) {
        this.playerCharacter = playerCharacter;
        getCurrentPlayerRoom().setPlayer(playerCharacter);
    }
    /*
    This assumes that you pass in a fresh playercharacter. Creates a new start room for them.
    Endless exclusive.
 */
    public void joinGame(PlayerCharacter pc){
        if(isEndless){
            playerCharacter = pc;
            if(mapBuilder == null){
                mapBuilder = new EndlessFactory(playerCharacter,this);
            }
            int newLocationID = mapBuilder.genNewStartRoom(rooms, connections);
            rooms = mapBuilder.getRooms();// update these two hashmaps with the new data
            connections = mapBuilder.getConnections();

            currentPlayerRoomId = newLocationID;
            getCurrentPlayerRoom().setPlayer(playerCharacter);

        }
    }

    /*
    This assumes that the player character has already been through the save load system, as that assigns it the saved locations.
    Endless exclusive.
     */
    public void switchToDiffCharacter(PlayerCharacter pc){
        if(isEndless){
            playerCharacter = pc;
            currentPlayerRoomId = pc.getRoomId();
            getCurrentPlayerRoom().setPlayer(playerCharacter);
        }

    }
    public boolean isEndless(){return this.isEndless;}
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Map))
            return false;

        Map other = (Map) obj;
        return this.currentPlayerRoomId == other.currentPlayerRoomId
                && this.rooms.equals(other.rooms)
                && this.connections.equals(other.connections)
                && this.playerCharacter.equals(other.playerCharacter)
                && this.isEndless == other.isEndless
                && this.roomMoveCounter == other.roomMoveCounter;
    }

    @JsonIgnore
    public Room getCurrentPlayerRoom() {
        return rooms.get(currentPlayerRoomId);
    }

    @JsonIgnore
    public int getCurrentRoomId(){
        return currentPlayerRoomId;
    }
    public void setCurrentPlayerRoomId(int currentPlayerRoomId) {
        this.currentPlayerRoomId = currentPlayerRoomId;
        updateRoomUI();
    }

    @JsonIgnore
    public PlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }

    /**
     * Gets the next room based on the exit the user uses. Direction.getValue
     * returns a
     * value from 0 to 3 that represents the direction and is used as the index
     * 
     * Example: this.connections.get(roomId)[Direction.UP] would return the id to
     * the room
     * that is linked to the top exit of the current room
     * 
     * @param currentRoomID The id of the room that the user is currently in
     * @param goingTowards  The direction the user is going (basically what exit is
     *                      the user using: UP, DOWN, LEFT, RIGHT)
     * @return The next room id if it exists or -1
     */
    @JsonIgnore
    public int getNextRoomID(int currentRoomID, int goingTowards) {
        if (!this.connections.containsKey(currentRoomID))
            return -1;
        return this.connections.get(currentRoomID)[goingTowards];
    }

    public Room getRoomByID(int roomID) {
        return rooms.get(roomID);
    }

    public void moveRoom(int goingTowards) {
        setCurrentPlayerRoomId(connections.get(currentPlayerRoomId)[goingTowards]);
        if (isEndless) {
            if(mapBuilder == null){
                mapBuilder = new EndlessFactory(playerCharacter,this);
            }
            mapBuilder.weNeedMoreRooms(rooms, currentPlayerRoomId, connections);// generate the room connections on demand for
            rooms = mapBuilder.getRooms();// update these two hashmaps with the new data
            connections = mapBuilder.getConnections();
        }
        rooms.get(currentPlayerRoomId).setPlayer(playerCharacter);
        if (isEndless)
            rooms.get(currentPlayerRoomId).testRespawn(roomMoveCounter);
        roomMoveCounter++;
        updateRoomUI();
    }

    // Used for json deserialization
    public void finishMapLoad(PlayerCharacter playerCharacter) {
        this.playerCharacter = playerCharacter;
        rooms.values().forEach(room -> {
            room.setMap(this);
            room.finishRoomLoad();
        });
    }

    public String[] doAttacks() {
        List<String> result = new ArrayList<>();
        getCurrentPlayerRoom().getAdjacentEnemies().forEach(enemy -> {
            int attackDamage = playerCharacter.getHealth();
            enemy.attack(playerCharacter);
            attackDamage -= playerCharacter.getHealth();
            result.add(String.format(
                    "You were attacked by an enemy in an adjacent tile for %d health! You have %d health remaining.\n",
                    attackDamage, playerCharacter.getHealth()));
        });
        return result.toArray(new String[result.size()]);
    }

    private void updateRoomUI() {
        if (observer != null) {
            observer.onChange();
        }
    }

    public void registerObserver(RoomChanger observer) {
        this.observer = observer;
    }

    public MapRevision createRevision() {
        return new MapRevision(rooms, playerCharacter, currentPlayerRoomId);
    }

    // TODO: FIX ISSUE WITH ROOM TRAVERSAL NOT WORKING AFTER THIS
    public void restoreRevision(MapRevision revision) {
        this.rooms = revision.getRooms();
        this.playerCharacter = revision.getPlayerCharacter();
        this.currentPlayerRoomId = revision.getCurrentRoomId();

        finishMapLoad(playerCharacter);
        updateRoomUI();
    }
}

// few functions i need with room:
// create start room(only thing special about this is the player will spawn in
// the middle or something?)
// Room already has a boolean for this

// create end room-- one of the given exits will be the "end". need to clear the
// room fully to open it.
// room needs a createfinalexit method or the like so i can define which wall
// this will be. obviously only works if said room is the final end room

// create room at given height, width, and difficulty
// base functionality

// assign doorways to walls
// if i pass in the walls that i want doorways on to an already existing room,
// it should be able to add them and make sure there is a clear obstacle free
// path there

// assign doorway connections to another room
// if two rooms have doorways, i should be able to pass the other room object to
// each room and link them that way

// getter for the room through a speific doorway
// if the player exits a doorway, i should be able to call a method for that
// doorway to get the room linked on the other side of that door