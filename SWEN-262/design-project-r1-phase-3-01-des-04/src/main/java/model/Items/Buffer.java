package model.Items;

import model.PlayerCharacter;
import model.Stat;

public class Buffer extends BaseItem{

    private Stat buffStat;

    public Buffer(int baseValue, String name, String description, Stat buffStat) {
        super(baseValue, name, description);
        this.buffStat = buffStat;
    }

    private Buffer() {
        
    }

    @Override
    public void onUse(PlayerCharacter target, int value) {
        target.addBuff(new Buff(value, buffStat));
        target.removeItemFromInventory(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Buffer)) return false;

        Buffer other = (Buffer) obj;
        return super.equals(obj) 
        && this.buffStat == other.buffStat;
    }
    
}
