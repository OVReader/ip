package duke;

import duke.exception.EmptyTaskDescriptionException;
import duke.exception.MissingDateTimeReferenceException;
import duke.exception.MissingListIndexException;
import duke.exception.UndefinedCommandException;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.Todo;

import java.util.Scanner;
import java.util.ArrayList;

public class Duke {

    public static String readTaskDescription(Scanner in) throws EmptyTaskDescriptionException {
        String taskDescription = in.nextLine().trim();
        if (taskDescription.isEmpty()) {
            throw new EmptyTaskDescriptionException();
        }
        return taskDescription;
    }

    public static String extractTaskName(String taskDescription, String dateTimeReference)
            throws MissingDateTimeReferenceException {
        int dateTimeIndex = taskDescription.indexOf(dateTimeReference);
        boolean haveDataTimeReference = (dateTimeIndex != -1);
        if (!haveDataTimeReference) {
            throw new MissingDateTimeReferenceException();
        }
        return taskDescription.substring(0, dateTimeIndex - 1).trim();
    }

    public static String extractTaskDateTime(String taskDescription, String dateTimeReference)
            throws MissingDateTimeReferenceException {
        int dateTimeIndex = taskDescription.indexOf(dateTimeReference);
        boolean haveDataTimeReference = (dateTimeIndex != -1);
        if (!haveDataTimeReference) {
            throw new MissingDateTimeReferenceException();
        }
        return taskDescription.substring(dateTimeIndex + 3).trim();
    }

    public static void addToDoTask(ArrayList<Task> taskList, String todoTaskName) {
        taskList.add(new Todo(todoTaskName));
    }

    public static void addDeadlineTask(ArrayList<Task> taskList, String deadlineTaskName, String deadlineTaskBy) {
        taskList.add(new Deadline(deadlineTaskName, deadlineTaskBy));
    }

    public static void addEventTask(ArrayList<Task> taskList, String eventTaskName, String eventTaskAt) {
        taskList.add(new Event(eventTaskName, eventTaskAt));
    }

    public static void printTaskAddedMessage(ArrayList<Task> taskList, int maxTaskIndex) {
        boolean isSingleTask = (maxTaskIndex == 0);
        String task = isSingleTask ? " task" : " tasks";
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + taskList.get(maxTaskIndex));
        System.out.println("Now you have " + (maxTaskIndex + 1) + task + " in the list.");
    }

    public static void markAsDone(ArrayList<Task> taskList, Scanner in) {
        try {
            String markIndexString = in.nextLine().trim();
            if (markIndexString.isEmpty()) {
                throw new MissingListIndexException();
            }
            int markIndex = Integer.parseInt(markIndexString) - 1;
            taskList.get(markIndex).setDone(true);
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + taskList.get(markIndex));
        } catch (MissingListIndexException e) {
            System.out.println("☹ OOPS!!! To mark a task, please specify the task index.\nPlease try again.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("☹ OOPS!!! I'm sorry, the task index does not exist in the task list.");
        } catch (NumberFormatException e) {
            System.out.println("☹ OOPS!!! Please kindly provide a valid task index. (numerical/exist)");
        }
    }

    public static void markAsUndone(ArrayList<Task> taskList, Scanner in) {
        try {
            String markIndexString = in.nextLine().trim();
            if (markIndexString.isEmpty()) {
                throw new MissingListIndexException();
            }
            int unmarkIndex = Integer.parseInt(markIndexString) - 1;
            taskList.get(unmarkIndex).setDone(false);
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + taskList.get(unmarkIndex));
        } catch (MissingListIndexException e) {
            System.out.println("☹ OOPS!!! To unmark a task, please specify the task index.\nPlease try again.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("☹ OOPS!!! I'm sorry, the task index does not exist in the task list.");
        } catch (NumberFormatException e) {
            System.out.println("☹ OOPS!!! Please kindly provide a valid task index. (numerical/exist)");
        }
    }

    public static void printList(ArrayList<Task> taskList, int maxTaskIndex) {
        boolean hasNoTask = (maxTaskIndex == 0);
        if (hasNoTask) {
            System.out.println("There is no task in your list.");
            return;
        }
        boolean isSingleTask = (maxTaskIndex == 1);
        String taskString = isSingleTask ? "is the task" : "are the tasks";
        System.out.println("Here " + taskString + " in your list:");
        int taskIndex = 1;
        for (Task task : taskList) {
            System.out.println((taskIndex) + "." +  task);
            taskIndex++;
        }
    }

    public static void main(String[] args) {
        //Greet
        showWelcomeMessage();

        //Read Input
        Scanner in = new Scanner(System.in);    //create object that reads input
        String command = in.next();             //variable to store line (input)

        ArrayList<Task> taskList = new ArrayList<>();
        int maxTaskIndex = 0;
        while (!command.equals("bye")) {
            switch (command) {
            case "list":
                printList(taskList, maxTaskIndex);
                break;
            case "unmark":
                markAsUndone(taskList, in);
                break;
            case "mark":
                markAsDone(taskList, in);
                break;
            //Add Task
            case "todo":
                try {
                    String todoTaskName = readTaskDescription(in);
                    addToDoTask(taskList, todoTaskName);
                    printTaskAddedMessage(taskList, maxTaskIndex);
                    maxTaskIndex++;
                } catch (EmptyTaskDescriptionException e) {
                    showEmptyToDoDescriptionExceptionMessage();
                }
                break;
            case "deadline":
                try {
                    String deadlineTask = readTaskDescription(in);
                    String deadlineTaskName = extractTaskName(deadlineTask ,"/by");
                    String deadlineTaskBy   = extractTaskDateTime(deadlineTask ,"/by");
                    addDeadlineTask(taskList, deadlineTaskName, deadlineTaskBy);
                    printTaskAddedMessage(taskList, maxTaskIndex);
                    maxTaskIndex++;
                } catch (EmptyTaskDescriptionException e) {
                    showEmptyDeadlineDescriptionExceptionMessage();
                } catch (MissingDateTimeReferenceException e) {
                    showMissingDeadlineDateTimeReferenceExceptionMessage();
                }
                break;
            case "event":
                try {
                    String eventTask = readTaskDescription(in);
                    String eventTaskName = extractTaskName(eventTask ,"/at");
                    String eventTaskAt   = extractTaskDateTime(eventTask ,"/at");
                    addEventTask(taskList, eventTaskName, eventTaskAt);
                    printTaskAddedMessage(taskList, maxTaskIndex);
                    maxTaskIndex++;
                } catch (EmptyTaskDescriptionException e) {
                    showEmptyEventDescriptionExceptionMessage();
                } catch (MissingDateTimeReferenceException e) {
                    showMissingEventDateTimeReferenceExceptionMessage();
                }
                break;
            default:
                showUndefinedCommandMessage();
            }
            command = in.next();                //Read first word (command)
        }

        //Exit
        showByeMessage();
    }

    private static void showMissingDeadlineDateTimeReferenceExceptionMessage() {
        System.out.println("☹ OOPS!!! The description of a deadline requires a specific date/time denoted after '/by'.");
        System.out.println("Example: deadline return book /by Sunday");
        System.out.println("Please try again.");
    }

    private static void showMissingEventDateTimeReferenceExceptionMessage() {
        System.out.println("☹ OOPS!!! The description of a event requires a date and specific start & end time " +
                "denoted after '/at'.");
        System.out.println("Example: event project meeting /at Mon 2-4pm");
        System.out.println("Please try again.");
    }

    private static void showEmptyToDoDescriptionExceptionMessage() {
        System.out.println("☹ OOPS!!! The description of a todo cannot be empty.");
    }

    private static void showEmptyDeadlineDescriptionExceptionMessage() {
        System.out.println("☹ OOPS!!! The description of a deadline cannot be empty.");
    }

    private static void showEmptyEventDescriptionExceptionMessage() {
        System.out.println("☹ OOPS!!! The description of a event cannot be empty.");
    }

    private static void showUndefinedCommandMessage() {
        try {
            throw new UndefinedCommandException();
        } catch (UndefinedCommandException e) {
            System.out.println("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    private static void showByeMessage() {
        System.out.println("{\\__/}");
        System.out.println("(´^ω^)ノ Bye. Hope to see you again soon!");
        System.out.println("/ v v/");
    }

    private static void showWelcomeMessage() {

        String logo = "          @@@@@@@@@@@@@@@@%\n"
                + "       @@@%%%%%%%%%%%%%%%%%&@@\n"
                + "       @@%%%%%%%%%%###%#####%@@@@@@\n"
                + "        @@%%%%%##%@@@&@###@##@##@##@@\n"
                + "         @@@@&@####@###%@##@#@##&&#@@\n"
                + "        @@%@%%%@@##@@@@@@@@@@#/**@@@\n"
                + "         @@%@@@@@#*****************/@@\n"
                + "          /@@************#@@@@@@@@@@@@@@\n"
                + "          @@********%@@(******         *%@@\n"
                + "         @@(*******@@###*****            **@@\n"
                + "   @@@@@@@@((******@#####*****          ****@\n"
                + ",@@******@%((******@@######**************###@%\n"
                + "@@******%@((((******@@#####################@@\n"
                + "@@**/((((@((((********@@%###############&@@\n"
                + "@@(((((((@(((((***********(@@@@@@@@@&/**&@@\n"
                + "@@(((((((@(((((**************************@@\n"
                + "@@(((((((@#((((**************************@@\n"
                + "@@(((((((@&(((((*************************@@\n"
                + "@@(((((((@@((((((**********************((@@\n"
                + "@@(((((((@@(((((((((***************/(((((@/\n"
                + "@@(((((((@@(((((((((((((((((((((((((((((&@\n"
                + " @@((((((@@(((((((((((((((((((((((((((((@@\n"
                + "   @@@@@@@@((((((((((((@@@@@@@@@@@&(((((@@\n"
                + "         @@((((((((((((@%   @@((((((((((@@\n"
                + "         @@((((((((((((@    *@((((((((((@/\n"
                + "         %@(((((((((((@@     @%((((((((@@\n"
                + "         .@@((((((((((@@     @@@@@@@@@@.\n"
                + "             #@@@@@@*";

        //Greet
        System.out.println("Hello I'm Duke\n" + logo);
        System.out.println("What can I do for you?");
    }
}
