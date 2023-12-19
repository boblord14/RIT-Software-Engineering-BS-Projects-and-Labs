package controller.Input;

import model.Game;
import model.InventoryInput;
import model.PlayerCharacter;
import model.Room;
import model.Inventory.Bag;
import model.Inventory.PlayerInventory;
import model.Items.ItemAttribute;

public class InventoryHandler implements KeyListener {

    @Override
    public void keyPressed(KeyPressEvent event, Game game) {
        int keyPressed = event.getKeyPressed();
        InventoryInput invInput = InventoryInput.intToInventoryInput(keyPressed);

        if (invInput == null)
            return;

        Room room = game.getCurrentPlayerRoom();
        PlayerCharacter character = (PlayerCharacter) room.getCharacterTile().getCharacter();
        PlayerInventory inv = character.getInventory();

        if (invInput == InventoryInput.VIEW) {
            if (inv.getItemCapacityRatio()[0] == 0) {
                System.err.println("Your inventory is empty!");
                return;
            }

            int listIndex = 0;
            for (int bagIndex = 0; bagIndex < inv.getBags().length; bagIndex++) {
                Bag bag = inv.getBags()[bagIndex];
                if(bag == null) continue;

                int[] capacityRatio = bag.getItemCapacityRatio();
                System.out.printf("Bag %d - Capacity: %d/%d - Gold Value: %d\n", bagIndex, capacityRatio[0], capacityRatio[1], bag.getValue());
                listIndex = printBag(bag, listIndex);
            }

            int[] capacityRatio = inv.getItemCapacityRatio();
            System.out.printf("\nInventory Statistics: Capacity: %d/%d - Gold Value: %d\n", capacityRatio[0], capacityRatio[1], inv.getValue());
            return;
        }

        // Indices are negated on use inputs to prevent conflicts
        // Discard inputs are in the following format 100000(index here) [100 is ASCII
        // for D, 000 is a deliminator]
        int index = invInput == InventoryInput.DISCARD ? getDiscardIndex(String.valueOf(keyPressed)) : -keyPressed;
        try {
            ItemAttribute item = inv.getInventory()[index];

            if (invInput == InventoryInput.DISCARD) {
                character.removeItemFromInventory(item);
                System.out.printf("Successfully removed %s from your inventory!\n", item.getName());
                return;
            }

            item.onUse(character);
            System.out.printf("Successfully used %s!\n", item.getName());
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.err.printf("There is no item associated with the number %d!\n", index);
        }

    }

    /**
     * Prints a bags items in the following format index - (itemType) itemName - itemDesc format
     * @param bag Bag being printed
     * @param listIndex Where to start the listIndex from (listIndex is used to select items)
     * @return The new listIndex
     */
    private int printBag(Bag bag, int listIndex) {
        for (int itemIndex = 0; itemIndex < bag.getInventory().length; itemIndex++) {
            ItemAttribute item = bag.getInventory()[itemIndex];
            if (item == null)
                break;

            String itemType = bag.getInventory()[itemIndex].getClass().getSimpleName();
            String itemDescription = item.getDescription();
            itemDescription = itemDescription == null || itemDescription.isBlank() ? "N/A" : itemDescription;

            System.out.printf("\t%d - (%s) %s - %s\n", listIndex, itemType, item.getName(), item.getDescription());
            listIndex++;
        }

        return listIndex;
    }

    private int getDiscardIndex(String keyPressed) {
        return Integer.parseInt(keyPressed.replace(InventoryInput.DISCARD + "000", ""));
    }

}
