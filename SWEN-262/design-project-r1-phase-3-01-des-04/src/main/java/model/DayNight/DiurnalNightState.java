package model.DayNight;

import com.fasterxml.jackson.annotation.JsonTypeName;

import model.NonPlayerCharacter;

public class DiurnalNightState implements TimeOfDayState {
    @Override
    public void handleStatChange(NonPlayerCharacter nonPlayerCharacter) {
        // Diurnal creatures during the night: stats decrease by 10%.
        nonPlayerCharacter.adjustStats(0.90);
    }

    @Override
    public void notifyPlayer() {
        System.out.println("Day turns to night, and diurnal creature stats decrease by 10%!");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DiurnalNightState;
    }
}