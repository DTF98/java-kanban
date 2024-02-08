package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private final ArrayList<Integer> subtaskIds = new ArrayList<>();

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public Epic(String title, String description) {
        super(title, description);
    }

    public Epic(String title, String description, int epicId) {
        super(title, description, epicId);
    }

    public ArrayList<Integer> getSubtasks() {
        return subtaskIds;
    }

    public void setSubtasks(Integer id) {
        this.subtaskIds.add(id);
    }

    private LocalDateTime endTime;

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void removeId(int id) {
        for (int i = 0; i < subtaskIds.size(); i++) {
            if (subtaskIds.get(i) == id) {
                subtaskIds.remove(i);
            }
        }
    }

    public void removeAllId() {
        subtaskIds.clear();
    }

    public String toString(String status) {
        if (super.startTime == null) {
            return super.getId() + "," + TasksType.EPIC + "," + super.getTitle() + "," + status
                    + "," + super.getDescription() + ",";
        } else {
            return super.getId() + "," + TasksType.EPIC + "," + super.getTitle() + "," + status
                    + "," + super.getDescription() + "," + super.startTime + "," + endTime + "," + duration + ",";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return getId() == epic.getId() && Objects.equals(getTitle(), epic.getTitle()) &&
                Objects.equals(getDescription(), epic.getDescription()) && Objects.equals(getStatus(), epic.getStatus());
    }
}
