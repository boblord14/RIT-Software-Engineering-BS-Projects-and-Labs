package model;

public enum InventoryInput {
    VIEW(118),
    DISCARD(100),
    USE(-1);

    private int keyCode;

    private InventoryInput(int keyCode) {
        this.keyCode = keyCode;
    }

    public static InventoryInput intToInventoryInput(int keyCode) {
        if(keyCode == VIEW.keyCode) return VIEW;
        if(String.valueOf(keyCode).matches("^" + DISCARD.keyCode + "[0]{3}\\d+$")) return DISCARD;
        if(keyCode - 1 < 0) return USE;
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(this.keyCode);
    }
}
