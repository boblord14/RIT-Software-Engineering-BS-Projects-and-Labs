package controller.Input;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import model.Tiles.CharacterTile;
import model.Tiles.Tile;
import view.MUDGUI;

public class TileChanger {
    
    private Button b;

    public TileChanger(Button b) {
        this.b = b;
    }

    public void onChange(Tile newTile){
        Node n = b.getGraphic();
        Rectangle r = null;
        if (n instanceof Rectangle){
            r = ((Rectangle)n);
        }
        r.setFill(MUDGUI.tileToColor(newTile));
        if (newTile instanceof CharacterTile){
            CharacterTile cTile = ((CharacterTile)newTile);
            b.setTooltip(MUDGUI.makeCharacterTooltip(cTile.getCharacter()));
        } else {
            b.setTooltip(null);
        }
    }

}
