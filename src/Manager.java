import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int uniqueId;

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getAllSubTasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtaskInEpic(int epicId) {
        ArrayList<Subtask> list = new ArrayList<>();
        for (Integer subtask : epics.get(epicId).getSubtasks()) {
            list.add(subtasks.get(subtask));
        }
        return list;
    }

    public void deleteAlltasks() {
        tasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllId();
            epic.setStatus("NEW");
        }
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public Task getTaskById(int taskId) {
        return tasks.get(taskId);
    }

    public Subtask getSubtaskById(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    public Epic getEpicById(int epicId) {
        return epics.get(epicId);
    }

    public void createTask(Task task) {
        int id = this.uniqueId + 1;
        this.uniqueId++;
        tasks.put(id, task);
    }

    public void createEpic(Epic epic) {
        int id = this.uniqueId + 1;
        this.uniqueId++;
        epics.put(id, epic);
    }

    public void createSubtask(Subtask subtask) {
        int id = this.uniqueId + 1;
        this.uniqueId++;
        epics.get(subtask.getEpicId()).setSubtasks(id);
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).setStatus(estimateStatusEpic(epics.get(subtask.getEpicId())));
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Такой задачи не существует!");
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            for (int i = 0; i < epics.get(epic.getId()).getSubtaskIds().size(); i++) {
                subtasks.remove(epics.get(epic.getId()).getSubtaskIds().get(i));
            }
            epics.put(epic.getId(), epic); //Я не обновляю статус Эпика и точно не трогаю набор подзадач(Для этого
            // специально завел в конструкторе Эпика перегрузку)
        } else {
            System.out.println("Такого эпика не существует!");
        }

    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).setStatus(estimateStatusEpic(epics.get(subtask.getEpicId())));
        } else {
            System.out.println("Такой подзадачи не существует!");
        }
    }

    public void deleteTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        } else {
            System.out.println("Такой задачи не существует!");
        }

    }

    public void deleteEpicById(int epicId) {
        if (epics.containsKey(epicId)) {
            for (int i = 0; i < epics.get(epicId).getSubtaskIds().size(); i++) {
                subtasks.remove(epics.get(epicId).getSubtaskIds().get(i));
            }
            epics.remove(epicId);
        } else {
            System.out.println("Такого эпика не существует!");
        }

    }

    public void deleteSubtaskById(int subtaskId, int epicId) {
        if (subtasks.containsKey(subtaskId)) {
            subtasks.remove(subtaskId);
            if (epics.containsKey(epicId)) {
                epics.get(epicId).removeId(subtaskId);
            } else {
                System.out.println("Ошибка! Такого эпика не существует.");
            }
        } else {
            System.out.println("Ошибка! Такой подзадачи не существует.");
        }

    }

    private String estimateStatusEpic(Epic epic) {
        int countNew = 0;
        int countDone = 0;
        for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
            if (subtasks.get(epic.getSubtaskIds().get(i)).getStatus().equals("NEW")) {
                countNew++;
            }
        }
        for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
            if (subtasks.get(epic.getSubtaskIds().get(i)).getStatus().equals("DONE")) {
                countDone++;
            }
        }
        if (countNew == epic.getSubtaskIds().size()) {
            return "NEW";
        } else if (countDone == epic.getSubtaskIds().size()) {
            return "DONE";
        } else {
            return "IN_PROGRESS";
        }
    }
}

