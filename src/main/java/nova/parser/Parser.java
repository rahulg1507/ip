package nova.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import nova.exception.NovaException;

/** Interprets user input as a validated Nova command. */
public class Parser {
    /** The command categories understood by Nova. */
    public enum CommandType {
        EXIT, LIST, FIND, ON, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT
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
        if (isCommandWithOptionalArgument(command, "find")) {
            return parseFindCommand(command);
        }
        if (command.startsWith("on ")) {
            return new ParsedCommand(CommandType.ON, 0, "", parseDate(command.substring(3).trim()), "", "");
        }
        if (isCommandWithOptionalArgument(command, "mark")) {
            return createTaskCommand(CommandType.MARK, command, "mark");
        }
        if (isCommandWithOptionalArgument(command, "unmark")) {
            return createTaskCommand(CommandType.UNMARK, command, "unmark");
        }
        if (isCommandWithOptionalArgument(command, "delete")) {
            return createTaskCommand(CommandType.DELETE, command, "delete");
        }
        if (isCommandWithOptionalArgument(command, "todo")) {
            return parseTodoCommand(command);
        }
        if (command.startsWith("deadline ")) {
            return parseDeadlineCommand(command);
        }
        if (command.startsWith("event ")) {
            return parseEventCommand(command);
        }
        throw new NovaException("I don't recognize that command.");
    }

    /** Returns whether a command is either complete or followed by an argument. */
    private static boolean isCommandWithOptionalArgument(String command, String commandWord) {
        return command.trim().equals(commandWord) || command.startsWith(commandWord + " ");
    }

    /** Parses a find command and validates its keyword. */
    private static ParsedCommand parseFindCommand(String command) throws NovaException {
        String keyword = command.trim().equals("find") ? "" : command.substring(5).trim();
        if (keyword.isEmpty()) {
            throw new NovaException("Please add a keyword after 'find'.");
        }
        return new ParsedCommand(CommandType.FIND, 0, keyword, null, "", "");
    }

    /** Parses a todo command and validates its description. */
    private static ParsedCommand parseTodoCommand(String command) throws NovaException {
        String description = command.trim().equals("todo") ? "" : command.substring(5).trim();
        if (description.isEmpty()) {
            throw new NovaException("Please add a description after 'todo'.");
        }
        return new ParsedCommand(CommandType.TODO, 0, description, null, "", "");
    }

    /** Parses a deadline command and validates its format and date. */
    private static ParsedCommand parseDeadlineCommand(String command) throws NovaException {
        int byIndex = command.indexOf(" /by ");
        if (byIndex <= 9 || byIndex + 5 >= command.length()) {
            throw new NovaException("Please use: deadline DESCRIPTION /by DATE.");
        }
        return new ParsedCommand(CommandType.DEADLINE, 0, command.substring(9, byIndex),
                parseDate(command.substring(byIndex + 5)), "", "");
    }

    /** Parses an event command and validates its time range. */
    private static ParsedCommand parseEventCommand(String command) throws NovaException {
        int fromIndex = command.indexOf(" /from ");
        int toIndex = command.indexOf(" /to ");
        if (fromIndex <= 6 || toIndex <= fromIndex + 7 || toIndex + 5 >= command.length()) {
            throw new NovaException("Please use: event DESCRIPTION /from START /to END.");
        }
        return new ParsedCommand(CommandType.EVENT, 0, command.substring(6, fromIndex), null,
                command.substring(fromIndex + 7, toIndex), command.substring(toIndex + 5));
    }

    /** Creates a parsed command whose only argument is a task number. */
    private static ParsedCommand createTaskCommand(CommandType type, String command, String commandWord)
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
