package com.mycompany.calendarapp;

// Import statements for file I/O operations
import java.io.*;  // Provides basic input/output classes for file handling
import java.nio.charset.StandardCharsets;  // For UTF-8 character encoding
import java.util.List;  // For working with lists of objects

/**
 * AdditionalFieldsHandler Class
 * 
 * This class handles the saving and loading of additional event fields that are not part
 * of the core event data. These additional fields include location, category, and priority.
 * 
 * Purpose:
 * - Separates additional event metadata from core event data
 * - Maintains data persistence for location, category, and priority fields
 * - Provides CSV file handling for the additional.csv file
 * 
 * File Format: eventId,location,category,priority
 */
public class AdditionalFieldsHandler {
    
    // Constants
    private static final String ADDITIONAL_CSV = "additional.csv";  // The CSV file name for storing additional fields
    
    /**
     * Save additional fields for all events to additional.csv
     * 
     * This method writes all additional event fields (location, category, priority) to a CSV file.
     * It creates a new file or overwrites the existing one.
     * 
     * @param manager The EventManager containing all events whose additional fields need to be saved
     */
    public static void saveAdditionalFields(EventManager manager) {
        // Try-with-resources statement - automatically closes the writer when done
        // BufferedWriter + OutputStreamWriter provides efficient writing with proper character encoding (UTF-8)
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ADDITIONAL_CSV), StandardCharsets.UTF_8))) {
            
            // Write the CSV header row (column names)
            writer.write("eventId,location,category,priority");
            writer.newLine();  // Move to the next line
            
            // Loop through all events in the manager and write their additional fields
            for (MainEvent event : manager.getAllEvents()) {
                // Get each additional field, using default values if null
                String location = event.getLocation() != null ? event.getLocation() : "";  // Empty string if no location
                String category = event.getCategory() != null ? event.getCategory() : "General";  // Default to "General"
                String priority = event.getPriority() != null ? event.getPriority() : "MEDIUM";  // Default to "MEDIUM"
                
                // Format and write the CSV line with escaped values (handles commas and quotes)
                writer.write(String.format("%d,%s,%s,%s",
                    event.getEventId(),  // Event's unique identifier
                    escapeCsv(location),  // Location with special characters escaped
                    escapeCsv(category),  // Category with special characters escaped
                    escapeCsv(priority)));  // Priority with special characters escaped
                writer.newLine();  // Move to next line for next event
            }
            
        } catch (IOException e) {
            // If file writing fails, print error message to standard error stream
            System.err.println("Error saving additional fields: " + e.getMessage());
        }
    }
    
    /**
     * Load additional fields from additional.csv and apply to events
     * 
     * This method reads the additional.csv file and applies the stored location, category,
     * and priority values to the corresponding events in the EventManager.
     * If the file doesn't exist, it silently returns without error.
     * 
     * @param manager The EventManager whose events will receive the additional field values
     */
    public static void loadAdditionalFields(EventManager manager) {
        // Create a File object to check if the CSV file exists
        File file = new File(ADDITIONAL_CSV);
        if (!file.exists()) {
            // If the file doesn't exist yet, there's nothing to load - exit gracefully
            return; // File doesn't exist yet, skip loading
        }
        
        // Try-with-resources for automatic file closing after reading
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            
            String line = reader.readLine(); // Skip the first line (header row)
            
            // Read each subsequent line containing event data
            while ((line = reader.readLine()) != null) {
                // Parse the CSV line, handling quoted values and commas
                String[] parts = parseCsvLine(line);
                if (parts.length >= 4) {  // Ensure we have all required fields
                    // Extract the data from the CSV columns
                    int eventId = Integer.parseInt(parts[0]);  // Column 1: Event ID
                    String location = parts[1];  // Column 2: Location
                    String category = parts[2];  // Column 3: Category
                    String priority = parts[3];  // Column 4: Priority
                    
                    // Find the matching event by ID and update its additional fields
                    for (MainEvent event : manager.getAllEvents()) {
                        if (event.getEventId() == eventId) {
                            // Apply the loaded values to the event
                            event.setLocation(location);
                            event.setCategory(category);
                            event.setPriority(priority);
                            break;  // Found and updated the event, exit the inner loop
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            // Handle file reading errors
            System.err.println("Error loading additional fields: " + e.getMessage());
        }
    }
    
    /**
     * Escape CSV special characters
     * 
     * This method handles special characters in CSV values:
     * - Wraps values containing commas, quotes, or newlines in double quotes
     * - Escapes existing double quotes by doubling them (" becomes "")
     * 
     * @param value The string value to escape
     * @return The escaped CSV-safe string
     */
    private static String escapeCsv(String value) {
        if (value == null) return "";  // Null values become empty strings
        // Check if the value contains special CSV characters
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            // Wrap in quotes and escape internal quotes by doubling them
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;  // No special characters, return as-is
    }
    
    /**
     * Parse a CSV line handling quoted values
     * 
     * This method correctly parses CSV lines that may contain:
     * - Comma-separated values
     * - Values wrapped in quotes
     * - Escaped quotes (doubled quotes "")
     * 
     * @param line The CSV line to parse
     * @return Array of parsed string values
     */
    private static String[] parseCsvLine(String line) {
        // Create a list to store the parsed values
        java.util.List<String> result = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();  // Builds the current field value
        boolean inQuotes = false;  // Tracks whether we're inside quoted text
        
        // Process each character in the line
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);  // Get the current character
            
            if (c == '"') {  // Handle quote characters
                // Check if this is an escaped quote (two quotes in a row)
                if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');  // Add a single quote to the result
                    i++; // Skip the next quote (we've already processed it)
                } else {
                    // Toggle the inQuotes flag (entering or leaving quoted section)
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {  // Comma outside quotes = field separator
                result.add(current.toString());  // Add the completed field to results
                current = new StringBuilder();  // Reset for the next field
            } else {
                current.append(c);  // Regular character - add to current field
            }
        }
        result.add(current.toString());  // Add the last field
        
        // Convert the list to an array and return
        return result.toArray(new String[0]);
    }
}
