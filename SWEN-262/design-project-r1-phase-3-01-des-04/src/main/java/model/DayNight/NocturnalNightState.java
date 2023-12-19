package model.DayNight;

import com.fasterxml.jackson.annotation.JsonTypeName;

import model.NonPlayerCharacter;

public class NocturnalNightState implements TimeOfDayState {
    @Override
    public void handleStatChange(NonPlayerCharacter nonPlayerCharacter) {
        // Nocturnal creatures during the night: stats increase by 20%.
        nonPlayerCharacter.adjustStats(1.20);
    }

    @Override
    public void notifyPlayer() {
        System.out.println("Day turns to night, and nocturnal creature stats increase by 20%!");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NocturnalNightState;
    }
}