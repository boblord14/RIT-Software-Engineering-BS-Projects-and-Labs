package unitXX;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import model.NonPlayerCharacter;
import model.DayNight.DiurnalNightState;
import model.DayNight.NocturnalDayState;

public class DayNightTest {

    private NonPlayerCharacter makeNonPlayerCharacterNocturnal(){
        return new NonPlayerCharacter("Shlob Bob", "Slime Lord", true, new NocturnalDayState());
    }

    private NonPlayerCharacter makeNonPlayerCharacterDiurnal(){
        return new NonPlayerCharacter("Shirley", "Squirrel Gone Bad", false, new DiurnalNightState());
    }

    @Test 
    /*
     * Tests if stats are improved once day turns to night
     * Stats should go up by 20%(Rounded down)
     */
    public void testNocturnalNight() {
        NonPlayerCharacter NPC = makeNonPlayerCharacterNocturnal();
        int initAttack = (int)Math.floor(NPC.getAttack() * 1.20);
        int initDefense = (int)Math.floor(NPC.getDefense() * 1.20);
        int initHealth = (int)Math.floor(NPC.getHealth() * 1.20);

        NPC.changeState(false);
        int testAttack = NPC.getAttack();
        int testDefense = NPC.getDefense();
        int testHealth = NPC.getHealth();

        assertEquals(initAttack, testAttack);
        assertEquals(initDefense, testDefense);
        assertEquals(initHealth, testHealth);
    }

    @Test 
    /*
     * Tests if stats are improved once night turns to day
     * Stats should go up by 10%(Rounded down)
     */
    public void testDiurnalDay() {
        NonPlayerCharacter NPC = makeNonPlayerCharacterDiurnal();
        int initAttack = (int)Math.floor(NPC.getAttack() * 1.10);
        int initDefense = (int)Math.floor(NPC.getDefense() * 1.10);
        int initHealth = (int)Math.floor(NPC.getHealth() * 1.10);

        NPC.changeState(true);
        int testAttack = NPC.getAttack();
        int testDefense = NPC.getDefense();
        int testHealth = NPC.getHealth();

        assertEquals(initAttack, testAttack);
        assertEquals(initDefense, testDefense);
        assertEquals(initHealth, testHealth);
    }
}
