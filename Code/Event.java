/**
* The Event class represents one-time events and recurring events.
* CS151 Hw1 Solution
*Instructor: Dr.Kim
* @author: Angie Do
* Date: 09/14/2024
*/


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public class Event {
    // Declare variables
    private String name;
    private TimeInterval timeInterval;
    private LocalDate date;  // Used for one-time events
    private boolean recurring;
    private List<LocalDate> recurrenceDates;  // Used for recurring events

    /**
     * Constructs a new event with the specified details.
     * @param name              the name of the event
     * @param timeInterval      the start and end times of the event
     * @param date              the date of the event (used for one-time events)
     * @param recurring         {@code true} if the event is recurring, {@code false} otherwise
     * @param recurrenceDates   a {@code List} of {@code LocalDate} representing the dates of occurrence for recurring events
     */
    public Event(String name, TimeInterval timeInterval, LocalDate date, boolean recurring, List<LocalDate> recurrenceDates) {
        this.name = name;
        this.timeInterval = timeInterval;
        this.date = date;
        this.recurring = recurring;
        this.recurrenceDates = recurrenceDates;
    }

    /**
     * Set the start time of the event.
     * @return the start time of the event
     */
    public LocalTime getStartTime() {
        return timeInterval.getStart();
    }

    /**
     * Set the end time of the event.
     * @return the end time of the event
     */
    public LocalTime getEndTime() {
        return timeInterval.getEnd();
    }

    /**
     * Get the date of the event.
     * @return the date of the event (used for one-time events)
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Set the name of the event.
     * @return the {@code String} name of the event
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the event is recurring.
     * @return {@code true} if the event is recurring, {@code false} otherwise
     */
    public boolean isRecurring() {
        return recurring;
    }

    /**
     * Returns the list of recurrence dates for a recurring event.
     * @return a list for representing the recurrence dates
     */
    public List<LocalDate> getRecurrenceDates() {
        return recurrenceDates;
    }

    /**
     * Returns the TimeInterval of the event.
     * @return the start and end times of the event
     */
    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    /**
     * Returns a string representation of the event.
     * The format includes the event name followed by its time interval.
     * @return the name and time interval of the event
     */
    @Override
    public String toString() {
        return name + ": " + timeInterval;
    }

    /**
     * Returns a formatted string to save the event details.
     * For recurring events, it includes the recurrence days and time range, with start and end dates.
     * For one-time events, it includes the event date and time range.
     * @return the formatted details of the event for saving
     */
    public String toSave() {
        StringBuilder sb = new StringBuilder();
    
        if (recurring) {
            // For recurring events, format with days of the week and recurrence period
            StringBuilder daysOfWeek = new StringBuilder();
            recurrenceDates.stream()
                    .map(date -> date.getDayOfWeek().toString().substring(0, 1)) // Get first letter of day (M, T, W, etc.)
                    .distinct() // Ensure no repeated days
                    .forEach(daysOfWeek::append); // Append distinct days
    
            sb.append(name).append(" on ").append(daysOfWeek.toString())
              .append(" from ").append(timeInterval.getStart())
              .append(" to ").append(timeInterval.getEnd())
              .append(" starting on ").append(recurrenceDates.get(0))  // First occurrence
              .append(" until ").append(recurrenceDates.get(recurrenceDates.size() - 1));  // Last occurrence
        } else {
            // For one-time events, format with date and time range
            sb.append(name).append(" on ").append(date)
              .append(" from ").append(timeInterval.getStart())
              .append(" to ").append(timeInterval.getEnd());
        }
    
        return sb.toString();
    }
}
