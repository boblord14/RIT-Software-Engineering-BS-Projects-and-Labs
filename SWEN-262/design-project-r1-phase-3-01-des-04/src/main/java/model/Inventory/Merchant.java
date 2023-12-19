package model.Inventory;

import model.Items.ItemAttribute;

public class Merchant extends Inventory {

    public Merchant() {
        super(new ItemAttribute[0]);
    }

    public Merchant(ItemAttribute[] items) {
        super(items);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Merchant)) return false;
        return super.equals(obj);
    }

    @Override
    public boolean transferItem(ItemAttribute item, Inventory other) {
        boolean result = false;
        if((other.getGold()>item.getValue())|(other instanceof Merchant)|(other instanceof Drop)){
        if (this.conatins(item) && other.recieveItem(item)){
            result = this.removeItem(item);
            if(other instanceof PlayerInventory){
            other.transferGold(item.getValue(), this);}
            if (observer != null){
                observer.refreshInventory();
            }
        }}
        return result;
    }
}
