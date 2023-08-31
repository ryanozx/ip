import java.io.IOException;

/**
 * Command to add task to task list
 */
public class AddTaskCommand extends Command {
    protected final Task task;

    /**
     * Constructs an AddTaskCommand
     * @param t Task to be added
     */
    public AddTaskCommand(Task t) {
        task = t;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        taskList.addTask(task);
        try {
            ui.printToScreen("Got it. I've added this task:\n"
                    + "\t"+ task);
            ui.printToScreen(taskList.getCountString());
            storage.write(task);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        } catch (SecurityException e) {
            ui.showError("Permission not granted to create file!");
        }
    }
}