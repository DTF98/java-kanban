package managers;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static java.sql.JDBCType.NULL;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager;
    protected TreeSet<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())).
            thenComparing(Task::getId).thenComparing(Task::getTitle).thenComparing(Task::getDescription).
            thenComparing(Task::getStatus)
    );

    protected int uniqueId = 1;

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public InMemoryTaskManager(HistoryManager newHistory) {
        this.historyManager = newHistory;
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
        sortedTasks.removeIf(task -> task.getClass().equals(Task.class));
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllId();
            epic.setStatus("NEW");
        }
        sortedTasks.removeIf(subtask -> subtask.getClass().equals(Subtask.class));
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
        sortedTasks.removeIf(epic -> epic.getClass().equals(Epic.class));
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
    public void createTask(Task task) throws TimeIntersectionException {
        if (checkingIntersections(task)) {
            int id = this.uniqueId;
            this.uniqueId++;
            task.setId(id);
            tasks.put(id, task);
            System.out.println((sortedTasks.add(task)));
        } else {
            throw new TimeIntersectionException("Задача пересекается с другими");
        }
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
    }

    @Override
    public void deleteTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
            sortedTasks.removeIf(task -> task.getId() == taskId);
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
            sortedTasks.removeIf(epic -> epic.getId() == epicId);
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
                epics.get(subtasks.get(subtaskId).getEpicId())
                        .setStatus(estimateStatusEpic(epics.get(subtasks.get(subtaskId).getEpicId())));
                sortedTasks.removeIf(subtask -> subtask.getId() == subtaskId);
            } else {
                System.out.println("Ошибка! Такого эпика не существует.");
            }
        } else {
            System.out.println("Ошибка! Такой подзадачи не существует.");
        }

    }

    protected String estimateStatusEpic(Epic epic) {
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

    public void estimateEndTimeEpic(Epic epic) {
        LocalDateTime maxEndTime = LocalDateTime.MIN;
        int subtasksSize = subtasks.size();
        int count = 0;
        for (int subtaskId : epic.getSubtaskIds()) {
            if (subtasks.get(subtaskId).getEndTime() == null) {
                count++;
            } else if (subtasks.get(subtaskId).getEndTime().isAfter(maxEndTime)) {
                maxEndTime = subtasks.get(subtaskId).getEndTime();
            }
        }
        if (count != subtasksSize) {
            epic.setEndTime(maxEndTime);
        } else {
            epic.setEndTime(null);
        }
    }

    public void estimateStartTimeEpic(Epic epic) {
        LocalDateTime minStartTime = LocalDateTime.MAX;
        int subtasksSize = subtasks.size();
        int count = 0;
        for (int subtaskId : epic.getSubtaskIds()) {
            if (subtasks.get(subtaskId).getStartTime() == null) {
                count++;
            } else if (subtasks.get(subtaskId).getStartTime().isBefore(minStartTime)) {
                minStartTime = subtasks.get(subtaskId).getStartTime();
            }
        }
        if (count != subtasksSize) {
            epic.setStartTime(minStartTime);
        } else {
            epic.setStartTime(null);
        }

    }

    protected void estimateDurationEpic(Epic epic) {
        if (epic.getStartTime() == null) {
            epic.setDuration(0);
        } else {
            Duration duration = Duration.between(epic.getStartTime(), epic.getEndTime());
            epic.setDuration(duration.toMinutes());
        }
    }

    public TreeSet<Task> getPrioritizedTasks() throws TimeIntersectionException {
        return sortedTasks;
    }

    protected boolean checkingIntersections(Task task) {
        Iterator<Task> itr = sortedTasks.iterator();
        LocalDateTime startTimeTask = task.getStartTime();
        if (startTimeTask == null) {
            return true;
        }
        LocalDateTime endTimeTask = task.getEndTime();
        while (itr.hasNext()) {
            Task nextTask = itr.next();
            if (nextTask.getStartTime() == null) {
                continue;
            }
            if (startTimeTask.isBefore(nextTask.getStartTime()) && endTimeTask.isAfter(nextTask.getStartTime()) && endTimeTask.isBefore(nextTask.getEndTime())
                    || startTimeTask.isBefore(nextTask.getStartTime()) && endTimeTask.isAfter(nextTask.getEndTime())
                    || startTimeTask.isAfter(nextTask.getStartTime()) && endTimeTask.isBefore(nextTask.getEndTime())
                    || startTimeTask.isBefore(nextTask.getEndTime()) && startTimeTask.isAfter(nextTask.getStartTime()) && endTimeTask.isAfter(nextTask.getEndTime())) {
                return false;
            }
        }
        return true;
    }
}

