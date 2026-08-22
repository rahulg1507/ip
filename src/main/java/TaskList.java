import java.util.ArrayList;
import java.util.Iterator;

/** Owns the tasks currently managed by Nova. */
public class TaskList implements Iterable<Task> {
    /** The tasks in their user-visible order. */
    private final ArrayList<Task> tasks = new ArrayList<>();

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Inserts a task at the specified zero-based position. */
    public void add(int index, Task task) {
        tasks.add(index, task);
    }

    /** Removes and returns the task at the specified zero-based position. */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /** Returns the task at the specified zero-based position. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Returns the number of tasks. */
    public int size() {
        return tasks.size();
    }

    /** Allows callers to process every task without owning the collection. */
    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}
