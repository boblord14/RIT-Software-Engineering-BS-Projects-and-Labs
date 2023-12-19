package model;

public enum Direction {
    UP (87, 1),
    DOWN (83, 3),
    LEFT (65, 0),
    RIGHT (68, 2);

    private final int keyCode;
    private final int value;
    
    private Direction(int keyCode, int value) {
        this.keyCode = keyCode;
        this.value = value;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public int getVal(){
        return value;
    }

    public static Direction intToDirection(int keyCode) {
        if(keyCode == UP.keyCode) return UP;
        if(keyCode == DOWN.keyCode) return DOWN;
        if(keyCode == LEFT.keyCode) return LEFT;
        if(keyCode == RIGHT.keyCode) return RIGHT;

        return null;
    }
}
