package model.Save;

import model.Tiles.EmptyTile;
import model.map.Map;
import model.PlayerCharacter;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class MapProfile {
    @JsonProperty("playerCharacter")
    private HashMap<String, PlayerCharacter> charData;
    @JsonProperty("map")
    private Map map;

    
    public MapProfile(
         HashMap<String, PlayerCharacter> charData,
         Map map
    ) {
        this.charData = charData;
        this.map = map;
        PlayerCharacter charToSave = this.charData.get(this.map.getPlayerCharacter().getName());
        charToSave.setRoomId(this.map.getCurrentRoomId());
        charToSave.setxCoord(this.map.getCurrentPlayerRoom().getCharacterTile().getX());
        charToSave.setyCoord(this.map.getCurrentPlayerRoom().getCharacterTile().getY());
        this.map.getCurrentPlayerRoom().prepForSaving();
    }
    public MapProfile(){

    }



    @JsonProperty
    public void setCharDataHashMap(HashMap<String, PlayerCharacter> newHashMap) {
        this.charData = newHashMap;
    }

    @JsonProperty
    public void setMap(Map newMap) {
        this.map = newMap;
    }

    @JsonProperty
    public HashMap<String, PlayerCharacter> getCharDataHashMap() {
        return this.charData;
    }

    @JsonProperty
    public void addCharData(UserProfile userData, PlayerCharacter character){
        charData.put(userData.getName(), character);
    }

    @JsonProperty
    public PlayerCharacter validateAndGetChar(UserProfile userData){
        return charData.get(userData.getName());
    }

    @JsonProperty
    public Map getMap() {
        return this.map;
    }
}
