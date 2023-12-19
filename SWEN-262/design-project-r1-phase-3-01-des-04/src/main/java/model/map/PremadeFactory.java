package model.map;

import java.util.HashMap;

import model.PlayerCharacter;
import model.Room;

public class PremadeFactory extends MapGen {

    public PremadeFactory(PlayerCharacter playerCharacter2, Map mapp) {
        super(playerCharacter2, mapp);
    }

    @Override
    void initiateMapgen() {
        rooms = new HashMap<Integer, Room>();
        connections = new HashMap<Integer, int[]>();
        // run the rest of the algos for mapgen here
        createBranches(createStartRoom());
        createEndRoom();
    }

    @Override
    void createBranches(int lastRoomId) {
        double oddsOfBranch = 1.0;//initalized to 100% chance
        //endless mode adaptation, make branch odds not guarenteed if not endless mode
        int difficulty = getRoom(lastRoomId).getDifficulty();
        oddsOfBranch = (1.0 - (0.17 * difficulty));
        

        Room lastRoom = rooms.get(lastRoomId);
        int everyRoomInt = 1;
        int startInt = 0;
        if (lastRoom.getHeight() == 3) {
            everyRoomInt = 2;
        }
        if (lastRoom.getWidth() == 3) {
            everyRoomInt = 2;
            startInt = 1;
        }

        for (int i = startInt; i < 4; i += everyRoomInt) {
            if (Math.random() < oddsOfBranch) { // if rng says to make a branch, 100% chance if endless anyhow
                if (!lastRoom.wallHasExit(i)) { // if an exit is not there already
                    int newDiff;
                    newDiff = lastRoom.getDifficulty();
                    int newRoomId = generateRoomWSizeCalcs(i, newDiff);
                    addConnection(lastRoomId, i, newRoomId);
                    fixConnections(newRoomId, (i + 2) % 4);
                    createBranches(newRoomId);
                }
            }
        }
    }

    @Override
    int generateRoomWSizeCalcs(int oldWall, int oldDiff) {
        int currentDiff = oldDiff + 1;//these two lines are a quick adaptation for endless mode

        Room newRoom;
        double roomMod = Math.random();
        if ((0.0 <= roomMod) && (roomMod < 0.20)) {// big room 11x11
            newRoom = new Room(11, 11, currentDiff, mapp, false);
        } else if ((0.20 <= roomMod) && (roomMod < 0.30)) {// thin short 3x5 or 5x3
            if ((oldWall == 0) || (oldWall == 2)) {// which direction does it go since asymmetric room
                newRoom = new Room(5, 3, currentDiff, mapp, false);
            } else {
                newRoom = new Room(3, 5, currentDiff, mapp, false);
            }
        } else if ((0.30 <= roomMod) && (roomMod < 0.40)) {// thin long 3x11 or 11x3
            if ((oldWall == 0) || (oldWall == 2)) {// which direction does it go since asymmetric room
                newRoom = new Room(5, 3, currentDiff, mapp, false);
            } else {
                newRoom = new Room(3, 5, currentDiff, mapp, false);
            }
        } else {// normal 5x5
            newRoom = new Room(5, 5, currentDiff, mapp, false);
        }
        return addRoom(newRoom);
    }
    
    protected void createEndRoom() {// finds the highest diff room, adds the end room off of that with a +1 to
        // difficulty(which can be higher than otherwise achievable)
        int highestDiffId = 0;
        for (int id : rooms.keySet()) {
            if (rooms.get(highestDiffId).getDifficulty() < rooms.get(id).getDifficulty()) {
                highestDiffId = id;
            }
        }
        Room highestDiff = rooms.get(highestDiffId);
        // End room shouldn't be a shrine
        Room endRoom = new Room(5, 5, highestDiff.getDifficulty() + 1, false, true, playerCharacter, mapp, false);
        int endRoomId = addRoom(endRoom);
        for (int i = 0; i < 4; i++) {
            if (!highestDiff.wallHasExit(i)) {
                addConnection(highestDiffId, i, endRoomId);
            }
        }
    }

}
