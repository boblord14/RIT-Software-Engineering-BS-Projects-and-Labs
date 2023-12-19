package model.Items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonSerializable.Base;

import model.PlayerCharacter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Armor.class, name = "ArmorItem"),
        @JsonSubTypes.Type(value = Weapon.class, name = "WeaponItem"),
        @JsonSubTypes.Type(value = Food.class, name = "FoodItem"),
        @JsonSubTypes.Type(value = BagItem.class, name = "BagItem"),
        @JsonSubTypes.Type(value = Buffer.class, name = "Buffer")
})
public abstract class BaseItem implements ItemAttribute{

    @JsonProperty("value")
    private int baseValue;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;

    // Used for jackson deserialization
    protected BaseItem() {

    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BaseItem)) return false;

        BaseItem other = (BaseItem) obj;
        return this.baseValue == other.baseValue 
        && this.name.equals(other.name) 
        && this.description == other.description;
    }

    public BaseItem(int baseValue, String name, String description) {
        this.baseValue = baseValue;
        this.name = name;
        this.description = description;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return name; //otherwise return the base value
    }  

    @Override
    @JsonIgnore
    public String getDescription() {
        return description; //otherwise return the base value
    }

    @Override
    @JsonIgnore
    public int getGold() {
        return getValue() * 100;
    }

    @Override
    @JsonIgnore
    public int getValue() {
        return baseValue; //otherwise return the base value
    }

    @Override
    public void onUse(PlayerCharacter target) {
        onUse(target, this.getValue());
    }

}
