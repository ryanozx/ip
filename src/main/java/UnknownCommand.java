/**
 * Represents a command that cannot be parsed correctly
 */
public class UnknownCommand extends Command {
    private final String errorMsg;

    /**
     * Constructs an Unknown Command object with the default error message
     */
    public UnknownCommand() {
        errorMsg = "I do not understand this command.\n"
                + "Here's what I can do though: I can create ToDos (todo),\n"
                + "Deadlines (deadline), Events (event), print them out (list),\n"
                + "print tasks occurring on a specific date (date) as well as \n"
                + "check (mark) and uncheck (unmark) them!";
    }

    /**
     * Constructs an Unknown Command object with a specified error message
     * @param errorMsg Error message to print
     */
    public UnknownCommand(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showError(errorMsg);
    }
}