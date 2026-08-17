/**
 * Represents a task with a description and completion status.
 */
public class Task {
    /** The text describing this task. */
    protected String description;

    /** Whether this task has been completed. */
    protected boolean isDone;

    /** Whether this task is a todo task. */
    protected boolean isTodo;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this(description, false);
    }

    /**
     * Creates an incomplete task with the given description and type.
     *
     * @param description the text describing the task
     * @param isTodo whether the task is a todo task
     */
    public Task(String description, boolean isTodo) {
        this.description = description;
        this.isDone = false;
        this.isTodo = isTodo;
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
     * Returns the icon used to show this task's type.
     *
     * @return {@code "[T]"} for a todo task, or an empty string otherwise
     */
    public String getTypeIcon() {
        return isTodo ? "[T]" : "";
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
