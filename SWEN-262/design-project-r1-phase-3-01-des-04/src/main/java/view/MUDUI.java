package view;

import model.Game;
import model.Inventory.Inventory;

public interface MUDUI {
    
    abstract void loginUser();
    abstract void openInventory(Inventory i1, Inventory i2);
    abstract void startGame(Game game);

}
