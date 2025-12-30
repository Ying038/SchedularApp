package com.mycompany.calendarapp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SearchEvent {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public void searchByDate(List<MainEvent> events, LocalDate date) {
        System.out.println("\n--- Event of " + date + " ---");
        boolean found = false;
        for (MainEvent event : events) {
            if (event.getStartDateTime().toLocalDate().equals(date)) {
                System.out.println(event.getStartDateTime().format(timeFormatter) + " " + event.getTitle());
                found = true;
            }
        }
        if (!found) 
            System.out.println("No events");
    }

    public void searchByDateRange(List<MainEvent> events, LocalDate startDate, LocalDate endDate){
        System.out.println("\n--- Event from " + startDate + " to " + endDate + " ---");
        boolean hasEvent = false;
        for (MainEvent event : events) {
            if (!event.getStartDateTime().toLocalDate().isBefore(startDate) && !event.getStartDateTime().toLocalDate().isAfter(endDate)) {
                System.out.println(event.getStartDateTime().format(dateTimeFormatter) + " " + event.getTitle());
                hasEvent = true;
            }
        }
        if (!hasEvent) 
            System.out.println("No events");
    }

    public void searchByEventName(List<MainEvent> events, String title){
        System.out.println("\n------ Event of " + title + " ------");
        boolean hasEvent = false;
        for (MainEvent event : events) {
            if (event.getTitle().equals(title)) {
                System.out.println(event.getStartDateTime().format(dateTimeFormatter) + " " + event.getDescription());
                hasEvent = true;
            }
        }
        if (!hasEvent) 
            System.out.println("No events");
    }

}
