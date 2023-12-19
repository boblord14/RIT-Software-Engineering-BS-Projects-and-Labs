package model.Items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import model.PlayerCharacter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RarityModifier.class, name = "RarityModifier")
})
public abstract class ItemModifier implements ItemAttribute{

    @JsonProperty("next")
    private ItemAttribute next;

    @Override
    @JsonIgnore
    public String getDescription() {
        return next.getDescription();
    }

    @Override
    @JsonIgnore
    public int getGold() {
        return next.getGold();
    }

    @Override
    @JsonIgnore
    public String getName() {
        return next.getName();
    }

    @Override
    public void onUse(PlayerCharacter target) {
        next.onUse(target, this.getValue());
    }

    @Override
    public void onUse(PlayerCharacter target, int value) {
        next.onUse(target, value);
    }

    @Override
    @JsonIgnore
    public int getValue() {
        return next.getValue();
    }
    
    public void setNext(ItemAttribute next) {
        this.next = next;
    }

    @JsonIgnore
    public ItemAttribute getNext(){
        return next;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ItemModifier)) return false;

        ItemModifier other = (ItemModifier) obj;
        boolean isNull = this.next == null || other.next == null;
        return ((!isNull) && (
            this.getDescription().equals(other.getDescription())
            && this.getGold() == other.getGold()
            && this.getName().equals(other.getName())
            && this.getValue() == other.getValue()
        ));
    }

}
