package model.Save;

import com.fasterxml.jackson.annotation.JsonProperty;
import model.PlayerCharacter;
import model.map.Map;

public class UserProfile {

    private String name;
    private String pass;
    private String desc;
    private int wins;
    private int losses;
    
    //store username, password, and description, nothing else
    public UserProfile(
        @JsonProperty("name") String name,
            @JsonProperty("pass") String pass,
            @JsonProperty("desc") String desc,
            @JsonProperty("wins") int wins,
            @JsonProperty("losses") int losses
    ) {
        this.name = name;
        this.pass = pass;
        this.desc = desc;
        this.wins = wins;
        this.losses = losses;
    }

    public int getWins() {
        return wins;
    }
    public void setWins(int wins) {
        this.wins = wins;
    }


    public int getLosses() {
        return losses;
    }
    public void setLosses(int losses) {
        this.losses = losses;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
