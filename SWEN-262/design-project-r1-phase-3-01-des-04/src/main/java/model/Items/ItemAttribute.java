package model.Items;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import model.PlayerCharacter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ItemModifier.class, name = "ItemModifier"),
        @JsonSubTypes.Type(value = BaseItem.class, name = "BaseItem"),
})
public interface ItemAttribute {

    /**
     * @return the name of the item
     */
    public String getName();

    /**
     * @return the description of the item
     */
    public String getDescription();

    /**
     * @return the amount of gold the item is worth
     */
    public int getGold();

    /**
     * the behavior that should happen 
     */
    public void onUse(PlayerCharacter target);

    /**
     * the behavior that should happen but is used to pass items value down
     */
    public void onUse(PlayerCharacter target, int value);

    /**
     * @return the modified value that will be used for the onuse
     */
    public int getValue();
    
}
