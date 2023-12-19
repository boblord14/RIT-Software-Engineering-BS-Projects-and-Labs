package unitXX.Shrine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.PlayerCharacter;
import model.Room;
import model.Shrine.MapRevision;
import model.Tiles.Tile;
import model.map.Map;

public class MapRevisionTest {

    private MapRevision revision;
    private Room roomOne;
    private Room roomTwo;
    private PlayerCharacter character;
    private Map mockMap;

    @BeforeEach
    public void setupMapRevision() {
        this.mockMap = mock(Map.class);

        this.character = new PlayerCharacter("Test", "test");
        this.roomOne = new Room(1, 1, 1, mockMap, false);
        this.roomTwo = new Room(2, 2, 2, mockMap, false);

        this.revision = new MapRevision(new HashMap<>() {
            {
                put(0, roomOne);
                put(1, roomTwo);
            }
        }, character, 0);
    }

    // This one test is sufficient to test all necessary functionality
    @Test
    public void testConstructor() {
        // Constructor is called in setupMapRevision method

        HashMap<Integer, Room> rooms = this.revision.getRooms();

        // Make sure references are not pointing to the same underlying object
        assertFalse(this.revision.getPlayerCharacter() == this.character);
        assertFalse(rooms.get(0) == roomOne);
        assertFalse(rooms.get(1) == roomTwo);

        // Make sure the content of the objects are equal to the original object
        assertTrue(this.character.equals(this.revision.getPlayerCharacter()));
        assertTrue(this.roomOne.equals(rooms.get(0)));
        assertTrue(this.roomTwo.equals(rooms.get(1)));
        assertEquals(0, this.revision.getCurrentRoomId());
    }

}
