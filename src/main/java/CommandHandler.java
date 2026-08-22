/** Executes validated commands against the task list and persistence layer. */
public class CommandHandler {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /** Creates a handler for the application's shared collaborators. */
    public CommandHandler(Storage storage, TaskList tasks, Ui ui) {
        this.storage = storage;
        this.tasks = tasks;
        this.ui = ui;
    }

    /** Executes a parsed command and returns whether the application should exit. */
    public boolean execute(Parser.ParsedCommand command) throws NovaException {
        switch (command.type()) {
        case EXIT -> {
            Command exitCommand = new ExitCommand();
            exitCommand.execute(tasks, ui, storage);
            return exitCommand.isExit();
        }
        case LIST -> ui.showTaskList(tasks);
        case ON -> ui.showTasksOn(command.date(), tasks.getTasksOn(command.date()));
        case MARK -> {
            int taskNumber = command.taskNumber();
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
            int taskNumber = command.taskNumber();
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
            int taskNumber = command.taskNumber();
            Task deletedTask = tasks.removeByNumber(taskNumber);
            try {
                storage.save(tasks);
            } catch (NovaException exception) {
                tasks.add(taskNumber - 1, deletedTask);
                throw exception;
            }
            ui.showDeleted(deletedTask, tasks.size());
        }
        case TODO -> addAndShow(new Todo(command.description()));
        case DEADLINE -> addAndShow(new Deadline(command.description(), command.date()));
        case EVENT -> addAndShow(new Event(command.description(), command.from(), command.to()));
        }
        return false;
    }

    /** Adds a task, persists it, and reports the successful addition. */
    private void addAndShow(Task task) throws NovaException {
        tasks.add(task);
        try {
            storage.save(tasks);
        } catch (NovaException exception) {
            tasks.remove(tasks.size() - 1);
            throw exception;
        }
        ui.showAdded(tasks.get(tasks.size() - 1), tasks.size());
    }
}
