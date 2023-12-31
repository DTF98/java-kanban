package managers;

import managers.HistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TasksStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int uniqueId = 1;
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager newHistory) {
        historyManager = newHistory;
    }


    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubTasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtaskInEpic(int epicId) {
        ArrayList<Subtask> list = new ArrayList<>();
        for (Integer subtask : epics.get(epicId).getSubtasks()) {
            list.add(subtasks.get(subtask));
        }
        return list;
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllId();
            epic.setStatus("NEW");
        }
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        historyManager.add(subtasks.get(subtaskId));
        return subtasks.get(subtaskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        historyManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public void createTask(Task task) {
        int id = this.uniqueId;
        this.uniqueId++;
        tasks.put(id, task);
    }

    @Override
    public void createEpic(Epic epic) {
        int id = this.uniqueId;
        this.uniqueId++;
        epics.put(id, epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        int id = this.uniqueId;
        this.uniqueId++;
        epics.get(subtask.getEpicId()).setSubtasks(id);
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).setStatus(estimateStatusEpic(epics.get(subtask.getEpicId())));
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Такой задачи не существует!");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Такого эпика не существует!");
        }

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).setStatus(estimateStatusEpic(epics.get(subtask.getEpicId())));
        } else {
            System.out.println("Такой подзадачи не существует!");
        }
    }

    @Override
    public void deleteTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        } else {
            System.out.println("Такой задачи не существует!");
        }

    }

    @Override
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

    @Override
    public void deleteSubtaskById(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            subtasks.remove(subtaskId);
            if (epics.containsKey(subtasks.get(subtaskId).getEpicId())) {
                epics.get(subtasks.get(subtaskId).getEpicId()).removeId(subtaskId);
                epics.get(subtasks.get(subtaskId).getEpicId()).setStatus(estimateStatusEpic(epics.get(subtasks.get(subtaskId).getEpicId())));
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
            if (Objects.equals(subtasks.get(epic.getSubtaskIds().get(i)).getStatus(), TasksStatus.NEW.name())) {
                countNew++;
            }
        }
        for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
            if (Objects.equals(subtasks.get(epic.getSubtaskIds().get(i)).getStatus(), TasksStatus.DONE.name())) {
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

