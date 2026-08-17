/** Represents an event with plain-text start and end date/time values. */
public class Event extends Task {
    /** The plain-text event start date and time. */
    protected String from;

    /** The plain-text event end date and time. */
    protected String to;

    /**
     * Creates an incomplete event task.
     *
     * @param description the text describing the event
     * @param from the plain-text start date and time
     * @param to the plain-text end date and time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** @return the event type prefix */
    @Override
    public String getTypeIcon() {
        return "[E]";
    }

    /** @return the event description with its time-range suffix */
    @Override
    public String getDisplayDescription() {
        return description + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns this event task with its type prefix and time range.
     *
     * @return the event display string
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
