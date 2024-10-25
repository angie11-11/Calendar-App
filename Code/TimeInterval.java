/**
* The TimeInterval class with a start and end time, and provide a method to check whether two intervals overlap
* CS151 Hw1 Solution
*Instructor: Dr.Kim
* @author: Angie Do
* Date: 09/14/2024
*/


import java.time.LocalTime;


public class TimeInterval {
    
    // Private variables
    private LocalTime start;
    private LocalTime end;

    /**
     * Default constructor with specified start and end times.
     * @param start the start time of the interval
     * @param end the end time of the interval
     */
    public TimeInterval(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Get the start time of this interval.
     * @return the start time 
     */
    public LocalTime getStart() {
        return start;
    }

    /**
     * Get the end time of this interval.
     * @return the end time 
     */
    public LocalTime getEnd() {
        return end;
    }

    /**
     * Checks if this interval overlaps with another interval.
     * @param other the other TimeInterval to check for overlap
     * @return {@code true} if the intervals overlap, {@code false} otherwise
     */
    public boolean overlaps(TimeInterval other) {
        return start.isBefore(other.getEnd()) && end.isAfter(other.getStart());
    }


    /**
     * Returns a string representation of this interval.
     * @return a {@code String} showing the start and end times of this interval
     */

    @Override
    public String toString() {
        return start + " - " + end;
    }
}
