package parkingoffice;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/** ParkingOffice is the class that reads both files and inputs them to where they need to go.
 * Constructor starts by reading in the car info as new CarDatas to the cars hashmap, and then creates an array of LotDatas
 * called lotlist in order to do things with the lots.
 */
public class ParkingOffice {
    private LotData[] lotList;
    private HashMap<String, CarData> cars;
    public ParkingOffice(int numLots, String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filename));
        this.cars = new HashMap<>();
            while (in.hasNextLine()) {
                String line = in.nextLine();
                String[] splitArr = line.split(" ");
                CarData tempCar = new CarData(splitArr[0], Integer.parseInt(splitArr[1]));
                cars.put(splitArr[0], tempCar);
        }
            this.lotList = new LotData[numLots];
            for(int i=0; i< numLots; i++){
                lotList[i] = new LotData(i);
            }
    }
    /** processDays is the bulk of the program. reads the event file line by line and uses switch cases to determine what
     * things are. For the easy ones that is, as the digit check(\\d*) needs the if statement and the .matches()
     *
     * So that and the actual plate management are in the default cases with a string of if-else-if shenanigans.
     *
     * "Beginday" prints out the line separating the info in the console and resets the lots by calling the newday() function
     * in every lot.
     *
     * "P" sets payment mode on(by flipping the payment flag on and the enforcement flag off)
     *
     * in the default case the digit check of "\\d*" sets enforcement mode on(vice versa of the payment mode's function)
     * but additionally stores the monitored lot.
     *
     * Plate inputs go to the default case, and fail the digit check(obviously), which then get read and what happens then
     * is decided by what flag is enabled(if payment then payTickets, if enforcement then do enforcement)
     *
     * When a plate is read in enforcement mode, it checks if its a new car in that lot with sawCar(), if it is a new
     * car it then checks if its valid in the lot, and if not applies a ticket. sawCar automatically registers it as unique
     * if it is, so if the car is valid in the lot nothing has to happen, and nothing happens either if it isnt a new car.
     *
     * "EndDay" prints the end of day text, converts the bozoList arraylist of offenders into a normal array, and then
     * calls a modified quicksort method(more info on that near the actual method). After it gets sorted a quick test
     * is done to determine if there is over 10 cars that were ticketed for the offender list. If there is, it is set to
     * take the top 10 offenders, if theres less than 10 offenders it is set to take however many actual offenders there are,
     * and prints out the data accordingly with the overridden toString() method.
     *After that, the lotData reports for every lot are printed out.
     *
     */
    public void processDays(String filename) throws FileNotFoundException {
        boolean enforcementFlag = false;
        boolean paymentFlag = false;
        int monitoredLot = 0;
        ArrayList<String> bozoList = new ArrayList<>();
        Scanner in = new Scanner(new File(filename));
            while (in.hasNextLine()) {
                String line = in.nextLine();
                switch(line){
                    case "BeginDay":
                        for (LotData data : lotList) { data.newDay(); }
                        System.out.println("------------");
                        break;
                    case "EndDay":
                        System.out.println("End of day. Worst offenders are:");
                        String[] bozoArr = new String[bozoList.size()];
                        bozoArr = bozoList.toArray(bozoArr);
                        quickSort(bozoArr, 0, (bozoArr.length-1));
                        int topBozoCount = Math.min(bozoList.size(), 10);
                        for (int p = 0; p<topBozoCount; p++){ System.out.println(cars.get(bozoArr[p]).toString()); }
                        System.out.println("Lot usage was:");
                        for (LotData lotData : lotList) { System.out.println(lotData.report()); }
                    break;
                    case "P":
                        enforcementFlag = false;
                        paymentFlag = true;
                        break;
                    default:
                        if(line.matches("\\d*")){
                            enforcementFlag = true;
                            paymentFlag = false;
                            monitoredLot = Integer.parseInt(line);
                            break;
                        }
                        else if (enforcementFlag) {
                            if(lotList[monitoredLot].sawCar(line)) {
                                if (!cars.get(line).isOk(monitoredLot)) {
                                    CarData tempCar = cars.get(line);
                                    tempCar.giveTicket();
                                    cars.remove(line);
                                    cars.put(line, tempCar);
                                    if (bozoList.contains(line)) {
                                        bozoList.remove(line);
                                    }
                                    bozoList.add(line);
                                }
                            }
                         } else if (paymentFlag) {
                             CarData tempCar = cars.get(line);
                            tempCar.payTickets();
                            cars.remove(line);
                            cars.put(line, tempCar);
                            bozoList.remove(line);
                        } else System.out.println("invalid input on this line: " + line);

                        break;
                }
            }
    }


    /** Sorting: just created 2 new methods to handle the sorting instead as it was easier
     * beforehand I used bubblesort which worked fine for the first 2 lot tests as they were short(O=n^2), and this was
     * just the first one that came to mind.
     *
     *But with the massive bigtest and longtest, this is impractical with that big O.
     * Instead, here's a copy of quicksort instead, which is adapted for the compareTo from CarData.
     * It has a muuuuuch more impressive big O time for everything.
     */
    public void quickSort(String arr[], int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex-1);
            quickSort(arr, partitionIndex+1, end);
        }
    }

    private int partition(String arr[], int begin, int end) {
        String pivot = arr[end];
        int i = (begin-1);

        for (int j = begin; j < end; j++) {
            if (cars.get(arr[j]).compareTo(cars.get(pivot))<0) {
                i++;

                String swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }

        String swapTemp = arr[i+1];
        arr[i+1] = arr[end];
        arr[end] = swapTemp;

        return i+1;
    }
}

