package com.mycompany.calendarapp;

import java.time.LocalDateTime;  // For date and time handling
import java.util.ArrayList;  // For creating array lists
import java.util.List;  // List interface

/**
 * EventManager Class
 * 
 * This class is the central manager for all events in the calendar application.
 * It acts as a container and controller for the event collection.
 * 
 * Purpose:
 * - Store and manage all calendar events (both normal and recurring)
 * - Provide methods to add, find, update, and delete events
 * - Generate unique event IDs
 * - Detect clashing/conflicting events
 * - Expand recurring events into their individual occurrences
 * 
 * Key Responsibilities:
 * - Maintaining the master list of all events
 * - ID generation and management
 * - Event conflict detection
 * - Event retrieval and filtering
 */
public class EventManager {

    // Instance variables
    private List<MainEvent> events = new ArrayList<>();  // The master list storing all events
    private int nextEventId = 1;  // Counter for generating unique event IDs

    /**
     * Generate a unique event ID
     * 
     * Each time this method is called, it returns a new unique ID and
     * increments the counter for the next call.
     * 
     * @return A unique integer ID for a new event
     */
    public int generateEventId() { 
        return nextEventId++;  // Return current value, then increment
    }

    /**
     * Add an event to the manager
     * 
     * @param event The event to add to the collection
     */
    public void addEvent(MainEvent event) { 
        events.add(event);  // Add to the internal list
    }
    
    /**
     * Get all events stored in the manager
     * 
     * Returns the raw list of events (recurring events not expanded).
     * 
     * @return List of all events
     */
    public List<MainEvent> getAllEvents() { 
        return events; 
    }

    /**
     * Find an event by its ID
     * 
     * Searches through all events to find one with the matching ID.
     * 
     * @param id The event ID to search for
     * @return The MainEvent with that ID, or null if not found
     */
    public MainEvent findEventById(int id) {
        // Loop through all events
        for (MainEvent e : events) {
            if (e.getEventId() == id) return e;  // Found it!
        }
        return null;  // Not found
    }

    /**
     * Delete an event by its ID
     * 
     * Finds and removes the event from the collection.
     * 
     * @param id The ID of the event to delete
     * @return true if the event was found and deleted, false otherwise
     */
    public boolean deleteEvent(int id) {
        MainEvent e = findEventById(id);  // First, find the event
        if (e != null) {
            events.remove(e);  // Remove it from the list
            return true;  // Success
        }
        return false;  // Event not found
    }

    /**
     * Set the next event ID counter
     * 
     * Used when loading events from file to ensure new events get
     * IDs that don't conflict with existing ones.
     * 
     * @param id The value to set as the next ID to be generated
     */
    public void setNextEventId(int id) { 
        this.nextEventId = id; 
    }
    
    /**
     * Get all events including expanded occurrences of recurring events
     * 
     * This method is crucial for displaying events in calendars and checking
     * for conflicts. Recurring events are expanded into their individual
     * occurrences.
     * 
     * Example: A weekly meeting with 4 occurrences becomes 4 separate events.
     * 
     * @return List of all events with recurring events expanded into individual occurrences
     */
    public List<MainEvent> getAllEventsExpanded() {
        List<MainEvent> expandedEvents = new ArrayList<>();  // Create new list for result
        
        // Go through each event in the collection
        for (MainEvent event : events) {
            if (event instanceof RecurringEvent) {
                // This is a recurring event - expand it into multiple occurrences
                RecurringEvent recurring = (RecurringEvent) event;  // Cast to RecurringEvent
                expandedEvents.addAll(recurring.generateOccurrences());  // Add all its occurrences
            } else {
                // Normal event - add it as-is
                expandedEvents.add(event);
            }
        }
        
        return expandedEvents;  // Return the expanded list
    }
    
    /**
     * Check if a new event clashes with any existing events
     * 
     * This method detects scheduling conflicts. Two events clash if their
     * time periods overlap. This is important for:
     * - Warning users about double-booking
     * - Preventing scheduling conflicts
     * - Validating event times before saving
     * 
     * For recurring events, all occurrences are checked individually.
     * 
     * @param newEvent The event to check for clashes
     * @return List of events that clash (overlap in time) with the new event
     */
    public List<MainEvent> findClashingEvents(MainEvent newEvent) {
        List<MainEvent> clashes = new ArrayList<>();  // List to store clashing events
        LocalDateTime newStart = newEvent.getStartDateTime();  // When new event starts
        LocalDateTime newEnd = newEvent.getEndDateTime();  // When new event ends
        
        // Check against all existing events
        for (MainEvent existing : events) {
            // Skip comparing an event with itself (important when updating events)
            if (existing.getEventId() == newEvent.getEventId()) {
                continue;  // Skip to next event
            }
            
            if (existing instanceof RecurringEvent) {
                // For recurring events, we need to check all occurrences
                RecurringEvent recurring = (RecurringEvent) existing;
                List<MainEvent> occurrences = recurring.generateOccurrences();  // Get all occurrences
                
                // Check each occurrence
                for (MainEvent occurrence : occurrences) {
                    if (eventsOverlap(newStart, newEnd, occurrence.getStartDateTime(), occurrence.getEndDateTime())) {
                        clashes.add(existing);  // Found a clash!
                        break; // Only add the recurring event once, even if multiple occurrences clash
                    }
                }
            } else {
                // For normal events, direct time comparison
                if (eventsOverlap(newStart, newEnd, existing.getStartDateTime(), existing.getEndDateTime())) {
                    clashes.add(existing);  // This event clashes
                }
            }
        }
        
        return clashes;  // Return all clashing events
    }
    
    /**
     * Check if two time ranges overlap
     * 
     * Two events overlap if:
     * - Event 1 starts before Event 2 ends, AND
     * - Event 2 starts before Event 1 ends
     * 
     * Examples:
     * - Event 1: 9:00-10:00, Event 2: 9:30-10:30 → OVERLAP (both conditions true)
     * - Event 1: 9:00-10:00, Event 2: 10:00-11:00 → NO OVERLAP (start2 not before end1)
     * - Event 1: 9:00-10:00, Event 2: 8:00-9:00 → NO OVERLAP (start1 not before end2)
     * 
     * @param start1 Start time of first event
     * @param end1 End time of first event
     * @param start2 Start time of second event
     * @param end2 End time of second event
     * @return true if the events overlap, false otherwise
     */
    private boolean eventsOverlap(LocalDateTime start1, LocalDateTime end1, 
                                   LocalDateTime start2, LocalDateTime end2) {
        // Events overlap if one starts before the other ends
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
    
    /**
     * Find all events that have clashes with any other events
     * 
     * This method scans the entire event collection and returns all events
     * that have at least one scheduling conflict.
     * 
     * Useful for:
     * - Showing users all their scheduling conflicts
     * - Generating conflict reports
     * - Calendar validation
     * 
     * @return List of events that have conflicts with other events
     */
    public List<MainEvent> findAllClashingEvents() {
        List<MainEvent> clashing = new ArrayList<>();  // Store conflicting events
        
        // Check each event against all others
        for (int i = 0; i < events.size(); i++) {
            MainEvent event = events.get(i);
            List<MainEvent> clashes = findClashingEvents(event);  // Find its clashes
            
            // If it has clashes and isn't already in our list, add it
            if (!clashes.isEmpty() && !clashing.contains(event)) {
                clashing.add(event);
            }
        }
        
        return clashing;  // Return all events with conflicts
    }
}
