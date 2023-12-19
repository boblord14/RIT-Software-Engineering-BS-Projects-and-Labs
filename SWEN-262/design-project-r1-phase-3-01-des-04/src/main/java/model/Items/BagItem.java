package model.Items;

import model.PlayerCharacter;

public class BagItem extends BaseItem{

    // Used for jackson deserialization
    private BagItem() {

    }

    public BagItem(int baseValue) {
        super(baseValue, "Bag", "This is for holding things");
    }

    @Override
    public void onUse(PlayerCharacter target, int value) {
        target.getInventory().addBag(value);
        target.removeItemFromInventory(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BagItem)) return false;
        return super.equals(obj);
    }
    
}
