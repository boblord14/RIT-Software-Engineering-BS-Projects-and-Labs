package toys;

public class ActionFigure extends Doll {
    private static int afCode = 300;
    final static int MIN_ENERGY_LEVEL = 1;
    final static Color HAIR_COLOR = Color.ORANGE;
    private int currentEnergy;
    private final String name;
    protected ActionFigure(String name, int age, String speak) {
        super(afCode, name, HAIR_COLOR, age, speak);
        this.currentEnergy = MIN_ENERGY_LEVEL;
        this.name = name;
        afCode++;
    }

    public int getEnergyLevel(){ return currentEnergy; }

    @Override
    protected void specialPlay(int time){
        System.out.println("\t" + name + " kung foo chops with " + (currentEnergy * time) + " energy!");
        currentEnergy++;
        super.specialPlay(time);
    }

    @Override
    public String toString(){ return super.toString() + ", ActionFigure{E:" + currentEnergy + "}"; }
}
