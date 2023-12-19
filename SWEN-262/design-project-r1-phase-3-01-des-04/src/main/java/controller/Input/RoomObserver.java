package controller.Input;

import model.Inventory.Inventory;
import view.MUDGUI;

public class RoomObserver {
    
    private MUDGUI gui;
    
    public RoomObserver(MUDGUI gui) {
        this.gui = gui;
    }

    public void openInventory(Inventory i1, Inventory i2){
        gui.openInventory(i1, i2);
    }

}
