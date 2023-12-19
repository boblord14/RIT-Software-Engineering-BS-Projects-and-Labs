package model;

import model.Inventory.Drop;
import model.Inventory.PlayerInventory;
import model.Items.ItemAttribute;
import java.util.Dictionary;
import java.util.Hashtable;
import model.Items.Buff;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import java.util.ArrayList;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @Type(value = PlayerCharacter.class, name = "playerCharacter"),
    @Type(value = NonPlayerCharacter.class, name = "nonPlayerCharacter"),
})

public abstract class Character {
    private String name;
    private String description;
    private PlayerInventory inventory;
    private Dictionary<Stat, Integer> stats;
    private List<Buff> buffs;

    protected Character(String name, String description, int health, int attack, int defense) {
        this.name = name;
        this.description = description;
        this.stats = new Hashtable<Stat, Integer>();
        this.stats.put(Stat.health, health);
        this.stats.put(Stat.attack, attack);
        this.stats.put(Stat.defense, defense);
        this.buffs = new ArrayList<Buff>();
        this.inventory = new PlayerInventory();
    }


    /**
     * Attacks a character. Decreases their health by the targets's
     * health minus the target's defense
     * 
     * @param target The character being targeted
     */
    public void attack(Character target) {
        int attack = getAttack() - target.getDefense();
        attack = attack < 0 ? 0 : attack;
        target.modifyHealth(-1 * attack);
    }

    /**
     * Adds an item to the character's inventory
     * 
     * @param item The item being added
     */
    public void addItemToInventory(ItemAttribute item) {
        this.inventory.recieveItem(item);
    }

    /**
     * Removes an item from the character's inventory
     * 
     * @param item The item being removed
     */
    public void removeItemFromInventory(ItemAttribute item) {
        this.inventory.transferItem(item, new Drop());
    }

    /**
     * Modifies a character's health by a given amount. Does not
     * allow health to go below 0
     * 
     * @param amount The amount the health is being modified guys
     */
    public void modifyHealth(int amount) {
        int newHealth = getHealth() + amount;
        setHealth(newHealth < 0 ? 0 : newHealth);
    }

    public void addBuff(Buff buff){
        buffs.add(buff);
        setStat(buff.getStat(), getStat(buff.getStat()) + buff.getValue());
    }

    public void incBuffs(){
        List<Buff> tempBuffs = new ArrayList<Buff>();
        for (Buff buff : buffs) {
            tempBuffs.add(buff);
        }
        for (Buff buff : buffs)  {
            if (buff.incturn()){
                setStat(buff.getStat(), getStat(buff.getStat()) - buff.getValue());
                tempBuffs.remove(buff);
            }
        }
        buffs = tempBuffs;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHealth() {
        return this.stats.get(Stat.health);
    }

    public void setHealth(int newHealth) {
        this.stats.put(Stat.health, newHealth);
    }

    public int getAttack() {
        return this.stats.get(Stat.attack);
    }

    public void setAttack(int newAttack) {
        this.stats.put(Stat.attack, newAttack);
    }

    public int getDefense() {
        return this.stats.get(Stat.defense);
    }

    public void setDefense(int newDefense) {
        this.stats.put(Stat.defense, newDefense);
    }

    public int getStat(Stat stat) {
        return this.stats.get(stat);
    }

    public int setStat(Stat stat, int newStat) {
        return this.stats.put(stat, newStat);
    }

    public PlayerInventory getInventory() {
        return inventory;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Character)) return false;
        Character other = (Character) obj;  

        return this.name.equals(other.name) 
            && this.description.equals(other.description) 
            && this.inventory.equals(other.inventory)
            && this.stats.equals(other.stats)
            && this.buffs.equals(other.buffs);
    }

}
