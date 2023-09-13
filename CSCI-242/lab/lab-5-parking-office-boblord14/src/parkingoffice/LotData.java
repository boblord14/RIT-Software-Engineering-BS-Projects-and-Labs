package parkingoffice;

import java.util.ArrayList;

/**
 * A class that represent a parking lot. Every lot has a unique number and a collection
 * of the cars parked in it.
 *
 * @author Ethan Patterson
 *
 * Not gonna go into detail here as everything was pre explained. This class took all of 5 minutes to write with descriptions
 * this detailed. Only cool thing is the fact it uses an arraylist to track the plates seen and just tests from there if a
 * plate has been seen before
 */
public class LotData {

    final static int INITIAL_LOT_COUNT = 0;
    private final int lot;
    private ArrayList<String> plateData;
    private int uniqueCount;
    public LotData(int lot) {
        this.plateData = new ArrayList<String>();
        this.lot = lot;
        this.uniqueCount = INITIAL_LOT_COUNT;
    }

    /**
     * Returns a string reporting the number of unique cars seen in this lot.
     * The report string is of the form:
     *
     * "Lot {id} was used by {n} car(s) today.", where id is the lot's unique number and n is the number of cars.
     * @return the usage report for the day.
     */
    public String report() {
        return ("Lot " + lot + " was used by " + uniqueCount + " car(s) today.");
    }

    /**
     * Takes in a license plate, records the information, and returns whether the car
     * is newly-seen in this lot this day.
     * @param plate A license plate
     * @return True if the car had not previously been seen.
     */
    public boolean sawCar(String plate) {
    if (plateData.contains(plate)) {
        return false;
    }
    else {
        plateData.add(plate);
        uniqueCount++;
        return true;
    }
    }

    /**
     * Resets any data structures for a new day of ticketing.
     */
    public void newDay() {
        uniqueCount = INITIAL_LOT_COUNT;
        plateData.clear();
    }
}