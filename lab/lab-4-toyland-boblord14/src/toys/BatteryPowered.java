package toys;

public abstract class BatteryPowered extends Toy{

    final static int FULLY_CHARGED = 100;
    final static int DEPLETED = 0;
    private int batteryLevel;
    private final int batteryNum;
    protected BatteryPowered(int productCode, String name, int batteryNum){
        super(productCode, name);
        this.batteryLevel = FULLY_CHARGED;
        this.batteryNum = batteryNum;
    }

    public int getBatteryLevel(){ return batteryLevel; }

    public int getNumBatteries(){ return batteryNum; }

    protected void useBatteries(int time){  //ok whose idea was it to let a batteryLevel of 0 not be considered depleted?
        batteryLevel-=(time + batteryNum);
        if (batteryLevel<DEPLETED){
            batteryLevel=DEPLETED;
            System.out.println("\tDEPLETED:" + this);
            batteryLevel=FULLY_CHARGED;
            System.out.println("\tRECHARGED:" + this);

        }
    }

    @Override
    public String toString(){ return super.toString() + ", BatteryPowered{BL:" + batteryLevel + ", NB:" + batteryNum + "}"; }

}
