/** Command that ends the Nova session. */
public class ExitCommand extends Command {
    /** Shows the farewell message. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showFarewell();
    }

    /** @return true because this command ends the application */
    @Override
    public boolean isExit() {
        return true;
    }
}
