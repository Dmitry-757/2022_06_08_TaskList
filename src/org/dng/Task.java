package org.dng;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Task implements Serializable {
    private static final long serialVersionUID = 44L;

    private static long lastTaskId;

    private long taskId;
    private String header;
    private String description;
    private PriorityLevel priorityLevel;
    private LocalDate creationDate;
    private LocalDate expirationDate;
    private String executor;
    private boolean taskDone;


    public Task(String header, String description,
                PriorityLevel priorityLevel, LocalDate creationDate,
                LocalDate expirationDate, String executor) {
        this.header = header;
        this.description = description;
        this.priorityLevel = priorityLevel;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.executor = executor;
        taskId = ++lastTaskId;
        TaskRepository.addTask(this);
    }

    public static long getLastTaskId() {
        return lastTaskId;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public boolean isTaskDone() {
        return taskDone;
    }

    public void setTaskDone(boolean taskDone) {
        this.taskDone = taskDone;
    }


    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", header='" + header + '\'' +
                ", description='" + description + '\'' +
                ", priorityLevel=" + priorityLevel +
                ", creationDate=" + creationDate +
                ", expirationDate=" + expirationDate +
                ", executor='" + executor + '\'' +
                ", taskDone=" + taskDone +
                '}';
    }

}
