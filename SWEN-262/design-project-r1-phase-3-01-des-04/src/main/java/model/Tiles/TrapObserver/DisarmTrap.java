package model.Tiles.TrapObserver;
import java.util.Random;

import model.Tiles.TrapTile;

public class DisarmTrap extends Observer{
    // Used for jackson deserialization
    private DisarmTrap(){

    }
    public DisarmTrap(TrapTile traptile){
        super(traptile);
    }
    public void update(){
        Random rand = new Random();
        if(this.tile.getDetected()==true){
            int dis = rand.nextInt(2);
            if(dis == 0){
                System.out.println("Disarmed Trap");
                this.tile.setDisarm(true);
            }
            else{
                this.tile.setDisarm(false);
                this.tile.notifyTrigger();
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DisarmTrap)) return false;
        return super.equals(obj);
    }

}