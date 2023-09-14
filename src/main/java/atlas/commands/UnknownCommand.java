package atlas.commands;

import atlas.components.Storage;
import atlas.components.TaskList;

/**
 * Command used to handle invalid commands (e.g. bad parsing)
 */
public class UnknownCommand extends Command {
    private final String errorMsg;

    /**
     * Constructs an UnknownCommand object with the default error message
     */
    public UnknownCommand() {
        errorMsg = "I do not understand this command. "
                + "Here's what I can do though: I can create ToDos (todo), "
                + "Deadlines (deadline), Events (event), print them out (list), "
                + "print tasks occurring on a specific date (date) as well as "
                + "check (mark) and uncheck (unmark) them!";
    }

    /**
     * Constructs an UnknownCommand object with a specified error message
     * @param errorMsg Error message to print
     */
    public UnknownCommand(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String execute(TaskList taskList, Storage storage) {
        return errorMsg;
    }
}
