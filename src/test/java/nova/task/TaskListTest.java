package nova.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import nova.exception.NovaException;
import org.junit.jupiter.api.Test;

/** Tests task-list operations that drive the chatbot's core task behavior. */
class TaskListTest {
    /** Verifies that added tasks remain in insertion order. */
    @Test
    void addAndGet_tasksRemainInInsertionOrder() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("first"));
        tasks.add(new Todo("second"));

        assertEquals(2, tasks.size());
        assertEquals("first", tasks.get(0).getDescription());
        assertEquals("second", tasks.get(1).getDescription());
    }

    /** Verifies that a valid user-facing task number returns the task. */
    @Test
    void getByNumber_validNumberReturnsTask_zeroBasedCollectionRemainsHidden() throws NovaException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("first"));

        assertEquals("first", tasks.getByNumber(1).getDescription());
    }

    /** Verifies that an invalid task number produces a clear error. */
    @Test
    void getByNumber_invalidNumber_throwsClearError() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("only task"));

        NovaException exception = assertThrows(NovaException.class,
                () -> tasks.getByNumber(0));

        assertEquals("Please provide a valid task number.", exception.getMessage());
    }

    /** Verifies that marking operations affect only the selected task. */
    @Test
    void markOperations_changeOnlyTheSelectedTask() throws NovaException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("first"));
        tasks.add(new Todo("second"));

        tasks.markAsDone(2);
        assertFalse(tasks.get(0).getStatusIcon().equals("X"));
        assertEquals("X", tasks.get(1).getStatusIcon());

        tasks.markAsNotDone(2);
        assertEquals(" ", tasks.get(1).getStatusIcon());
    }

    /** Verifies that removal preserves the order of remaining tasks. */
    @Test
    void removeByNumber_removesSelectedTaskAndPreservesRemainingOrder() throws NovaException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("first"));
        tasks.add(new Todo("second"));
        tasks.add(new Todo("third"));

        Task removed = tasks.removeByNumber(2);

        assertEquals("second", removed.getDescription());
        assertEquals(2, tasks.size());
        assertEquals("first", tasks.get(0).getDescription());
        assertEquals("third", tasks.get(1).getDescription());
    }

    /** Verifies that invalid removal leaves the task list unchanged. */
    @Test
    void removeByNumber_invalidNumber_throwsWithoutChangingList() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("only task"));

        assertThrows(NovaException.class, () -> tasks.removeByNumber(2));
        assertEquals(1, tasks.size());
    }

    /** Verifies that date filtering returns matching deadlines and events only. */
    @Test
    void getTasksOn_returnsMatchingDeadlinesAndEvents_only() {
        LocalDate targetDate = LocalDate.of(2026, 8, 24);
        TaskList tasks = new TaskList();
        tasks.add(new Todo("ordinary task"));
        tasks.add(new Deadline("deadline", targetDate));
        tasks.add(new Event("event", "2026-08-24 9am", "2026-08-24 10am"));
        tasks.add(new Deadline("different date", targetDate.plusDays(1)));

        ArrayList<Task> matchingTasks = tasks.getTasksOn(targetDate);

        assertEquals(2, matchingTasks.size());
        assertEquals("deadline", matchingTasks.get(0).getDescription());
        assertEquals("event", matchingTasks.get(1).getDescription());
        assertTrue(matchingTasks.stream().noneMatch(task -> task instanceof Todo));
    }
}
