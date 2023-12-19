package model.map;

import java.util.HashMap;
import java.util.Random;

import model.PlayerCharacter;
import model.Room;

public class EndlessFactory extends MapGen{

    public EndlessFactory(PlayerCharacter playerCharacter2, Map mapp) {
        super(playerCharacter2, mapp);
    }

    
    @Override
    /**
     * generates the odds of branching paths, and the paths themselves for the map.
     * For a normal map generation, gets fed the start room and recurses for the rest of the map automatically.
     * <p>
     * For endless map generation, creates guaranteed rooms and connections for the passed in room, only for the initial
     * connections though, not recursively like normal maps.
     *
     * @param lastRoomId room to generate branches for
     */
    protected void createBranches(int lastRoomId) {
        double oddsOfBranch = 1.0;//initalized to 100% chance

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
                    newDiff = calculateEndlessDifficulty();
                    int newRoomId = generateRoomWSizeCalcs(i, newDiff);
                    addConnection(lastRoomId, i, newRoomId);
                    fixConnections(newRoomId, (i + 2) % 4);
                }
            }
        }
    }

    @Override
    protected void initiateMapgen(){
        rooms = new HashMap<Integer, Room>();
        connections = new HashMap<Integer, int[]>();
        // run the rest of the algos for mapgen here
        createBranches(createStartRoom());
    }

    @Override
    protected int generateRoomWSizeCalcs(int oldWall,int oldDiff){
        int currentDiff = oldDiff;//these two lines are a quick adaptation for endless mode

        Room newRoom;
        double roomMod = Math.random();
        if ((0.0 <= roomMod) && (roomMod < 0.20)) {// big room 11x11
            newRoom = new Room(11, 11, currentDiff, mapp, shouldRoomBeShrine());
        } else if ((0.20 <= roomMod) && (roomMod < 0.30)) {// thin short 3x5 or 5x3
            if ((oldWall == 0) || (oldWall == 2)) {// which direction does it go since asymmetric room
                newRoom = new Room(5, 3, currentDiff, mapp, shouldRoomBeShrine());
            } else {
                newRoom = new Room(3, 5, currentDiff, mapp, shouldRoomBeShrine());
            }
        } else if ((0.30 <= roomMod) && (roomMod < 0.40)) {// thin long 3x11 or 11x3
            if ((oldWall == 0) || (oldWall == 2)) {// which direction does it go since asymmetric room
                newRoom = new Room(5, 3, currentDiff, mapp, shouldRoomBeShrine());
            } else {
                newRoom = new Room(3, 5, currentDiff, mapp, shouldRoomBeShrine());
            }
        } else {// normal 5x5
            newRoom = new Room(5, 5, currentDiff, mapp, shouldRoomBeShrine());
        }
        return addRoom(newRoom);
    }
    

    /**
     * Generates a difficulty value for endless mode. Uses probability stuff, mean of 3 difficulty, std. dev of 1.5 difficulty
     * Puts 95.44% of all generated difficulty values between 0 and 6, which is a good range.
     * Anything negative gets the sign flipped to positive, and we can work with anything above 6 anyhow.
     *
     * @return generated difficulty value
     */
    private int calculateEndlessDifficulty() {
        Random rand = new Random();
        int value = (int) Math.round(rand.nextGaussian(3, 1.5));
        if (value < 0) value = -1 * value;//remove chances of negative values(low but there)

        return value;
    }
    
    /**
     * Whether a room should be a shrine or not
     * (1 in 10 rooms should be a shrine)
     *
     * @return True if the random number is less than equal to 0.1
     */
    private boolean shouldRoomBeShrine() {
            return Math.random() <= 0.2;
    }


    
}
