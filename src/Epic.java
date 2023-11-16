import java.util.ArrayList;

public class Epic extends Task {

    ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public ArrayList<Integer> getSubtasks() {
        return subtaskIds;
    }

    public void setSubtasks(Integer id) {
        this.subtaskIds.add(id);
    }

    public void removeId(int id) {
        subtaskIds.remove(id);
    }

}
