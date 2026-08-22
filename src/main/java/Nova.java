import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starts the Nova chatbot application.
 */
public class Nova {
    /** The file used to store the current task list. */
    private static final Path TASK_FILE = Path.of("data", "nova.txt");

    /**
     * Starts the Nova chatbot and processes commands until the input ends.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String divider = "____________________________________________________________";
        String banner = " _   _    ___    _   _    _\n"
                + "| \\ | |  / _ \\  | | | |  / \\\n"
                + "|  \\| | | | | | | | | | / _ \\\n"
                + "| |\\  | | |_| |  \\ V / / ___ \\\n"
                + "|_| \\_|  \\___/    \\_/ /_/   \\_\\\n";

        System.out.println(divider);
        System.out.print(banner);
        System.out.println("Hello! I'm Nova.");
        System.out.println("What can I do for you?");
        System.out.println(divider);

        ArrayList<Task> tasks = loadTasks();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(divider);

            try {
                if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(divider);
                break;
                }

                if (command.equals("list")) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(" " + (i + 1) + "." + tasks.get(i));
                }
                } else if (command.trim().equals("mark") || command.startsWith("mark ")) {
                int taskNumber = getTaskNumber(command, "mark");
                int taskIndex = getTaskIndex(taskNumber, tasks.size());
                tasks.get(taskIndex).markAsDone();
                try {
                    saveTasks(tasks);
                } catch (NovaException exception) {
                    tasks.get(taskIndex).markAsNotDone();
                    throw exception;
                }
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks.get(taskIndex));
                } else if (command.trim().equals("unmark") || command.startsWith("unmark ")) {
                int taskNumber = getTaskNumber(command, "unmark");
                int taskIndex = getTaskIndex(taskNumber, tasks.size());
                tasks.get(taskIndex).markAsNotDone();
                try {
                    saveTasks(tasks);
                } catch (NovaException exception) {
                    tasks.get(taskIndex).markAsDone();
                    throw exception;
                }
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks.get(taskIndex));
                } else if (command.trim().equals("delete") || command.startsWith("delete ")) {
                int taskNumber = getTaskNumber(command, "delete");
                int taskIndex = getTaskIndex(taskNumber, tasks.size());
                Task deletedTask = tasks.remove(taskIndex);
                try {
                    saveTasks(tasks);
                } catch (NovaException exception) {
                    tasks.add(taskIndex, deletedTask);
                    throw exception;
                }
                System.out.println(" Noted. I've removed this task:");
                System.out.println("   " + deletedTask);
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.trim().equals("todo") || command.startsWith("todo ")) {
                String description = command.trim().equals("todo") ? "" : command.substring(5).trim();
                if (description.isEmpty()) {
                        throw new NovaException("Please add a description after 'todo'.");
                } else {
                    addTaskAndSave(tasks, new Todo(description));
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("  " + tasks.get(tasks.size() - 1));
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                }
                } else if (command.startsWith("deadline ")) {
                int byIndex = command.indexOf(" /by ");
                if (byIndex <= 9 || byIndex + 5 >= command.length()) {
                    throw new NovaException("Please use: deadline DESCRIPTION /by DATE.");
                }
                String description = command.substring(9, byIndex);
                LocalDate by;
                try {
                    by = LocalDate.parse(command.substring(byIndex + 5));
                } catch (DateTimeParseException exception) {
                    throw new NovaException("Please use a valid date in yyyy-MM-dd format.");
                }
                addTaskAndSave(tasks, new Deadline(description, by));
                System.out.println(" Got it. I've added this task:");
                System.out.println("  " + tasks.get(tasks.size() - 1));
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.startsWith("event ")) {
                int fromIndex = command.indexOf(" /from ");
                int toIndex = command.indexOf(" /to ");
                if (fromIndex <= 6 || toIndex <= fromIndex + 7 || toIndex + 5 >= command.length()) {
                    throw new NovaException("Please use: event DESCRIPTION /from START /to END.");
                }
                String description = command.substring(6, fromIndex);
                String from = command.substring(fromIndex + 7, toIndex);
                String to = command.substring(toIndex + 5);
                addTaskAndSave(tasks, new Event(description, from, to));
                System.out.println(" Got it. I've added this task:");
                System.out.println("  " + tasks.get(tasks.size() - 1));
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                } else {
                    throw new NovaException("I don't recognize that command.");
                }
            } catch (NovaException exception) {
                System.out.println(" " + exception.getMessage());
            }

            System.out.println(divider);
        }
    }

    /**
     * Saves the current task list to the configured storage file.
     *
     * @param tasks the task list to save
     * @throws NovaException if the storage file cannot be written
     */
    private static void saveTasks(ArrayList<Task> tasks) throws NovaException {
        Path temporaryFile = TASK_FILE.resolveSibling(TASK_FILE.getFileName() + ".tmp");
        try {
            Files.createDirectories(TASK_FILE.getParent());
            ArrayList<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(task.toStorageString());
            }
            Files.write(temporaryFile, lines);
            try {
                Files.move(temporaryFile, TASK_FILE, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                Files.move(temporaryFile, TASK_FILE, StandardCopyOption.REPLACE_EXISTING);
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

    /**
     * Adds a task and rolls back the addition if persistence fails.
     *
     * @param tasks the task list to update
     * @param task the task to add
     * @throws NovaException if the updated list cannot be saved
     */
    private static void addTaskAndSave(ArrayList<Task> tasks, Task task) throws NovaException {
        tasks.add(task);
        try {
            saveTasks(tasks);
        } catch (NovaException exception) {
            tasks.remove(tasks.size() - 1);
            throw exception;
        }
    }

    /**
     * Loads tasks from the configured storage file when it exists.
     *
     * @return the tasks found in storage, or an empty list when no file exists
     */
    private static ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(TASK_FILE)) {
            return tasks;
        }

        int corruptedLineCount = 0;
        try {
            for (String line : Files.readAllLines(TASK_FILE)) {
                if (!line.isBlank()) {
                    boolean isTaskLoaded = addTaskFromStorageLine(tasks, line);
                    if (!isTaskLoaded) {
                        corruptedLineCount++;
                    }
                }
            }
        } catch (IOException exception) {
            System.out.println(" Warning: Unable to read saved tasks. Starting with an empty list.");
            return new ArrayList<>();
        }

        if (corruptedLineCount > 0) {
            System.out.println(" Warning: Ignored " + corruptedLineCount
                    + " corrupted task record(s).");
        }
        return tasks;
    }

    /**
     * Adds one valid persisted task line to the task list.
     *
     * @param tasks the task list being restored
     * @param line the persisted task line
     */
    private static boolean addTaskFromStorageLine(ArrayList<Task> tasks, String line) {
        String[] parts = line.split("\\s*\\|\\s*", -1);
        if (parts.length < 3 || !isValidStatus(parts[1]) || parts[2].isBlank()) {
            return false;
        }

        String type = parts[0];
        String description = parts[2];
        Task task;
        if ("T".equals(type) && parts.length == 3) {
            task = new Todo(description);
        } else if ("D".equals(type) && parts.length == 4 && !parts[3].isBlank()) {
            try {
                task = new Deadline(description, LocalDate.parse(parts[3]));
            } catch (DateTimeParseException exception) {
                return false;
            }
        } else if ("E".equals(type) && parts.length == 5
                && !parts[3].isBlank() && !parts[4].isBlank()) {
            task = new Event(description, parts[3], parts[4]);
        } else if ("B".equals(type) && parts.length == 3) {
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

    /**
     * Returns whether a persisted completion value is supported.
     *
     * @param status the persisted completion value
     * @return true if the value represents an incomplete or completed task
     */
    private static boolean isValidStatus(String status) {
        return "0".equals(status) || "1".equals(status);
    }

    /**
     * Converts a one-based task number into an ArrayList index after validating it.
     *
     * @param taskNumber the number entered by the user
     * @param taskCount the number of tasks currently stored
     * @return the zero-based array index
     * @throws NovaException if the number does not identify an existing task
     */
    private static int getTaskIndex(int taskNumber, int taskCount) throws NovaException {
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new NovaException("Please provide a valid task number.");
        }
        return taskNumber - 1;
    }

    /**
     * Extracts a task number from a command and reports malformed values consistently.
     *
     * @param command the complete user command
     * @param commandWord the command word before the task number
     * @return the parsed one-based task number
     * @throws NovaException if the task number is missing or not an integer
     */
    private static int getTaskNumber(String command, String commandWord) throws NovaException {
        try {
            return Integer.parseInt(command.substring(commandWord.length()).trim());
        } catch (NumberFormatException exception) {
            throw new NovaException("Please provide a valid task number.");
        }
    }
}
