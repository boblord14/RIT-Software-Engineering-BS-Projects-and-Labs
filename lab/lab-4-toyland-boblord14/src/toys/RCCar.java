package toys;

public class RCCar extends BatteryPowered{

    final static int STARTING_SPEED = 10;
    final static int SPEED_INCREASE = 5;
    private static int RCCode = 400;
    private int currentSpeed;
    private final String name;
    protected RCCar(String name, int batteryNum){
        super(RCCode, name, batteryNum);
        this.currentSpeed = STARTING_SPEED;
        this.name = name;
        RCCode++;
    }

    public int getSpeed(){ return currentSpeed; }

    @Override
    protected void specialPlay(int time){
        System.out.println("\t" + name + " races around at " + currentSpeed + "mph!");
        super.useBatteries(time);
        super.increaseWear(currentSpeed);
        currentSpeed+=SPEED_INCREASE;
    }

    @Override
    public String toString(){ return super.toString() + ", RCCar{S:" + currentSpeed + "}"; }
}
