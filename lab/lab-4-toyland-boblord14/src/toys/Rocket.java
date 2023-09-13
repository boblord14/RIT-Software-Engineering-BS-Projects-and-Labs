package toys;
import java.lang.Math;

/**
 * Rocket takes a name, weight, and engine count.
 * When specialPlay is called it can either launch or be repaired.
 * If there are any engines remaining, it launches, and if there are no more engines, it gets repaired instead.
 *
 * Launching subtracts one engine, calculates a height to fly(equation is 500*sqrt(weight)*log10(time)), and "launches" it.
 * Launching tacks on a fair bit of wear, and adds the distance flown to the total distance flown
 * (which has no purpose other than being a cool statistic). Note that the height calc needs Java.lang.Math as sqrt and log
 * to function properly
 *
 * Repairing "repairs and polishes" the rocket, removing half the wear from it.
 */
public class Rocket extends Toy{

    private static int rocketCode = 600;
    final static int MINIMUM_ENGINES = 0;
    final static int MINIMUM_FLIGHT_DIST = 0;
    private int engineCount;
    private final String name;
    private final Double weight;
    private int totalFlight;

    protected Rocket(String name, double weight, int engineCount){
        super(rocketCode, name);
        this.engineCount = engineCount;
        this.name = name;
        this.weight = weight;
        this.totalFlight = MINIMUM_FLIGHT_DIST;
        rocketCode++;
    }
    @Override
    protected void specialPlay(int time) {
        if (engineCount>MINIMUM_ENGINES){
            double heightCalc = (500 * Math.sqrt(weight) * Math.log10(time));
            totalFlight += ((int) heightCalc);
            System.out.println("\t" + name + " launches and goes " + (int) heightCalc + " feet into the air! It has " + (engineCount-1) + " launches remaining and has flown for a total of " + totalFlight + " feet.");
            engineCount--;
            increaseWear((int)(heightCalc/15));
        }
        else{
            System.out.println("\t" + name + " has no more engines, so it was repaired and polished for " + time + " minutes and had " + (super.getWear()/2) + " wear removed");
            super.increaseWear(-1*(super.getWear()/2));
        }



    }

    @Override
    public String toString(){ return super.toString() + ", Rocket{W:" + weight + ", EC:" + engineCount + ", TF:" + totalFlight + "}"; }
}
