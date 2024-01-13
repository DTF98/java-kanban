package model;

import java.util.ArrayList;

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


    //    public String toString() {
//        return super.getId() + "," + TasksType.EPIC + "," + super.getTitle() + "," + super.getStatus()
//                + "," + super.getDescription() + ",";
//    }
    public String toString(String status) {
        return super.getId() + "," + TasksType.EPIC + "," + super.getTitle() + "," + status
                + "," + super.getDescription() + ",";
    }
}
