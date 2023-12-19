package controller.Input;

import java.util.ArrayList;
import java.util.List;

import model.Game;

public class KeyEmitter implements KeyComponent {

    private List<KeyListener> listeners;

    public KeyEmitter() {
        this.listeners = new ArrayList<>();
    }

    /**
     * Adds the KeyListener to the observer list
     * 
     * @param The listener being added
     */
    @Override
    public void register(KeyListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Removes a KeyListener from observing player input
     * 
     * @param listener The KeyListener being removed from the observers list
     */
    @Override
    public void deregister(KeyListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Notifies all observers listening to player inputs
     * 
     * @param event The key event being broadcasted
     */
    @Override
    public void notify(KeyPressEvent event, Game game) {
        for(KeyListener listener: listeners) {
            listener.keyPressed(event, game);
        }
    }
    
}
