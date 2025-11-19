package com.mycompany.calendarapp;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RecurringEvent extends MainEvent {

    private String recurrenceType;
    private int occurrences;       

    public RecurringEvent(int eventId, String title, String description,
                          LocalDateTime startDateTime, LocalDateTime endDateTime,
                          String recurrenceType, int occurrences) {
        super(eventId, title, description, startDateTime, endDateTime);
        this.recurrenceType = recurrenceType.toUpperCase();
        this.occurrences = occurrences;
    }

    public String getRecurrenceType() { return recurrenceType; }
    public int getOccurrences() { return occurrences; }

    public void setRecurrenceType(String recurrenceType) { this.recurrenceType = recurrenceType.toUpperCase(); }
    public void setOccurrences(int occurrences) { this.occurrences = occurrences; }

    public LocalDateTime getNextOccurrence(LocalDateTime current) {
        switch (recurrenceType) {
            case "DAILY": return current.plus(1, ChronoUnit.DAYS);
            case "WEEKLY": return current.plus(1, ChronoUnit.WEEKS);
            case "MONTHLY": return current.plus(1, ChronoUnit.MONTHS);
            default: return current;
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                ", RecurringEvent{" +
                "recurrenceType='" + recurrenceType + '\'' +
                ", occurrences=" + occurrences +
                '}';
    }
}

