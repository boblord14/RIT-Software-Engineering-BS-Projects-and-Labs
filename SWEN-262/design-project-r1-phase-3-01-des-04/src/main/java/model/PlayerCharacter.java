package model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Items.Armor;
import model.Items.BaseItem;
import model.Items.ItemAttribute;
import model.Items.Weapon;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")

public class PlayerCharacter extends Character {

    private ItemAttribute armor;
    private ItemAttribute weapon;

    private int roomId;

    private int xCoord;

    private int yCoord;

    public PlayerCharacter(String name, String description) {
        super(name, description, 100, 10, 0);
    }

    public void equipWeapon(Weapon newWeapon) {
        if (weapon != null) {
            addItemToInventory(weapon);
            setAttack(getAttack() - weapon.getValue());
        }
        this.weapon = this.getInventory().baseItemRoot((BaseItem)newWeapon); //this is done that way the lead attribute is passed around rather than the base item
        setAttack(getAttack() + weapon.getValue());
        removeItemFromInventory(newWeapon);
    }

    public void equipArmor(Armor newArmor) {
        if (armor != null) {
            addItemToInventory(armor);
            setDefense(getDefense() - armor.getValue());
        }
        this.armor = this.getInventory().baseItemRoot((BaseItem)newArmor);
        setDefense(getDefense() + armor.getValue());
        removeItemFromInventory(newArmor);
    }

    public ItemAttribute getWeapon() {
        return weapon;
    }

    public ItemAttribute getArmor() {
        return armor;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    @JsonCreator
    public PlayerCharacter(
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("health") int health,
            @JsonProperty("attack") int attack,
            @JsonProperty("defense") int defense) {
        super(name, description, health, attack, defense);

    }

    /**
     * Deep clones the player character (meaning every obj is newly instantiated)
     * 
     * @return A copy of this player character
     * @throws JsonMappingException    If unable to player character to Room object
     * @throws JsonProcessingException If unable to process json
     */
    @JsonIgnore
    public PlayerCharacter deepClone() throws JsonMappingException, JsonProcessingException {
        ObjectMapper objMapper = new ObjectMapper();
        return objMapper.readValue(objMapper.writeValueAsString(this), PlayerCharacter.class);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PlayerCharacter)) return false;

        PlayerCharacter other = (PlayerCharacter) obj;
        
        return super.equals(other) 
        && ((this.armor == null && other.armor == null) || this.armor.equals(other.armor)) 
        && ((this.weapon == null && other.weapon  == null) ||this.weapon.equals(other.weapon));
    }

}
