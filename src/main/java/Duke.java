import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Duke {
    // Chatbot's name
    static final String name = "Atlas";
    // Number of tasks currently in task list
    static int taskCount = 0;
    // Maximum number of tasks in task list
    static final int maxTaskCount = 100;
    // Task list
    static Task[] taskList = new Task[maxTaskCount];

    /**
     * Main function
     * @param args Arguments to initialise chatbot (none so far)
     */
    public static void main(String[] args) {
        greet();
        listen();
    }

    /**
     * Prints greeting to console.
     */
    protected static void greet() {
        String logo =
                "        _______ _                _____ \n" +
                        "     /\\|__   __| |        /\\    / ____|\n" +
                        "    /  \\  | |  | |       /  \\  | (___  \n" +
                        "   / /\\ \\ | |  | |      / /\\ \\  \\___ \\ \n" +
                        "  / ____ \\| |  | |____ / ____ \\ ____) |\n" +
                        " /_/    \\_\\_|  |______/_/    \\_\\_____/ \n";
        System.out.println(logo);
        printHorizontalLine();
        System.out.printf("Hello, I'm %s!\n", Duke.name);
        System.out.println("What would you like me to do today?");
        printHorizontalLine();
    }

    /**
     * Listens and executes commands
     */
    protected static void listen() {
        class UnknownCommandException extends RuntimeException {
            public UnknownCommandException(String command) {
                super("I don't know what this means :( You requested: " + command);
            }
        }
        String input, command, args;
        boolean exitChatbot = false;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (!exitChatbot) {
            try {
                input = reader.readLine();
                printHorizontalLine();
                String[] splitInput = input.split(" ", 2);
                command = splitInput[0];
                args = splitInput.length > 1 ? splitInput[1] : "";
                switch (command) {
                    case "todo":
                        addTask(args, "todo");
                        break;
                    case "deadline":
                        addTask(args, "deadline");
                        break;
                    case "event":
                        addTask(args, "event");
                        break;
                    case "list":
                        printList();
                        break;
                    case "bye":
                        exitChatbot = true;
                        break;
                    case "mark":
                        toggleTaskStatus(args, true);
                        break;
                    case "unmark":
                        toggleTaskStatus(args, false);
                        break;
                    default:
                        throw new UnknownCommandException(command);
                }
            } catch (UnknownCommandException e) {
                System.out.println(e.getMessage());
                System.out.println("Here's what I can do though: I can create ToDos (todo), \n" +
                        "Deadlines (deadline), Events (event), print them out (list), \n" +
                        "as well as check (mark) and uncheck (unmark) them!");
            } catch (IOException e) {
                System.out.println("Unable to read command, exiting");
                printHorizontalLine();
                exitChatbot = true;
            } finally {
                if (!exitChatbot) {
                    printHorizontalLine();
                }
            }
        }
        exit();
        printHorizontalLine();
    }

    /**
     * Adds task to list. If the list is already full, an error message is
     * instead printed and returned. If not, the item is added to the list.
     * @param args Item to add to list
     * @param taskType One of "todo", "deadline", or "event"
     */
    protected static void addTask(String args, String taskType) {
        class InvalidTaskType extends RuntimeException {
            final protected String taskType;
            public InvalidTaskType(String taskType) {
                this.taskType = taskType;
            }
        }
        if (taskCount == maxTaskCount) {
            System.out.println("Unable to add task due to exceeding max task count");
            return;
        }
        Task newTask;
        try {
            switch (taskType) {
                case "todo":
                    newTask = createTodo(args);
                    break;
                case "deadline":
                    newTask = createDeadline(args);
                    break;
                case "event":
                    newTask = createEvent(args);
                    break;
                default:
                    throw new InvalidTaskType(taskType);
            }
            taskList[taskCount++] = newTask;
            System.out.printf("Got it. I've added this task:\n" +
                    "\t%s\n" +
                    "Now you have %d %s in the list.\n", newTask, taskCount, taskCount == 1 ? "task" : "tasks");
        } catch (IllegalArgumentException e) {
            System.out.printf("I think you missed something! %s\n", e.getMessage());
        } catch (InvalidTaskType e) {
            System.out.printf("I can't handle this task type: %s\n", e.taskType);
        }
    }

    /**
     * Creates new Todo
     * @param name Name of Todo
     * @return new Todo with given name
     */
    protected static Todo createTodo(String name) {
        return new Todo(name);
    }

    /**
     * Create new Deadline
     * @param argString String containing name and date in the format "[name] \by [date]"
     * @return New Deadline with given name and date
     * @throws IllegalArgumentException argString improperly formatted: [name] \by [date]
     */
    protected static Deadline createDeadline(String argString) throws IllegalArgumentException {
        String[] args = argString.split(" /by ");
        if (args.length != 2) {
            throw new IllegalArgumentException("Deadlines should be created with the following format:\n" +
                    "deadline [name] /by [date]");
        }
        String name = args[0];
        String date = args[1];
        return new Deadline(name, date);
    }

    /**
     * Create new Event
     * @param argString String containing name, start time, and end time in the format
     *                  "[name] \from [start time] \to [end time]
     * @return New Event with given name, start time, and end time
     * @throws IllegalArgumentException argString improperly formatted: [name] \from [start time] \to [end time]
     */
    protected static Event createEvent(String argString) throws IllegalArgumentException {
        IllegalArgumentException badFormat = new IllegalArgumentException("Events should be created " +
                "with the following format\n: event [name] /from [start time] /to [end time]");
        String[] splitNameDates = argString.split(" /from ");
        if (splitNameDates.length != 2) {
            throw badFormat;
        }
        String name = splitNameDates[0];
        String[] splitTime = splitNameDates[1].split(" /to ");
        if (splitTime.length != 2) {
            throw badFormat;
        }
        String startTime = splitTime[0];
        String endTime = splitTime[1];
        return new Event(name, startTime, endTime);
    }

    /**
     * Prints list in the order in which items are added.
     */
    protected static void printList() {
        System.out.println("Here are your tasks:");
        for (int i = 0; i < taskCount; ++i) {
            System.out.printf("%d.%s\n", i + 1, taskList[i]);
        }
    }

    /**
     * Marks task as done
     * @param idx 1-based task index
     * @param checkTask Mark the task as done
     */
    protected static void toggleTaskStatus(String idx, boolean checkTask) {
        try {
            Task selectedTask = getTaskByIndex(idx);
            if (checkTask) {
                selectedTask.markAsDone();
                System.out.printf("Nice! I've marked this task as done:\n" +
                        "%s\n", selectedTask);
            } else {
                selectedTask.markAsNotDone();
                System.out.printf("OK, I've marked this task as not done yet:\n" +
                        "%s\n", selectedTask);
            }
        } catch (NumberFormatException e) {
            System.out.println("I need a positive integer to know which task you're referring to!");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("You're not referring to a valid task!");
        }
    }

    /**
     * Returns task from task list using 1-based index
     * @param args String containing task index (1-based index)
     * @return Task at selected index (e.g. if args is "2", returns 2nd task in list)
     * @throws IndexOutOfBoundsException Task index does not refer to a valid task
     */
    protected static Task getTaskByIndex(String args) throws IndexOutOfBoundsException {
        int idx = Integer.parseUnsignedInt(args, 10);
        if (idx <= 0 || idx > taskCount) {
            throw new IndexOutOfBoundsException();
        }
        return taskList[idx - 1];
    }

    /**
     * Prints goodbye to console.
     */
    protected static void exit() {
        System.out.println("Goodbye!");
    }

    /**
     * Prints a horizontal line containing the character '-' of width 80.
     */
    protected static void printHorizontalLine() {
        final int consoleWidth = 80;
        String line = "_".repeat(consoleWidth);
        System.out.println(line);
    }
}
