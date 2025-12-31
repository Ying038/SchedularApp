package com.mycompany.calendarapp;

import java.io.*;  // For file input/output
import java.time.LocalDate;  // For date handling
import java.time.LocalDateTime;  // For date and time handling
import java.time.format.DateTimeFormatter;  // For parsing/formatting dates
import java.time.temporal.ChronoUnit;  // For date calculations
import java.util.HashMap;  // For hash maps
import java.util.Map;  // For map interface

/**
 * CSVHandlerCompliant Class
 * 
 * This CSV handler complies with the assignment specification by using
 * separate CSV files for different types of data:
 * - event.csv: Core event data (eventId, title, description, startDateTime, endDateTime)
 * - recurrent.csv: Recurrence data (eventId, recurrentInterval, recurrentTimes, recurrentEndDate)
 * - additional.csv: Extra fields (eventId, location, category, priority)
 * 
 * Purpose:
 * - Separate concerns by splitting data into multiple CSV files
 * - Match the exact CSV format specified in the assignment
 * - Coordinate with AdditionalFieldsHandler for complete data persistence
 * 
 * How It Works:
 * 1. When saving: Splits each event's data across multiple CSV files
 * 2. When loading: Reads all CSV files and reconstructs complete events
 * 3. Uses HashMap to efficiently link related data across files
 * 
 * This is the ACTIVE CSV handler used by the application.
 */
public class CSVHandlerCompliant {

    // Constants for file names
    private static final String EVENT_FILE = "event.csv";  // Core event data
    private static final String RECURRENT_FILE = "recurrent.csv";  // Recurrence data
    // ISO format: "2025-12-31T14:30:00" (includes T separator)
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Save all events to CSV files
     * 
     * This is the main public method for saving. It coordinates the saving
     * process across multiple CSV files.
     * 
     * @param manager The EventManager containing all events to save
     */
    public static void saveEvents(EventManager manager) {
        saveEventCSV(manager);  // Save core event data to event.csv
        saveRecurrentCSV(manager);  // Save recurrence data to recurrent.csv
        AdditionalFieldsHandler.saveAdditionalFields(manager);  // Save additional fields to additional.csv
    }

    /**
     * Save core event data to event.csv
     * 
     * Saves ALL events (both normal and recurring) with their first occurrence data.
     * For recurring events, only the first occurrence is saved here; the recurrence
     * pattern is saved in recurrent.csv.
     * 
     * Format: eventId, title, description, startDateTime, endDateTime
     * 
     * @param manager The EventManager containing events to save
     */
    private static void saveEventCSV(EventManager manager) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(EVENT_FILE))) {
            // Write header row
            pw.println("eventId,title,description,startDateTime,endDateTime");
            
            // Write each event
            for (MainEvent e : manager.getAllEvents()) {
                // Save all events (including recurring) with their first occurrence times
                pw.println(e.getEventId() + "," + 
                          escapeCsvValue(e.getTitle()) + "," +  // Escape special chars in title
                          escapeCsvValue(e.getDescription()) + "," +  // Escape special chars in description
                          e.getStartDateTime().format(formatter) + "," + 
                          e.getEndDateTime().format(formatter));
            }
        } catch (IOException ex) {
            System.out.println("Error saving event.csv: " + ex.getMessage());
        }
    }

    /**
     * Save recurring event data to recurrent.csv
     * 
     * Only saves data for events that are instances of RecurringEvent.
     * Normal events are not included in this file.
     * 
     * Format: eventId, recurrentInterval, recurrentTimes, recurrentEndDate
     * 
     * @param manager The EventManager containing events
     */
    private static void saveRecurrentCSV(EventManager manager) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RECURRENT_FILE))) {
            // Write header row
            pw.println("eventId,recurrentInterval,recurrentTimes,recurrentEndDate");
            
            // Loop through all events and filter for recurring ones
            for (MainEvent e : manager.getAllEvents()) {
                if (e instanceof RecurringEvent) {  // Only process recurring events
                    RecurringEvent re = (RecurringEvent) e;  // Cast to RecurringEvent
                    
                    // Convert recurrence type (DAILY/WEEKLY/MONTHLY) to interval format (1d/1w/1m)
                    String interval = convertRecurrenceTypeToInterval(re.getRecurrenceType());
                    int times = re.getOccurrences();  // How many times it repeats
                    String endDate = "0";  // Using times instead of end date (so this is "0")
                    
                    // Write CSV line
                    pw.println(e.getEventId() + "," + interval + "," + times + "," + endDate);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error saving recurrent.csv: " + ex.getMessage());
        }
    }

    /**
     * Load all events from CSV files
     * 
     * This is the main public method for loading. It coordinates loading
     * data from multiple CSV files and reconstructs complete events.
     * 
     * Process:
     * 1. Load basic events from event.csv
     * 2. Load recurrence data from recurrent.csv
     * 3. Merge the data to create complete RecurringEvent objects
     * 4. Load additional fields from additional.csv
     * 
     * @param manager The EventManager to add loaded events to
     */
    public static void loadEvents(EventManager manager) {
        // First load basic events from event.csv
        Map<Integer, MainEvent> basicEvents = loadEventCSV();
        
        // Then load recurrent data and link to events
        Map<Integer, RecurrentEventData> recurrentData = loadRecurrentCSV();
        
        // Merge events with recurrent data
        int maxId = 0;
        for (Map.Entry<Integer, MainEvent> entry : basicEvents.entrySet()) {
            int eventId = entry.getKey();
            MainEvent event = entry.getValue();
            
            if (recurrentData.containsKey(eventId)) {
                // This is a recurring event
                RecurrentEventData rd = recurrentData.get(eventId);
                String recurrenceType = convertIntervalToRecurrenceType(rd.getRecurrentInterval());
                int occurrences = rd.getRecurrentTimes();
                
                // Create RecurringEvent from basic event data
                RecurringEvent recurringEvent = new RecurringEvent(
                    eventId,
                    event.getTitle(),
                    event.getDescription(),
                    event.getStartDateTime(),
                    event.getEndDateTime(),
                    recurrenceType,
                    occurrences
                );
                
                // Copy reminder if exists
                if (event.getReminder() != null) {
                    recurringEvent.setReminder(event.getReminder());
                }
                
                manager.addEvent(recurringEvent);
            } else {
                // Normal event
                manager.addEvent(event);
            }
            
            if (eventId > maxId) maxId = eventId;
        }
        
        manager.setNextEventId(maxId + 1);
        
        // Load additional fields after all events are loaded
        AdditionalFieldsHandler.loadAdditionalFields(manager);
    }

    /**
     * Load events from event.csv
     */
    private static Map<Integer, MainEvent> loadEventCSV() {
        Map<Integer, MainEvent> events = new HashMap<>();
        File file = new File(EVENT_FILE);
        if (!file.exists()) return events;

        try (BufferedReader br = new BufferedReader(new FileReader(EVENT_FILE))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = parseCsvLine(line);
                if (parts.length >= 5) {
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String desc = parts[2];
                    LocalDateTime start = LocalDateTime.parse(parts[3], formatter);
                    LocalDateTime end = LocalDateTime.parse(parts[4], formatter);
                    
                    MainEvent event = new MainEvent(id, title, desc, start, end);
                    events.put(id, event);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error loading event.csv: " + ex.getMessage());
        }
        
        return events;
    }

    /**
     * Load recurring event data from recurrent.csv
     */
    private static Map<Integer, RecurrentEventData> loadRecurrentCSV() {
        Map<Integer, RecurrentEventData> recurrentData = new HashMap<>();
        File file = new File(RECURRENT_FILE);
        if (!file.exists()) return recurrentData;

        try (BufferedReader br = new BufferedReader(new FileReader(RECURRENT_FILE))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    int eventId = Integer.parseInt(parts[0]);
                    String interval = parts[1];
                    int times = Integer.parseInt(parts[2]);
                    String endDate = parts[3];
                    
                    RecurrentEventData rd = new RecurrentEventData(eventId, interval, times, endDate);
                    recurrentData.put(eventId, rd);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error loading recurrent.csv: " + ex.getMessage());
        }
        
        return recurrentData;
    }

    /**
     * Convert RecurrenceType (DAILY, WEEKLY, MONTHLY) to interval format (1d, 1w, 1m)
     */
    private static String convertRecurrenceTypeToInterval(String recurrenceType) {
        switch (recurrenceType.toUpperCase()) {
            case "DAILY": return "1d";
            case "WEEKLY": return "1w";
            case "MONTHLY": return "1m";
            default: return "1d";
        }
    }

    /**
     * Convert interval format (1d, 1w, 1m) to RecurrenceType (DAILY, WEEKLY, MONTHLY)
     */
    private static String convertIntervalToRecurrenceType(String interval) {
        if (interval.endsWith("d")) return "DAILY";
        if (interval.endsWith("w")) return "WEEKLY";
        if (interval.endsWith("m")) return "MONTHLY";
        return "DAILY";
    }

    /**
     * Escape CSV values that contain commas or quotes
     */
    private static String escapeCsvValue(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * Parse CSV line handling quoted values
     */
    private static String[] parseCsvLine(String line) {
        java.util.List<String> values = new java.util.ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        values.add(current.toString());
        
        return values.toArray(new String[0]);
    }
}
