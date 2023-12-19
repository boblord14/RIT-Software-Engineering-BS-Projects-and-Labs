package model.Tiles;

public class ExitTile implements Tile {
    private int x;
    private int y;
    private int exitTowards;

    public ExitTile(int exitTowards, int x, int y) {
        this.x = x;
        this.y = y;
        this.exitTowards = exitTowards;
    }

    // Used for jackson deserialization
    public ExitTile() {

    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ExitTile)) return false;

        ExitTile other = (ExitTile) obj;
        return this.x == other.x
        && this.y == other.y
        && this.exitTowards == other.exitTowards;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getExitTowards() {
        return this.exitTowards;
    }

    public String stringDes() {
        return null;
    }

    public String stringRep() {
        return "R";
    }

    public void notifys() {

    }

    @Override
    public String toString() {
        return "Exit";
    }
}
