package model.Tiles.TrapObserver;

import java.util.Random;

import model.Tiles.TrapTile;

public class DetectTrap extends Observer {
    // Used for jackson deserialization
    private DetectTrap() {

    }

    public DetectTrap(TrapTile traptile) {
        super(traptile);
    }

    public void update() {
        Random rand = new Random();
        int dis = rand.nextInt(2);
        if (dis == 0) {
            System.out.println("DetectedTrap");
            this.tile.setDetect(true);
        } else {
            tile.setAttempt(true);
            this.tile.setDetect(false);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DetectTrap)) return false;
        return super.equals(obj);
    }
}
