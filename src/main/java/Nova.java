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

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(divider);
                break;
            }

            if (command.equals("list")) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i].getTypeIcon()
                            + "[" + tasks[i].getStatusIcon()
                            + "] " + tasks[i].getDisplayDescription());
                }
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsDone();
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks[taskIndex].getTypeIcon() + "[X] "
                        + tasks[taskIndex].getDisplayDescription());
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks[taskIndex].getTypeIcon() + "[ ] "
                        + tasks[taskIndex].getDisplayDescription());
            } else if (command.startsWith("todo ")) {
                String description = command.substring(5);
                tasks[taskCount] = new Task(description, true);
                taskCount++;
                System.out.println(" Got it. I've added this task:");
                System.out.println("  [T][ ] " + description);
                System.out.println(" Now you have " + taskCount + " tasks in the list.");
            } else if (command.startsWith("deadline ")) {
                int byIndex = command.indexOf(" /by ");
                String description = command.substring(9, byIndex);
                String by = command.substring(byIndex + 5);
                tasks[taskCount] = new Task(description, by);
                taskCount++;
                System.out.println(" Got it. I've added this task:");
                System.out.println("  [D][ ] " + tasks[taskCount - 1].getDisplayDescription());
                System.out.println(" Now you have " + taskCount + " tasks in the list.");
            } else if (command.startsWith("event ")) {
                int fromIndex = command.indexOf(" /from ");
                int toIndex = command.indexOf(" /to ");
                String description = command.substring(6, fromIndex);
                String from = command.substring(fromIndex + 7, toIndex);
                String to = command.substring(toIndex + 5);
                tasks[taskCount] = new Task(description, from, to);
                taskCount++;
                System.out.println(" Got it. I've added this task:");
                System.out.println("  [E][ ] " + tasks[taskCount - 1].getDisplayDescription());
                System.out.println(" Now you have " + taskCount + " tasks in the list.");
            } else {
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println(" added: " + command);
            }

            System.out.println(divider);
        }
    }
}
