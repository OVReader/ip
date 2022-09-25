package duke;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Storage {
    private static final String DATA_FILEPATH = "data";
    private static final String DUKE_FILEPATH = "data/duke.txt";
    private static final File dataFile = new File(DATA_FILEPATH);
    private static final File dukeFile = new File(DUKE_FILEPATH);

    public Storage() {
    }

    public boolean findDataFile() throws SecurityException {
        return dataFile.mkdir();
    }

    public boolean findDukeTextFile() throws IOException {
        return dukeFile.createNewFile();
    }

    public int loadDukeTextFile(TaskList taskList) throws FileNotFoundException {
        String taskDescription;
        int taskIndex = 0;
        boolean isMarked;
        int taskDetailsSize;
        Scanner dukeScanner = new Scanner(dukeFile);

        while (dukeScanner.hasNext()) {
            taskDescription = dukeScanner.nextLine().trim();
            char taskType = taskDescription.charAt(0);

            if (taskType == 'D' || taskType == 'E' ) {
                taskDetailsSize = 5;
            } else {
                taskDetailsSize = 3;
            }

            String[] taskDetails = taskDescription.trim().split("\\|", taskDetailsSize);
            for (int i = 0; i < taskDetails.length; i++) {
                taskDetails[i] = taskDetails[i].trim();
            }

            String markedStatus = taskDetails[1];
            String taskName     = taskDetails[2];

            String date = "";
            String time = "";
            if (taskType == 'D' || taskType == 'E' ) {
                if (taskDetails.length == 5) {
                    time = taskDetails[4];
                }
                date = processDate(taskDetails[3]);

            }

            switch (taskType) {
            case 'T':
                taskList.addToDoTask(taskName);
                break;
            case 'D':
                LocalDate deadlineDate = LocalDate.parse(date);
                taskList.addDeadlineTask(taskName, deadlineDate, time);
                break;
            case 'E':
                LocalDate eventDate = LocalDate.parse(date);
                taskList.addEventTask(taskName, eventDate, time);
                break;
            default:
            }

            isMarked = (markedStatus.equals("1"));
            if (isMarked) {
                taskList.markAsDone(taskIndex);
            } else {
                taskList.markAsUndone(taskIndex);
            }
            taskIndex++;
        }
        return taskIndex;
    }

    private String processDate(String dateStr) {
        String[] dateMonthDayYear = dateStr.trim().split(" ", 3);
        HashMap<String, String> month = new HashMap<>();
        String[] monthName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (int i = 1; i <= monthName.length; i++) {
            month.put(monthName[i - 1], String.format("%02d", i));
        }
        String dateYear  = dateMonthDayYear[2];
        String dateMonth = month.get(dateMonthDayYear[0]);
        String dateDay   = String.format("%02d", Integer.parseInt(dateMonthDayYear[1]));
        return dateYear + "-" + dateMonth + "-" + dateDay;
    }

    public void updateDukeTextFile(int lineToEdit, Boolean isMarked) throws IOException {
        StringBuilder taskDescriptions = new StringBuilder();
        int lineNumber = 0;

        Scanner dukeScanner = new Scanner(dukeFile);
        while (dukeScanner.hasNext()) {
            String currentLine = dukeScanner.nextLine();
            if (lineNumber != lineToEdit) {
                taskDescriptions.append(currentLine).append(System.lineSeparator());
            } else if (isMarked != null) {
                taskDescriptions.append(updateMarkStatus(isMarked, currentLine)).append(System.lineSeparator());
            }
            lineNumber++;
        }
        writeToDukeTextFile(taskDescriptions.toString());
    }

    private static String updateMarkStatus(boolean isMarked, String currentLine) {
        String markStatus = isMarked ? "1" : "0";
        return currentLine.substring(0, 4) + markStatus + currentLine.substring(5);
    }

    public void addTaskToDukeTextFile(String addedTaskDescription) throws IOException {
        Scanner dukeScanner = new Scanner(dukeFile);
        String newDukeTextFileContent = copyFileContent(dukeScanner);
        newDukeTextFileContent += addedTaskDescription + System.lineSeparator();
        writeToDukeTextFile(newDukeTextFileContent);
    }

    private static String copyFileContent(Scanner dukeFileScanner) {
        StringBuilder taskDescriptions = new StringBuilder();
        while (dukeFileScanner.hasNext()) {
            String currentLine = dukeFileScanner.nextLine();
            taskDescriptions.append(currentLine).append(System.lineSeparator());
        }
        return taskDescriptions.toString();
    }

    private static void writeToDukeTextFile(String newDukeTextFileContent) throws IOException {
        FileWriter dukeFileWriter = new FileWriter(DUKE_FILEPATH);
        dukeFileWriter.write(newDukeTextFileContent);
        dukeFileWriter.close();
    }
}
