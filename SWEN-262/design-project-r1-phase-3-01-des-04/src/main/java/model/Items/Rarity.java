package model.Items;

public enum Rarity {
    Decent(1),
    Uncommon(2),
    Rare(3),
    Epic(4),
    Perfect(5),
    Legendary(6),
    Impossible(7),
    Unfathomable(8);



    private int valueMod;
    private Rarity(int mod){ valueMod = mod; }
    public int getMod(){ return valueMod; }

    public static Rarity ParseString(String str){
        for (Rarity rarity : Rarity.values()) {
            if (str.equalsIgnoreCase(rarity.toString())){
                return rarity;
            }
        }
        return null;
    }
}
