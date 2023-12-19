package controller.Input;

import model.Game;

public interface KeyComponent {
    public void register(KeyListener listener);
    public void deregister(KeyListener listener);
    public void notify(KeyPressEvent event, Game game);
}
