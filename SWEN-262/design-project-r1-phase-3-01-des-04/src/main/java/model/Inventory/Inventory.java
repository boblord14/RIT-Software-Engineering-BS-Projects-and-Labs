package model.Inventory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

import controller.Input.InventoryObserver;
import model.Items.BaseItem;
import model.Items.ItemAttribute;
import model.Items.ItemModifier;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Bag.class, name = "Bag"),
        @JsonSubTypes.Type(value = Drop.class, name = "Drop"),
        @JsonSubTypes.Type(value = Merchant.class, name = "Merchant"),
        @JsonSubTypes.Type(value = PlayerInventory.class, name = "PlayerInventory"),
})
public abstract class Inventory{

    @JsonProperty("inventory")
    protected List<ItemAttribute> inventory;
    @JsonProperty("gold")
    private int gold;
    protected InventoryObserver observer;

    public Inventory() {
    }

    public Inventory(ItemAttribute[] items) {
        this.inventory = new ArrayList<>();
        for (ItemAttribute item : items) {
            this.recieveItem(item);
        }
        gold = 0;
    }



    /**
     * this is the private add method that will always add to inventory
     * this is here mostly so observers will always trigger use getItem
     * @param item the item to be added
     * @return wether the add was successful
     */
    protected boolean addItem(ItemAttribute item) {
        inventory.add(item);
        if (observer != null){
            observer.refreshInventory();
        }
        return true;
    }

    /**
     * this is the private remove method that will always add to inventory
     * this is here mostly so observers will always trigger use transferItem
     * @param item the item to be added
     * @return wether the add was successful
     */
    protected boolean removeItem(ItemAttribute item) {
        if (item instanceof BaseItem){
            item = baseItemRoot((BaseItem)item);
        }
        if (conatins(item)){
            inventory.remove(item);
            if (observer != null){
                observer.refreshInventory();
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean recieveItem(ItemAttribute item){
        return addItem(item);
    }

    public ItemAttribute[] getInventory() {
        ItemAttribute[] arr = new ItemAttribute[inventory.size()];
        inventory.toArray(arr);
        return arr;
    }

    @JsonIgnore
    public int[] getItemCapacityRatio() {
        int arr[] = {inventory.size(),inventory.size()};//fully filled inventory
        return arr;
    } // returns an array with 2 slots, one for current item count, one for total. 
    
    @JsonIgnore
    public int getValue() {
        ItemAttribute[] currentItems = getInventory();
        int goldValue = getGold();
        for(int i=0;i<getItemCapacityRatio()[0];i++){
            goldValue = goldValue + currentItems[i].getGold();
        }
        return goldValue;
    }

    public int getGold() {
        return gold;
    }

    public boolean transferGold(int amount, Inventory other) {
        if (gold >= amount){
            gold -= amount;
            other.recieveGold(amount);
            if (observer != null) {
                observer.refreshGold();
            }
            return true;
        } 
        return false;
    }

    public void recieveGold(int amount){
        this.gold += amount;
        if (observer != null) {
                observer.refreshGold();
        }
    }

    public boolean transferItem(ItemAttribute item, Inventory other) {
        boolean result = false;
        if (this.conatins(item) && other.recieveItem(item)){
            result = this.removeItem(item);
            if (observer != null){
                observer.refreshInventory();
            }
        }
        return result;
    }

    public boolean conatins(ItemAttribute item) {
        if (item instanceof BaseItem){
            item = baseItemRoot((BaseItem)item);
        }
        return inventory.contains(item);
    }

    public void registerObserver(InventoryObserver o){
        observer = o;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Inventory)) return false;

        Inventory other = (Inventory) obj;

        return this.gold == other.gold 
        && this.inventory.equals(other.inventory); 
    }

    public ItemAttribute baseItemRoot(BaseItem item){
        for (ItemAttribute itemAttribute : inventory) {
            ItemAttribute curCheck = itemAttribute;
            while(! (curCheck instanceof BaseItem)){
                if (curCheck instanceof ItemModifier){
                    curCheck = ((ItemModifier)curCheck).getNext(); 
                }
            }
            if ((BaseItem)curCheck == item){
                return itemAttribute;
            }
        }
        return null;
    }
}