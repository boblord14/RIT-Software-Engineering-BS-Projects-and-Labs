package model.Items;

import model.PlayerCharacter;
import model.Inventory.Drop;

public class Armor extends BaseItem{

    // Used for jackson deserialization
    private Armor() {

    }

    public Armor(int baseValue, String name, String description) {
        super(baseValue, name, description);
    }

    @Override
    public void onUse(PlayerCharacter target, int value) {
        target.equipArmor(this);
        target.removeItemFromInventory(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Armor)) return false;
        return super.equals(obj);
    }
    
}
