package model.Shrine;

import java.util.LinkedList;

import model.map.Map;

public class MapHistory {

    public static MapHistory INSTANCE = new MapHistory();

    private LinkedList<MapRevision> history;
    private Map map;

    private MapHistory() {
        this.history = new LinkedList<>();
    }

    /**
     * Updates the map field and clears the history
     * 
     * @param map The new map
     */
    public void setMap(Map map) {
        this.map = map;

        // History must be cleared because this is a new map
        history.clear();
    }

    /**
     * Creates a new revision and adds it to the front of the history linkedlist
     */
    public void storeRevision() {
        System.out.println("CALLED????");
        history.addFirst(map.createRevision());
    }

    /**
     * Restores a the map to the state of a given revision. Removes the restored
     * revision and all prior revisions from the history linkedlist
     * 
     * @param index The index of the revision
     * @throws IndexOutOfBoundsException If the index does not exist
     */
    public void restoreRevision(int index) throws IndexOutOfBoundsException {
        map.restoreRevision(history.get(index));
        // Should delete the current revision and all prior
        purge(index);
    }

    /**
     * Gets all map revisions
     * 
     * @return A linked list of MapRevision
     */
    public LinkedList<MapRevision> getRevisions() {
        return history;
    }

    /**
     * Removes the first n map revisions
     * 
     * @param amount The amount of revisions to remove
     */
    public void purge(int amount) {
        for (int index = 0; index <= amount; index++) {
            history.removeFirst();
        }
    }

    public Map getMap() {
        return map;
    }

}
