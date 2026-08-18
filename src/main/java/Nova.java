import java.util.Scanner;

/**
 * Starts the Nova chatbot application.
 */
public class Nova {
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

        Task[] tasks = new Task[100];
        int taskCount = 0;
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
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
                } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                int taskIndex = getTaskIndex(taskNumber, taskCount);
                tasks[taskIndex].markAsDone();
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks[taskIndex]);
                } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7));
                int taskIndex = getTaskIndex(taskNumber, taskCount);
                tasks[taskIndex].markAsNotDone();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks[taskIndex]);
                } else if (command.startsWith("delete ")) {
                int taskNumber;
                try {
                    taskNumber = Integer.parseInt(command.substring(7).trim());
                } catch (NumberFormatException exception) {
                    throw new NovaException("Please provide a valid task number.");
                }
                int taskIndex = getTaskIndex(taskNumber, taskCount);
                Task deletedTask = tasks[taskIndex];
                for (int i = taskIndex; i < taskCount - 1; i++) {
                    tasks[i] = tasks[i + 1];
                }
                tasks[--taskCount] = null;
                System.out.println(" Noted. I've removed this task:");
                System.out.println("   " + deletedTask);
                System.out.println(" Now you have " + taskCount + " tasks in the list.");
                } else if (command.trim().equals("todo") || command.startsWith("todo ")) {
                String description = command.trim().equals("todo") ? "" : command.substring(5).trim();
                if (description.isEmpty()) {
                        throw new NovaException("Please add a description after 'todo'.");
                } else {
                    tasks[taskCount] = new Todo(description);
                    taskCount++;
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("  " + tasks[taskCount - 1]);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                }
                } else if (command.startsWith("deadline ")) {
                int byIndex = command.indexOf(" /by ");
                String description = command.substring(9, byIndex);
                String by = command.substring(byIndex + 5);
                tasks[taskCount] = new Deadline(description, by);
                taskCount++;
                System.out.println(" Got it. I've added this task:");
                System.out.println("  " + tasks[taskCount - 1]);
                System.out.println(" Now you have " + taskCount + " tasks in the list.");
                } else if (command.startsWith("event ")) {
                int fromIndex = command.indexOf(" /from ");
                int toIndex = command.indexOf(" /to ");
                String description = command.substring(6, fromIndex);
                String from = command.substring(fromIndex + 7, toIndex);
                String to = command.substring(toIndex + 5);
                tasks[taskCount] = new Event(description, from, to);
                taskCount++;
                System.out.println(" Got it. I've added this task:");
                System.out.println("  " + tasks[taskCount - 1]);
                System.out.println(" Now you have " + taskCount + " tasks in the list.");
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
     * Converts a one-based task number into an array index after validating it.
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
