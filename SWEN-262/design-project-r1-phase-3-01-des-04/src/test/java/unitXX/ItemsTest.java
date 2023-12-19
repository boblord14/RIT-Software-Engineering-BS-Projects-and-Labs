package unitXX;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import model.PlayerCharacter;
import model.Stat;
import model.Items.Armor;
import model.Items.Buffer;
import model.Items.Food;
import model.Items.Rarity;
import model.Items.RarityModifier;
import model.Items.Weapon;

public class ItemsTest {
    private static int BASEHEALTH = 100;
    private static int BASEATTACK = 10;
    private static int BASEDEFENCE = 0;

    private PlayerCharacter makePlayerCharacter(){
        return new PlayerCharacter("John", "old");
    }

    @Test
    /**
     * tests that charactr has correct base stats
     */
    public void testMakePC() {
        PlayerCharacter PC = makePlayerCharacter();
        assertEquals(BASEATTACK, PC.getAttack());
        assertEquals(BASEHEALTH, PC.getHealth());
        assertEquals(BASEDEFENCE, PC.getDefense());
    }
    
    @Test
    /**
     * tests that an item's base value is correct
     */
    public void testItemValue() {
        int baseVal = 2;
        Weapon weapon = new Weapon(baseVal, "sword", "cool");
        assertEquals(baseVal, weapon.getValue());
    }    

    @Test
    /**
     * tests that an item's gold is correct
     */
    public void testItemGold() {
        int baseVal = 2;
        Weapon weapon = new Weapon(baseVal, "sword", "cool");
        assertEquals(baseVal * 100, weapon.getGold());
    }  
    
    @Test
    /**
     * tests that an item's name is correct
     */
    public void testItemName() {
        int baseVal = 2;
        Weapon weapon = new Weapon(baseVal, "sword", "cool");
        assertEquals("sword", weapon.getName());
    }

    @Test
    /**
     * tests that an item's description is correct
     */
    public void testItemDescription() {
        int baseVal = 2;
        Weapon weapon = new Weapon(baseVal, "sword", "cool");
        assertEquals("cool", weapon.getDescription());
    }

    @Test
    /**
     * tests that a weapon can be equiped to character
     */
    public void testWeaponEquip() {
        int baseVal = 2;
        Weapon weapon = new Weapon(baseVal, "sword", "cool");
        PlayerCharacter PC = makePlayerCharacter();
        PC.equipWeapon(weapon);
        assertEquals(PC.getWeapon(), weapon);
    }

    @Test
    public void testWeaponUse(){
        int baseVal = 2;
        Weapon weapon = new Weapon(baseVal, "sword", "cool");
        PlayerCharacter PC = makePlayerCharacter();
        weapon.onUse(PC);
        assertEquals(PC.getWeapon(), weapon);
    }

    @Test
    /**
     * tests that an rairity properly changes the item.
     */
    public void testRarity() {
        int baseVal = 2;
        PlayerCharacter PC = makePlayerCharacter();
        Weapon weapon = new Weapon(baseVal, "sword", "cool");
        // weapon.addAttribute(new RarityModifier(Rarity.Uncommon));
        PC.equipWeapon(weapon);
        assertEquals("uncommon sword", weapon.getName());
        assertEquals(baseVal * 100 * Rarity.Uncommon.getMod(), weapon.getGold());
        assertEquals(BASEATTACK + baseVal * 2, PC.getAttack());
    }

    @Test
    /**
     * tests that a armor can be equiped to character
     */
    public void testArmorEquip() {
        int baseVal = 2;
        Armor armor = new Armor(baseVal, "chain", "cool");
        PlayerCharacter PC = makePlayerCharacter();
        PC.equipArmor(armor);
        assertEquals(armor, PC.getArmor());
        assertEquals(BASEDEFENCE + baseVal, PC.getDefense());
    }

    @Test
    public void testFood() {
        int baseVal = 2;
        Food food = new Food(baseVal, "watermelon", "green and pink");
        PlayerCharacter PC = makePlayerCharacter();
        food.onUse(PC);
        assertEquals(BASEHEALTH + baseVal, PC.getHealth());
    }

    @Test
    /**
     * adds buff to player character, check stats line up, waits 10 turns, cheacks stats have been reduced
     * @return
     */
    public void testBuff(){
        int baseVal = 2;
        Buffer pot = new Buffer(baseVal, "Strength Potion", "green and pink", Stat.attack);
        PlayerCharacter PC = makePlayerCharacter();
        pot.onUse(PC);
        assertEquals(BASEATTACK + baseVal, PC.getAttack());
        for (int i = 0; i < 10; i++) {
            PC.incBuffs();
        }
        assertEquals(BASEATTACK, PC.getAttack());
    }

}
