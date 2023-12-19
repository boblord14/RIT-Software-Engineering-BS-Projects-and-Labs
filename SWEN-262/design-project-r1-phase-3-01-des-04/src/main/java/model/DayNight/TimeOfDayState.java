package model.DayNight;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import model.NonPlayerCharacter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DiurnalDayState.class, name = "DiurnalDayState"),
        @JsonSubTypes.Type(value = DiurnalNightState.class, name = "DiurnalNightState"), 
        @JsonSubTypes.Type(value = NocturnalDayState.class, name = "NocturnalDayState"), 
        @JsonSubTypes.Type(value = NocturnalNightState.class, name = "NocturnalNightState")
    })
public interface TimeOfDayState {
    void handleStatChange(NonPlayerCharacter nonPlayerCharacter);
    void notifyPlayer();
}