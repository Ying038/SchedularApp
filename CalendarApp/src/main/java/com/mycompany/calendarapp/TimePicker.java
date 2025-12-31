package com.mycompany.calendarapp;

// JavaFX imports for GUI components
import javafx.geometry.Insets;  // For spacing/padding
import javafx.scene.control.Label;  // For text labels
import javafx.scene.control.Spinner;  // For number spinners
import javafx.scene.control.SpinnerValueFactory;  // For spinner configuration
import javafx.scene.layout.HBox;  // For horizontal layout
import java.time.LocalTime;  // For time representation

/**
 * TimePicker Class
 * 
 * This is a custom JavaFX component for selecting times (hours and minutes).
 * It provides an intuitive way for users to pick time values using spinners.
 * 
 * Purpose:
 * - Create a time selection widget for the GUI
 * - Allow users to select hours (0-23) and minutes (0-59)
 * - Provide a reusable component for event time input
 * 
 * Features:
 * - Two spinners: one for hours, one for minutes
 * - 24-hour format (0-23 for hours)
 * - Visual labels to identify each spinner
 * - Styled appearance to match the app's design
 * 
 * Usage:
 * - TimePicker timePicker = new TimePicker();  // Creates picker with current time
 * - TimePicker timePicker = new TimePicker(LocalTime.of(9, 30));  // Creates picker with 9:30
 * - LocalTime selected = timePicker.getValue();  // Get the selected time
 * 
 * Note: Extends HBox, so it can be added to JavaFX layouts like any other node.
 */
public class TimePicker extends HBox {
    // Instance variables - the two spinners for hour and minute selection
    private Spinner<Integer> hourSpinner;  // Spinner for selecting hours (0-23)
    private Spinner<Integer> minuteSpinner;  // Spinner for selecting minutes (0-59)

    /**
     * Default Constructor
     * 
     * Creates a TimePicker initialized to the current system time.
     */
    public TimePicker() {
        this(LocalTime.now());  // Call the other constructor with current time
    }

    /**
     * Constructor with initial time
     * 
     * Creates a TimePicker and sets it to display the specified time.
     * 
     * @param initialTime The time to display initially (can be null for midnight)
     */
    public TimePicker(LocalTime initialTime) {
        super(5);  // Call HBox constructor with 5 pixels spacing between children
        setPadding(new Insets(5));  // Add 5 pixels padding around the edges
        
        // Apply styling to make it look nice
        setStyle("-fx-border-color: #ddd; " +  // Light gray border
                 "-fx-border-radius: 4; " +  // Rounded corners
                 "-fx-background-color: #f9f9f9;");  // Light background color

        // Extract hour and minute from initialTime, or use 0 if null
        int hour = initialTime != null ? initialTime.getHour() : 0;
        int minute = initialTime != null ? initialTime.getMinute() : 0;

        // Create hour spinner (0-23)
        hourSpinner = new Spinner<>(0, 23, hour);  // Min=0, Max=23, Initial=hour
        hourSpinner.setEditable(true);  // Allow typing numbers directly
        hourSpinner.setPrefWidth(60);  // Set width to 60 pixels

        // Create minute spinner (0-59)
        minuteSpinner = new Spinner<>(0, 59, minute);  // Min=0, Max=59, Initial=minute
        minuteSpinner.setEditable(true);  // Allow typing numbers directly
        minuteSpinner.setPrefWidth(60);  // Set width to 60 pixels

        // Create text labels to identify what each spinner is for
        Label hourLabel = new Label("Hour:");
        Label minuteLabel = new Label("Minute:");

        // Add all components to the HBox in order: Hour label, Hour spinner, Minute label, Minute spinner
        getChildren().addAll(hourLabel, hourSpinner, minuteLabel, minuteSpinner);
    }

    /**
     * Get the selected time as LocalTime
     * 
     * Reads the current values from both spinners and creates a LocalTime object.
     * 
     * @return LocalTime object with the selected hour and minute
     */
    public LocalTime getValue() {
        int hour = hourSpinner.getValue();  // Get the hour value
        int minute = minuteSpinner.getValue();  // Get the minute value
        return LocalTime.of(hour, minute);  // Create and return LocalTime
    }

    /**
     * Set the time displayed in the picker
     * 
     * Updates both spinners to show the specified time.
     * 
     * @param time LocalTime to display (if null, does nothing)
     */
    public void setValue(LocalTime time) {
        if (time != null) {
            // Update both spinners with the new time values
            hourSpinner.getValueFactory().setValue(time.getHour());  // Set hour
            minuteSpinner.getValueFactory().setValue(time.getMinute());  // Set minute
        }
    }
}

