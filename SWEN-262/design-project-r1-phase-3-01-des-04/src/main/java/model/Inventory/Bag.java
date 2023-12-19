package model.Inventory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import model.Items.ItemAttribute;

public class Bag extends Inventory{
    @JsonProperty("maxSize")
    private int maxSize;
    
    // Used for jackson deserialization
    protected Bag() {

    }

    public Bag(int maxSize) {
        super(new ItemAttribute[0]);
        this.maxSize = maxSize;
    }

    @Override
    public boolean recieveItem(ItemAttribute item) {
        if (! isFull()){
            return addItem(item);
        }
        return false;
    }

    @Override
    @JsonIgnore
    public int[] getItemCapacityRatio() {
        int arr[] = {inventory.size(), maxSize};
        return arr;
    }

    @JsonIgnore
    public boolean isFull(){
        if(getItemCapacityRatio()[0]/getItemCapacityRatio()[1] == 1){
            return true;
        } else return false;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Bag)) return false;

        Bag other = (Bag) obj;
        return super.equals(obj) && this.maxSize == other.maxSize;
    }
    
}
