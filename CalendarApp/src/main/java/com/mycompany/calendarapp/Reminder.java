package com.mycompany.calendarapp;

/**
 * Reminder Class
 * 
 * This class represents a reminder for an event. It stores how many minutes
 * before an event the user should be reminded.
 * 
 * Purpose:
 * - Store reminder timing information for events
 * - Convert reminder times into human-readable format
 * - Enable notification functionality
 * 
 * Example Usage:
 * - new Reminder(15) = Remind 15 minutes before the event
 * - new Reminder(60) = Remind 1 hour before the event
 * - new Reminder(1440) = Remind 1 day before the event
 */
public class Reminder {
    // Instance variable
    private int minutesBefore;  // How many minutes before the event to send the reminder

    /**
     * Constructor - Creates a new Reminder object
     * 
     * @param minutesBefore The number of minutes before an event to trigger the reminder
     */
    public Reminder(int minutesBefore) {
        this.minutesBefore = minutesBefore;
    }

    /**
     * Get the reminder time in minutes
     * 
     * @return The number of minutes before the event this reminder triggers
     */
    public int getMinutesBefore() {
        return minutesBefore;
    }

    /**
     * Set the reminder time in minutes
     * 
     * @param minutesBefore The new number of minutes before the event
     */
    public void setMinutesBefore(int minutesBefore) {
        this.minutesBefore = minutesBefore;
    }

    /**
     * Get a human-readable display text for the reminder
     * 
     * This method converts minutes into friendly time descriptions:
     * - "15 minutes before" for short durations
     * - "2 hours before" for hour-based durations
     * - "1 day before" for day-based durations
     * 
     * @return A user-friendly string describing when the reminder triggers
     */
    public String getDisplayText() {
        // Less than an hour - show in minutes
        if (minutesBefore < 60) {
            return minutesBefore + " minutes before";
        } 
        // Exactly 1 day (1440 minutes = 24 hours)
        else if (minutesBefore == 1440) {
            return "1 day before";
        } 
        // Multiple days (divisible by 1440)
        else if (minutesBefore % 1440 == 0) {
            return (minutesBefore / 1440) + " days before";
        } 
        // Hours (divisible by 60)
        else if (minutesBefore % 60 == 0) {
            return (minutesBefore / 60) + " hours before";
        }
        // Default fallback - show in minutes
        return minutesBefore + " minutes before";
    }

    /**
     * toString method - Returns the display text
     * 
     * This is called when the Reminder object is converted to a string.
     * 
     * @return The human-readable reminder description
     */
    @Override
    public String toString() {
        return getDisplayText();
    }
}
