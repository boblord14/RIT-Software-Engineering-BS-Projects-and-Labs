package controller.Input;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import model.Direction;
import model.Game;

public class MoveHandler implements KeyListener, EventHandler<KeyEvent>{

    @Override
    public void keyPressed(KeyPressEvent event, Game game) {
        Direction direction = Direction.intToDirection(event.getKeyPressed());
        if (direction == null) return;
        
        game.move(direction);
    }

    @Override
    public void handle(KeyEvent event) {
        // TODO Auto-generated method stub
    }

    
}
