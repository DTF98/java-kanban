package managers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(HistoryManager newHistory, File newFile) {
        super(newHistory);
        this.file = newFile;
    }

    public static void main(String[] args) {
        File fileSave = new File("./src/Files/Data.csv");
        FileBackedTasksManager manager;
        try {
            manager = FileBackedTasksManager.loadFromFile(fileSave);
        } catch (Exception e) {
            System.out.println("Файл пустой!");
            manager = new FileBackedTasksManager(Managers.getDefaultHistory(), fileSave);
        }
        /** Файл пустой, эти тесты для его заполнения, закомментируйте для проверки чтения из файла */
//
//        manager.createTask(new Task("Название1", "Описание1"));//id = 1
//        manager.createTask(new Task("Название2", "Описание2"));//id = 2
//        manager.createEpic(new Epic("Название3", "Описание3"));//id = 3
//        manager.createSubtask(new Subtask("Название4", "Описание4", 3));//id = 4
//        manager.createSubtask(new Subtask("Название5", "Описание5", 3));//id = 5
//        System.out.println(manager.getTaskById(1).getTitle());
//        System.out.println(manager.getTaskById(2).getTitle());
//        System.out.println(manager.getEpicById(3).getTitle());
//        System.out.println(manager.getSubtaskById(4).getTitle());
//        System.out.println(manager.getSubtaskById(5).getTitle());

        /** Файл не пустой, закоментируйте тесты выше, и раскомментируйте тесты ниже для проверки чтения из файла */

        System.out.println("Проверка чтения из файла");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task.toString());
        }
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic.toString(manager.estimateStatusEpic(epic)));
        }
        for (Subtask subtask : manager.getAllSubTasks()) {
            System.out.println(subtask.toString());
        }
        for (Task task : manager.getHistoryManager().getHistory()) {
            System.out.println(task.getId());
        }

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
    public void createTask(Task task) {
        int id = this.uniqueId;
        this.uniqueId++;
        task.setId(id);
        tasks.put(id, task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        int id = this.uniqueId;
        this.uniqueId++;
        epic.setId(id);
        epics.put(id, epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        int id = this.uniqueId;
        this.uniqueId++;
        subtask.setId(id);
        epics.get(subtask.getEpicId()).setSubtasks(id);
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).setStatus(estimateStatusEpic(epics.get(subtask.getEpicId())));
        save();
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Такой задачи не существует!");
        }
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Такого эпика не существует!");
        }
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).setStatus(estimateStatusEpic(epics.get(subtask.getEpicId())));
        } else {
            System.out.println("Такой подзадачи не существует!");
        }
        save();
    }

    @Override
    public void deleteTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        } else {
            System.out.println("Такой задачи не существует!");
        }
        save();
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
        save();
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

    public static FileBackedTasksManager loadFromFile(File file) {
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
                if (buf.contains(task.getId())) { //TODO: here add to history
                    newHistory.add(task);
                }
                manager.createTask(task); //TODO: here save
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