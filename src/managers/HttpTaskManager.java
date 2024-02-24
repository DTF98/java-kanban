package managers;

import client.KVTaskClient;
import com.google.gson.*;
import exception.TimeIntersectionException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    private final String key;

    private final Gson gson = Managers.getGson();

    public HttpTaskManager(URI KVUrl, HistoryManager newHistory, String key) throws IOException {
        super(KVUrl, newHistory);
        this.key = key;
        this.client = new KVTaskClient(KVUrl.toString());
    }

    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(tasks.get(taskId));
        client.put(key, sirializeManager());
        return tasks.get(taskId);
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        historyManager.add(subtasks.get(subtaskId));
        client.put(key, sirializeManager());
        return subtasks.get(subtaskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        historyManager.add(epics.get(epicId));
        client.put(key, sirializeManager());
        return epics.get(epicId);
    }

    @Override
    public void createTask(Task task) throws TimeIntersectionException {
        if (checkingIntersections(task)) {
            int id = this.uniqueId;
            this.uniqueId++;
            task.setId(id);
            tasks.put(id, task);
            sortedTasks.add(task);
        } else {
            throw new TimeIntersectionException("Задача пересекается с другими");
        }
        client.put(key, sirializeManager());
    }

    @Override
    public void createEpic(Epic epic) throws TimeIntersectionException {
        if (checkingIntersections(epic)) {
            int id = this.uniqueId;
            this.uniqueId++;
            epic.setId(id);
            epics.put(id, epic);
            sortedTasks.add(epic);
        } else {
            throw new TimeIntersectionException("Эпик пересекается с другими");
        }
        client.put(key, sirializeManager());
    }

    @Override
    public void createSubtask(Subtask subtask) throws TimeIntersectionException {
        if (checkingIntersections(subtask)) {
            int id = this.uniqueId;
            this.uniqueId++;
            epics.get(subtask.getEpicId()).setSubtasks(id);
            subtask.setId(id);
            subtasks.put(id, subtask);
            epics.get(subtask.getEpicId()).setStatus(estimateStatusEpic(epics.get(subtask.getEpicId())));
            sortedTasks.add(subtask);
            estimateStatusEpic(epics.get(subtask.getEpicId()));
            estimateStartTimeEpic(epics.get(subtask.getEpicId()));
            estimateEndTimeEpic(epics.get(subtask.getEpicId()));
            estimateDurationEpic(epics.get(subtask.getEpicId()));
        } else {
            throw new TimeIntersectionException("Подзадача пересекается с другими");
        }
        client.put(key, sirializeManager());
    }

    @Override
    public void updateTask(Task task) throws TimeIntersectionException {
        if (tasks.containsKey(task.getId())) {
            if (checkingIntersections(task)) {
                tasks.put(task.getId(), task);
                sortedTasks.removeIf(taskToDelete -> taskToDelete.getId() == task.getId());
                sortedTasks.add(task);
            } else {
                throw new TimeIntersectionException("Задача пересекается с другими");
            }
        } else {
            System.out.println("Такой задачи не существует!");
        }
        client.put(key, sirializeManager());
    }

    @Override
    public void updateEpic(Epic epic) throws TimeIntersectionException {
        if (epics.containsKey(epic.getId())) {
            if (checkingIntersections(epic)) {
                epics.put(epic.getId(), epic);
                sortedTasks.removeIf(epicToDelete -> epicToDelete.getId() == epic.getId());
                sortedTasks.add(epic);
            } else {
                throw new TimeIntersectionException("Эпик пересекается с другими");
            }
        } else {
            System.out.println("Такого эпика не существует!");
        }
        client.put(key, sirializeManager());
    }

    @Override
    public void updateSubtask(Subtask subtask) throws TimeIntersectionException {
        if (subtasks.containsKey(subtask.getId())) {
            if (checkingIntersections(subtask)) {
                subtasks.put(subtask.getId(), subtask);
                epics.get(subtask.getEpicId()).setStatus(estimateStatusEpic(epics.get(subtask.getEpicId())));
                sortedTasks.removeIf(subtaskToDelete -> subtaskToDelete.getId() == subtask.getId());
                sortedTasks.add(subtask);
                estimateStatusEpic(epics.get(subtask.getEpicId()));
                estimateStartTimeEpic(epics.get(subtask.getEpicId()));
                estimateEndTimeEpic(epics.get(subtask.getEpicId()));
                estimateDurationEpic(epics.get(subtask.getEpicId()));
            } else {
                throw new TimeIntersectionException("Подзадача пересекается с другими");
            }
        } else {
            System.out.println("Такой подзадачи не существует!");
        }
        client.put(key, sirializeManager());
    }

    @Override
    public void deleteTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
            sortedTasks.removeIf(task -> task.getId() == taskId);
        } else {
            System.out.println("Такой задачи не существует!");
        }
        client.put(key, sirializeManager());
    }

    @Override
    public void deleteEpicById(int epicId) {
        if (epics.containsKey(epicId)) {
            for (int i = 0; i < epics.get(epicId).getSubtaskIds().size(); i++) {
                subtasks.remove(epics.get(epicId).getSubtaskIds().get(i));
            }
            epics.remove(epicId);
            sortedTasks.removeIf(epic -> epic.getId() == epicId);
        } else {
            System.out.println("Такого эпика не существует!");
        }
        client.put(key, sirializeManager());
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            int epicId = subtasks.get(subtaskId).getEpicId();
            subtasks.remove(subtaskId);
            if (epics.containsKey(epicId)) {
                epics.get(epicId).removeId(subtaskId);
                epics.get(epicId)
                        .setStatus(estimateStatusEpic(epics.get(epicId)));
                sortedTasks.removeIf(subtask -> subtask.getId() == subtaskId);
            } else {
                System.out.println("Ошибка! Такого эпика не существует.");
            }
        } else {
            System.out.println("Ошибка! Такой подзадачи не существует.");
        }
        client.put(key, sirializeManager());
    }

    public String sirializeManager() {
        String json = "{";
        if (!tasks.isEmpty()) {
            json += "\"tasks\":";
            json += gson.toJson(tasks);
            json += ",";
        }
        if (!subtasks.isEmpty()) {
            json += "\"subtasks\":";
            json += gson.toJson(subtasks);
            json += ",";
        }
        if (!epics.isEmpty()) {
            json += "\"epics\":";
            json += gson.toJson(epics);
            json += ",";
        }
        json += "\"history\":";
        json += gson.toJson(historyManager.getHistory());
        json += "}";
        return json;
    }

    public HttpTaskManager desirializeManager(String gsonManager) throws IOException {
        HttpTaskManager newmanager = Managers.getDefault(Managers.getDefaultHistory(),
                URI.create("http://localhost:8078"), "MyKey2");
        JsonElement jsonElement = JsonParser.parseString(gsonManager);
        JsonObject obj = jsonElement.getAsJsonObject();
        Type type1 = new TypeToken<Map<Integer, Task>>() {
        }.getType();
        Type type2 = new TypeToken<Map<Integer, Subtask>>() {
        }.getType();
        Type type3 = new TypeToken<Map<Integer, Epic>>() {
        }.getType();
        Type type4 = new TypeToken<List<Task>>() {
        }.getType();
        if (obj.get("tasks") != null) {
            JsonElement o = obj.get("tasks");
            HashMap<Integer, Task> tasks = gson.fromJson(obj.get("tasks"), type1);
            newmanager.tasks.putAll(tasks);
        }
        if (obj.get("subtasks") != null) {
            HashMap<Integer, Subtask> subtasks = gson.fromJson(obj.get("subtasks"), type2);
            newmanager.subtasks.putAll(subtasks);
        }
        if (obj.get("epics") != null) {
            HashMap<Integer, Epic> epics = gson.fromJson(obj.get("epics"), type3);
            newmanager.epics.putAll(epics);
        }
        if (obj.get("history") != null) {
            List<Task> history = gson.fromJson(obj.get("history"), type4);
            history.forEach(newmanager.historyManager::add);
        }
        return newmanager;
    }

    public KVTaskClient getClient() {
        return client;
    }
}
