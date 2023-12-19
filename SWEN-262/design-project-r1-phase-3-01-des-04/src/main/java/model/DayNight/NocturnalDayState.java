package model.DayNight;

import com.fasterxml.jackson.annotation.JsonTypeName;

import model.NonPlayerCharacter;

public class NocturnalDayState implements TimeOfDayState {
    @Override
    public void handleStatChange(NonPlayerCharacter nonPlayerCharacter) {
        // Nocturnal creatures during the day: stats decrease by 20%.
        nonPlayerCharacter.adjustStats(0.80);
    }

    @Override
    public void notifyPlayer() {
        System.out.println("Night turns to day, and nocturnal creature stats decrease by 20%!");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NocturnalDayState;
    }
}