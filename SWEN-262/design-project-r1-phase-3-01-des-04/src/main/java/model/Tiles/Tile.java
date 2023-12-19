package model.Tiles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CharacterTile.class, name = "CharacterTile"),
        @JsonSubTypes.Type(value = ChestTile.class, name = "ChestTile"), 
        @JsonSubTypes.Type(value = EmptyTile.class, name = "EmptyTile"), 
        @JsonSubTypes.Type(value = ExitTile.class, name = "ExitTile"), 
        @JsonSubTypes.Type(value = ObstacleTile.class, name = "ObstacleTile"), 
        @JsonSubTypes.Type(value = MerchantTile.class, name = "MerchantTile"),
        @JsonSubTypes.Type(value = TrapTile.class, name = "TrapTile") 
    })
public interface Tile {
    public void notifys();

    public int getX();

    public int getY();

    public String stringDes();

    public String stringRep();
}