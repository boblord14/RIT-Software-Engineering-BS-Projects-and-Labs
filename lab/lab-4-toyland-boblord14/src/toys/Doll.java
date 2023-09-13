package toys;

public class Doll extends Toy{
    private static int dollCode = 200;
    private final String name;
    private final Color hairColor;
    private final int age;
    private final String speak;

    protected Doll(String name, Color hairColor, int age, String speak){
        super(dollCode, name);
        this.name = name;
        this.hairColor = hairColor;
        this.age = age;
        this.speak = speak;
        dollCode++;
    }

    protected Doll(int productCode, String name, Color hairColor, int age, String speak){//action figure constructor
        super(productCode, name);
        this.name = name;
        this.hairColor = hairColor;
        this.age = age;
        this.speak = speak;
    }

    public Color getHairColor(){ return hairColor; }

    public int getAge(){ return age; }

    public String getSpeak(){ return speak; }

    @Override
    protected void specialPlay(int time) {
        System.out.println("\t" + name + " brushes their " + hairColor + " hair and says, \"" + speak + "\"");
        super.increaseWear(age);
    }

    @Override
    public String toString(){ return super.toString() + ", Doll{HC:" + hairColor + ", A:" + age + ", S:" + speak + "}"; }

}
