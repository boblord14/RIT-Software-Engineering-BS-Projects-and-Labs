package model.Tiles;

public class EmptyTile implements Tile {
    private int x;
    private int y;

    public EmptyTile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Used for jackson deserialization
    private EmptyTile() {

    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof EmptyTile)) return false;

        EmptyTile other = (EmptyTile) obj;
        return this.x == other.x
        && this.y == other.y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String stringDes() {
        return null;
    }

    public String stringRep() {
        return "O";
    }

    public void notifys() {

    }

    @Override
    public String toString() {
        return "Empty";
    }
}