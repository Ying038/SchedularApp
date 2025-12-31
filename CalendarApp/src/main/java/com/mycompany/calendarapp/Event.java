package com.mycompany.calendarapp;

import java.time.LocalDateTime;  // For handling date and time

/**
 * Event Class
 * 
 * This is a basic event data class designed for CSV compatibility.
 * It represents a simple calendar event with core information only.
 * 
 * Purpose:
 * - Store basic event information (id, title, description, start/end times)
 * - Match the event.csv file format exactly
 * - Provide a lightweight event structure
 * 
 * CSV Format: eventId, title, description, startDateTime, endDateTime
 * 
 * Note: This class is primarily used for CSV operations. The MainEvent class
 * is used for most application logic as it includes additional features.
 */
public class Event {
    // Instance variables (fields) that store the event's data
    private int eventId;  // Unique identifier for the event
    private String title;  // The name/title of the event
    private String description;  // Detailed description of what the event is about
    private LocalDateTime startDateTime;  // When the event begins (date + time)
    private LocalDateTime endDateTime;  // When the event ends (date + time)

    /**
     * Constructor - Creates a new Event object
     * 
     * @param eventId Unique identifier for this event
     * @param title The event's title/name
     * @param description Details about the event
     * @param startDateTime When the event starts
     * @param endDateTime When the event ends
     */
    public Event(int eventId, String title, String description, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    // Getter methods - Allow reading the private fields from outside the class
    public int getEventId() { return eventId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }

    // Setter methods - Allow modifying the private fields from outside the class
    public void setEventId(int eventId) { this.eventId = eventId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    /**
     * Convert this event to a CSV line for event.csv
     * 
     * Format: eventId, title, description, startDateTime, endDateTime
     * 
     * @return A CSV-formatted string representing this event
     */
    public String toCSVLine() {
        return eventId + "," + title + "," + description + "," + 
               startDateTime.toString() + "," + endDateTime.toString();
    }

    /**
     * toString method - Provides a readable string representation of the event
     * 
     * This method is automatically called when you print the object or convert it to a string.
     * Useful for debugging and logging.
     * 
     * @return A formatted string with all event details
     */
    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
