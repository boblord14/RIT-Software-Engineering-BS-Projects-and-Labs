package model.Tiles;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ObstacleTile implements Tile {
    private int x;
    private int y;
    @JsonProperty("name")
    private String name;

    public ObstacleTile(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    // Used for jackson deserialization
    private ObstacleTile() {

    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ObstacleTile)) return false;

        ObstacleTile other = (ObstacleTile) obj;
        return this.x == other.x 
        && this.y == other.y 
        && this.name.equals(other.name);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String stringDes() {
        return this.name;
    }

    public String stringRep() {
        return "W";
    }

    public void notifys() {

    }
    @Override
    public String toString() {
        return "Obstacle";
    }
}