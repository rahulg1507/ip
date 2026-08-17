/**
 * Represents a task with a description and completion status.
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

    /**
     * Returns the icon used to show this task's completion status.
     *
     * @return {@code "X"} when done, or a space when not done
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
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
}
