package com.mycompany.calendarapp;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalendarView {

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // DAILY VIEW
    public void displayDailyList(List<MainEvent> events, LocalDate date) {
        System.out.println("\n--- Daily View of " + date + " ---");
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

    // WEEKLY LIST VIEW
    public void displayWeeklyList(List<MainEvent> events, LocalDate weekDate) {
        DateTimeFormatter dayNameFormat = DateTimeFormatter.ofPattern("EEE");
        DateTimeFormatter dayNumberFormat = DateTimeFormatter.ofPattern("dd");
        System.out.println("\n--- Week of " + weekDate + " ---");

        LocalDate startOfWeek = weekDate.minusDays(weekDate.getDayOfWeek().getValue() % 7);

        for (int i = 0; i < 7; i++) {
            LocalDate currentDay = startOfWeek.plusDays(i);
            System.out.print(currentDay.format(dayNameFormat) + " " + currentDay.format(dayNumberFormat) + ": ");

            boolean hasEvent = false;
            for (MainEvent event : events) {
                if (event.getStartDateTime().toLocalDate().equals(currentDay)) {
                    System.out.println(event.getTitle() + " (" + event.getStartDateTime().format(timeFormatter) + ")");
                    hasEvent = true;
                }
            }

            if (!hasEvent) 
                System.out.println("No events");
        }
    }

    // MONTHLY LIST VIEW
    public void displayMonthlyList(List<MainEvent> events, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        System.out.println("\n------ " + yearMonth.getMonth() + " " + year + " ------");

        for (MainEvent event : events) {
            if (event.getStartDateTime().getYear() == year && event.getStartDateTime().getMonthValue() == month) {
                System.out.println(event.getStartDateTime().format(dateTimeFormatter) + " - " + event.getTitle());
            }
        }
    }

    // WEEKLY GRID VIEW
    public void displayWeeklyView(List<MainEvent> events, LocalDate weekDate) {
        System.out.println("\n--- Week of " + weekDate + " ---");
        System.out.println("Su  Mo  Tu  We  Th  Fr  Sa");

        LocalDate startOfWeek = weekDate.minusDays(weekDate.getDayOfWeek().getValue() % 7);
        List<Integer> eventDays = new ArrayList<>();

        for (MainEvent event : events) {
            LocalDate eventDate = event.getStartDateTime().toLocalDate();
            if (!eventDate.isBefore(startOfWeek) && !eventDate.isAfter(startOfWeek.plusDays(6))) {
                eventDays.add(eventDate.getDayOfMonth());
            }
        }

        LocalDate currentDay = startOfWeek;
        for (int i = 0; i < 7; i++) {
            int day = currentDay.getDayOfMonth();
            if (eventDays.contains(day)) 
                System.out.printf("%2d* ", day);
            else 
                System.out.printf("%2d  ", day);
            currentDay = currentDay.plusDays(1);
        }
        System.out.println("\n");

        for (MainEvent event : events) {
            LocalDate eventDate = event.getStartDateTime().toLocalDate();
            if (!eventDate.isBefore(startOfWeek) && !eventDate.isAfter(startOfWeek.plusDays(6))) {
                System.out.println("* " + eventDate.getDayOfMonth() + ": " + event.getTitle() + " (" + event.getStartDateTime().format(timeFormatter) + ")");
            }
        }
    }

    // MONTHLY GRID VIEW
    public void displayMonthlyView(List<MainEvent> events, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = LocalDate.of(year, month, 1);
        int totalDays = yearMonth.lengthOfMonth();

        System.out.println("\n------ " + yearMonth.getMonth() + " " + year + " ------");
        System.out.println("Su  Mo  Tu  We  Th  Fr  Sa");

        int leadingSpaces = firstDay.getDayOfWeek().getValue() % 7;
        for (int i = 0; i < leadingSpaces; i++) {
            System.out.printf("%4s", " ");
        }

        List<Integer> eventDays = new ArrayList<>();
        for (MainEvent event : events) {
            LocalDate eventDate = event.getStartDateTime().toLocalDate();
            if (eventDate.getYear() == year && eventDate.getMonthValue() == month) {
                eventDays.add(eventDate.getDayOfMonth());
            }
        }

        for (int day = 1; day <= totalDays; day++) {
            System.out.printf("%2d", day);
            if (eventDays.contains(day)) 
                System.out.print("* ");
            else 
                System.out.print("  ");

            if ((day + leadingSpaces) % 7 == 0) 
                System.out.println();
        }

        System.out.println("\n");

        for (MainEvent event : events) {
            LocalDate eventDate = event.getStartDateTime().toLocalDate();
            if (eventDate.getYear() == year && eventDate.getMonthValue() == month) {
                System.out.println("* " + eventDate.getDayOfMonth() + ": "
                        + event.getTitle() + " (" + event.getStartDateTime().format(timeFormatter) + ")");
            }
        }
    }
}
