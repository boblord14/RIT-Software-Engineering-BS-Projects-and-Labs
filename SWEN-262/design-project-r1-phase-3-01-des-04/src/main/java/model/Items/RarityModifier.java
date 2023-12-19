package model.Items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RarityModifier extends ItemModifier{

    @JsonProperty("rarity")
    Rarity rarity;

    private RarityModifier() {

    }

    public RarityModifier(Rarity rarity) {
        this.rarity = rarity;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return rarity + " " + super.getName();
    }

    @Override
    @JsonIgnore
    public int getValue() {
        return super.getValue() + rarity.getMod();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof RarityModifier)) return false;

        RarityModifier other = (RarityModifier) obj;
        return super.equals(obj) && this.rarity == other.rarity;
    }
    

}
