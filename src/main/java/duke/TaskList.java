package duke;

import duke.task.Task;
import duke.task.Todo;
import duke.task.Deadline;
import duke.task.Event;

import java.time.LocalDate;
import java.util.ArrayList;

public class TaskList {

    private static int currentListSize;
    private static ArrayList<Task> taskList;

    public TaskList() {
        taskList = new ArrayList<>();
        currentListSize = 0;
    }

    public int getCurrentListSize() {
        return currentListSize;
    }

    public void setCurrentListSize(int currentListSize) {
        TaskList.currentListSize = currentListSize;
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void markAsUndone(int unmarkTaskIndex) {
        taskList.get(unmarkTaskIndex).setDone(false);
    }

    public void markAsDone(int markTaskIndex) {
        taskList.get(markTaskIndex).setDone(true);
    }

    public void deleteTask(int deleteTaskIndex) {
        taskList.remove(deleteTaskIndex);
        currentListSize--;
    }

    public void addToDoTask(String todoTaskName) {
        taskList.add(new Todo(todoTaskName));
        currentListSize++;
    }

    public void addDeadlineTask(String deadlineTaskName, LocalDate deadlineTaskByDate, String deadlineTaskByTime) {
        taskList.add(new Deadline(deadlineTaskName, deadlineTaskByDate, deadlineTaskByTime));
        currentListSize++;
    }

    public void addEventTask(String eventTaskName, LocalDate eventTaskAtDate, String eventTaskAtTime) {
        taskList.add(new Event(eventTaskName, eventTaskAtDate, eventTaskAtTime));
        currentListSize++;
    }
}
