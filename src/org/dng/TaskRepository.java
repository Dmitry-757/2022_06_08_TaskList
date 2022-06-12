package org.dng;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class TaskRepository {
    private static LinkedHashSet<Task> taskSet = new LinkedHashSet<>();

    public static HashSet<Task> getTaskSet() {
        return taskSet;
    }

    public static void addTask(Task task){
        taskSet.add(task);
        IOService.writeToFile(task);
    }

}
