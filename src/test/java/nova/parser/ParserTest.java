package nova.parser;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import nova.exception.NovaException;
import org.junit.jupiter.api.Test;

/** Tests command parsing and validation. */
class ParserTest {
    private final Parser parser = new Parser();

    @Test
    void parse_exitAndListCommands_returnsExpectedTypes() throws NovaException {
        Parser.ParsedCommand exit = parser.parse("bye");
        Parser.ParsedCommand list = parser.parse("list");

        assertAll(
                () -> assertEquals(Parser.CommandType.EXIT, exit.type()),
                () -> assertEquals(Parser.CommandType.LIST, list.type())
        );
    }

    @Test
    void parse_taskNumberCommands_extractsCommandAndNumber() throws NovaException {
        Parser.ParsedCommand mark = parser.parse("mark 12");
        Parser.ParsedCommand unmark = parser.parse("unmark 2");
        Parser.ParsedCommand delete = parser.parse("delete 1");

        assertAll(
                () -> assertEquals(Parser.CommandType.MARK, mark.type()),
                () -> assertEquals(12, mark.taskNumber()),
                () -> assertEquals(Parser.CommandType.UNMARK, unmark.type()),
                () -> assertEquals(2, unmark.taskNumber()),
                () -> assertEquals(Parser.CommandType.DELETE, delete.type()),
                () -> assertEquals(1, delete.taskNumber())
        );
    }

    @Test
    void parse_todoCommand_trimsDescription() throws NovaException {
        Parser.ParsedCommand result = parser.parse("todo   buy milk  ");

        assertAll(
                () -> assertEquals(Parser.CommandType.TODO, result.type()),
                () -> assertEquals("buy milk", result.description())
        );
    }

    @Test
    void parse_deadlineCommand_parsesIsoDate() throws NovaException {
        Parser.ParsedCommand result = parser.parse("deadline submit report /by 2026-08-24");

        assertAll(
                () -> assertEquals(Parser.CommandType.DEADLINE, result.type()),
                () -> assertEquals("submit report", result.description()),
                () -> assertEquals(LocalDate.of(2026, 8, 24), result.date())
        );
    }

    @Test
    void parse_eventCommand_extractsDescriptionAndTimeRange() throws NovaException {
        Parser.ParsedCommand result = parser.parse(
                "event project meeting /from Monday 9am /to Monday 10am");

        assertAll(
                () -> assertEquals(Parser.CommandType.EVENT, result.type()),
                () -> assertEquals("project meeting", result.description()),
                () -> assertEquals("Monday 9am", result.from()),
                () -> assertEquals("Monday 10am", result.to())
        );
    }

    @Test
    void parse_onCommand_parsesIsoDate() throws NovaException {
        Parser.ParsedCommand result = parser.parse("on 2026-08-24");

        assertAll(
                () -> assertEquals(Parser.CommandType.ON, result.type()),
                () -> assertEquals(LocalDate.of(2026, 8, 24), result.date())
        );
    }

    @Test
    void parse_invalidTaskNumber_throwsClearError() {
        NovaException exception = assertThrows(NovaException.class,
                () -> parser.parse("mark abc"));

        assertEquals("Please provide a valid task number.", exception.getMessage());
    }

    @Test
    void parse_missingTodoDescription_throwsClearError() {
        NovaException exception = assertThrows(NovaException.class,
                () -> parser.parse("todo"));

        assertEquals("Please add a description after 'todo'.", exception.getMessage());
    }

    @Test
    void parse_invalidDeadlineDate_throwsClearError() {
        NovaException exception = assertThrows(NovaException.class,
                () -> parser.parse("deadline submit report /by 2026-02-30"));

        assertEquals("Please use a valid date in yyyy-MM-dd format.", exception.getMessage());
    }

    @Test
    void parse_malformedDeadlineAndEventCommands_throwFormatErrors() {
        NovaException deadlineException = assertThrows(NovaException.class,
                () -> parser.parse("deadline submit report"));
        NovaException eventException = assertThrows(NovaException.class,
                () -> parser.parse("event meeting /from Monday"));

        assertAll(
                () -> assertEquals("Please use: deadline DESCRIPTION /by DATE.",
                        deadlineException.getMessage()),
                () -> assertEquals("Please use: event DESCRIPTION /from START /to END.",
                        eventException.getMessage())
        );
    }

    @Test
    void parse_unknownCommand_throwsClearError() {
        NovaException exception = assertThrows(NovaException.class,
                () -> parser.parse("what is this"));

        assertEquals("I don't recognize that command.", exception.getMessage());
    }
}
