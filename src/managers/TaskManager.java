package managers;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TimeIntersectionException;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {
    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, Subtask> getSubtasks();

    ArrayList<Task> getAllTasks();

    ArrayList<Subtask> getAllSubTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getSubtaskInEpic(int epicId);

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    Task getTaskById(int taskId);

    Subtask getSubtaskById(int subtaskId);

    Epic getEpicById(int epicId);

    void createTask(Task task) throws TimeIntersectionException;

    void createEpic(Epic epic) throws TimeIntersectionException;

    void createSubtask(Subtask subtask) throws TimeIntersectionException;

    void updateTask(Task task) throws TimeIntersectionException;

    void updateEpic(Epic epic) throws TimeIntersectionException;

    void updateSubtask(Subtask subtask) throws TimeIntersectionException;

    void deleteTaskById(int taskId);

    void deleteEpicById(int epicId);

    void deleteSubtaskById(int subtaskId);

}
