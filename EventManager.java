/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author xuanpei
 */
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class EventManager {
private static final String EVENT_FILE = "event.csv"; // relative path
private List<Event> events;
private int nextId = 1;

public EventManager() {
events = new ArrayList<>();
loadEvents();
}

public List<Event> getAllEvents() {
return Collections.unmodifiableList(events);
}

public synchronized Event addEvent(String title, String description, LocalDateTime start, LocalDateTime end) throws IOException {
Event e = new Event(nextId++, title, description, start, end);
events.add(e);
saveEvents();
return e;
}
public synchronized boolean updateEvent(Event updated) throws IOException {
for (int i = 0; i < events.size(); i++) {
if (events.get(i).getId() == updated.getId()) {
events.set(i, updated);
saveEvents();
return true;
}
}
return false;
}

public synchronized boolean deleteEvent(int id) throws IOException {
boolean removed = events.removeIf(ev -> ev.getId() == id);
if (removed) saveEvents();
return removed;
}

public synchronized void saveEvents() throws IOException {
Path path = Paths.get(EVENT_FILE);
try (BufferedWriter writer = Files.newBufferedWriter(path)) {
writer.write("eventId,title,description,startDateTime,endDateTime\n");
for (Event e : events) {
writer.write(e.toCSVLine());
writer.write('\n');
}
}
}
public final synchronized void loadEvents() {
events.clear();
Path path = Paths.get(EVENT_FILE);
if (!Files.exists(path)) {
// create file with header
try {
Files.createFile(path);
try (BufferedWriter writer = Files.newBufferedWriter(path)) {
writer.write("eventId,title,description,startDateTime,endDateTime\n");
}
} catch (IOException ex) {
System.err.println("Failed to create event.csv: " + ex.getMessage());
}
nextId = 1;
return;
}

try (BufferedReader reader = Files.newBufferedReader(path)) {
String header = reader.readLine(); // skip header
String line;
int maxId = 0;
while ((line = reader.readLine()) != null) {
line = line.trim();
if (line.isEmpty()) continue;
try {
Event e = Event.fromCSVLine(line);
events.add(e);
if (e.getId() > maxId) maxId = e.getId();
} catch (Exception ex) {
System.err.println("Skipping invalid event line: " + line);
}
}
nextId = maxId + 1;
} catch (IOException ex) {
System.err.println("Failed to read event.csv: " + ex.getMessage());
}
}
public synchronized void replaceAllEvents(List<Event> newEvents) throws IOException {
this.events = new ArrayList<>(newEvents);
// recalc nextId
int max = newEvents.stream().mapToInt(Event::getId).max().orElse(0);
this.nextId = max + 1;
saveEvents();
}

public synchronized void appendEvents(List<Event> extraEvents) throws IOException {
// ensure no id collision: if ids overlap, we remap appended events to new ids
int maxExisting = events.stream().mapToInt(Event::getId).max().orElse(0);
Map<Integer,Integer> remap = new HashMap<>();
for (Event e : extraEvents) {
if (events.stream().anyMatch(ev -> ev.getId() == e.getId())) {
int newId = ++maxExisting;
remap.put(e.getId(), newId);
Event remapped = new Event(newId, e.getTitle(), e.getDescription(), e.getStartDateTime(), e.getEndDateTime());
events.add(remapped);
} else {
events.add(e);
}
}
nextId = events.stream().mapToInt(Event::getId).max().orElse(0) + 1;
saveEvents();
}

}

