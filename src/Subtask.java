public class Subtask extends Task {
    private final int epicId;

    public int getEpicId() {
        return epicId;
    }

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public Subtask(int subtaskId, String title, String description, String status, int epicId) {
        super(subtaskId, title, description, status);
        this.epicId = epicId;
    }
}
