package parkingoffice;

import java.io.FileNotFoundException;

/**take basic args of number of lots and the car/event filenames and start up the ParkingOffice with them,
 *and once its good to go, process the event file
 *
 * nothing special here really
 */

public class ParkingSim {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 3){
            System.out.println("(Usage: java ParkingOffice <num-lots> <car-filename> <days-filename>");
        } else {
            int numLots = Integer.parseInt(args[0]);
            String carFile = args[1];
            String eventFile = args[2];
            ParkingOffice office = new ParkingOffice(numLots, carFile);
            office.processDays(eventFile);
        }

    }
}