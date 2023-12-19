package unitXX.Shrine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.PlayerCharacter;
import model.Room;
import model.Shrine.MapHistory;
import model.Shrine.MapRevision;
import model.map.Map;

public class MapHistoryTest {

    private MapHistory mapHistory = MapHistory.INSTANCE;
    private Map mockMap;

    private PlayerCharacter character;
    private Room roomOne;
    private Room roomTwo;

    private MapRevision revisionOne;
    private MapRevision revisionTwo;

    @BeforeEach
    public void setupMapHistory() {
        this.mockMap = mock(Map.class);
        this.mapHistory.setMap(mockMap);
        this.character = new PlayerCharacter("Test", "test");

        this.roomOne = new Room(1, 1, 1, mockMap, false);
        this.roomTwo = new Room(2, 2, 2, mockMap, false);

        this.revisionOne = new MapRevision(new HashMap<>() {
            {
                put(0, roomOne);
                put(1, roomTwo);
            }
        }, character, 1);

        this.revisionTwo = new MapRevision(new HashMap<>() {
            {
                put(0, roomTwo);
            }
        }, character, 0);
    }

    @Test
    public void testSetMap() {
        when(mockMap.createRevision()).thenReturn(null);

        mapHistory.storeRevision();
        mapHistory.storeRevision();
        assertEquals(2, mapHistory.getRevisions().size());
        
        this.mapHistory.setMap(null);
        assertNull(mapHistory.getMap());
        assertEquals(0, mapHistory.getRevisions().size());
    }

    @Test
    public void testStoreRevision() {
        when(mockMap.createRevision()).thenReturn(this.revisionOne);
        mapHistory.storeRevision();
        
        when(mockMap.createRevision()).thenReturn(this.revisionTwo);
        mapHistory.storeRevision();

        LinkedList<MapRevision> revisions = mapHistory.getRevisions();
        assertEquals(2, revisions.size());
        assertTrue(this.revisionTwo.equals(revisions.getFirst()));
        assertTrue(this.revisionOne.equals(revisions.get(1)));
    }

    @Test
    public void testRestoreRevision() {
        when(mockMap.createRevision()).thenReturn(this.revisionTwo);
        mapHistory.storeRevision();
        when(mockMap.createRevision()).thenReturn(this.revisionOne);
        mapHistory.storeRevision();
        mapHistory.storeRevision(); // Restore to this revision; so delete this revision and one in front
        when(mockMap.createRevision()).thenReturn(this.revisionOne);
        mapHistory.storeRevision(); 
        this.character.setHealth(10);

        mapHistory.restoreRevision(1);
        LinkedList<MapRevision> revisions = mapHistory.getRevisions();
        
        assertEquals(2, mapHistory.getRevisions().size());

        MapRevision firstRevision = mapHistory.getRevisions().getFirst();
        
        this.character = firstRevision.getPlayerCharacter();
        assertEquals(100, firstRevision.getPlayerCharacter().getHealth());

        assertTrue(this.revisionOne.equals(revisions.pollFirst()));
        assertTrue(this.revisionTwo.equals(revisions.pollFirst()));
    }

}
