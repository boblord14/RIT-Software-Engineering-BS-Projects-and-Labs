package model.map;

import model.PlayerCharacter;
import model.Room;


import java.util.HashMap;
import java.lang.Math;
import java.util.Random;

public abstract class MapGen {

    HashMap<Integer, Room> rooms;
    Map mapp;
    PlayerCharacter playerCharacter;
    protected HashMap<Integer, int[]> connections;
    private int nextId;

    public MapGen(PlayerCharacter playerCharacter2, Map mapp) {
        this.playerCharacter = playerCharacter2;
        this.mapp = mapp;
        initiateMapgen();
        this.nextId = 0;
    }

    public HashMap<Integer, Room> getRooms() {
        return rooms;
    }

    public HashMap<Integer, int[]> getConnections() {
        return connections;
    }

    abstract void initiateMapgen();

    protected int createStartRoom() {
        // Start room shouldn't be a shrine
        Room startRoom = new Room(5, 5, 0, true, false, playerCharacter, mapp, false);
        return addRoom(startRoom);
    }

    /**
     * generates the odds of branching paths, and the paths themselves for the map.
     * For a normal map generation, gets fed the start room and recurses for the rest of the map automatically.
     * <p>
     * For endless map generation, creates guaranteed rooms and connections for the passed in room, only for the initial
     * connections though, not recursively like normal maps.
     *
     * @param lastRoomId room to generate branches for
     */
    abstract void createBranches(int lastRoomId);

    abstract int generateRoomWSizeCalcs(int oldWall, int oldDiff);


    /**
     * this function will connect a room to rooms that are next to it that it isn't
     * already connected to
     * goes through each direction and takes a left x3 and right x3 to see if there
     * is a room there
     *
     * @param targetRoomId the room to be connected up
     * @param whereFrom    the direction from which it was branched
     */
    protected void fixConnections(int targetRoomId, int whereFrom) {
        for (int cd = whereFrom; cd < whereFrom + 4; cd++) {
            int currentDirection = cd % 4; // fixes the value from the loop so the direction is correct
            int currentRoomId = targetRoomId; // current room is the room that the loop will check if there is a branch
            // from
            if (hasConnection(targetRoomId, currentDirection)) { // if there is a connection in the direction changes
                // currentroom
                currentRoomId = connections.get(targetRoomId)[currentDirection];
            } else {
                continue; // other wise I dont care
            }
            for (int direction = -1; direction < 2; direction += 2) { // this loop descides withere to check lefts or
                // rights
                currentRoomId = connections.get(targetRoomId)[currentDirection];

                for (int mod = 1; mod < 4; mod++) { // actually takes the left x3 or right x3
                    // next 3 lines just make sure that the next direction is a left or right from
                    // the current one
                    int newDirection = currentDirection + direction * mod;
                    if (newDirection < 0) {
                        newDirection = 4 + newDirection;
                    }
                    newDirection = newDirection % 4;
                    if (mod == 3 && !hasConnection(currentRoomId, newDirection)) { // if the room exists and doesn't
                        // already have a connection to
                        // target, make one
                        addConnection(currentRoomId, newDirection, targetRoomId);
                    }
                    if (hasConnection(currentRoomId, newDirection)) {
                        currentRoomId = connections.get(currentRoomId)[newDirection];
                    } else {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Generates more connections on demand for a given room id. For use only with endless mode.
     * <p>
     * End result should create rooms on all 4 sides of the room with the passed in id(barring a preexisting room or a thin room that has 2 exits)
     * <p>
     * I don't believe I need to pass in a new copy of map, assuming its by reference rather than a copy of the object.
     *
     * @param roomData       current state of the rooms. Needs to be passed in to preserve state of cleared rooms and the like.
     * @param roomIdToBranch the room to put branches on
     */
    public void weNeedMoreRooms(HashMap<Integer, Room> roomData, int roomIdToBranch, HashMap<Integer, int[]> connectionData) {
        rooms = roomData;
        connections = connectionData;
        createBranches(roomIdToBranch);
    }

    public int genNewStartRoom(HashMap<Integer, Room> roomData, HashMap<Integer, int[]> connectionData){
        rooms=roomData;
        connections = connectionData;
        nextId = rooms.size();
        int lastRoomID=nextId-1;
        createBranches(lastRoomID);
        Room startRoom = new Room(5, 5, 0, true, false, playerCharacter, mapp, false);
        int startRoomID = rooms.size()-1;
        rooms.put(startRoomID, startRoom);
        createBranches(startRoomID);
        return startRoomID;
    }


    private int getNextId() {
        return this.nextId++;
    }

    protected int addRoom(Room newRoom) {
        int id = getNextId();
        rooms.put(id, newRoom);
        connections.put(id, new int[]{-1, -1, -1, -1});
        return id;
    }

    protected Room getRoom(int id) {
        return rooms.get(id);
    }

    protected void addConnection(int firstRoomId, int directionTowards, int secondRoomId) {
        getRoom(firstRoomId).addExit(directionTowards);
        getRoom(secondRoomId).addExit((directionTowards + 2) % 4);
        connections.get(firstRoomId)[directionTowards] = secondRoomId;
        connections.get(secondRoomId)[(directionTowards + 2) % 4] = firstRoomId;
    }

    public boolean hasConnection(int roomId, int direction) {
        return connections.get(roomId)[direction] != -1;
    }

}