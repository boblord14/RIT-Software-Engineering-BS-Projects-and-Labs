package controller.Input;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import model.PlayerCharacter;
import model.Inventory.Inventory;
import model.Items.ItemAttribute;
import view.MUDGUI;

public class InventoryObserver {
    private GridPane invPane;
    private Button goldButton;
    private Inventory inv;
    private Inventory othInventory;
    private PlayerCharacter pc;

    public InventoryObserver(GridPane invPane, Button goldButton, Inventory inv, Inventory othInventory,
            PlayerCharacter pc) {
        this.invPane = invPane;
        this.goldButton = goldButton;
        this.inv = inv;
        this.othInventory = othInventory;
        this.pc = pc;
    }

    public void refreshInventory(){
        invPane.getChildren().clear();
        int col = 0;
        int row = 0;
        for (ItemAttribute item : inv.getInventory()) {
            if (item != null){
                invPane.add(MUDGUI.makeInventoryButton(item, inv, othInventory, pc), col, row);
                if (++col == MUDGUI.INVENTORYCOLCOUNT){
                    col = 0;
                    row++;
                }
            }
        }
        refreshGold();
    }

    public void refreshGold(){
        goldButton.setText(Integer.toString(inv.getGold()));
    }
}
