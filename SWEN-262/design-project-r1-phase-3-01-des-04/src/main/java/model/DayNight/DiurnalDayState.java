package model.DayNight;

import com.fasterxml.jackson.annotation.JsonTypeName;

import model.NonPlayerCharacter;

public class DiurnalDayState implements TimeOfDayState {
    @Override
    public void handleStatChange(NonPlayerCharacter nonPlayerCharacter) {
        // Diurnal creatures during the day: stats increase by 10%.
        nonPlayerCharacter.adjustStats(1.10);
    }

    @Override
    public void notifyPlayer() {
        System.out.println("Night turns to day, and diurnal creature stats increase by 10%!");
    }

    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof DiurnalDayState;
    }
}