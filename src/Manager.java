import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int uniqueId;

    public ArrayList<String> getAllTasks() {
        ArrayList<String> list = new ArrayList<>();
        for (Task task : tasks.values()) {
            list.add(task.getTitle());
        }
        return list;
    }

    public ArrayList<String> getAllSubTasks() {
        ArrayList<String> list = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            list.add(subtask.getTitle());
        }
        return list;
    }

    public ArrayList<String> getAllEpics() {
        ArrayList<String> list = new ArrayList<>();
        for (Epic epic : epics.values()) {
            list.add(epic.getTitle());
        }
        return list;
    }

    public ArrayList<String> getSubtaskInEpic(int epicId) {
        ArrayList<String> list = new ArrayList<>();
        for (Integer subtask : epics.get(epicId).getSubtasks()) {
            list.add(subtasks.get(subtask).getTitle());
        }
        return list;
    }

    public void deleteAlltasks() {
        tasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public String getTaskById(int taskId) {
        return tasks.get(taskId).getTitle();
    }

    public String getSubtaskById(int subtaskId) {
        return epics.get(subtaskId).getTitle();
    }

    public String getEpicById(int epicId) {
        return epics.get(epicId).getTitle();
    }

    public void createTask(String title, String description) {
        int id = this.uniqueId + 1;
        if (searchRepeatTask(title)) {
            System.out.println("Такая задача уже существует");
        } else {
            tasks.put(id, new Task(title, description));
        }
    }

    public void createEpic(String title, String description) {
        int id = this.uniqueId + 1;
        if (searchRepeatEpic(title)) {
            System.out.println("Такой Эпик уже существует");
        } else {
            epics.put(id, new Epic(title, description));
        }
    }

    public void createSubtask(String title, String description, int epicId) {
        int id = this.uniqueId + 1;
        if (searchRepeatSubtask(title)) {
            System.out.println("Такая Подзадача уже существует");
        } else {
            epics.get(epicId).setSubtasks(id);
            subtasks.put(id, new Subtask(title, description, epicId));
            epics.get(epicId).setStatus(estimateStatusEpic(epics.get(epicId)));
        }
    }

    public void updateTask(int taskId, String title, String description, String status) {
        int id = this.uniqueId + 1;
        if (searchRepeatTask(title)) {
            System.out.println("Такая задача уже существует");
        } else {
            tasks.remove(taskId);
            tasks.put(id, new Task(title, description, status));
        }
    }

    public void updateEpic(int epicId, String title, String description) {
        for (int i = 0; i < epics.get(epicId).subtaskIds.size(); i++) {
            subtasks.remove(epics.get(epicId).subtaskIds.get(i));
        }
        epics.remove(epicId);
        int id = this.uniqueId + 1;
        epics.put(id, new Epic(title, description));
    }

    public void updateSubtask(int subtaskId, String title, String description, String status, int epicId) {
        subtasks.remove(subtaskId);
        epics.get(epicId).removeId(subtaskId);
        int id = this.uniqueId + 1;
        epics.get(epicId).setSubtasks(id);
        subtasks.put(id, new Subtask(title, description, epicId, status));
        epics.get(epicId).setStatus(estimateStatusEpic(epics.get(epicId)));
    }

    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteEpicById(int epicId) {
        epics.remove(epicId);
    }

    public void deleteSubtaskById(int subtaskId, int epicId) {
        subtasks.remove(subtaskId);
        epics.get(epicId).removeId(subtaskId);
    }

    private boolean searchRepeatTask(String title) {
        boolean flag = false;
        for (Task task : tasks.values()) {
            if (task.getTitle().equals(title)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private boolean searchRepeatEpic(String title) {
        boolean flag = false;
        for (Epic epic : epics.values()) {
            if (epic.getTitle().equals(title)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private boolean searchRepeatSubtask(String title) {
        boolean flag = false;
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getTitle().equals(title)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private String estimateStatusEpic(Epic epic) {
        int countNew = 0;
        int countDone = 0;
        for (int i = 0; i < epic.subtaskIds.size(); i++) {
            if (subtasks.get(epic.subtaskIds.get(i)).getStatus().equals("NEW")) {
                countNew++;
            }
        }
        for (int i = 0; i < epic.subtaskIds.size(); i++) {
            if (subtasks.get(epic.subtaskIds.get(i)).getStatus().equals("DONE")) {
                countDone++;
            }
        }
        if (countNew == epic.subtaskIds.size()) {
            return "NEW";
        } else if (countDone == epic.subtaskIds.size()) {
            return "DONE";
        } else {
            return "IN_PROGRESS";
        }
    }
}
