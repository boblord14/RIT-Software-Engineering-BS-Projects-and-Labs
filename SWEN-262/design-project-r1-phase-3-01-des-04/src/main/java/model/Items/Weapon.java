package model.Items;

import model.PlayerCharacter;

public class Weapon extends BaseItem{

    // Used for jackson deserialization
    private Weapon() {

    }

    public Weapon(int baseValue, String name, String description) {
        super(baseValue, name, description);
    }

    @Override
    public void onUse(PlayerCharacter target, int value) {
        target.equipWeapon(this);
        target.removeItemFromInventory(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Weapon)) return false;
        return super.equals(obj);
    }
    
}
