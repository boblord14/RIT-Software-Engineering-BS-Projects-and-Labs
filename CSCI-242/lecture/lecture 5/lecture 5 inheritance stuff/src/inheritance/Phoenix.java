package inheritance;

/**
 * The mighty Phoenix possesses the power of flight and can regenerate
 * hit points when taking damage if the damage is non-critical.
 *
 * @author Sean Strout @ RIT CS
 */
public class Phoenix extends Monster implements Flyer {
    /** how far has this phoenix flown? */
    private int distanceFlown;

    /**
     * Create a new phoenix.
     *
     * @param name their name
     * @param hitPoints their hit points
     */
    public Phoenix(String name, int hitPoints) {
        super(name, hitPoints);
        this.distanceFlown = 0;
    }

    public int getDistanceFlown() {
        return this.distanceFlown;
    }

    @Override
    public String toString() {
        return "Phoenix{" +
                "distanceFlown=" + this.distanceFlown +
                ", " + super.toString();
    }

    @Override
    public void fly(int distance) {
        System.out.println(getName() + " flies " + distance + " units.");
        this.distanceFlown += distance;
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (getHitPoints() > 0) {
            System.out.println(getName() + " regens 10 hit points.");
            setHitPoints(getHitPoints() + 10);
        }
    }
}