package com.mycompany.calendarapp;

import java.time.LocalDate;  // For date handling
import java.time.YearMonth;  // For month/year operations
import java.time.format.DateTimeFormatter;  // For formatting dates/times
import java.util.ArrayList;  // For creating lists
import java.util.List;  // List interface

/**
 * CalendarView Class
 * 
 * This class provides various ways to display events in calendar format.
 * It offers both list views (detailed) and grid views (visual calendar layout).
 * 
 * Purpose:
 * - Display events in different time periods (daily, weekly, monthly)
 * - Provide both list and grid visualization options
 * - Format event information for console output
 * 
 * View Types:
 * 1. Daily List: Shows all events for a specific day
 * 2. Weekly List: Shows events for each day of the week
 * 3. Monthly List: Shows all events in a month chronologically
 * 4. Weekly Grid: Shows a week in calendar grid format with event indicators
 * 5. Monthly Grid: Shows a month in traditional calendar format with event markers
 * 
 * Note: This class is designed for console/terminal output, not GUI.
 * The GUI version has its own calendar display logic.
 */
public class CalendarView {

    // Date/time formatters for consistent output formatting
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");  // e.g., "14:30"
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");  // e.g., "2025-12-31 14:30"

    /**
     * Display all events for a specific day (Daily List View)
     * 
     * Shows all events occurring on the specified date in a simple list format.
     * Each event is shown with its time and title.
     * 
     * @param events List of all events to search through
     * @param date The specific date to display events for
     */
    public void displayDailyList(List<MainEvent> events, LocalDate date) {
        System.out.println("\n--- Daily View of " + date + " ---");  // Header
        boolean found = false;  // Track whether we found any events
        
        // Loop through all events
        for (MainEvent event : events) {
            // Check if this event is on the specified date
            if (event.getStartDateTime().toLocalDate().equals(date)) {
                // Display: time + title (e.g., "14:30 Team Meeting")
                System.out.println(event.getStartDateTime().format(timeFormatter) + " " + event.getTitle());
                found = true;
            }
        }
        
        // If no events on this day, inform the user
        if (!found) 
            System.out.println("No events");
    }

    /**
     * Display events for a week in list format (Weekly List View)
     * 
     * Shows each day of the week with its events listed underneath.
     * The week starts on Sunday and ends on Saturday.
     * 
     * @param events List of all events
     * @param weekDate Any date within the week to display
     */
    public void displayWeeklyList(List<MainEvent> events, LocalDate weekDate) {
        // Create formatters for day names and day numbers
        DateTimeFormatter dayNameFormat = DateTimeFormatter.ofPattern("EEE");  // e.g., "Mon"
        DateTimeFormatter dayNumberFormat = DateTimeFormatter.ofPattern("dd");  // e.g., "15"
        
        System.out.println("\n--- Week of " + weekDate + " ---");

        // Calculate the Sunday of this week (start of week)
        // getDayOfWeek().getValue() returns 1-7 (Mon-Sun), we adjust to make Sunday = 0
        LocalDate startOfWeek = weekDate.minusDays(weekDate.getDayOfWeek().getValue() % 7);

        // Loop through all 7 days of the week
        for (int i = 0; i < 7; i++) {
            LocalDate currentDay = startOfWeek.plusDays(i);  // Calculate each day
            // Print day name and number (e.g., "Mon 15: ")
            System.out.print(currentDay.format(dayNameFormat) + " " + currentDay.format(dayNumberFormat) + ": ");

            boolean hasEvent = false;  // Track if this day has events
            
            // Check all events for this specific day
            for (MainEvent event : events) {
                if (event.getStartDateTime().toLocalDate().equals(currentDay)) {
                    // Display event title and time (e.g., "Team Meeting (14:30)")
                    System.out.println(event.getTitle() + " (" + event.getStartDateTime().format(timeFormatter) + ")");
                    hasEvent = true;
                }
            }

            // If no events on this day, print "No events"
            if (!hasEvent) 
                System.out.println("No events");
        }
    }

    /**
     * Display all events in a month as a list (Monthly List View)
     * 
     * Shows all events in the specified month in chronological order.
     * Each event is displayed with full date, time, and title.
     * 
     * @param events List of all events
     * @param year The year (e.g., 2025)
     * @param month The month (1-12)
     */
    public void displayMonthlyList(List<MainEvent> events, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);  // Create YearMonth object
        System.out.println("\n------ " + yearMonth.getMonth() + " " + year + " ------");  // Header

        // Loop through all events and display those in this month
        for (MainEvent event : events) {
            // Check if event is in the specified year and month
            if (event.getStartDateTime().getYear() == year && event.getStartDateTime().getMonthValue() == month) {
                // Display full date-time and title (e.g., "2025-12-31 14:30 - New Year's Eve Party")
                System.out.println(event.getStartDateTime().format(dateTimeFormatter) + " - " + event.getTitle());
            }
        }
    }

    /**
     * Display a week in grid/calendar format (Weekly Grid View)
     * 
     * Shows a visual calendar grid for one week with:
     * - Day numbers in a row (Su Mo Tu We Th Fr Sa)
     * - Asterisks (*) marking days with events
     * - Detailed list of events below the grid
     * 
     * @param events List of all events
     * @param weekDate Any date within the week to display
     */
    public void displayWeeklyView(List<MainEvent> events, LocalDate weekDate) {
        System.out.println("\n--- Week of " + weekDate + " ---");
        System.out.println("Su  Mo  Tu  We  Th  Fr  Sa");  // Day name headers

        // Calculate the Sunday of this week
        LocalDate startOfWeek = weekDate.minusDays(weekDate.getDayOfWeek().getValue() % 7);
        
        // Create a list to store which days have events
        List<Integer> eventDays = new ArrayList<>();

        // Find all days in this week that have events
        for (MainEvent event : events) {
            LocalDate eventDate = event.getStartDateTime().toLocalDate();
            // Check if event is within this week (between Sunday and Saturday)
            if (!eventDate.isBefore(startOfWeek) && !eventDate.isAfter(startOfWeek.plusDays(6))) {
                eventDays.add(eventDate.getDayOfMonth());  // Add the day number
            }
        }

        // Print the week grid
        LocalDate currentDay = startOfWeek;
        for (int i = 0; i < 7; i++) {
            int day = currentDay.getDayOfMonth();  // Get day number
            
            // Print day number with asterisk if it has events
            if (eventDays.contains(day)) 
                System.out.printf("%2d* ", day);  // Day with event (e.g., "15*")
            else 
                System.out.printf("%2d  ", day);  // Day without event (e.g., "15 ")
            
            currentDay = currentDay.plusDays(1);  // Move to next day
        }
        System.out.println("\n");  // Extra line after grid

        // Print detailed event list for this week
        for (MainEvent event : events) {
            LocalDate eventDate = event.getStartDateTime().toLocalDate();
            // Check if event is within this week
            if (!eventDate.isBefore(startOfWeek) && !eventDate.isAfter(startOfWeek.plusDays(6))) {
                // Print: * day: title (time)
                System.out.println("* " + eventDate.getDayOfMonth() + ": " + event.getTitle() + " (" + event.getStartDateTime().format(timeFormatter) + ")");
            }
        }
    }

    /**
     * Display a month in traditional calendar grid format (Monthly Grid View)
     * 
     * Shows a full month calendar with:
     * - Traditional calendar grid layout
     * - Proper alignment (Sunday on left, Saturday on right)
     * - Asterisks (*) marking days with events
     * - Detailed event list below the calendar
     * 
     * @param events List of all events
     * @param year The year (e.g., 2025)
     * @param month The month (1-12)
     */
    public void displayMonthlyView(List<MainEvent> events, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);  // Create YearMonth object
        LocalDate firstDay = LocalDate.of(year, month, 1);  // First day of the month
        int totalDays = yearMonth.lengthOfMonth();  // Total days in this month (28-31)

        System.out.println("\n------ " + yearMonth.getMonth() + " " + year + " ------");
        System.out.println("Su  Mo  Tu  We  Th  Fr  Sa");  // Day name headers

        // Calculate how many blank spaces we need before day 1
        // (getDayOfWeek().getValue() % 7 converts Monday=1 format to Sunday=0 format)
        int leadingSpaces = firstDay.getDayOfWeek().getValue() % 7;
        
        // Print leading blank spaces to align the first day correctly
        for (int i = 0; i < leadingSpaces; i++) {
            System.out.printf("%4s", " ");  // 4 spaces (enough for "dd* ")
        }

        // Create list to store which days have events
        List<Integer> eventDays = new ArrayList<>();
        for (MainEvent event : events) {
            LocalDate eventDate = event.getStartDateTime().toLocalDate();
            // Check if event is in this specific month
            if (eventDate.getYear() == year && eventDate.getMonthValue() == month) {
                eventDays.add(eventDate.getDayOfMonth());  // Add the day number
            }
        }

        // Print all days of the month
        for (int day = 1; day <= totalDays; day++) {
            System.out.printf("%2d", day);  // Print day number (right-aligned, 2 digits)
            
            // Add asterisk if day has events, otherwise spaces
            if (eventDays.contains(day)) 
                System.out.print("* ");  // Day with events
            else 
                System.out.print("  ");  // Day without events

            // Move to next line after Saturday (every 7 days, accounting for leading spaces)
            if ((day + leadingSpaces) % 7 == 0) 
                System.out.println();
        }

        System.out.println("\n");  // Extra line after calendar grid

        // Print detailed event list for this month
        for (MainEvent event : events) {
            LocalDate eventDate = event.getStartDateTime().toLocalDate();
            // Check if event is in this month
            if (eventDate.getYear() == year && eventDate.getMonthValue() == month) {
                // Print: * day: title (time)
                System.out.println("* " + eventDate.getDayOfMonth() + ": "
                        + event.getTitle() + " (" + event.getStartDateTime().format(timeFormatter) + ")");
            }
        }
    }
}
