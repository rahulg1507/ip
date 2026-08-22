import java.time.LocalDate;
import java.util.Scanner;

/**
 * Starts the Nova chatbot application.
 */
public class Nova {
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

        Storage storage = new Storage();
        TaskList tasks = storage.load();
        Parser parser = new Parser();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(divider);

            try {
                Parser.ParsedCommand parsedCommand = parser.parse(command);
                if (parsedCommand.type() == Parser.CommandType.EXIT) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(divider);
                break;
                }

                switch (parsedCommand.type()) {
                case LIST -> {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(" " + (i + 1) + "." + tasks.get(i));
                }
                }
                case ON -> {
                LocalDate date = parsedCommand.date();
                System.out.println(" Tasks on " + date + ":");
                for (Task task : tasks) {
                    if (occursOn(task, date)) {
                        System.out.println(" " + task);
                    }
                }
                }
                case MARK -> {
                int taskNumber = parsedCommand.taskNumber();
                int taskIndex = getTaskIndex(taskNumber, tasks.size());
                tasks.get(taskIndex).markAsDone();
                try {
                    storage.save(tasks);
                } catch (NovaException exception) {
                    tasks.get(taskIndex).markAsNotDone();
                    throw exception;
                }
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks.get(taskIndex));
                }
                case UNMARK -> {
                int taskNumber = parsedCommand.taskNumber();
                int taskIndex = getTaskIndex(taskNumber, tasks.size());
                tasks.get(taskIndex).markAsNotDone();
                try {
                    storage.save(tasks);
                } catch (NovaException exception) {
                    tasks.get(taskIndex).markAsDone();
                    throw exception;
                }
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks.get(taskIndex));
                }
                case DELETE -> {
                int taskNumber = parsedCommand.taskNumber();
                int taskIndex = getTaskIndex(taskNumber, tasks.size());
                Task deletedTask = tasks.remove(taskIndex);
                try {
                    storage.save(tasks);
                } catch (NovaException exception) {
                    tasks.add(taskIndex, deletedTask);
                    throw exception;
                }
                System.out.println(" Noted. I've removed this task:");
                System.out.println("   " + deletedTask);
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                }
                case TODO -> {
                addTaskAndSave(storage, tasks, new Todo(parsedCommand.description()));
                System.out.println(" Got it. I've added this task:");
                System.out.println("  " + tasks.get(tasks.size() - 1));
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                }
                case DEADLINE -> {
                addTaskAndSave(storage, tasks, new Deadline(parsedCommand.description(), parsedCommand.date()));
                System.out.println(" Got it. I've added this task:");
                System.out.println("  " + tasks.get(tasks.size() - 1));
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                }
                case EVENT -> {
                addTaskAndSave(storage, tasks, new Event(parsedCommand.description(),
                        parsedCommand.from(), parsedCommand.to()));
                System.out.println(" Got it. I've added this task:");
                System.out.println("  " + tasks.get(tasks.size() - 1));
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                }
                case EXIT -> { }
                }
            } catch (NovaException exception) {
                System.out.println(" " + exception.getMessage());
            }

            System.out.println(divider);
        }
    }

    /**
     * Adds a task and rolls back the addition if persistence fails.
     *
     * @param tasks the task list to update
     * @param task the task to add
     * @throws NovaException if the updated list cannot be saved
     */
    private static void addTaskAndSave(Storage storage, TaskList tasks, Task task) throws NovaException {
        tasks.add(task);
        try {
            storage.save(tasks);
        } catch (NovaException exception) {
            tasks.remove(tasks.size() - 1);
            throw exception;
        }
    }

    /** Returns whether a deadline or event occurs on the requested date. */
    private static boolean occursOn(Task task, LocalDate date) {
        if (task instanceof Deadline deadline) {
            return deadline.by.equals(date);
        }
        if (task instanceof Event event) {
            String dateText = date.toString();
            return event.from.contains(dateText) || event.to.contains(dateText);
        }
        return false;
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

}
