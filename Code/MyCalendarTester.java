/**
*A class navigates the option to control the event
* CS151 Hw1 Solution
*Instructor: Dr.Kim
* @author: Angie Do
* Date: 09/14/2024
*/


import java.io.FileWriter;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Comparator;


public class MyCalendarTester {
    private static LocalDate currentDate = LocalDate.now(); 
    // Default view is Month
    private static String currentView = "M"; 

     /**
     * Main method to start the calendar application.
     * @param args Command line arguments.
     */

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MyCalendar myCalendar = new MyCalendar();
        System.out.println("Calendar");
        CalendarPrinter calendarPrinter = new CalendarPrinter();
        calendarPrinter.printCalendar(currentDate, myCalendar); // Show the current view
        try {
            myCalendar.loadEvents("events.txt");
        } catch (Exception e) {
        }
        System.out.println("Done loading!!!");
       
        boolean running = true;
        while (running) {
            System.out.println("\nSelect one of the following main menu options:");
            System.out.println("[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
            String option = scanner.next().toUpperCase();

            switch (option) {
                case "V":
                    viewByMenu(scanner, myCalendar, calendarPrinter);
                    break;

                case "C":
                    createEvent(myCalendar, scanner);
                    break;

                case "G":
                    System.out.println("Enter a date (MM/DD/YYYY): ");
                    scanner.nextLine(); // consume newline
                    String goToDate = scanner.nextLine();
                    displayDayViewForDate(myCalendar, goToDate);
                    break;

                case "E":
                    displayEventList(myCalendar);
                    break;

                case "D":
                DeleteOption(myCalendar, scanner);
                    break;

                case "Q":
                    running = false;
                    Quitapp(myCalendar);
                    System.out.println("Good Bye");
                    break;

                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
        scanner.close();
    }


     /**
     * Handles the menu for viewing options.
     * @param scanner The scanner to read user input.
     * @param myCalendar The calendar containing events.
     * @param calendarPrinter The printer used to display the calendar.
     */

    public static void viewByMenu(Scanner scanner, MyCalendar myCalendar, CalendarPrinter calendarPrinter) {
        boolean running = true;
        while (running) {
            System.out.println("[D]ay view or [M]onth view ?");
            String viewstyle = scanner.next().toUpperCase();

            switch (viewstyle) {
                case "D":
                    // Reset currentDate to today for day view
                    currentDate = LocalDate.now();
                    currentView = "D";
                    displayDayView(myCalendar);
                    break;
                    
                case "M":
                    // Reset currentDate to the current month for month view
                    currentDate = LocalDate.now();
                    currentView = "M";
                    calendarPrinter.printCalendar(currentDate, myCalendar);
                    break;
                    
                default:
                    System.out.println("Invalid option. Try again.");
                    continue;
            }
            

            while (true) {
                System.out.println("[P]revious or [N]ext or [G]o back to the main menu ?");
                String option = scanner.next().toUpperCase();

                switch (option) {
                    //Move to the previous month and display the updated month view
                    case "P":
                        if (currentView.equals("D")) {
                            currentDate = currentDate.minusDays(1);
                            displayDayView(myCalendar);
                        } else if (currentView.equals("M")) {
                            currentDate = currentDate.minusMonths(1);
                            calendarPrinter.printCalendar(currentDate, myCalendar);
                        }
                        break;

                    // Move to the next month and display the updated month view
                    case "N": 
                        if (currentView.equals("D")) {
                            currentDate = currentDate.plusDays(1); 
                            System.out.println("Next day: " + currentDate);
                            displayDayView(myCalendar);
                        } else if (currentView.equals("M")) {
                            currentDate = currentDate.plusMonths(1); 
                            System.out.println("Next month: " + currentDate.getMonth());
                            calendarPrinter.printCalendar(currentDate, myCalendar);
                        }
                        break;

                    // Go back to the main menu 
                    case "G":
                        running = false;
                        return;

                    default:
                        System.out.println("Invalid option, please try again.");
                }
            }
        }
    }

    /**
     * Creates a new event based on user input.
     * @param myCalendar The calendar to which the event will be added.
     * @param scanner The scanner to read user input imformation to create event
     */
    public static void createEvent(MyCalendar myCalendar, Scanner scanner) {
        System.out.println("Enter the event's name: ");
        scanner.nextLine(); // consume newline
        String name = scanner.nextLine();

        System.out.println("Enter the date (MM/DD/YYYY): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MM/dd/yyyy")); // Convert String to LocalDate

        System.out.println("Enter the start time (HH:mm): ");
        String startTimeStr = scanner.nextLine();
        LocalTime startTime = LocalTime.parse(startTimeStr);

        System.out.println("Enter the end time (HH:mm): ");
        String endTimeStr = scanner.nextLine();
        LocalTime endTime = LocalTime.parse(endTimeStr);

        if (endTime.isBefore(startTime)) {
            System.out.println("The end-time must be after start time. Cannot create this event!");
            return;
        }

        TimeInterval newTimeInterval = new TimeInterval(startTime, endTime); // Initialize newTimeInterval

        // Check for time conflicts before adding the event
        List<Event> events = myCalendar.getEventsForDate(date); // Now passing LocalDate
        boolean conflict = events.stream().anyMatch(e -> e.getTimeInterval().overlaps(newTimeInterval));

        if (conflict) {
            System.out.println("Time conflict with an existing event. Cannot create this event!");
        } else {
            Event event = new Event(name, newTimeInterval, date, false, null);
            myCalendar.add(event);
            System.out.println("Event was created successfully.");
        }
    }

    /**
     * Displays the day view for the current date.
     * @param myCalendar The calendar containing events.
     */
    public static void displayDayView(MyCalendar myCalendar) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM d yyyy");
        System.out.println(formatter.format(currentDate));

        List<Event> events = myCalendar.getEventsForDate(currentDate);
        if (events.isEmpty()) {
            System.out.println("No events for today.");
        } else {
            System.out.println("Event for this day:");
            events.stream()
                .sorted((e1, e2) -> e1.getStartTime().compareTo(e2.getStartTime()))
                .forEach(System.out::println);
        }
    }

    /**
     * Displays the day view for a specified date 
     * @param myCalendar The calendar containing events.
     * @param dateStr The date in MM/DD/YYYY format.
     */
    public static void displayDayViewForDate(MyCalendar myCalendar, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        currentDate = date; // Update the currentDate to the specified date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM d yyyy");

        List<Event> events = myCalendar.getEventsForDate(date);
        if (events.isEmpty()) {
            System.out.println("No events on " + formatter.format(date));
        } else {
            System.out.println("Event on " + formatter.format(date));
            events.stream()
                .sorted((e1, e2) -> e1.getStartTime().compareTo(e2.getStartTime()))
                .forEach(System.out::println);
        }
    }

    /**
     * Displays a list of all events with detail and be sort by date and time
     * @param myCalendar The calendar containing one time events and recurring events
     */
    public static void displayEventList(MyCalendar myCalendar) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        System.out.println("One-time Events:");

        // Get one-time events
        List<Event> oneTimeEvents = myCalendar.getAllOneTimeEvents();
        List<Event> recurringEvents = myCalendar.getAllRecurringEvents();

        // Sort one-time events by date and start time
        Collections.sort(oneTimeEvents, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                if (e1.getDate().equals(e2.getDate())) {
                    return e1.getStartTime().compareTo(e2.getStartTime());
                }
                return e1.getDate().compareTo(e2.getDate());
            }
        });

        // Display sorted one-time events
        for (Event event : oneTimeEvents) {
            System.out.println(event.getName() + " on " + event.getDate().format(dateFormatter) + " from " + event.getStartTime() + " to " + event.getEndTime());
        }

        System.out.println("\nRecurring Events:");

        // Sort recurring events by date 
        Collections.sort(recurringEvents, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                LocalDate date1 = e1.isRecurring() ? e1.getRecurrenceDates().get(0) : e1.getDate();
                LocalDate date2 = e2.isRecurring() ? e2.getRecurrenceDates().get(0) : e2.getDate();
                return date1.compareTo(date2);
            }
        });
        
        // Display sorted recurring events
        for (Event event : recurringEvents) {
            LocalDate firstDay = event.getRecurrenceDates().get(0);
            LocalDate lastDay = event.getRecurrenceDates().get(event.getRecurrenceDates().size() - 1);
            
            System.out.println(event.getName()  + " with first day is " + firstDay.format(dateFormatter) + " and last day is " + lastDay.format(dateFormatter) + " from " + event.getStartTime() + " to " + event.getEndTime());        }
    }

    /**
     * Handles the delete option menu based on user input.
     * @param myCalendar The calendar from which events will be deleted.
     * @param scanner The scanner to read user input.
     */
       
    private static void DeleteOption(MyCalendar myCalendar, Scanner scanner) {
            System.out.println("Choose the deletion type:");
            System.out.println("[S]elected: Delete a specific one-time event.");
            System.out.println("[A]ll: Delete all one-time events on a specific date.");
            System.out.println("[DR]: Delete all occurrences of a recurring event.");
    
            scanner.nextLine(); 
        
            String option = scanner.nextLine().toUpperCase().trim(); 
    
            switch (option) {
                case "S":
                    deleteEvent(myCalendar, scanner, true);
                    break;
                case "A":
                    deleteEvent(myCalendar, scanner, false);
                    break;
                case "DR":
                    deleteRecurringEvent(myCalendar, scanner);
                    break;
                default:
                    System.out.println("Invalid option. Please choose S, A, or DR.");
                    break;
            }
    }
    

    /**
     * Deletes a specific or all one-time events on a given date if calendar have
     * @param myCalendar The calendar from which events will be deleted.
     * @param scanner The scanner to read user input.
     * @param isSelected If {@code true}, deletes a specific event; otherwise, deletes all one-time events on the date.
     */

    private static void deleteEvent(MyCalendar myCalendar, Scanner scanner, boolean isSelected) {
        LocalDate date = null;
        while (date == null) {
            System.out.println("Enter the date [MM/dd/yyyy]: ");
            try {
                String dateStr = scanner.nextLine().trim(); // Ensure to trim any extra whitespace
                date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in the format MM/dd/yyyy.");
            }
        }
    
        List<Event> EventChosen = myCalendar.getEventsForDate(date);
    
        if (EventChosen.isEmpty()) {
            System.out.println("No events found on this date.");
            return;
        }
    
        if (isSelected) {
            System.out.println("Events on this date:");
            EventChosen.forEach(event -> System.out.println(event.getName()));
    
            System.out.println("Enter the name of the event to delete: ");
            String eventName = scanner.nextLine().trim(); // Ensure to trim any extra whitespace
    
            Event RemoveEvent = EventChosen.stream()
                    .filter(event -> event.getName().equals(eventName) && !event.isRecurring())
                    .findFirst()
                    .orElse(null);
    
            if (RemoveEvent != null) {
                myCalendar.remove(RemoveEvent);
                System.out.println("Event '" + eventName + "' is deleted.");
            } else {
                System.out.println("Event not found or cannot delete recurring events.");
            }
        } else {
            EventChosen.stream()
                    .filter(event -> !event.isRecurring())
                    .forEach(myCalendar::remove);
    
            System.out.println("All one-time events on " + date + " are deleted.");
        }
    }
    
     /**
     * Deletes all occurrences of a recurring event if the calendar have
     * @param myCalendar The calendar from which recurring events will be deleted.
     * @param scanner The scanner to read user input.
     */
    private static void deleteRecurringEvent(MyCalendar myCalendar, Scanner scanner) {
        System.out.println("Enter the name of the recurring event to delete: ");
        String RecurringEvent = scanner.nextLine().trim(); // Ensure to trim any extra whitespace
    
        List<Event> recurringEvents = myCalendar.getAllRecurringEvents();
        List<Event> RemoveEvent = recurringEvents.stream()
                .filter(event -> event.getName().equals(RecurringEvent))
                .collect(Collectors.toList());
    
        if (RemoveEvent.isEmpty()) {
            System.out.println("No recurring event with the name '" + RecurringEvent + "' found.");
            return;
        }
    
        RemoveEvent.forEach(myCalendar::remove);
        System.out.println("Recurring event '" + RecurringEvent + "' is deleted.");
    }
      
    /**
     * Saves all events to an output file.
     * @param myCalendar The calendar containing events to save.
     */
    public static void Quitapp(MyCalendar myCalendar) {
        try (FileWriter writer = new FileWriter("output.txt")) {
            List<Event> oneTimeEvents = myCalendar.getAllOneTimeEvents();
            List<Event> recurringEvents = myCalendar.getAllRecurringEvents();

            writer.write("One-Time Events:\n");
            for (Event event : oneTimeEvents) {
                writer.write(event.toSave() + System.lineSeparator());
            }

            writer.write("\nRecurring Events:\n");
            for (Event event : recurringEvents) {
                writer.write(event.toSave() + System.lineSeparator());
            }

            System.out.println("Events saved to output.txt");
        } catch (IOException e) {
            System.out.println("An error occurred while saving events.");
            e.printStackTrace(); 
        }
    }
}
