package nova.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Represents a task that must be completed by a calendar date. */
public class Deadline extends Task {
    /** The deadline date. */
    protected LocalDate by;

    /** The format used when showing dates to the user. */
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy");

    /**
     * Creates an incomplete deadline task.
     *
     * @param description the text describing the task
     * @param by the deadline date
     */
    public Deadline(String description, LocalDate by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    /** @return the task description with its deadline suffix */
    @Override
    public String getDisplayDescription() {
        return description + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Returns this deadline in the format used for persistent storage.
     *
     * @return the deadline type, completion state, description, and deadline separated by pipes
     */
    @Override
    public String toStorageString() {
        return super.toStorageString() + " | " + by;
    }

    /**
     * Returns this deadline task with its type prefix and deadline.
     *
     * @return the deadline display string
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }
}
