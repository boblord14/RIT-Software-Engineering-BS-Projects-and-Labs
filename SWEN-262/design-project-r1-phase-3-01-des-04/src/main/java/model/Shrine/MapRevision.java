package model.Shrine;

import java.util.HashMap;

import model.PlayerCharacter;
import model.Room;
import model.map.Map;

public class MapRevision {

    private HashMap<Integer, Room> rooms;
    private PlayerCharacter playerCharacter;
    private int currentRoomId;

    public MapRevision(HashMap<Integer, Room> rooms, PlayerCharacter playerCharacter, int currentRoomId) {
        this.rooms = new HashMap<>();
        this.playerCharacter = null;
        this.currentRoomId = currentRoomId;

        clonePlayerCharacter(playerCharacter);
        cloneRooms(rooms);
    }

    private void clonePlayerCharacter(PlayerCharacter playerCharacter) {
        try {
            this.playerCharacter = playerCharacter.deepClone();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("FAILED TO DEEP CLONE PLAYER CHARACTER");

        }
    }

    private void cloneRooms(HashMap<Integer, Room> oRooms) {
        for (int index : oRooms.keySet()) {
            try {
                this.rooms.put(index, oRooms.get(index).deepClone());
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("FAILED TO DEEP CLONE ROOM");
            }
        }
    }

    public HashMap<Integer, Room> getRooms() {
        return rooms;
    }

    public PlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }

    public int getCurrentRoomId() {
        return currentRoomId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MapRevision)) return false;
        MapRevision other = (MapRevision) obj;

        return this.playerCharacter.equals(other.playerCharacter)
                && this.rooms.equals(other.rooms)
                && this.currentRoomId == other.currentRoomId;
    }

}
