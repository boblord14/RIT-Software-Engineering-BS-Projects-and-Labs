package model.Tiles;

import model.Inventory.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import model.Character;
import model.Items.ItemAttribute;

public class ChestTile implements Tile {
    private int x;
    private int y;
    @JsonProperty("chest")
    private Inventory chest;

    public ChestTile(Inventory chest, int x, int y) {
        this.chest = chest;
        this.x = x;
        this.y = y;
    }

    // Used for jackson deserialization
    protected ChestTile() {

    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ChestTile)) return false;

        
        ChestTile other = (ChestTile) obj;
        System.out.println("---------------------------");
        return this.x == other.x 
        && this.y == other.y 
        && this.chest.equals(other.chest);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void giveChest(Character player) {
        ItemAttribute[] items = chest.getInventory();
        for (ItemAttribute item : items) {
            chest.transferItem(item, player.getInventory());
        }
    }

    public Inventory getInventory(){
        return chest;
    }

    public String stringDes() {
        return "chest";
    }

    public String stringRep() {
        return "C";
    }

    public void notifys() {

    }

    @Override
    public String toString() {
        return "Chest";
    }
}