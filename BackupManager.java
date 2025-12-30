/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author xuanpei
 */
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class BackupManager {
private static final String EVENT_FILE = "event.csv";
private static final String RECURRENT_FILE = "recurrent.csv"; // may not exist

/**
* Create a backup file that contains sections for each CSV used.
* The backup file is written to the given relative path (example: backups/backup_2025-12-01.txt)
*/
public static void createBackup(String backupPath) throws IOException {
Path backup = Paths.get(backupPath);
// ensure parent directories exist
if (backup.getParent() != null) Files.createDirectories(backup.getParent());

try (BufferedWriter writer = Files.newBufferedWriter(backup, StandardCharsets.UTF_8)) {
writer.write("--- event.csv ---\n");
copyFileToWriter(EVENT_FILE, writer);
writer.write("\n--- recurrent.csv ---\n");
copyFileToWriter(RECURRENT_FILE, writer);
writer.flush();
}
}

private static void copyFileToWriter(String sourceFile, BufferedWriter writer) throws IOException {
Path path = Paths.get(sourceFile);
if (!Files.exists(path)) {
writer.write("# (file missing)\n");
return;
}
try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
String line;
while ((line = reader.readLine()) != null) {
writer.write(line);
writer.write('\n');
}
}
}
public static void restoreBackup(String backupPath, boolean overwrite, EventManager eventManager) throws IOException {
Path backup = Paths.get(backupPath);
if (!Files.exists(backup)) throw new FileNotFoundException("Backup file not found: " + backupPath);

List<String> allLines = Files.readAllLines(backup, StandardCharsets.UTF_8);
Map<String, List<String>> sections = extractSections(allLines);

List<String> eventLines = sections.getOrDefault("event.csv", Collections.emptyList());
List<String> recurrentLines = sections.getOrDefault("recurrent.csv", Collections.emptyList());

// write recurrent.csv
Path recPath = Paths.get(RECURRENT_FILE);
if (overwrite) {
Files.createDirectories(recPath.getParent() == null ? Paths.get("") : recPath.getParent());
Files.write(recPath, recurrentLines, StandardCharsets.UTF_8);
} else {
if (!Files.exists(recPath)) Files.createFile(recPath);
Files.write(recPath, recurrentLines, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
}

// parse events into Event objects (skip header if present)
List<Event> parsedEvents = parseEventLines(eventLines);

if (overwrite) {
// overwrite the event.csv completely
// write header
List<String> out = new ArrayList<>();
out.add("eventId,title,description,startDateTime,endDateTime");
for (Event e : parsedEvents) out.add(e.toCSVLine());
Files.write(Paths.get(EVENT_FILE), out, StandardCharsets.UTF_8);
// ask EventManager to reload
eventManager.loadEvents();
} else {
// append mode: attempt to append events, remapping ids if collision
eventManager.appendEvents(parsedEvents);
}
}
private static Map<String, List<String>> extractSections(List<String> lines) {
Map<String, List<String>> map = new HashMap<>();
String current = null;
for (String raw : lines) {
String line = raw.trim();
if (line.startsWith("---") && line.endsWith("---")) {
// line like: --- event.csv ---
String inner = line.substring(3, line.length() - 3).trim();
current = inner;
map.putIfAbsent(current, new ArrayList<>());
} else if (current != null) {
map.get(current).add(raw);
}
}
return map;
}

private static List<Event> parseEventLines(List<String> lines) {
List<Event> list = new ArrayList<>();
if (lines.isEmpty()) return list;
int startIdx = 0;
// detect header
if (lines.get(0).toLowerCase().startsWith("eventid")) startIdx = 1;
for (int i = startIdx; i < lines.size(); i++) {
String l = lines.get(i).trim();
if (l.isEmpty() || l.startsWith("#")) continue;
try {
Event e = Event.fromCSVLine(l);
list.add(e);
} catch (Exception ex) {
System.err.println("Skipping invalid event line during restore: " + l + " (" + ex.getMessage() + ")");
}
}
return list;
}
}
