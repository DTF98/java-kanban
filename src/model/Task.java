package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private final String title;
    private final String description;
    private int id;
    private TasksStatus taskStatus;
    protected long duration = 0;
    protected LocalDateTime startTime;

    public Task(String title, String description, LocalDateTime startTime, int duration) {
        this.title = title;
        this.description = description;
        this.taskStatus = TasksStatus.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.taskStatus = TasksStatus.valueOf(status);
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plusMinutes(duration);
        } else {
            return null;
        }

    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


    @Override
    public String toString() {
        if (startTime == null) {
            return id + "," + TasksType.TASK + "," + title + "," + taskStatus.toString()
                    + "," + description + ",";
        } else {
            return id + "," + TasksType.TASK + "," + title + "," + taskStatus.toString()
                    + "," + description + "," + startTime + "," + getEndTime() + "," + duration;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.getId() && Objects.equals(title, task.getTitle()) && Objects.equals(description,
                task.getDescription());
    }

}
