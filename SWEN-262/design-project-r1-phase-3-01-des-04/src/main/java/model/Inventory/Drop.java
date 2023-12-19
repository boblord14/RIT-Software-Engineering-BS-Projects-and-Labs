package model.Inventory;

import model.Items.ItemAttribute;

public class Drop extends Inventory{

    public Drop() {
        super(new ItemAttribute[0]);
    }

    public Drop(ItemAttribute[] items) {
        super(items);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Drop)) return false;
        return super.equals(obj);
    }

}
