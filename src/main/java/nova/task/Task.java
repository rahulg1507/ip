package nova.task;

/**
 * Represents a basic task with a description and completion status.
 * Specialized task types extend this class and customize their display.
 */
public class Task {
    /** The text describing this task. */
    protected String description;

    /** The kind of this task. */
    protected final TaskType taskType;

    /** The completion state of this task. */
    protected TaskStatus status;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this(description, TaskType.BASIC);
    }

    /**
     * Creates an incomplete task of the given type.
     *
     * @param description the text describing the task
     * @param taskType the kind of task being created
     */
    protected Task(String description, TaskType taskType) {
        this.description = description;
        this.taskType = taskType;
        this.status = TaskStatus.NOT_DONE;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        status = TaskStatus.DONE;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        status = TaskStatus.NOT_DONE;
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
     * @return the task type's display prefix
     */
    public String getTypeIcon() {
        return taskType.getDisplayIcon();
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
     * @return the display marker for the current task status
     */
    public String getStatusIcon() {
        return status.getDisplayIcon();
    }

    /**
     * Returns this task in the format used for persistent storage.
     *
     * @return the task type, completion state, and description separated by pipes
     */
    public String toStorageString() {
        return taskType.name().charAt(0) + " | "
                + (status == TaskStatus.DONE ? "1" : "0") + " | " + description;
    }

    /**
     * Returns this task in its display format.
     *
     * @return the status marker followed by the description
     */
    @Override
    public String toString() {
        return getTypeIcon() + "[" + getStatusIcon() + "] " + description;
    }
}
