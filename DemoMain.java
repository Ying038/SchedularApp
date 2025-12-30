/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author xuanpei
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DemoMain {
public static void main(String[] args) throws Exception {
EventManager em = new EventManager();

System.out.println("Loaded events: ");
for (Event e : em.getAllEvents()) System.out.println(e);

// create an event
LocalDateTime s = LocalDateTime.parse("2025-10-05T11:00:00", Event.FORMATTER);
LocalDateTime eEnd = LocalDateTime.parse("2025-10-05T12:00:00", Event.FORMATTER);
Event newE = new Event(0, "Test Meeting", "Demo", s, eEnd);

if (ConflictChecker.hasConflict(newE, em.getAllEvents())) {
System.out.println("Conflict detected, not adding.");
} else {
Event added = em.addEvent(newE.getTitle(), newE.getDescription(), newE.getStartDateTime(), newE.getEndDateTime());
System.out.println("Added: " + added);
}

// create a backup
String backupPath = "backups/backup_demo.txt";
BackupManager.createBackup(backupPath);
System.out.println("Backup created at: " + backupPath);

// restore (example overwrite)
// BackupManager.restoreBackup(backupPath, true, em);
}
}
