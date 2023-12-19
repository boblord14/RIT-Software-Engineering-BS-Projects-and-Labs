package model.Tiles;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import model.Tiles.TrapObserver.*;
import model.Character;

public class TrapTile implements Tile {
    @JsonProperty("observers")
    private List<Observer> observers;
    @JsonProperty("attack")
    private int attack;
    private boolean detected = false;
    private boolean attempt = false;
    private boolean disarmed = false;
    @JsonProperty("player")
    private Character player;
    private int x;
    private int y;

    public TrapTile(int attack, int x, int y) {
        this.attack = attack;
        this.x = x;
        this.y = y;
        this.observers = new ArrayList<>(3);
        this.observers.add(new DetectTrap(this));
        this.observers.add(new TriggerTrap(this));
        this.observers.add(new DisarmTrap(this));
    }

    // Used for jackson deserialization
    private TrapTile() {
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TrapTile)) return false;

        TrapTile other = (TrapTile) obj;
        return this.observers.equals(other.observers)
        && equalsWithNoObserver(other);
    }

    public boolean equalsWithNoObserver(TrapTile other) {
        return this.attack == other.attack
        && this.detected == other.detected
        && this.attempt == other.attempt
        && this.disarmed == other.disarmed
        && ((this.player == null && other.player == null) || this.player.equals(other.player))
        && this.x == other.x
        && this.y == other.y;
    }

    public boolean hasObserver(Observer observer) {
        return this.observers.contains(observer);
    }

    // Used for jackson deserialization
    public void fixObservers() {
        this.observers.forEach(obs -> obs.setTile(this));
    }

    public void setPlayer(Character player) {
        this.player = player;
    }

    public void notifys() {

    }

    public boolean getAttempt() {
        return this.attempt;
    }

    public void setAttempt(boolean attempt) {
        this.attempt = attempt;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void attack() {
        int damage = this.attack - player.getDefense();
        player.modifyHealth(-damage);
        System.out.printf("The trap dealt %d damage! You have %d health left!\n", damage, player.getHealth());
    }

    public boolean getDetected() {
        return detected;
    }

    public boolean getDisarmed() {
        return disarmed;
    }

    public void setDisarm(boolean set) {
        this.disarmed = set;
    }

    public void setDetect(boolean set) {
        this.detected = set;
    }

    public String stringDes() {
        return null;
    }

    public String stringRep() {
        if (this.detected == true) {
            return "T";
        } else if (this.disarmed == true) {
            return "D";
        } else {
            return "I";
        }
    }

    @JsonIgnore
    public Character getCharacter() {
        return this.player;
    }

    public void notifyTrigger() {
        for (Observer ob : observers) {
            if (ob instanceof TriggerTrap) {
                ob.update();
            }
        }
    }

    public void notifyDisarm() {
        for (Observer ob : observers) {
            if (ob instanceof DisarmTrap) {
                ob.update();
            }
        }
    }

    public void notifyDetect() {
        for (Observer ob : observers) {
            if (ob instanceof DetectTrap) {
                ob.update();
            }
        }
    }

    public void setPlayer() {
    }

    @Override
    public String toString() {
        return "Trap";
    }
}