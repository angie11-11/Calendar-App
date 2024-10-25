/**
*Responsible for printing a calendar for a given month and year, including
*highlighting days with events and marking today's date.
* CS151 Hw1 Solution
*Instructor: Dr.Kim
* @author: Angie Do
* Date: 09/14/2024
*/


import java.time.*;
import java.time.format.DateTimeFormatter;

public class CalendarPrinter {


    /**
     * Prints the calendar for the specified month and year, highlighting
     * days with events and marking today's date.
     * @param date       represent the month and year to print
     * @param myCalendar used to check for events on specific dates
     */

    public void printCalendar(LocalDate date, MyCalendar myCalendar) {
        // Display the month and year
        System.out.println(date.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        // Display as order Sunday Monday Tuesday Wednesday Thursday Friday Saturday
        System.out.println("Su Mo Tu We Th Fr Sa");

        // First day of the month and number of days in the month
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        int lengthOfMonth = date.lengthOfMonth();
        
        // Calculate the starting position (Sunday = 0)
        int startDay = firstDayOfMonth.getDayOfWeek().getValue() % 7;
        System.out.print("   ".repeat(startDay));

        // Print the days of the month
        for (int day = 1; day <= lengthOfMonth; day++) {
            LocalDate currentDay = firstDayOfMonth.withDayOfMonth(day);
            boolean hasEvent = hasEventOnDate(currentDay, myCalendar);
            boolean isToday = currentDay.equals(LocalDate.now());
        
            switch (isToday ? 1 : (hasEvent ? 2 : 3)) {
                case 1:
                    System.out.printf("[%2d] ", day); // Highlight today
                    break;
                case 2:
                    System.out.printf("{%2d} ", day); // Highlight event days
                    break;
                case 3:
                    System.out.printf("%2d ", day); // Regular days
                    break;
            }
        
            // Move to the next line after Saturday
            if ((startDay + day) % 7 == 0) {
                System.out.println();
            }
        }
        
        // Ensure the last row ends on a new line
        if ((startDay + lengthOfMonth) % 7 != 0) {
            System.out.println();
        }
    }

     /**
     * Checks if there is any events on the specified date.
     * @param date       the {@code LocalDate} to check for events
     * @param myCalendar the {@code MyCalendar} object used to check for events on the date
     * @return {@code true} if there are events on the specified date, {@code false} otherwise
     */

    private boolean hasEventOnDate(LocalDate date, MyCalendar myCalendar) {
        return !myCalendar.getEventsForDate(date).isEmpty();
    }
}
