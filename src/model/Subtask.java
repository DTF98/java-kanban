package model;

import model.Task;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private final int epicId;

    public int getEpicId() {
        return epicId;
    }

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int epicId, LocalDateTime startTime, int duration) {
        super(title, description, startTime, duration);
        this.epicId = epicId;

    }

    public Subtask(int subtaskId, String title, String description, String status, int epicId) {
        super(subtaskId, title, description, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        if (super.startTime == null) {
            return super.getId() + "," + TasksType.SUBTASK + "," + super.getTitle() + "," + super.getStatus()
                    + "," + super.getDescription() + "," + epicId + ",";
        } else {
            return super.getId() + "," + TasksType.SUBTASK + "," + super.getTitle() + "," + super.getStatus()
                    + "," + super.getDescription() + "," + epicId + "," + super.startTime
                    + "," + super.getEndTime() + "," + duration;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return getId() == subtask.getId() && Objects.equals(getTitle(), subtask.getTitle()) &&
                Objects.equals(getDescription(), subtask.getDescription()) && getEpicId() == subtask.getEpicId();
    }
}
