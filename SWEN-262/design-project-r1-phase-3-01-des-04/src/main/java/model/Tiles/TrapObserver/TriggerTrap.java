package model.Tiles.TrapObserver;
import model.Tiles.TrapTile;

public class TriggerTrap extends Observer{
    // Used for jackson deserialization
    private TriggerTrap() {

    }
    public TriggerTrap(TrapTile traptile){
        super(traptile);
    }
    public void update(){
        System.out.println("Attacked by trap");
        this.tile.attack();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TriggerTrap)) return false;
        return super.equals(obj);
    }

}