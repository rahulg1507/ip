/** Compatibility launcher for tools that expect a default-package Nova class. */
public class Nova {
    /** Delegates to the packaged application entry point. */
    public static void main(String[] args) {
        nova.Nova.main(args);
    }
}
