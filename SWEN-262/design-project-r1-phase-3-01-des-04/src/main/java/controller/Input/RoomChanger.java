package controller.Input;

import view.MUDGUI;

public class RoomChanger {
    
    private MUDGUI gui;

    public RoomChanger(MUDGUI gui) {
        this.gui = gui;
    }

    public void onChange(){
        gui.renderRoom();
    }

}
