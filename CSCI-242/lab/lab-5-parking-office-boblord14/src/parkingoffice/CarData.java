package parkingoffice;


/** CarData stores the actual info from the car file and has most of the functions that make this program tick.
 *
 * Im not going to reexplain most of the functions here as they were detailed pretty extensively in the lab paper.
 *
 * Only differences is the existence of the getter functions(necessary to work ofc) and how compareTo was done
 *
 * compareTo takes the ticketCount into consideration first when determining which is bigger, and then if that's equal,
 * calls the default string compareTo on the names, and the result just gets spit out from there instead.
 *
 * Also side note, the requirement to have the equals and the hashcode methods are kinda stupid as theyre only needed for
 * the junit test. It was pretty confusing to what they were supposed to be from the lab, so I had to look at the junit
 * code in order to find out what I was supposed to do. Might be worth depreciating from the actual lab in my opinion.
 *
 */
public class CarData {
    final static int BASE_TICKET_COUNT = 0;
    private final String licensePlate;
    private final int lot;
    private int ticketCount;

    public CarData(String licensePlate, int lot){
        this.licensePlate = licensePlate;
        this.lot = lot;
        this.ticketCount = BASE_TICKET_COUNT;
    }

    public int getTicketCount(){
        return ticketCount;
    }

    public String getLicensePlate(){
        return licensePlate;
    }

    public int getLot() { return lot; }

    public Boolean isOk(int lot){
        if (this.lot==lot){
            return true;
        }
        else return false;
    }

    public void giveTicket() {
        ticketCount++;
    }
    @Override
    public String toString(){
        return (licensePlate + " (lot " + lot + ") : " + ticketCount + " ticket(s)");
    }
    public void payTickets() {
        ticketCount = 0;
    }

    @Override
    public int hashCode(){
        return (this.licensePlate.hashCode());
    }


    public Boolean equals(CarData car){
        if((this.getTicketCount() == car.getTicketCount()) && (this.getLicensePlate().equals(car.getLicensePlate()))){
            return true;
        }
        else return false;
    }
    public int compareTo(CarData car){
        if(car.getTicketCount()>this.ticketCount){
            return 1;
        } else if (car.getTicketCount()<this.ticketCount) {
            return -1;
        }
        else return (this.licensePlate.compareTo(car.getLicensePlate()));
    }

}