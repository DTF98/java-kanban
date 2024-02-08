package tests;

import managers.TaskManager;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;

    void shouldReturnListWithOneTaskWhenTaskCreated() {
        try {
            taskManager.createTask(new Task("Название1", "Описание1"));
            Task expectedTask = new Task("Название1", "Описание1");
            expectedTask.setId(1);
            Task test = taskManager.getAllTasks().get(0);
            assertEquals(test, expectedTask);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }
    }

    void shouldThrowExceptionWhenListTasksIsEmpty() {
        AtomicReference<List<Task>> test0Task = new AtomicReference<>(new ArrayList<>(taskManager.getAllTasks()));
        IndexOutOfBoundsException ex = assertThrows(
                IndexOutOfBoundsException.class,
                () -> test0Task.get().get(0)
        );
        assertEquals("Index 0 out of bounds for length 0", ex.getMessage());
    }

    void shouldReturnListWithOneSubtaskWhenSubtaskAndEpicCreated() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));
            taskManager.createSubtask(new Subtask("Название2", "Описание2", 1));//id = 2
            Subtask expectedSubtask = new Subtask("Название2", "Описание2", 1);
            expectedSubtask.setId(2);
            Subtask test = taskManager.getAllSubTasks().get(0);
            assertEquals(test, expectedSubtask);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }
    }

    void shouldThrowExceptionWhenListSubtasksIsEmpty() {
        AtomicReference<List<Subtask>> test0Subtask =
                new AtomicReference<>(new ArrayList<>(taskManager.getAllSubTasks()));
        IndexOutOfBoundsException ex = assertThrows(
                IndexOutOfBoundsException.class,
                () -> test0Subtask.get().get(0)
        );
        assertEquals("Index 0 out of bounds for length 0", ex.getMessage());
    }

    void shouldReturnListWithOneEpicWhenEpicAndSubtaskCreated() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            Epic test = taskManager.getAllEpics().get(0);
            Epic expectedEpic = new Epic("Название1", "Описание1");
            expectedEpic.setId(1);
            assertEquals(test, expectedEpic);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldReturnListWith0EpicWhenListEpicsIsEmpty() {
        List<Epic> test0Epic = new ArrayList<>(taskManager.getAllEpics());
        int expectedSize = 0;
        int testSize = test0Epic.size();
        assertEquals(testSize, expectedSize);
    }

    void shouldReturnHashMapWithOneTaskWhenTaskCreated() {
        try {
            taskManager.createTask(new Task("Название1", "Описание1"));
            Task expectedTask = new Task("Название1", "Описание1");
            expectedTask.setId(1);
            Task test = taskManager.getTasks().get(1);
            assertEquals(test, expectedTask);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }
    }

    void shouldReturnHashMapWith0TaskWhenHashMapTasksIsEmpty() {
        Map<Integer, Task> test0Task = new HashMap<>(taskManager.getTasks());
        int expectedSize = 0;
        int testSize = test0Task.size();
        assertEquals(testSize, expectedSize);
    }

    void shouldReturnHashMapWithOneSubtaskWhenEpicAndSubtaskCreated() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));
            taskManager.createSubtask(new Subtask("Название2", "Описание2", 1));//id = 2
            Subtask expectedSubtask = new Subtask("Название2", "Описание2", 1);
            expectedSubtask.setId(2);
            Subtask test = taskManager.getSubtasks().get(2);
            assertEquals(test, expectedSubtask);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldReturnHashMapWith0SubtaskWhenHashMapSubtasksIsEmpty() {
        Map<Integer, Subtask> test0Task = new HashMap<>(taskManager.getSubtasks());
        int expectedSize = 0;
        int testSize = test0Task.size();
        assertEquals(testSize, expectedSize);
    }

    void shouldReturnHashMapWithOneEpicWhenEpicAndSubtaskCreated() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            Epic test = taskManager.getEpics().get(1);
            Epic expectedEpic = new Epic("Название1", "Описание1");
            expectedEpic.setId(1);
            assertEquals(test, expectedEpic);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }
    }

    void shouldReturnHashMapWith0EpicWhenHashMapEpicsIsEmpty() {
        Map<Integer, Epic> test0Task = new HashMap<>(taskManager.getEpics());
        int expectedSize = 0;
        int testSize = test0Task.size();
        assertEquals(testSize, expectedSize);
    }

    void shouldReturnListWithTwoSubtaskWhenEpicAndSubtasksCreated() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            taskManager.createSubtask(new Subtask("Название2", "Описание2", 1));//id = 2
            taskManager.createSubtask(new Subtask("Название3", "Описание3", 1));//id = 3
            List<Subtask> test2Subtasks = taskManager.getSubtaskInEpic(1);
            Subtask expected1 = new Subtask("Название2", "Описание2", 1);
            expected1.setId(2);
            Subtask expected2 = new Subtask("Название3", "Описание3", 1);
            expected2.setId(3);
            assertEquals(expected1, test2Subtasks.get(0));
            assertEquals(expected2, test2Subtasks.get(1));
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldReturnListWith0SubtaskWhenEpicCreated() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            List<Subtask> test0Subtasks = taskManager.getSubtaskInEpic(1);
            int expectedSize = 0;
            assertEquals(expectedSize, test0Subtasks.size());
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldReturnListWith0TaskWhen2TaskCreated() {
        try {
            taskManager.createTask(new Task("Название1", "Описание1"));
            taskManager.createTask(new Task("Название2", "Описание2"));
            taskManager.deleteAllTasks();
            int expectedSize = 0;
            int testSize = taskManager.getAllTasks().size();
            assertEquals(expectedSize, testSize);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }
    }

    void shouldReturnListWith0TaskWhen0TaskCreated() {
        taskManager.deleteAllTasks();
        int expectedSize = 0;
        int testSize = taskManager.getAllTasks().size();
        assertEquals(expectedSize, testSize);
    }

    void shouldReturnListWith0SubtaskWhen2SubtaskCreated() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            taskManager.createSubtask(new Subtask("Название2", "Описание2", 1));//id = 2
            taskManager.createSubtask(new Subtask("Название3", "Описание3", 1));//id = 3
            taskManager.deleteAllSubtasks();
            int expectedSize = 0;
            int testSize = taskManager.getAllSubTasks().size();
            assertEquals(expectedSize, testSize);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldReturnListWith0SubtaskWhen0SubtaskCreated() {
        taskManager.deleteAllSubtasks();
        int expectedSize = 0;
        int testSize = taskManager.getAllSubTasks().size();
        assertEquals(expectedSize, testSize);
    }

    void shouldReturnListWith0EpicWhen2EpicCreated() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            taskManager.createEpic(new Epic("Название2", "Описание2"));// id = 2
            taskManager.createSubtask(new Subtask("Название2", "Описание2", 1));//id = 3
            taskManager.createSubtask(new Subtask("Название3", "Описание3", 2));//id = 4
            taskManager.deleteAllEpics();
            int expectedSize = 0;
            int testSizeEpics = taskManager.getAllEpics().size();
            int testSizeSubtasks = taskManager.getAllSubTasks().size();
            assertEquals(expectedSize, testSizeEpics);
            assertEquals(expectedSize, testSizeSubtasks);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldReturnListWith0EpicWhen0EpicCreated() {
        taskManager.deleteAllEpics();
        int expectedSize = 0;
        int testSizeEpics = taskManager.getAllEpics().size();
        int testSizeSubtasks = taskManager.getAllSubTasks().size();
        assertEquals(expectedSize, testSizeEpics);
        assertEquals(expectedSize, testSizeSubtasks);
    }

    void shouldReturnTaskWhenIdExists() {
        try {
            taskManager.createTask(new Task("Название1", "Описание1"));// id = 1
            Task expectedTask = new Task("Название1", "Описание1");
            expectedTask.setId(1);
            Task testTask = taskManager.getTaskById(1);
            assertEquals(expectedTask, testTask);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldThrowExceptionWhenTasksIdDoesNotExist() {
        try {
            taskManager.createTask(new Task("Название1", "Описание1"));// id = 1
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> taskManager.getTaskById(2)
            );
            assertNull(ex.getMessage());
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldReturnSubtaskWhenIdExists() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            taskManager.createSubtask(new Subtask("Название2", "Описание2", 1));//id = 2
            Subtask expectedSubtask = new Subtask("Название2", "Описание2", 1);
            expectedSubtask.setId(2);
            Subtask testSubtask = taskManager.getSubtaskById(2);
            assertEquals(expectedSubtask, testSubtask);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldThrowExceptionWhenSubtasksIdDoesNotExist() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            taskManager.createSubtask(new Subtask("Название2", "Описание2", 1));//id = 2
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> taskManager.getSubtaskById(3)
            );
            assertNull(ex.getMessage());
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldReturnEpicWhenIdExists() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            Epic expectedEpic = new Epic("Название1", "Описание1");
            expectedEpic.setId(1);
            Epic testEpic = taskManager.getEpicById(1);
            assertEquals(expectedEpic, testEpic);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldThrowExceptionWhenEpicsIdDoesNotExist() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> taskManager.getEpicById(2)
            );
            assertNull(ex.getMessage());
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldReturnUpdateTask() {
        try {
            taskManager.createTask(new Task("Название1", "Описание1"));// id = 1
            Task updateTask = new Task("Название2", "Описание2");
            updateTask.setId(1);
            taskManager.updateTask(updateTask);
            Task testTask = taskManager.getTaskById(1);
            assertEquals(updateTask, testTask);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldReturnUpdateEpic() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            Epic updateEpic = new Epic("Название2", "Описание2");
            updateEpic.setId(1);
            taskManager.updateEpic(updateEpic);
            Epic testEpic = taskManager.getEpicById(1);
            assertEquals(updateEpic, testEpic);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldReturnUpdateSubtask() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            taskManager.createSubtask(new Subtask("Название2", "Описание2", 1));//id = 2
            Subtask updateSubtask = new Subtask("Название3", "Описание3", 1);
            updateSubtask.setId(2);
            taskManager.updateSubtask(updateSubtask);
            Subtask testSubtask = taskManager.getSubtaskById(2);
            assertEquals(updateSubtask, testSubtask);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldSetTheStatusWhenAnEmptyListOfSubtasks() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            String expectedStatus = TasksStatus.NEW.name();
            String testStatus = taskManager.getEpicById(1).getStatus();
            assertEquals(expectedStatus, testStatus);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldSetTheStatusWhenAllSubtasksWithStatusNew() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            taskManager.createSubtask(new Subtask("Название2", "Описание2", 1));//id = 2
            taskManager.createSubtask(new Subtask("Название3", "Описание3", 1));//id = 3
            String expectedStatus = TasksStatus.NEW.name();
            String testStatus = taskManager.getEpicById(1).getStatus();
            assertEquals(expectedStatus, testStatus);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldSetTheStatusWhenAllSubtasksWithStatusDone() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            taskManager.createSubtask(new Subtask(2, "Название2", "Описание2", "DONE", 1));//id = 2
            taskManager.createSubtask(new Subtask(3, "Название3", "Описание3", "DONE", 1));//id = 3
            String expectedStatus = TasksStatus.DONE.name();
            String testStatus = taskManager.getEpicById(1).getStatus();
            assertEquals(expectedStatus, testStatus);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldSetTheStatusWhenSubtasksWithTheStatusesNewAndDone() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            taskManager.createSubtask(new Subtask("Название2", "Описание2", 1));//id = 2
            taskManager.createSubtask(new Subtask(3, "Название3", "Описание3", "DONE", 1));//id = 3
            taskManager.getSubtaskById(3).setStatus("DONE");
            String expectedStatus = TasksStatus.IN_PROGRESS.name();
            String testStatus = taskManager.getEpicById(1).getStatus();
            assertEquals(expectedStatus, testStatus);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }

    void shouldSetTheStatusWhenSubtasksWithTheStatusesIN_PROGRESS() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));// id = 1
            taskManager.createSubtask(new Subtask(2, "Название2", "Описание2", "IN_PROGRESS", 1));//id = 2
            taskManager.createSubtask(new Subtask(3, "Название3", "Описание3", "IN_PROGRESS", 1));//id = 3
            String expectedStatus = TasksStatus.IN_PROGRESS.name();
            String testStatus = taskManager.getEpicById(1).getStatus();
            assertEquals(expectedStatus, testStatus);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }
}

