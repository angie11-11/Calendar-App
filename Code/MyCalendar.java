/**
*Manages a calendar with one-time and recurring events. Events can be loaded from a file,
*added, removed, and retrieved based on specific dates.
* CS151 Hw1 Solution
*Instructor: Dr.Kim
* @author: Angie Do
* Date: 09/14/2024
*/



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class MyCalendar {
    // Declare events list variable
    private List<Event> oneTimeEvents;
    private List<Event> recurringEvents;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    /**
     * Initializes a new {@code MyCalendar} with empty lists for one-time and recurring events.
     */
    public MyCalendar() {
        this.oneTimeEvents = new ArrayList<>();
        this.recurringEvents = new ArrayList<>();
    }

    /**
     * Loads events from a  file.
     * @param filename the name of the file containing event data
     * @throws IOException if an I/O error occurs while reading the file
     */
    public void loadEvents(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            String eventName = line.trim();
            line = reader.readLine().trim();

            if (isRecurringEvent(line)) {
                // Recurring event
                processRecurringEvent(eventName, line);
            } else {
                // One-time event
                processOneTimeEvent(eventName, line);
            }
        }

        reader.close();
        System.out.println("Loading is done!");
    }

    /**
     * Process and add the event to recurring event if they are 
     * @param name the name of the recurring event
     * @param line the line containing event details
     */
    private void processRecurringEvent(String name, String line) {
        String[] parts = line.split(" ");
        String days = parts[0]; // Days of the week (e.g., TR)
        LocalTime startTime = LocalTime.parse(parts[1], TIME_FORMATTER);
        LocalTime endTime = LocalTime.parse(parts[2], TIME_FORMATTER);
        LocalDate startDate = LocalDate.parse(parts[3], DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(parts[4], DATE_FORMATTER);

        List<LocalDate> recurrenceDates = calculateRecurrenceDates(days, startDate, endDate);

        TimeInterval timeInterval = new TimeInterval(startTime, endTime);
        Event event = new Event(name, timeInterval, startDate, true, recurrenceDates);
        recurringEvents.add(event);
    }

    /**
     * Process and add the evnt to one-time event if they are
     * @param name the name of the one-time event
     * @param line the line containing event details
     */
    private void processOneTimeEvent(String name, String line) {
        String[] parts = line.split(" ");
        LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
        LocalTime startTime = LocalTime.parse(parts[1], TIME_FORMATTER);
        LocalTime endTime = LocalTime.parse(parts[2], TIME_FORMATTER);

        TimeInterval timeInterval = new TimeInterval(startTime, endTime);
        Event event = new Event(name, timeInterval, date, false, null);
        oneTimeEvents.add(event);
    }

    /**
     * Check if there is an recurring event.
     * @param line the line of text to check
     * @return {@code true} if the line matches the format for recurring events, {@code false} otherwise
     */
    private boolean isRecurringEvent(String line) {
        return line.matches("[SMTWRFA]+ .*");
    }

    /**
     * Calculates recurrence dates based on specified days of the week and date range.
     * @param days the days of the week when the event recurs (S, M, T, W, R, F, A)
     * @param startDate the start date of the recurrence period
     * @param endDate the end date of the recurrence period
     * @return a list of {@code LocalDate} instances representing the recurrence dates
     */
    private List<LocalDate> calculateRecurrenceDates(String days, LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            if (days.contains(currentDate.getDayOfWeek().toString().substring(0, 1))) {
                dates.add(currentDate);
            }
            currentDate = currentDate.plusDays(1); // Move to the next day
        }
        return dates;
    }

    /**
     * Process and add an event to the calendar.
     * @param event the {@code Event} to be added
     */
    public void add(Event event) {
        if (event.isRecurring()) {
            recurringEvents.add(event);
        } else {
            oneTimeEvents.add(event);
        }
    }

    /**
     * Delete an event from thelist
     * @param event the {@code Event} to be deleted
     */
    public void remove(Event event) {
        if (event.isRecurring()) {
            recurringEvents.removeIf(e -> e.getName().equals(event.getName()));
        } else {
            oneTimeEvents.remove(event);
        }
    }

    /**
     * Retrieves a list of events for a specific date.
     * @param date the {@code LocalDate} to find events for
     * @return a list of {@code Event} instances occurring on the specified date
     */
    public List<Event> getEventsForDate(LocalDate date) {
        List<Event> events = new ArrayList<>();
        
        // Add one-time events that match the date
        for (Event e : oneTimeEvents) {
            if (e.getDate().equals(date)) {
                events.add(e);
            }
        }

        // Add recurring events that match the date
        for (Event e : recurringEvents) {
            if (e.getRecurrenceDates().contains(date)) {
                events.add(e);
            }
        }

        return events;
    }

    /**
     * Returns a list of all one-time events in the calendar.
     * @return a list of  one-time events
     */
    public List<Event> getAllOneTimeEvents() {
        return new ArrayList<>(oneTimeEvents);
    }

    /**
     * Returns a list of all recurring events in the calendar.
     * 
     * @return a list of recurring events
     */
    public List<Event> getAllRecurringEvents() {
        return new ArrayList<>(recurringEvents);
    }
}
