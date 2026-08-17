/**
 * Signals an invalid command or other input error in the Nova chatbot.
 */
public class NovaException extends Exception {
    /**
     * Creates an exception with a user-facing error message.
     *
     * @param message the explanation of the input error
     */
    public NovaException(String message) {
        super(message);
    }
}
