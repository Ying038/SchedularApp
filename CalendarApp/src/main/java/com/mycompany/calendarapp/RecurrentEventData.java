package com.mycompany.calendarapp;

import java.time.LocalDate;  // For date handling

/**
 * RecurrentEventData Class
 * 
 * This class stores recurrence data for CSV compatibility.
 * It matches the recurrent.csv file format exactly.
 * 
 * Purpose:
 * - Store recurrence information separately from basic event data
 * - Match the recurrent.csv format: eventId, recurrentInterval, recurrentTimes, recurrentEndDate
 * - Link recurrence data to events via eventId
 * 
 * CSV Format Fields:
 * - eventId: Links this recurrence data to an event in event.csv
 * - recurrentInterval: How often it repeats (e.g., "1d" = daily, "1w" = weekly, "1m" = monthly)
 * - recurrentTimes: Number of times the event repeats (0 if using end date instead)
 * - recurrentEndDate: When to stop repeating (0 or empty if using times instead)
 * 
 * Note: This class is primarily for CSV file operations. RecurringEvent is used
 * for the main application logic.
 */
public class RecurrentEventData {
    // Instance variables matching the CSV columns
    private int eventId;  // The event ID this recurrence data belongs to
    private String recurrentInterval;  // Recurrence pattern (e.g., "1d", "1w", "1m")
    private int recurrentTimes;  // How many times to repeat (0 if using end date)
    private String recurrentEndDate;  // End date for recurrence ("0" or empty if using times)

    /**
     * Constructor - Creates a new RecurrentEventData object
     * 
     * @param eventId The ID of the event this recurrence data applies to
     * @param recurrentInterval The interval pattern (e.g., "1d" for daily, "1w" for weekly)
     * @param recurrentTimes How many times the event repeats
     * @param recurrentEndDate The end date for recurrence (or "0" if using times)
     */
    public RecurrentEventData(int eventId, String recurrentInterval, int recurrentTimes, String recurrentEndDate) {
        this.eventId = eventId;
        this.recurrentInterval = recurrentInterval;
        this.recurrentTimes = recurrentTimes;
        this.recurrentEndDate = recurrentEndDate;
    }

    // Getter methods - Provide read access to private fields
    public int getEventId() { return eventId; }
    public String getRecurrentInterval() { return recurrentInterval; }
    public int getRecurrentTimes() { return recurrentTimes; }
    public String getRecurrentEndDate() { return recurrentEndDate; }

    // Setter methods - Allow modification of private fields
    public void setEventId(int eventId) { this.eventId = eventId; }
    public void setRecurrentInterval(String recurrentInterval) { this.recurrentInterval = recurrentInterval; }
    public void setRecurrentTimes(int recurrentTimes) { this.recurrentTimes = recurrentTimes; }
    public void setRecurrentEndDate(String recurrentEndDate) { this.recurrentEndDate = recurrentEndDate; }

    /**
     * Convert to CSV line for recurrent.csv
     * 
     * Format: eventId, recurrentInterval, recurrentTimes, recurrentEndDate
     * 
     * @return A CSV-formatted string with comma-separated values
     */
    public String toCSVLine() {
        return eventId + "," + recurrentInterval + "," + recurrentTimes + "," + recurrentEndDate;
    }

    /**
     * toString method - Readable string representation
     * 
     * Used for debugging and logging purposes.
     * 
     * @return Formatted string showing all recurrence data fields
     */
    @Override
    public String toString() {
        return "RecurrentEventData{" +
                "eventId=" + eventId +
                ", recurrentInterval='" + recurrentInterval + '\'' +
                ", recurrentTimes=" + recurrentTimes +
                ", recurrentEndDate='" + recurrentEndDate + '\'' +
                '}';
    }
}
