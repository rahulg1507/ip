package nova.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import nova.exception.NovaException;
import nova.task.Deadline;
import nova.task.Event;
import nova.task.Task;
import nova.task.TaskList;
import nova.task.Todo;

/** Loads and saves Nova tasks in a file. */
public class Storage {
    /** The file used to store tasks. */
    private final Path taskFile;

    /** Creates storage backed by the default task file. */
    public Storage() {
        this(Path.of("data", "nova.txt"));
    }

    /** Creates storage backed by the specified file. */
    public Storage(Path taskFile) {
        this.taskFile = taskFile;
    }

    /** Loads saved tasks, ignoring malformed records. */
    public TaskList load() {
        TaskList tasks = new TaskList();
        if (!Files.exists(taskFile)) {
            return tasks;
        }
        int corruptedLineCount = 0;
        try {
            for (String line : Files.readAllLines(taskFile)) {
                if (!line.isBlank() && !addTaskFromLine(tasks, line)) {
                    corruptedLineCount++;
                }
            }
        } catch (IOException exception) {
            System.out.println(" Warning: Unable to read saved tasks. Starting with an empty list.");
            return new TaskList();
        }
        if (corruptedLineCount > 0) {
            System.out.println(" Warning: Ignored " + corruptedLineCount
                    + " corrupted task record(s).");
        }
        return tasks;
    }

    /** Saves all tasks atomically where supported by the file system. */
    public void save(TaskList tasks) throws NovaException {
        Path temporaryFile = taskFile.resolveSibling(taskFile.getFileName() + ".tmp");
        try {
            Files.createDirectories(taskFile.getParent());
            ArrayList<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(task.toStorageString());
            }
            Files.write(temporaryFile, lines);
            try {
                Files.move(temporaryFile, taskFile, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                Files.move(temporaryFile, taskFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            try {
                Files.deleteIfExists(temporaryFile);
            } catch (IOException ignoredException) {
                // Preserve the original save failure for the user.
            }
            throw new NovaException("Unable to save tasks.");
        }
    }

    /** Reconstructs one task from a storage record. */
    private static boolean addTaskFromLine(TaskList tasks, String line) {
        String[] parts = line.split("\\s*\\|\\s*", -1);
        if (parts.length < 3 || !isValidStatus(parts[1]) || parts[2].isBlank()) {
            return false;
        }
        String description = parts[2];
        Task task;
        if ("T".equals(parts[0]) && parts.length == 3) {
            task = new Todo(description);
        } else if ("D".equals(parts[0]) && parts.length == 4 && !parts[3].isBlank()) {
            try {
                task = new Deadline(description, LocalDate.parse(parts[3]));
            } catch (DateTimeParseException exception) {
                return false;
            }
        } else if ("E".equals(parts[0]) && parts.length == 5
                && !parts[3].isBlank() && !parts[4].isBlank()) {
            task = new Event(description, parts[3], parts[4]);
        } else if ("B".equals(parts[0]) && parts.length == 3) {
            task = new Task(description);
        } else {
            return false;
        }
        if ("1".equals(parts[1])) {
            task.markAsDone();
        }
        tasks.add(task);
        return true;
    }

    /** Returns whether a persisted completion value is supported. */
    private static boolean isValidStatus(String status) {
        return "0".equals(status) || "1".equals(status);
    }
}
