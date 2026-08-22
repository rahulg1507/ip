import java.time.LocalDate;

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
                ui.showTasksOn(date, tasks.getTasksOn(date));
                }
                case MARK -> {
                int taskNumber = parsedCommand.taskNumber();
                Task task = tasks.getByNumber(taskNumber);
                tasks.markAsDone(taskNumber);
                try {
                    storage.save(tasks);
                } catch (NovaException exception) {
                    tasks.markAsNotDone(taskNumber);
                    throw exception;
                }
                ui.showMarkedDone(task);
                }
                case UNMARK -> {
                int taskNumber = parsedCommand.taskNumber();
                Task task = tasks.getByNumber(taskNumber);
                tasks.markAsNotDone(taskNumber);
                try {
                    storage.save(tasks);
                } catch (NovaException exception) {
                    tasks.markAsDone(taskNumber);
                    throw exception;
                }
                ui.showMarkedNotDone(task);
                }
                case DELETE -> {
                int taskNumber = parsedCommand.taskNumber();
                Task deletedTask = tasks.removeByNumber(taskNumber);
                try {
                    storage.save(tasks);
                } catch (NovaException exception) {
                    tasks.add(taskNumber - 1, deletedTask);
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

}
