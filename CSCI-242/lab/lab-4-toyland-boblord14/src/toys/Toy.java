package toys;

public abstract class Toy implements IToy{

    final static int INITIAL_HAPPINESS = 0;
    final static int MAX_HAPPINESS = 100;
    final static double INITIAL_WEAR = 0.0;

    private final int productCode;
    private final String name;
    private int currentHappiness;
    private double currentWear;

    private boolean retired;

    protected Toy(int productCode, String name){
        this.productCode = productCode;
        this.name = name;
        this.currentHappiness = INITIAL_HAPPINESS;
        this.currentWear = INITIAL_WEAR;
    }
    @Override
    public int getProductCode() { return productCode; }
    @Override
    public String getName() { return name; }
    @Override
    public int getHappiness() { return currentHappiness; }
    @Override
    public boolean isRetired() { return retired; }
    @Override
    public double getWear() { return currentWear; }
    @Override
    public void increaseWear(double amount) {currentWear += amount;}
    @Override
    public void play(int time) {
        System.out.println("PLAYING(" + time + "): " + toString());
        specialPlay(time);
        currentHappiness+=time;
        if (currentHappiness>=MAX_HAPPINESS){
            retired = true;
            System.out.println("RETIRED: " + toString());
        }
    }
    abstract protected void specialPlay(int time);
    @Override
    public String toString(){ return "Toy{PC:" + productCode + ", N:" + name + ", H:" + currentHappiness + ", R:" + retired + ", W:" + currentWear + "}"; }
}
