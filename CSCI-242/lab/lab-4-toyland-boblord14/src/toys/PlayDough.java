package toys;

public class PlayDough extends Toy{

    private static int doughCode = 100;
    final static double WEAR_MULTIPLIER = .05;
    private final Color color;
    private final String name;
    protected PlayDough(String name, Color color){
        super(doughCode, name);
        this.color = color;
        this.name = name;
        doughCode++;
    }

    public Color getColor(){ return color; }

    @Override
    protected void specialPlay(int time){
        System.out.println("\tArts and crafting with " + color + " " + name);
        super.increaseWear(WEAR_MULTIPLIER*time);
    }

    @Override
    public String toString(){
        return (super.toString() + ", PlayDough{C:" + color + "}");
    }
}
