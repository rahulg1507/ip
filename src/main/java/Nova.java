import java.util.ArrayList;
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

        ArrayList<Task> tasks = new ArrayList<>();
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
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks.get(taskIndex));
                } else if (command.trim().equals("unmark") || command.startsWith("unmark ")) {
                int taskNumber = getTaskNumber(command, "unmark");
                int taskIndex = getTaskIndex(taskNumber, tasks.size());
                tasks.get(taskIndex).markAsNotDone();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks.get(taskIndex));
                } else if (command.trim().equals("delete") || command.startsWith("delete ")) {
                int taskNumber = getTaskNumber(command, "delete");
                int taskIndex = getTaskIndex(taskNumber, tasks.size());
                Task deletedTask = tasks.remove(taskIndex);
                System.out.println(" Noted. I've removed this task:");
                System.out.println("   " + deletedTask);
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.trim().equals("todo") || command.startsWith("todo ")) {
                String description = command.trim().equals("todo") ? "" : command.substring(5).trim();
                if (description.isEmpty()) {
                        throw new NovaException("Please add a description after 'todo'.");
                } else {
                    tasks.add(new Todo(description));
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
                String by = command.substring(byIndex + 5);
                tasks.add(new Deadline(description, by));
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
                tasks.add(new Event(description, from, to));
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
