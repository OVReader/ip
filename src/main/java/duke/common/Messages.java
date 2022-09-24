package duke.common;

public class Messages {
    public static final String MESSAGE_WELCOME_ICON = "          @@@@@@@@@@@@@@@@%\n"
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
            + "             #@@@@@@*\n";

    public static final String MESSAGE_WELCOME = "Hello I'm Duke\n" + MESSAGE_WELCOME_ICON + "What can I do for you?";

    public static final String MESSAGE_BYE = "{\\__/}\n(´^ω^)ノ Bye. Hope to see you again soon!\n/ v v/";

    public static final String MESSAGE_DATA_FILE_CREATION = "'data' directory has been created as it does not exist.";

    public static final String MESSAGE_DUKE_FILE_CREATION = "'duke.txt' has been created as it does not exist.";

    public static final String MESSAGE_DUKE_FILE_NOT_FOUND = "duke.txt cannot be found";

    public static final String MESSAGE_IO_EXCEPTION_TRIGGERED = "IO Exception Triggered";

    public static final String MESSAGE_SECURITY_EXCEPTION_TRIGGERED = "Security Exception Triggered";

    public static final String MESSAGE_MISSING_TASK_INDEX = "OOPS!!! To unmark/mark/delete a task, " +
            "please specify the task index.\nPlease try again.";

    public static final String MESSAGE_INCORRECT_NUMBER_FORMAT = "OOPS!!! Please kindly provide a valid task index. " +
            "(numerical/exist)";

    public static final String MESSAGE_OUT_OF_BOUNDS = "OOPS!!! I'm sorry, the task index does not exist" +
            " in the task list.";

    public static final String MESSAGE_EMPTY_TASK_DESCRIPTION = "OOPS!!! The description of a todo/deadline/event " +
            "cannot be empty.";

    public static final String MESSAGE_EMPTY_LIST = "There is no task in your list.";

    public static final String MESSAGE_DEADLINE_MISSING_BY = "OOPS!!! The description of a deadline requires " +
            "a specific date/time denoted after '/by'."  +
            "\nExample: deadline return book /by Sunday" +
            "\nPlease try again.";

    public static final String MESSAGE_EVENT_MISSING_AT = "OOPS!!! The description of a event requires " +
            "a date and specific start & end time denoted after '/at'." +
            "\nExample: event project meeting /at Mon 2-4pm" +
            "\nPlease try again.";

    public static final String MESSAGE_UNDEFINED_COMMAND = "OOPS!!! I'm sorry, but I don't know what that means :-(";
}
