package managers;

import model.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(HistoryManager newHistory, File newFile) {
        super(newHistory);
        this.file = newFile;
    }
    

    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(tasks.get(taskId));
        save();
        return tasks.get(taskId);
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        historyManager.add(subtasks.get(subtaskId));
        save();
        return subtasks.get(subtaskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        historyManager.add(epics.get(epicId));
        save();
        return epics.get(epicId);
    }

    @Override
    public void createTask(Task task) throws TimeIntersectionException {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) throws TimeIntersectionException {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) throws TimeIntersectionException {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) throws TimeIntersectionException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws TimeIntersectionException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws TimeIntersectionException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        super.deleteSubtaskById(subtaskId);
        save();
    }

    public void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic" + "\n");
            if (!getAllTasks().isEmpty()) {
                for (Task task : getAllTasks()) {
                    fileWriter.write(task.toString() + "\n");
                }
            }
            if (!getAllEpics().isEmpty()) {
                for (Epic epic : getAllEpics()) {
                    fileWriter.write(epic.toString(estimateStatusEpic(epic)) + "\n");
                }
            }
            if (!getAllSubTasks().isEmpty()) {
                for (Subtask subtask : getAllSubTasks()) {
                    fileWriter.write(subtask.toString() + "\n");
                }
            }
            fileWriter.write("\n" + historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи данных в файл!");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws TimeIntersectionException {
        List<String> newManager = new ArrayList<>();
        HistoryManager newHistory = new InMemoryHistoryManager();
        FileBackedTasksManager manager = new FileBackedTasksManager(newHistory, file);
        try (FileReader reader = new FileReader(file); BufferedReader br = new BufferedReader(reader)) {
            while (br.ready()) {
                newManager.add(br.readLine());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        List<Integer> buf = new ArrayList<>(historyFromString(newManager.get(newManager.size() - 1)));
        for (int i = 1; i < newManager.size() - 2; i++) {
            Task task = fromString(newManager.get(i));
            if (task.getClass().equals(Task.class)) {
                if (buf.contains(task.getId())) {
                    newHistory.add(task);
                }
                manager.createTask(task);
            } else if (task.getClass().equals(Epic.class)) {
                if (buf.contains(task.getId())) {
                    newHistory.add(task);
                }
                manager.createEpic((Epic) task);
            } else {
                if (buf.contains(task.getId())) {
                    newHistory.add(task);
                }
                manager.createSubtask((Subtask) task);

            }
        }
        return manager;
    }

    private static Task fromString(String value) {
        String[] task = value.split(",");

        if (task[1].equals("TASK")) {
            return new Task(Integer.parseInt(task[0]), task[2], task[4], task[3]);
        } else if (task[1].equals("EPIC")) {
            return new Epic(task[2], task[4], Integer.parseInt(task[0]));
        } else {
            return new Subtask(Integer.parseInt(task[0]), task[2], task[4], task[3], Integer.parseInt(task[5]));
        }

    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder historyString = new StringBuilder();
        int count = 0;
        if (manager.getHistory().isEmpty()) {
            return "null";
        }
        for (Task task : manager.getHistory()) {
            count++;
            if (count < manager.getHistory().size()) {
                historyString.append(task.getId()).append(",");
            } else {
                historyString.append(task.getId());
            }
        }
        return historyString.toString();
    }

    public static List<Integer> historyFromString(String value) {
        String[] toHistory = value.split(",");
        List<Integer> fromString = new ArrayList<>();
        for (String element : toHistory) {
            fromString.add(Integer.parseInt(element));
        }
        return fromString;
    }


}