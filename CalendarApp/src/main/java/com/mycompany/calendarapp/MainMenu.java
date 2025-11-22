package com.mycompany.calendarapp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MainMenu {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        EventManager manager = new EventManager();
        CSVHandler.loadEvents(manager);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CalendarView calendarView = new CalendarView();

        while (true) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Event Management");
            System.out.println("2. Calendar View");
            System.out.println("3. Save & Exit");
            System.out.print("Enter choice: ");
            int mainChoice = scanner.nextInt();
            scanner.nextLine();

            switch (mainChoice) {
                case 1:
                    eventManagementMenu(manager, scanner, dateTimeFormatter);
                    break;

                case 2:
                    calendarViewMenu(manager, scanner, calendarView);
                    break;

                case 3:
                    CSVHandler.saveEvents(manager);
                    System.out.println("Saved! Goodbye!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void eventManagementMenu(EventManager manager, Scanner scanner, DateTimeFormatter dateTimeFormatter) {
        while (true) {
            System.out.println("\n--- EVENT MANAGEMENT ---");
            System.out.println("1. Add Event");
            System.out.println("2. Add Recurring Event");
            System.out.println("3. View All Events");
            System.out.println("4. Update Event");
            System.out.println("5. Delete Event");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Description: ");
                    String description = scanner.nextLine();
                    System.out.print("Start (yyyy-MM-dd HH:mm): ");
                    LocalDateTime start = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);
                    System.out.print("End (yyyy-MM-dd HH:mm): ");
                    LocalDateTime end = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);
                    MainEvent event = new MainEvent(manager.generateEventId(), title, description, start, end);
                    manager.addEvent(event);
                    System.out.println("Event added!");
                    break;

                case 2:
                    System.out.print("Title: ");
                    String rTitle = scanner.nextLine();
                    System.out.print("Description: ");
                    String rDescription = scanner.nextLine();
                    System.out.print("Start (yyyy-MM-dd HH:mm): ");
                    LocalDateTime rStart = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);
                    System.out.print("End (yyyy-MM-dd HH:mm): ");
                    LocalDateTime rEnd = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);
                    System.out.print("Recurrence Type (DAILY, WEEKLY, MONTHLY): ");
                    String rType = scanner.nextLine();
                    System.out.print("Number of occurrences: ");
                    int rOccurrences = scanner.nextInt();
                    scanner.nextLine();
                    RecurringEvent recurringEvent = new RecurringEvent(manager.generateEventId(), rTitle, rDescription, rStart, rEnd, rType, rOccurrences);
                    manager.addEvent(recurringEvent);
                    System.out.println("Recurring event added!");
                    break;

                case 3:
                    System.out.println("\n--- ALL EVENTS ---");
                    System.out.printf("%-4s | %-9s | %-15s | %-16s | %-16s | %-12s\n",
                            "ID", "Type", "Title", "Start", "End", "Recurrence");
                    System.out.println("--------------------------------------------------------------------------------");
                    for (MainEvent ev : manager.getAllEvents()) {
                        String type = ev instanceof RecurringEvent ? "RECURRING" : "NORMAL";
                        String recurrence = "-";
                        if (ev instanceof RecurringEvent re) {
                            recurrence = re.getRecurrenceType() + " x" + re.getOccurrences();
                        }
                        System.out.printf("%-4d | %-9s | %-15s | %-16s | %-16s | %-12s\n",
                                ev.getEventId(),
                                type,
                                ev.getTitle(),
                                ev.getStartDateTime().format(dateTimeFormatter),
                                ev.getEndDateTime().format(dateTimeFormatter),
                                recurrence
                        );
                    }
                    break;

                case 4:
                    System.out.print("Enter Event ID to update: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();
                    MainEvent eventToUpdate = manager.findEventById(updateId);
                    if (eventToUpdate == null) {
                        System.out.println("Event not found!");
                        break;
                    }
                    System.out.print("New title: ");
                    eventToUpdate.setTitle(scanner.nextLine());
                    System.out.print("New description: ");
                    eventToUpdate.setDescription(scanner.nextLine());
                    System.out.print("New start (yyyy-MM-dd HH:mm): ");
                    eventToUpdate.setStartDateTime(LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter));
                    System.out.print("New end (yyyy-MM-dd HH:mm): ");
                    eventToUpdate.setEndDateTime(LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter));
                    if (eventToUpdate instanceof RecurringEvent re) {
                        System.out.print("New recurrence type: ");
                        re.setRecurrenceType(scanner.nextLine());
                        System.out.print("New number of occurrences: ");
                        re.setOccurrences(scanner.nextInt());
                        scanner.nextLine();
                    }
                    System.out.println("Event updated!");
                    break;

                case 5:
                    System.out.print("Enter Event ID to delete: ");
                    int deleteId = scanner.nextInt();
                    scanner.nextLine();
                    if (manager.deleteEvent(deleteId)) System.out.println("Event deleted!");
                    else System.out.println("Event not found!");
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void calendarViewMenu(EventManager manager, Scanner scanner, CalendarView calendarView) {
        while (true) {
            System.out.println("\n--- CALENDAR VIEW ---");
            System.out.println("1. Daily View");
            System.out.println("2. Weekly List View");
            System.out.println("3. Monthly List View");
            System.out.println("4. Weekly Grid View");
            System.out.println("5. Monthly Grid View");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            System.out.println();

            List<MainEvent> allEvents = manager.getAllEvents();

            switch (choice) {
                case 1:
                    System.out.print("Enter date (yyyy-MM-dd): ");
                    LocalDate date = LocalDate.parse(scanner.nextLine());
                    calendarView.displayDailyList(allEvents, date);
                    break;

                case 2:
                    System.out.print("Enter date within the week (yyyy-MM-dd): ");
                    LocalDate weekDate = LocalDate.parse(scanner.nextLine());
                    calendarView.displayWeeklyList(allEvents, weekDate);
                    break;

                case 3:
                    System.out.print("Enter year: ");
                    int year = scanner.nextInt();
                    System.out.print("Enter month (1-12): ");
                    int month = scanner.nextInt();
                    scanner.nextLine();
                    calendarView.displayMonthlyList(allEvents, year, month);
                    break;

                case 4:
                    System.out.print("Enter date within the week (yyyy-MM-dd): ");
                    LocalDate week = LocalDate.parse(scanner.nextLine());
                    calendarView.displayWeeklyView(allEvents, week);
                    break;

                case 5:
                    System.out.print("Enter year: ");
                    int y = scanner.nextInt();
                    System.out.print("Enter month (1-12): ");
                    int m = scanner.nextInt();
                    scanner.nextLine();
                    calendarView.displayMonthlyView(allEvents, y, m);
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}
