package com.mycompany.calendarapp;

import java.io.*;  // For file input/output operations
import java.time.LocalDateTime;  // For date and time handling
import java.time.format.DateTimeFormatter;  // For parsing and formatting dates

/**
 * CSVHandler Class (Legacy)
 * 
 * This is the original CSV handler used before CSVHandlerCompliant was created.
 * It uses a single file (events.csv) for all event data.
 * 
 * Purpose:
 * - Save all events to a single CSV file
 * - Load events from a single CSV file
 * - Provide backward compatibility with older data format
 * 
 * Note: The current application uses CSVHandlerCompliant instead, which separates
 * event data into multiple CSV files (event.csv, recurrent.csv, additional.csv) as
 * per the assignment specification.
 * 
 * This class is kept for reference and potential migration purposes.
 */
public class CSVHandler {

    private static final String FILE_NAME = "events.csv";  // Single CSV file for all data
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");  // Date format

    /**
     * Save all events to events.csv
     * 
     * Creates a single CSV file with all event information including
     * event type (NORMAL or RECURRING) and recurrence details.
     * 
     * @param manager The EventManager containing all events to save
     */
    public static void saveEvents(EventManager manager) {
        // Try-with-resources ensures the file is properly closed
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            // Loop through all events
            for (MainEvent e : manager.getAllEvents()) {
                // Determine event type
                String type = e instanceof RecurringEvent ? "RECURRING" : "NORMAL";
                String extra = "";  // Extra fields for recurring events
                
                // If recurring, add recurrence type and occurrences
                if (e instanceof RecurringEvent r) {
                    extra = "," + r.getRecurrenceType() + "," + r.getOccurrences();
                }
                
                // Add reminder info if present
                String reminder = e.getReminder() != null ? "," + e.getReminder().getMinutesBefore() : ",";
                
                // Write CSV line: id,type,title,description,start,end,[recurrence],[occurrences],[reminder]
                pw.println(e.getEventId() + "," + type + "," + e.getTitle() + "," + e.getDescription() + "," +
                        e.getStartDateTime().format(formatter) + "," + e.getEndDateTime().format(formatter) + extra + reminder);
            }
        } catch (IOException ex) {
            System.out.println("Error saving CSV: " + ex.getMessage());
        }
    }

    /**
     * Load events from events.csv
     * 
     * Reads the CSV file and reconstructs all events (both normal and recurring).
     * Also restores reminder settings for each event.
     * 
     * @param manager The EventManager to add loaded events to
     */
    public static void loadEvents(EventManager manager) {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;  // Exit if file doesn't exist

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int maxId = 0;  // Track the highest event ID
            
            // Read each line from the file
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");  // Split by comma
                
                // Parse common fields (present in all events)
                int id = Integer.parseInt(parts[0]);  // Event ID
                String type = parts[1];  // Event type (NORMAL or RECURRING)
                String title = parts[2];  // Title
                String desc = parts[3];  // Description
                LocalDateTime start = LocalDateTime.parse(parts[4], formatter);  // Start date/time
                LocalDateTime end = LocalDateTime.parse(parts[5], formatter);  // End date/time

                MainEvent event;
                
                // Create appropriate event type based on the type field
                if ("RECURRING".equals(type)) {
                    // Recurring event - has additional fields
                    String recurrenceType = parts[6];  // e.g., "DAILY", "WEEKLY"
                    int occurrences = Integer.parseInt(parts[7]);  // Number of times it repeats
                    event = new RecurringEvent(id, title, desc, start, end, recurrenceType, occurrences);
                    
                    // Reminder is at index 8 for recurring events
                    if (parts.length > 8 && !parts[8].isEmpty()) {
                        int reminderMinutes = Integer.parseInt(parts[8]);
                        event.setReminder(new Reminder(reminderMinutes));
                    }
                } else {
                    // Normal event
                    event = new MainEvent(id, title, desc, start, end);
                    
                    // Reminder is at index 6 for normal events
                    if (parts.length > 6 && !parts[6].isEmpty()) {
                        int reminderMinutes = Integer.parseInt(parts[6]);
                        event.setReminder(new Reminder(reminderMinutes));
                    }
                }

                manager.addEvent(event);  // Add the event to the manager
                if (id > maxId) maxId = id;  // Track highest ID
            }
            
            // Set the next ID to be one higher than the highest ID we found
            manager.setNextEventId(maxId + 1);
        } catch (IOException ex) {
            System.out.println("Error loading CSV: " + ex.getMessage());
        }
    }
}
