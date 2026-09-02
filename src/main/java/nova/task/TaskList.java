package nova.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import nova.exception.NovaException;

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

    /** Returns the task at a one-based user-facing position. */
    public Task getByNumber(int taskNumber) throws NovaException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new NovaException("Please provide a valid task number.");
        }
        return tasks.get(taskNumber - 1);
    }

    /** Marks the task at a one-based user-facing position as done. */
    public void markAsDone(int taskNumber) throws NovaException {
        getByNumber(taskNumber).markAsDone();
    }

    /** Marks the task at a one-based user-facing position as not done. */
    public void markAsNotDone(int taskNumber) throws NovaException {
        getByNumber(taskNumber).markAsNotDone();
    }

    /** Removes and returns the task at a one-based user-facing position. */
    public Task removeByNumber(int taskNumber) throws NovaException {
        getByNumber(taskNumber);
        return tasks.remove(taskNumber - 1);
    }

    /** Returns deadlines and events that occur on the requested date. */
    public ArrayList<Task> getTasksOnDate(LocalDate date) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task instanceof Deadline deadline && deadline.by.equals(date)) {
                matchingTasks.add(task);
            } else if (task instanceof Event event
                    && (event.from.contains(date.toString()) || event.to.contains(date.toString()))) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /** Returns tasks whose descriptions contain the keyword, ignoring letter case. */
    public ArrayList<Task> findByKeyword(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /** Allows callers to process every task without owning the collection. */
    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}
