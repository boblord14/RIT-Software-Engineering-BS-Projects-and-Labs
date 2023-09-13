package toys;

public class Robot extends BatteryPowered{

    final static int FLY_SPEED = 25;
    final static int RUN_SPEED = 10;
    final static int INITIAL_SPEED = 0;
    private static int robotCode = 500;
    private final boolean flight;
    private int distance;
    private final String name;

    protected Robot(String name, int batteryNum, boolean doesFly){
        super(robotCode, name, batteryNum);
        this.distance = INITIAL_SPEED;
        this.flight = doesFly;
        this.name = name;
        robotCode++;
    }

    public boolean isFlying(){ return flight; }

    public int getDistance(){ return distance; }

    @Override
    protected void specialPlay(int time) {
        if (flight){
            distance+=(time*FLY_SPEED);
            System.out.println("\t" + name + " is flying around with total distance: " + distance);
            super.increaseWear(FLY_SPEED);
        }
        else{
            distance+=(time*RUN_SPEED);
            System.out.println("\t" + name + " is running around with total distance: " + distance);
            super.increaseWear(RUN_SPEED);
        }
        super.useBatteries(time);
    }

    @Override
    public String toString(){ return super.toString() + ", Robot{F:" + flight + ", D:" + distance + "}"; }
}
