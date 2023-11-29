package model;

import java.util.Objects;

public class Task {
    private final String title;
    private final String description;
    private int id;
    private TasksStatus taskStatus;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.taskStatus = TasksStatus.NEW;
    }

    public Task(String title, String description, int id) {
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public Task(int id, String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.taskStatus = TasksStatus.valueOf(status);
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return taskStatus.name();
    }

    public void setStatus(String status) {
        this.taskStatus = TasksStatus.valueOf(status);
    }

}
