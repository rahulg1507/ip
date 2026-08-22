import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Interprets user input as a validated Nova command. */
public class Parser {
    /** The command categories understood by Nova. */
    public enum CommandType {
        EXIT, LIST, ON, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT
    }

    /** The validated result of parsing one user command. */
    public record ParsedCommand(CommandType type, int taskNumber, String description,
                                LocalDate date, String from, String to) {
    }

    /** Parses one complete command and extracts its arguments. */
    public ParsedCommand parse(String command) throws NovaException {
        if (command.equals("bye")) {
            return new ParsedCommand(CommandType.EXIT, 0, "", null, "", "");
        }
        if (command.equals("list")) {
            return new ParsedCommand(CommandType.LIST, 0, "", null, "", "");
        }
        if (command.startsWith("on ")) {
            return new ParsedCommand(CommandType.ON, 0, "", parseDate(command.substring(3).trim()), "", "");
        }
        if (command.trim().equals("mark") || command.startsWith("mark ")) {
            return taskCommand(CommandType.MARK, command, "mark");
        }
        if (command.trim().equals("unmark") || command.startsWith("unmark ")) {
            return taskCommand(CommandType.UNMARK, command, "unmark");
        }
        if (command.trim().equals("delete") || command.startsWith("delete ")) {
            return taskCommand(CommandType.DELETE, command, "delete");
        }
        if (command.trim().equals("todo") || command.startsWith("todo ")) {
            String description = command.trim().equals("todo") ? "" : command.substring(5).trim();
            if (description.isEmpty()) {
                throw new NovaException("Please add a description after 'todo'.");
            }
            return new ParsedCommand(CommandType.TODO, 0, description, null, "", "");
        }
        if (command.startsWith("deadline ")) {
            int byIndex = command.indexOf(" /by ");
            if (byIndex <= 9 || byIndex + 5 >= command.length()) {
                throw new NovaException("Please use: deadline DESCRIPTION /by DATE.");
            }
            return new ParsedCommand(CommandType.DEADLINE, 0, command.substring(9, byIndex),
                    parseDate(command.substring(byIndex + 5)), "", "");
        }
        if (command.startsWith("event ")) {
            int fromIndex = command.indexOf(" /from ");
            int toIndex = command.indexOf(" /to ");
            if (fromIndex <= 6 || toIndex <= fromIndex + 7 || toIndex + 5 >= command.length()) {
                throw new NovaException("Please use: event DESCRIPTION /from START /to END.");
            }
            return new ParsedCommand(CommandType.EVENT, 0, command.substring(6, fromIndex), null,
                    command.substring(fromIndex + 7, toIndex), command.substring(toIndex + 5));
        }
        throw new NovaException("I don't recognize that command.");
    }

    /** Parses a command whose only argument is a task number. */
    private static ParsedCommand taskCommand(CommandType type, String command, String commandWord)
            throws NovaException {
        try {
            int taskNumber = Integer.parseInt(command.substring(commandWord.length()).trim());
            return new ParsedCommand(type, taskNumber, "", null, "", "");
        } catch (NumberFormatException exception) {
            throw new NovaException("Please provide a valid task number.");
        }
    }

    /** Parses a date and reports malformed values consistently. */
    private static LocalDate parseDate(String value) throws NovaException {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            throw new NovaException("Please use a valid date in yyyy-MM-dd format.");
        }
    }
}
