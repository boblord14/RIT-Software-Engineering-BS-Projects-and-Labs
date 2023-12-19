package model.Items;

import model.PlayerCharacter;

public class Food extends BaseItem{

    // Used for jackson deserialization
    public Food() {

    }

    public Food(int baseValue, String name, String description) {
        super(baseValue, name, description);
    }

    @Override
    public void onUse(PlayerCharacter target, int value) {
        target.setHealth(target.getHealth() + value);
        target.removeItemFromInventory(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Food)) return false;
        return super.equals(obj);
    }

}
