/** Represents a todo task. */
public class Todo extends Task {
    /**
     * Creates an incomplete todo task.
     *
     * @param description the text describing the task
     */
    public Todo(String description) {
        super(description);
    }

    /** @return the todo type prefix */
    @Override
    public String getTypeIcon() {
        return "[T]";
    }

    /**
     * Returns this todo task with its type prefix.
     *
     * @return the todo display string
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
