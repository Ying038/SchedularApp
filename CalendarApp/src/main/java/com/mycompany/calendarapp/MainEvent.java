package com.mycompany.calendarapp;

import java.time.LocalDateTime;  // For date and time handling

/**
 * MainEvent Class
 * 
 * This is the primary event class used throughout the application.
 * It extends the basic Event class by adding reminder functionality and
 * additional fields like location, category, and priority.
 * 
 * Purpose:
 * - Represent a complete calendar event with all features
 * - Support reminders for events
 * - Store additional metadata (location, category, priority)
 * - Serve as the base class for RecurringEvent
 * 
 * Additional Fields:
 * - Reminder: When to notify the user about this event
 * - Location: Where the event takes place
 * - Category: Type of event (Work, Personal, Meeting, etc.)
 * - Priority: Importance level (HIGH, MEDIUM, LOW)
 */
public class MainEvent {
    // Core event fields
    private int eventId;  // Unique identifier for this event
    private String title;  // Event name/title
    private String description;  // Detailed description
    private LocalDateTime startDateTime;  // When the event starts
    private LocalDateTime endDateTime;  // When the event ends
    
    // Optional features
    private Reminder reminder;  // Reminder settings (null = no reminder set)
    
    // Additional fields for enhanced functionality
    private String location;  // Where the event takes place (can be empty)
    private String category;  // Event category/type
    private String priority;  // Importance: HIGH, MEDIUM, or LOW

    /**
     * Constructor - Creates a new MainEvent
     * 
     * Initializes the event with default values:
     * - No reminder (null)
     * - Empty location
     * - "General" category
     * - "MEDIUM" priority
     * 
     * @param eventId Unique event identifier
     * @param title The event's name
     * @param description Details about the event
     * @param startDateTime When the event begins
     * @param endDateTime When the event ends
     */
    public MainEvent(int eventId, String title, String description, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.reminder = null;  // No reminder by default
        this.location = "";  // Empty location by default
        this.category = "General";  // Default category
        this.priority = "MEDIUM";  // Default priority
    }

    // Getter methods - Provide read access to private fields
    public int getEventId() { return eventId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }
    public Reminder getReminder() { return reminder; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }

    // Setter methods - Allow modification of private fields
    public void setEventId(int eventId) { this.eventId = eventId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }
    public void setReminder(Reminder reminder) { this.reminder = reminder; }
    public void setLocation(String location) { this.location = location; }
    public void setCategory(String category) { this.category = category; }
    public void setPriority(String priority) { this.priority = priority; }

    /**
     * toString method - Creates a readable string representation
     * 
     * Used for debugging and logging. Shows all core event details.
     * 
     * @return Formatted string with event information
     */
    @Override
    public String toString() {
        return "MainEvent{" +
                "eventId=" + eventId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
