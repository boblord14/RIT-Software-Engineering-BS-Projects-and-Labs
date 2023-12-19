package model.Tiles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import model.Character;
import model.PlayerCharacter;
import model.NonPlayerCharacter;

public class CharacterTile implements Tile {
    private int x;
    private int y;
    @JsonProperty("character")
    private Character chara;
    private String type;

    public CharacterTile(Character chara, int x, int y) {
        this.chara = chara;
        this.x = x;
        this.y = y;

        if (chara instanceof PlayerCharacter) {
            this.type = "playerCharacter";
        } else if (chara instanceof NonPlayerCharacter) {
            this.type = "nonPlayerCharacter";
        }
    }

    // Used for jackson deserialization
    private CharacterTile() {

    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CharacterTile)) return false;

        CharacterTile other = (CharacterTile) obj;
        return this.x == other.x
        && this.y == other.y
        && this.chara.equals(other.chara)
        && this.type.equals(type);
    }

    public String getType() {
        return this.type;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Character getCharacter() {
        return this.chara;
    }

    @JsonIgnore
    public String stringDes() {
        return chara.getName();
    }

    @JsonIgnore
    public String stringRep() {
        if (chara instanceof PlayerCharacter) {
            return "P";
        } else if (chara instanceof NonPlayerCharacter) {
            return "E";
        }
        return " ";
    }

    public void notifys() {

    }

    public boolean isPC(){
        return chara instanceof PlayerCharacter;
    }

    @Override
    public String toString() {
        return "Character";
    }

}