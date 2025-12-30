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

public class Event {
private int id;
private String title;
private String description;
private LocalDateTime startDateTime;
private LocalDateTime endDateTime;

public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public Event(int id, String title, String description, LocalDateTime start, LocalDateTime end) {
this.id = id;
this.title = title;
this.description = description;
this.startDateTime = start;
this.endDateTime = end;
}

public int getId() { return id; }
public void setId(int id) { this.id = id; }
public String getTitle() { return title; }
public String getDescription() { return description; }
public LocalDateTime getStartDateTime() { return startDateTime; }
public LocalDateTime getEndDateTime() { return endDateTime; }

public String toCSVLine() {
// Note: This simple CSV assumes fields have no commas or newlines.
return String.format("%d,%s,%s,%s,%s",
id,
escape(title),
escape(description),
startDateTime.format(FORMATTER),
endDateTime.format(FORMATTER));
}

public static Event fromCSVLine(String line) throws IllegalArgumentException {
String[] parts = line.split(",", -1);
if (parts.length < 5) throw new IllegalArgumentException("Invalid CSV line: " + line);
int id = Integer.parseInt(parts[0].trim());
String title = unescape(parts[1].trim());
String desc = unescape(parts[2].trim());
LocalDateTime start = LocalDateTime.parse(parts[3].trim(), FORMATTER);
LocalDateTime end = LocalDateTime.parse(parts[4].trim(), FORMATTER);
return new Event(id, title, desc, start, end);
}

private static String escape(String s) {
if (s == null) return "";
return s.replace("\\", "\\\\").replace(",", "\\,");
}

private static String unescape(String s) {
if (s == null) return "";
return s.replace("\\,", ",").replace("\\\\", "\\");
}

@Override
public String toString() {
return String.format("Event[%d] %s (%s -> %s)", id, title, startDateTime.format(FORMATTER), endDateTime.format(FORMATTER));
}
}

