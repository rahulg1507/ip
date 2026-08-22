import java.time.LocalDate;
import java.util.ArrayList;

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
        Ui ui = new Ui();
        ui.showGreeting();
        Storage storage = new Storage();
        TaskList tasks = storage.load();
        Parser parser = new Parser();
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showDivider();

            try {
                Parser.ParsedCommand parsedCommand = parser.parse(command);
                if (parsedCommand.type() == Parser.CommandType.EXIT) {
                ui.showFarewell();
                break;
                }

                switch (parsedCommand.type()) {
                case LIST -> {
                ui.showTaskList(tasks);
                }
                case ON -> {
                LocalDate date = parsedCommand.date();
                ArrayList<Task> matchingTasks = new ArrayList<>();
                for (Task task : tasks) {
                    if (occursOn(task, date)) {
                        matchingTasks.add(task);
                    }
                }
                ui.showTasksOn(date, matchingTasks);
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
                ui.showMarkedDone(tasks.get(taskIndex));
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
                ui.showMarkedNotDone(tasks.get(taskIndex));
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
                ui.showDeleted(deletedTask, tasks.size());
                }
                case TODO -> {
                addTaskAndSave(storage, tasks, new Todo(parsedCommand.description()));
                ui.showAdded(tasks.get(tasks.size() - 1), tasks.size());
                }
                case DEADLINE -> {
                addTaskAndSave(storage, tasks, new Deadline(parsedCommand.description(), parsedCommand.date()));
                ui.showAdded(tasks.get(tasks.size() - 1), tasks.size());
                }
                case EVENT -> {
                addTaskAndSave(storage, tasks, new Event(parsedCommand.description(),
                        parsedCommand.from(), parsedCommand.to()));
                ui.showAdded(tasks.get(tasks.size() - 1), tasks.size());
                }
                case EXIT -> { }
                }
            } catch (NovaException exception) {
                ui.showError(exception);
            }

            ui.showDivider();
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
