package abstract_classes;

/**
 * Ogre's like clubbing other monsters and they have a cool color too.
 *
 * @author Sean Strout @ RIT CS
 */
public class Ogre extends Monster {
    /** this ogre's color */
    public String color;

    /**
     * Create a new ogre.
     *
     * @param name their name
     * @param hitPoints their hit points
     * @param color their color
     */
    public Ogre(String name, int hitPoints, String color) {
        super(name, hitPoints);
        this.color = color;
    }

    @Override
    public void attack(Monster m) {
        System.out.println(getName() + " clubs " + m.getName() + ", Hue-hue!");
        m.takeDamage(30);
    }

    @Override
    public String toString() {
        return "Ogre{" +
                "color=" + this.color +
                ", " + super.toString();
    }
}

