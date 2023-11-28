import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTaskById(int taskId);

    void deleteEpicById(int epicId);

    void deleteSubtaskById(int subtaskId);

}
