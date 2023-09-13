package abstract_classes;

/**
 * Testing the abstract Monster class with concrete Phoenix and Ogre classes.
 *
 * @author Sean Strout @ RIT CS
 */
public class TestMonsters {
    /**
     * The main method engages Fawkes the Phoenix and Shrek the Ogre in
     * a classic battle.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        // Monster m = new Monster("m1", 100);      // Monster is abstract
        Phoenix fawkes = new Phoenix("Fawkes", 100);
        Ogre shrek = new Ogre("Shrek", 60, "Green");

        while (fawkes.getHitPoints() > 0 && shrek.getHitPoints() > 0) {
            fawkes.attack(shrek);
            shrek.attack(fawkes);
            System.out.println(fawkes);
            System.out.println(shrek);
            System.out.println();
        }
    }
}

/*
$ java TestMonsters
Fawkes claws Shrek, Kaw-kaw!
Shrek takes 10 damage.
Shrek clubs Fawkes, Hue-hue!
Fawkes takes 30 damage.
Fawkes regens 10 hit points.
Phoenix{distanceFlown=0, Monster{name='Fawkes', hitPoints=80}
Ogre{color=Green, Monster{name='Shrek', hitPoints=50}

Fawkes claws Shrek, Kaw-kaw!
Shrek takes 10 damage.
Shrek clubs Fawkes, Hue-hue!
Fawkes takes 30 damage.
Fawkes regens 10 hit points.
Phoenix{distanceFlown=0, Monster{name='Fawkes', hitPoints=60}
Ogre{color=Green, Monster{name='Shrek', hitPoints=40}

Fawkes claws Shrek, Kaw-kaw!
Shrek takes 10 damage.
Shrek clubs Fawkes, Hue-hue!
Fawkes takes 30 damage.
Fawkes regens 10 hit points.
Phoenix{distanceFlown=0, Monster{name='Fawkes', hitPoints=40}
Ogre{color=Green, Monster{name='Shrek', hitPoints=30}

Fawkes claws Shrek, Kaw-kaw!
Shrek takes 10 damage.
Shrek clubs Fawkes, Hue-hue!
Fawkes takes 30 damage.
Fawkes regens 10 hit points.
Phoenix{distanceFlown=0, Monster{name='Fawkes', hitPoints=20}
Ogre{color=Green, Monster{name='Shrek', hitPoints=20}

Fawkes claws Shrek, Kaw-kaw!
Shrek takes 10 damage.
Shrek clubs Fawkes, Hue-hue!
Fawkes takes 30 damage.
Fawkes was vanquished!
Phoenix{distanceFlown=0, Monster{name='Fawkes', hitPoints=0}
Ogre{color=Green, Monster{name='Shrek', hitPoints=10}
*/