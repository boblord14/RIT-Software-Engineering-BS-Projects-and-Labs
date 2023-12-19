package model;

import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import model.DayNight.DiurnalDayState;
import model.DayNight.DiurnalNightState;
import model.DayNight.NocturnalDayState;
import model.DayNight.NocturnalNightState;
import model.DayNight.TimeOfDayState;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")

public class NonPlayerCharacter extends Character {

    private static final Random RANDOM = new Random();
    @JsonProperty("nocturnal")
    private boolean nocturnal;
    @JsonProperty("currentState")
    private TimeOfDayState currentState;


    public NonPlayerCharacter(
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("nocturnal") boolean nocturnal,
            @JsonProperty("currentState") TimeOfDayState currentState
    ) {
        // name, description, health, attack, defense
        super(name, description, RANDOM.nextInt(150-50) + 50, RANDOM.nextInt(15-5) + 5, RANDOM.nextInt(10-0) + 10);
        this.nocturnal = nocturnal;
        this.currentState = currentState;

    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof NonPlayerCharacter)) return false;

        NonPlayerCharacter other = (NonPlayerCharacter) obj;
        return super.equals(obj)
        && this.nocturnal == other.nocturnal
        && this.currentState.equals(other.currentState);
    }

    public boolean getNocturnal() {
        return this.nocturnal;
    }

    public TimeOfDayState getTimeOfDayState() {
        return this.currentState;
    }

    public void changeState(boolean isDay) {
        TimeOfDayState newState;
        if (isDay == true) {
            if (this.nocturnal == true) { //day and nocturnal
                newState = new NocturnalDayState();
            }
            else {  //day and diurnal
                newState = new DiurnalDayState();
            }
        }
        else {
            if (this.nocturnal == true) { //night and nocturnal
                newState = new NocturnalNightState();
            }
            else {  //night and diurnal
                newState = new DiurnalNightState();
            }
        }
        this.currentState = newState;
        this.currentState.handleStatChange(this);
    }

    /**
     * Increases stats by 10% (rounded down) during the day if diurnal
     * Increases stats by 20% (rounded down) during night if nocturnal
     * 
     * If not during given day stats are reset
     * 
     * @param multiplier value that Health, Attack, and Defense stats are multiplied by
     */
    public void adjustStats(double multiplier) {
        setHealth((int)Math.floor(getHealth() * multiplier));
        setAttack((int)Math.floor(getAttack() * multiplier));
        setDefense((int)Math.floor(getDefense() * multiplier));
    }

}