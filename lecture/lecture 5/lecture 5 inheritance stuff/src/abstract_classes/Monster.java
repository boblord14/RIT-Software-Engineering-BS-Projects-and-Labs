package abstract_classes;

/**
 * The monster is abstract now, with an abstract attack() method that
 * each real monster subclass must implement.  It is not illegal to
 * create a Monster object (what would happen if attack were called on it?).
 *
 * @author Sean Strout @ RIT CS
 */
public abstract class Monster {
    /** the name of our esteemed monster */
    private String name;
    /** the current number of hit points */
    private int hitPoints;

    /**
     * Create a new monster.
     *
     * @param name the name
     * @param hitPoints current hit points
     */
    public Monster(String name, int hitPoints) {
        this.name = name;
        this.hitPoints = hitPoints;
    }

    /**
     * Get the name.
     *
     * @return the name (derp!)
     */
    public String getName() {
        return name;
    }

    /**
     * Get the current hit points.
     *
     * @return hit points
     */
    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    /**
     * Called by a monster when they want to attack another monster.
     *
     * @param monster the monster being attacked
     */
    public abstract void attack(Monster monster);

    /**
     * Our monster takes some damage.
     *
     * @param amount amount of damage to take
     */
    public void takeDamage(int amount) {
        System.out.println(this.name + " takes " + amount + " damage.");
        this.hitPoints -= amount;
        if (this.hitPoints <= 0) {
            System.out.println(this.name + " was vanquished!");
            this.hitPoints = 0;
        }
    }

    @Override
    public String toString() {
        return "Monster{" +
                "name='" + name + '\'' +
                ", hitPoints=" + hitPoints +
                '}';
    }
}
