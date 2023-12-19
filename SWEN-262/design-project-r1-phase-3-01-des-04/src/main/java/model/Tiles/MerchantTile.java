package model.Tiles;

import model.Inventory.*;
import model.Character;
import model.Items.ItemAttribute;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MerchantTile extends ChestTile {
    private int x;
    private int y;
    @JsonProperty("mech")
    private Merchant mech;


    public MerchantTile(Merchant mech,int x, int y){
        super(mech, x, y);
        this.mech = mech;
    }

    @JsonCreator
    private MerchantTile(){
    }

    public void selItem(Character player, int itemNum){
        PlayerInventory pl = player.getInventory();
        ItemAttribute[] pla = pl.getInventory();
        ItemAttribute it = null;
        try {
            it = pla[itemNum];
        } catch (Exception e) {
            //item doesnt exist
            return;
        }
        pl.recieveGold(it.getValue());
        pl.transferItem(it,mech);
    }

    public void purchItem(Character player, int itemNum){
        PlayerInventory pl = player.getInventory();
        ItemAttribute[] mecha = mech.getInventory();
        ItemAttribute it = null;
        try {
            it = mecha[itemNum];
        } catch (Exception e) {
            //item doesnt exist
            return;
        }
        if(pl.getGold()>it.getValue()){
            mech.transferItem(it,pl);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof MerchantTile)) return false;

        
        MerchantTile other = (MerchantTile) obj;
        System.out.println("---------------------------");
        return this.x == other.x 
        && this.y == other.y 
        && this.mech.equals(other.mech);
    }

    @Override
    public void notifys() {
        }

    @Override
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public String stringDes() {
        return "Merchant";
        }

    @Override
    public String stringRep() {
        return "M";    
    }
    
}
