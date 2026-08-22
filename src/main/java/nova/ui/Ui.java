package nova.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import nova.exception.NovaException;
import nova.task.Task;
import nova.task.TaskList;

/** Handles Nova's console input and output. */
public class Ui {
    /** The divider used between console interactions. */
    private static final String DIVIDER = "____________________________________________________________";

    /** Reads commands from the console. */
    private final Scanner scanner;

    /** Creates a console UI using standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Prints Nova's greeting. */
    public void showGreeting() {
        System.out.println(DIVIDER);
        System.out.print(" _   _    ___    _   _    _\n"
                + "| \\ | |  / _ \\  | | | |  / \\\n"
                + "|  \\| | | | | | | | | | / _ \\\n"
                + "| |\\  | | |_| |  \\ V / / ___ \\\n"
                + "|_| \\_|  \\___/    \\_/ /_/   \\_\\\n");
        System.out.println("Hello! I'm Nova.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);
    }

    /** Returns whether another command is available. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Reads the next complete command. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Prints the interaction divider. */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /** Prints the farewell and its closing divider. */
    public void showFarewell() {
        System.out.println("Bye. Hope to see you again soon!");
        showDivider();
    }

    /** Prints all tasks in their numbered list format. */
    public void showTaskList(TaskList tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /** Prints tasks matching a requested date. */
    public void showTasksOn(LocalDate date, ArrayList<Task> tasks) {
        System.out.println(" Tasks on " + date + ":");
        for (Task task : tasks) {
            System.out.println(" " + task);
        }
    }

    /** Prints a successful task-addition confirmation. */
    public void showAdded(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /** Prints a completion confirmation. */
    public void showMarkedDone(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /** Prints an uncompletion confirmation. */
    public void showMarkedNotDone(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /** Prints a deletion confirmation. */
    public void showDeleted(Task task, int taskCount) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /** Prints a user-facing error. */
    public void showError(NovaException exception) {
        System.out.println(" " + exception.getMessage());
    }
}
