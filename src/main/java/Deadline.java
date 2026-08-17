/** Represents a task that must be completed by a plain-text deadline. */
public class Deadline extends Task {
    /** The plain-text deadline. */
    protected String by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description the text describing the task
     * @param by the plain-text deadline
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /** @return the deadline type prefix */
    @Override
    public String getTypeIcon() {
        return "[D]";
    }

    /** @return the task description with its deadline suffix */
    @Override
    public String getDisplayDescription() {
        return description + " (by: " + by + ")";
    }

    /**
     * Returns this deadline task with its type prefix and deadline.
     *
     * @return the deadline display string
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
