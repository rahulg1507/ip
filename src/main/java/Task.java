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

    /** The deadline text for this task, or {@code null} when it has no deadline. */
    protected String by;

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
        this(description, isTodo, null);
    }

    /**
     * Creates an incomplete deadline task with the given description and deadline text.
     *
     * @param description the text describing the task
     * @param by the plain-text deadline
     */
    public Task(String description, String by) {
        this(description, false, by);
    }

    /**
     * Creates an incomplete task with the given description, type, and optional deadline.
     *
     * @param description the text describing the task
     * @param isTodo whether the task is a todo task
     * @param by the plain-text deadline, or {@code null} when there is no deadline
     */
    public Task(String description, boolean isTodo, String by) {
        this.description = description;
        this.isDone = false;
        this.isTodo = isTodo;
        this.by = by;
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
     * @return {@code "[D]"} for a deadline, {@code "[T]"} for a todo, or an empty string otherwise
     */
    public String getTypeIcon() {
        if (by != null) {
            return "[D]";
        }
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

    /**
     * Returns the description with its deadline suffix when this task has a deadline.
     *
     * @return the task description, including deadline text when applicable
     */
    public String getDisplayDescription() {
        return by == null ? description : description + " (by: " + by + ")";
    }
}
