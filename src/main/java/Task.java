/**
 * Represents a basic task with a description and completion status.
 * Specialized task types extend this class and customize their display.
 */
public class Task {
    /** The text describing this task. */
    protected String description;

    /** Whether this task has been completed. */
    protected boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns this task's description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the type prefix for this task.
     *
     * @return an empty string for a basic task
     */
    public String getTypeIcon() {
        return "";
    }

    /**
     * Returns the description portion of this task's display.
     *
     * @return the task description
     */
    public String getDisplayDescription() {
        return description;
    }

    /**
     * Returns the completion marker used in the task display.
     *
     * @return {@code "X"} when done, or a space when not done
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns this task in its display format.
     *
     * @return the status marker followed by the description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
