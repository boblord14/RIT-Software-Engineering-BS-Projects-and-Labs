package model.Items;

import model.Stat;

public class Buff {
    public int remainingTurn;
    public int value;
    public Stat stat;
    public static int BUFFLENGTH = 10;

    public Buff(int value, Stat stat) {
        this.value = value;
        this.stat = stat;
        remainingTurn = BUFFLENGTH;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Buff)) return false;

        Buff other = (Buff) obj;
        return super.equals(obj) 
        && this.remainingTurn == other.remainingTurn 
        && this.value == other.value
        && this.stat == other.stat;
    }

    /**
     * @return true if the buff should be over
     */
    public boolean incturn(){
        remainingTurn -= 1;
        return remainingTurn == 0;
    }

    public int getValue(){
        return value;
    }

    public Stat getStat() {
        return stat;
    }

}
