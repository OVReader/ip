package duke.parser;

import duke.commands.Command;
import duke.commands.add.ToDoCommand;
import duke.commands.add.DeadlineCommand;
import duke.commands.add.EventCommand;
import duke.commands.ListCommand;
import duke.commands.UnmarkCommand;
import duke.commands.MarkCommand;
import duke.commands.DeleteCommand;
import duke.commands.ByeCommand;
import duke.commands.IncorrectCommand;

import duke.exception.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public Parser() {
    }

    public Command parseCommand(String userInput) throws MissingListIndexException, EmptyTaskDescriptionException,
            MissingDeadlineDateTimeReferenceException, MissingEventDateTimeReferenceException, ParseException,
            DateTimeParseException, MissingEventTimeException, InvalidTimeFormatException {
        // split the input into command and arguments
        String[] words = userInput.trim().split(" ", 2);
        if (words.length == 0) {
            return new IncorrectCommand();
        }

        final String commandWord = words[0];
        final String commandDetails = userInput.replaceFirst(commandWord, "").trim();

        switch (commandWord) {
        case ToDoCommand.COMMAND_WORD:
            return prepareToDoCommand(commandDetails);

        case DeadlineCommand.COMMAND_WORD:
            return prepareDeadlineCommand(commandDetails);

        case EventCommand.COMMAND_WORD:
            return prepareEventCommand(commandDetails);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case UnmarkCommand.COMMAND_WORD:
            return prepareUnmarkCommand(commandDetails);

        case MarkCommand.COMMAND_WORD:
            return prepareMarkCommand(commandDetails);

        case DeleteCommand.COMMAND_WORD:
            return prepareDeleteCommand(commandDetails);

        case ByeCommand.COMMAND_WORD:
            return new ByeCommand();

        default:
            return new IncorrectCommand();
        }
    }

    private static Command prepareToDoCommand(String commandDetails) throws EmptyTaskDescriptionException {
        checkForEmptyTaskDescription(commandDetails);
        return new ToDoCommand(commandDetails);
    }

    private static Command prepareDeadlineCommand(String commandDetails) throws EmptyTaskDescriptionException ,
            MissingDeadlineDateTimeReferenceException, ParseException, DateTimeParseException,
            InvalidTimeFormatException {
        checkForEmptyTaskDescription(commandDetails);
        try {
            String deadlineTaskName = extractTaskName(commandDetails, "/by");
            String deadlineTaskBy = extractTaskDateTime(commandDetails, "/by");
            ArrayList<String> processedDateTime = processTaskDateTime(deadlineTaskBy, "deadline");
            return new DeadlineCommand(deadlineTaskName, processedDateTime.get(0), processedDateTime.get(1));
        } catch (MissingDateTimeReferenceException e) {
            throw new MissingDeadlineDateTimeReferenceException();
        }
    }

    private static Command prepareEventCommand(String commandDetails)
            throws EmptyTaskDescriptionException, MissingEventDateTimeReferenceException, ParseException,
            InvalidTimeFormatException, MissingEventTimeException {
        checkForEmptyTaskDescription(commandDetails);
        try {
            String eventTaskName = extractTaskName(commandDetails, "/at");
            String eventTaskAt = extractTaskDateTime(commandDetails, "/at");
            checkForMissingEventStartEndTime(eventTaskAt);
            ArrayList<String> processedDateTime = processTaskDateTime(eventTaskAt, "event");
            checkForMissingEventTime(processedDateTime);
            return new EventCommand(eventTaskName, processedDateTime.get(0), processedDateTime.get(1));
        } catch (MissingDateTimeReferenceException e) {
            throw new MissingEventDateTimeReferenceException();
        }
    }

    private static void checkForMissingEventStartEndTime(String eventTaskAt) throws MissingEventTimeException {
        if (!eventTaskAt.contains("-")) {
            throw new MissingEventTimeException();
        }
    }

    private static void checkForMissingEventTime(ArrayList<String> processedDateTime) throws MissingEventTimeException {
        if (processedDateTime.get(1).isEmpty()) {
            throw new MissingEventTimeException();
        }
    }

    private static void checkForEmptyTaskDescription(String commandDetails) throws EmptyTaskDescriptionException {
        if (commandDetails.isEmpty()) {
            throw new EmptyTaskDescriptionException();
        }
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

    private static ArrayList<String> processTaskDateTime(String deadlineOrEventDateTime, String command) throws
            DateTimeParseException, ParseException, InvalidTimeFormatException {
        String date;
        String time = "";

        int dateTimePartitionIndex = deadlineOrEventDateTime.trim().indexOf(" ");
        boolean hasTime = (dateTimePartitionIndex != -1);

        if (hasTime) {
            date = deadlineOrEventDateTime.trim().substring(0, dateTimePartitionIndex).trim();
            time = deadlineOrEventDateTime.trim().substring(dateTimePartitionIndex + 1).trim();
            if (command.contains("deadline")) {
                checkForValidTimeFormat(time);
                time = processTime(time);
            }
            if (command.contains("event")) {
                //event start time - end time
                String startTime = time.substring(0, (time.indexOf("-"))).trim();
                String endTime = time.substring(time.indexOf("-") + 1).trim();
                checkForValidTimeFormat(startTime);
                checkForValidTimeFormat(endTime);
                String taskStartTime = processTime(startTime);
                String taskEndTime = processTime(endTime);
                time = taskStartTime + "-" + taskEndTime;
            }
        } else {
            date = deadlineOrEventDateTime.trim();
        }

        ArrayList<String> processedDateTime = new ArrayList<>();
        String taskDate = processTaskDate(date);
        processedDateTime.add(taskDate);
        processedDateTime.add(time);

        return processedDateTime;
    }

    private static String processTime(String time) {
        int hour   = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(2));

        String timeSuffix;
        boolean isBeforeNoon = (hour < 12);
        if (isBeforeNoon) {
            timeSuffix = "am";
        } else {
            hour -=12;
            timeSuffix = "pm";
        }

        String hourStr = String.valueOf(hour);
        String minuteStr = String.valueOf(minute);

        boolean hasMinute = (minute != 0);
        String formattedTime;
        if (hasMinute) {
            formattedTime = hourStr + "." + minuteStr + timeSuffix;
        } else {
            formattedTime = hourStr + timeSuffix;
        }
        return formattedTime;
    }

    private static String processTaskDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy");
        dateFormat.setLenient(false);
        dateFormat.parse(date);
        String[] dateDayMonthYear = date.trim().split("/", 3);
        //Convert to Year-Month-Day Format
        String dateYear  = dateDayMonthYear[2] ;
        String dateMonth = String.format("%02d", Integer.parseInt(dateDayMonthYear[1]));
        String dateDay   = String.format("%02d", Integer.parseInt(dateDayMonthYear[0]));
        return dateYear + "-" + dateMonth + "-" + dateDay;
    }

    private static void checkForValidTimeFormat(String timeString) throws InvalidTimeFormatException {
        String regex = "([01]?[0-9]|2[0-3])[0-5][0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(timeString);
        if (!matcher.matches()) {
            throw new InvalidTimeFormatException();
        }
    }

    private static Command prepareUnmarkCommand(String commandDetails)
            throws MissingListIndexException, IndexOutOfBoundsException, NumberFormatException {
        int taskIndex = extractTaskIndex(commandDetails);
        return new UnmarkCommand(taskIndex);
    }

    private static Command prepareMarkCommand(String commandDetails)
            throws MissingListIndexException, IndexOutOfBoundsException, NumberFormatException {
        int taskIndex = extractTaskIndex(commandDetails);
        return new MarkCommand(taskIndex);
    }

    private static Command prepareDeleteCommand(String commandDetails)
            throws MissingListIndexException, IndexOutOfBoundsException, NumberFormatException {
        int taskIndex = extractTaskIndex(commandDetails);
        return new DeleteCommand(taskIndex);
    }

    private static int extractTaskIndex(String commandDetails) throws MissingListIndexException {
        if (commandDetails.isEmpty()) {
            throw new MissingListIndexException();
        }
        return Integer.parseInt(commandDetails) - 1;
    }
}
