package model;

public enum Stat {
    attack,
    defense,
    health;

    public static Stat ParseString(String str){
        for (Stat stat : Stat.values()) {
            if (str.equalsIgnoreCase(stat.toString())){
                return stat;
            }
        }
        return null;
    }
}
