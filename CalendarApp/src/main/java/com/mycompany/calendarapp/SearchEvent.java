package com.mycompany.calendarapp;

import java.time.LocalDate;  // For date handling
import java.time.format.DateTimeFormatter;  // For formatting dates/times
import java.util.List;  // For working with lists

/**
 * SearchEvent Class
 * 
 * This class provides search functionality for finding events in the calendar.
 * It supports multiple search criteria to help users locate events quickly.
 * 
 * Purpose:
 * - Search for events by specific date
 * - Search for events within a date range
 * - Search for events by title/name
 * - Display search results to the console
 * 
 * Search Types Supported:
 * 1. By Date: Find all events on a specific day
 * 2. By Date Range: Find all events between two dates
 * 3. By Name: Find events matching a specific title
 * 
 * Note: This class is primarily used by the console-based menu system.
 */
public class SearchEvent {
    // Formatters for displaying dates and times in consistent format
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");  // Full date + time
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");  // Just time (hours:minutes)

    /**
     * Search for events on a specific date
     * 
     * Finds and displays all events that start on the given date.
     * Useful for answering "What do I have scheduled on Monday?"
     * 
     * @param events The list of events to search through
     * @param date The specific date to search for
     */
    public void searchByDate(List<MainEvent> events, LocalDate date) {
        System.out.println("\n--- Event of " + date + " ---");  // Print header
        boolean found = false;  // Track if we found any events
        
        // Loop through all events
        for (MainEvent event : events) {
            // Check if the event's start date matches our search date
            if (event.getStartDateTime().toLocalDate().equals(date)) {
                // Print the event (time + title)
                System.out.println(event.getStartDateTime().format(timeFormatter) + " " + event.getTitle());
                found = true;  // We found at least one event
            }
        }
        
        // If no events found, tell the user
        if (!found) 
            System.out.println("No events");
    }

    /**
     * Search for events within a date range
     * 
     * Finds and displays all events that start between the startDate and endDate (inclusive).
     * Useful for questions like "What's happening this week?" or "Show me next month's events."
     * 
     * @param events The list of events to search through
     * @param startDate The beginning of the search range (inclusive)
     * @param endDate The end of the search range (inclusive)
     */
    public void searchByDateRange(List<MainEvent> events, LocalDate startDate, LocalDate endDate){
        System.out.println("\n--- Event from " + startDate + " to " + endDate + " ---");  // Print header
        boolean hasEvent = false;  // Track if any events found
        
        // Loop through all events
        for (MainEvent event : events) {
            LocalDate eventDate = event.getStartDateTime().toLocalDate();
            
            // Check if event date is within range:
            // Not before startDate AND not after endDate
            if (!eventDate.isBefore(startDate) && !eventDate.isAfter(endDate)) {
                // Print full date-time + title
                System.out.println(event.getStartDateTime().format(dateTimeFormatter) + " " + event.getTitle());
                hasEvent = true;  // Found at least one
            }
        }
        
        // If no events found, inform user
        if (!hasEvent) 
            System.out.println("No events");
    }

    /**
     * Search for events by name/title
     * 
     * Finds and displays all events with an exact title match.
     * Useful for finding a specific event like "Team Meeting" or "Doctor Appointment"
     * 
     * Note: This uses exact matching (case-sensitive). The title must match exactly.
     * 
     * @param events The list of events to search through
     * @param title The exact event title to search for
     */
    public void searchByEventName(List<MainEvent> events, String title){
        System.out.println("\n------ Event of " + title + " ------");  // Print header
        boolean hasEvent = false;  // Track if we found matches
        
        // Loop through all events
        for (MainEvent event : events) {
            // Check for exact title match
            if (event.getTitle().equals(title)) {
                // Print the event's date-time + description
                System.out.println(event.getStartDateTime().format(dateTimeFormatter) + " " + event.getDescription());
                hasEvent = true;  // Found at least one
            }
        }
        
        // If no matching events, inform user
        if (!hasEvent) 
            System.out.println("No events");
    }

}
