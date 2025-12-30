/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author xuanpei
 */
import java.util.List;

public class ConflictChecker {

/**
* Returns true if newEvent conflicts with any event in allEvents (excluding same id)
*/
public static boolean hasConflict(Event newEvent, List<Event> allEvents) {
for (Event e : allEvents) {
if (e.getId() == newEvent.getId()) continue; // ignore same event (for updates)

boolean overlaps = newEvent.getStartDateTime().isBefore(e.getEndDateTime())
&& newEvent.getEndDateTime().isAfter(e.getStartDateTime());

if (overlaps) return true;
}
return false;
}
}

