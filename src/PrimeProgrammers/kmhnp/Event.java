package PrimeProgrammers.kmhnp;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class Event {

    private String eventName;
    private LocalDate date;
    private String eventDescription;
    private int eventId;

    private static int nextEventId = 1;

    public Event(String eventName, LocalDate date, String eventDescription) {
        this.eventName = eventName;
        this.date = date;
        this.eventDescription = eventDescription;
        this.eventId = nextEventId++;
    }

    public Event(int eventId, String eventName, LocalDate date, String eventDescription) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.date = date;
        this.eventDescription = eventDescription;

        if(eventId >= nextEventId)
        {
            nextEventId = eventId + 1;
        }

    }

    public String getEventName() {
        return eventName;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String dateToString()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Corrected to lowercase yyyy
        return date.format(formatter);
    }

    public static LocalDate stringToDate(String dateString)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Use yyyy-MM-dd format!"); 
            return null;
        }
    }

    @Override
    public String toString()
    {
        return "Option: " + eventId + "| Event Name: " + eventName + "| Date: " + dateToString() + " | Event description: " + eventDescription;
    }


}