package model.Tiles.TrapObserver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import model.Tiles.TrapTile;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DetectTrap.class, name = "DetectTrap"),
        @JsonSubTypes.Type(value = DisarmTrap.class, name = "DisarmTrap"),
        @JsonSubTypes.Type(value = TriggerTrap.class, name = "TriggerTrap"),
})
public abstract class Observer {
    protected TrapTile tile;

    public void update() {
    }

    public Observer(TrapTile tile) {
        this.tile = tile;
    }

    public Observer() {}

    @JsonIgnore()
    public void setTile(TrapTile tile) {
        this.tile = tile;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Observer)) return false;

        Observer other = (Observer) obj;
        // Will infinitely loop if you check equals with observer
        return this.tile.equalsWithNoObserver(other.tile);
    }
}