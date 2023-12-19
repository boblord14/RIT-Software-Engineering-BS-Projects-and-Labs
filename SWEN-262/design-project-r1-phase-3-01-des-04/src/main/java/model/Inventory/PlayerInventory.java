package model.Inventory;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import model.Items.BaseItem;
import model.Items.ItemAttribute;

public class PlayerInventory extends Inventory {

    static final int MAX_BAG_COULNT = 6;
    @JsonProperty("bags")
    private Bag[] bagInventory;

    public PlayerInventory() {
        super(new ItemAttribute[0]);
        bagInventory = new Bag[MAX_BAG_COULNT]; // stores inventory objects(bags). Hard cap of 6 bags.
        bagInventory[0] = new Bag(6); // player starts with an empty bag
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerInventory))
            return false;

        PlayerInventory other = (PlayerInventory) obj;
        return super.equals(obj) && Arrays.equals(this.bagInventory, other.bagInventory);
    }

    @Override
    public boolean recieveItem(ItemAttribute item) {
        for (Bag bag : bagInventory) {
            if (bag != null && bag.recieveItem(item)) {
                if (observer != null) {
                    observer.refreshInventory();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean transferItem(ItemAttribute item, Inventory other) {
        for (Bag bag : bagInventory) {
            if (bag != null && bag.transferItem(item, other)) {
                if (observer != null) {
                    observer.refreshInventory();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    @JsonIgnore
    public ItemAttribute[] getInventory() {
        ItemAttribute[] fullInventory = new ItemAttribute[getItemCapacityRatio()[1]];
        int index = 0;
        for (int i = 0; i < getBagCount(); i++) {
            System.arraycopy(bagInventory[i].getInventory(), 0, fullInventory, index,
                    bagInventory[i].getItemCapacityRatio()[0]);
            index = index + bagInventory[i].getItemCapacityRatio()[0];
        }
        return fullInventory;
    }

    @Override
    @JsonIgnore
    public int[] getItemCapacityRatio() {
        int totalItems = 0;
        int totalCapacity = 0;
        for (int i = 0; i < getBagCount(); i++) {
            totalItems = totalItems + bagInventory[i].getItemCapacityRatio()[0];
            totalCapacity = totalCapacity + bagInventory[i].getItemCapacityRatio()[1];
        }
        int arr[] = { totalItems, totalCapacity };
        return arr;
    }

    @Override
    @JsonIgnore
    public int getValue() {
        int value = getGold();
        for (int i = 0; i < getBagCount(); i++) {
            value = value + bagInventory[i].getValue();
        }
        return value;
    }

    public int addBag(int size) {
        if (getBagCount() == MAX_BAG_COULNT) {
            for (int i = 0; i < MAX_BAG_COULNT; i++) {
                if (bagInventory[i].getItemCapacityRatio()[1] < size) {
                    ItemAttribute[] tempInventory = bagInventory[i].getInventory();
                    bagInventory[i] = new Bag(size);
                    for (int j = 0; j < tempInventory.length; j++) {
                        bagInventory[i].recieveItem(tempInventory[j]);
                    }
                    return 1;
                }
            }
            return -1; // bag to add is smaller than all existing bags and theres no room for a new one
        } else {
            bagInventory[getBagCount()] = new Bag(size);
            return 1;
        }
    }

    @JsonIgnore
    public int getBagCount() {
        int bagCount = 0;
        for (int i = 0; i < bagInventory.length; i++) {
            if (bagInventory[i] != null) {
                bagCount++;
            }
        }
        return bagCount;
    }

    @JsonIgnore()
    public Bag[] getBags() {
        return bagInventory;
    }

    @Override
    public boolean conatins(ItemAttribute item) {
        for (Bag bag : bagInventory) {
            if (bag.conatins(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemAttribute baseItemRoot(BaseItem item) {
        ItemAttribute retAttribute = null;
        for (Bag bag : bagInventory) {
            retAttribute = bag.baseItemRoot(item);
            if (retAttribute != null) {
                break;
            }
        }
        return retAttribute;
    }

}
