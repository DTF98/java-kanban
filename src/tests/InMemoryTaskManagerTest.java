package tests;

import managers.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import exception.TimeIntersectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import static managers.Managers.getDefaultHistory;


public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void createTaskManager() {
        taskManager = new InMemoryTaskManager(getDefaultHistory());
    }

    @Test
    @DisplayName("getAllTasks & createTask")
    @Override
    void shouldReturnListWithOneTaskWhenTaskCreated() {
        super.shouldReturnListWithOneTaskWhenTaskCreated();
    }

    @Test
    @DisplayName("getAllTasks(Empty)")
    @Override
    void shouldThrowExceptionWhenListTasksIsEmpty() {
        super.shouldThrowExceptionWhenListTasksIsEmpty();
    }

    @Test
    @DisplayName("getAllSubTasks & createEpic & createSubtask")
    @Override
    void shouldReturnListWithOneSubtaskWhenSubtaskAndEpicCreated() {
        super.shouldReturnListWithOneSubtaskWhenSubtaskAndEpicCreated();
    }

    @Test
    @DisplayName("getAllSubtasks(Empty)")
    @Override
    void shouldThrowExceptionWhenListSubtasksIsEmpty() {
        super.shouldThrowExceptionWhenListSubtasksIsEmpty();
    }

    @Test
    @DisplayName("getAllEpics & createEpic & createSubtask")
    @Override
    void shouldReturnListWithOneEpicWhenEpicAndSubtaskCreated() {
        super.shouldReturnListWithOneEpicWhenEpicAndSubtaskCreated();
    }

    @Test
    @DisplayName("getAllEpics(Empty)")
    @Override
    void shouldReturnListWith0EpicWhenListEpicsIsEmpty() {
        super.shouldReturnListWith0EpicWhenListEpicsIsEmpty();
    }

    @Test
    @DisplayName("getTasks")
    @Override
    void shouldReturnHashMapWithOneTaskWhenTaskCreated() {
        super.shouldReturnHashMapWithOneTaskWhenTaskCreated();
    }

    @Test
    @DisplayName("getTasks(Empty)")
    @Override
    void shouldReturnHashMapWith0TaskWhenHashMapTasksIsEmpty() {
        super.shouldReturnHashMapWith0TaskWhenHashMapTasksIsEmpty();
    }

    @Test
    @DisplayName("getSubtasks")
    @Override
    void shouldReturnHashMapWithOneSubtaskWhenEpicAndSubtaskCreated() {
        super.shouldReturnHashMapWithOneSubtaskWhenEpicAndSubtaskCreated();
    }

    @Test
    @DisplayName("getSubtasks(Empty)")
    @Override
    void shouldReturnHashMapWith0SubtaskWhenHashMapSubtasksIsEmpty() {
        super.shouldReturnHashMapWith0SubtaskWhenHashMapSubtasksIsEmpty();
    }

    @Test
    @DisplayName("getEpics")
    @Override
    void shouldReturnHashMapWithOneEpicWhenEpicAndSubtaskCreated() {
        super.shouldReturnHashMapWithOneEpicWhenEpicAndSubtaskCreated();
    }

    @Test
    @DisplayName("getEpics(Empty)")
    @Override
    void shouldReturnHashMapWith0EpicWhenHashMapEpicsIsEmpty() {
        super.shouldReturnHashMapWith0EpicWhenHashMapEpicsIsEmpty();
    }

    @Test
    @DisplayName("getSubtaskInEpic")
    @Override
    void shouldReturnListWithTwoSubtaskWhenEpicAndSubtasksCreated() {
        super.shouldReturnListWithTwoSubtaskWhenEpicAndSubtasksCreated();
    }

    @Test
    @DisplayName("getSubtaskInEpic(Empty)")
    @Override
    void shouldReturnListWith0SubtaskWhenEpicCreated() {
        super.shouldReturnListWith0SubtaskWhenEpicCreated();
    }

    @Test
    @DisplayName("deleteAllTasks(2TaskCreated)")
    @Override
    void shouldReturnListWith0TaskWhen2TaskCreated() {
        super.shouldReturnListWith0TaskWhen2TaskCreated();
    }

    @Test
    @DisplayName("deleteAllTasks(0TaskCreated)")
    @Override
    void shouldReturnListWith0TaskWhen0TaskCreated() {
        super.shouldReturnListWith0TaskWhen0TaskCreated();
    }

    @Test
    @DisplayName("deleteAllSubtask(2SubtaskCreated)")
    @Override
    void shouldReturnListWith0SubtaskWhen2SubtaskCreated() {
        super.shouldReturnListWith0SubtaskWhen2SubtaskCreated();
    }

    @Test
    @DisplayName("deleteAllSubtask(0SubtaskCreated)")
    @Override
    void shouldReturnListWith0SubtaskWhen0SubtaskCreated() {
        super.shouldReturnListWith0SubtaskWhen0SubtaskCreated();
    }

    @Test
    @DisplayName("deleteAllEpics(2EpicCreated)")
    @Override
    void shouldReturnListWith0EpicWhen2EpicCreated() {
        super.shouldReturnListWith0EpicWhen2EpicCreated();
    }

    @Test
    @DisplayName("deleteAllEpics(0EpicCreated)")
    @Override
    void shouldReturnListWith0EpicWhen0EpicCreated() {
        super.shouldReturnListWith0EpicWhen0EpicCreated();
    }

    @Test
    @DisplayName("getTaskById")
    @Override
    void shouldReturnTaskWhenIdExists() {
        super.shouldReturnTaskWhenIdExists();
    }

    @Test
    @DisplayName("getTaskById(IdDoesNotExist)")
    @Override
    void shouldThrowExceptionWhenTasksIdDoesNotExist() {
        super.shouldThrowExceptionWhenTasksIdDoesNotExist();
    }

    @Test
    @DisplayName("getSubtaskById")
    @Override
    void shouldReturnSubtaskWhenIdExists() {
        super.shouldReturnSubtaskWhenIdExists();
    }

    @Test
    @DisplayName("getSubtaskById(IdDoesNotExist)")
    @Override
    void shouldThrowExceptionWhenSubtasksIdDoesNotExist() {
        super.shouldThrowExceptionWhenSubtasksIdDoesNotExist();
    }

    @Test
    @DisplayName("getEpicById")
    @Override
    void shouldReturnEpicWhenIdExists() {
        super.shouldReturnEpicWhenIdExists();
    }


    @Test
    @DisplayName("getEpicById(IdDoesNotExist)")
    @Override
    void shouldThrowExceptionWhenEpicsIdDoesNotExist() {
        super.shouldThrowExceptionWhenEpicsIdDoesNotExist();
    }

    @Test
    @DisplayName("updateTask")
    @Override
    void shouldReturnUpdateTask() {
        super.shouldReturnUpdateTask();
    }

    @Test
    @DisplayName("updateEpic")
    @Override
    void shouldReturnUpdateEpic() {
        super.shouldReturnUpdateEpic();
    }

    @Test
    @DisplayName("updateSubtask")
    @Override
    void shouldReturnUpdateSubtask() {
        super.shouldReturnUpdateSubtask();
    }

    @Test
    @DisplayName("estimateStatusEpic(AnEmptyListOfSubtasks)")
    void shouldSetTheStatusWhenAnEmptyListOfSubtasks() {
        super.shouldSetTheStatusWhenAnEmptyListOfSubtasks();
    }

    @Test
    @DisplayName("estimateStatusEpic(AllSubtasksWithStatusNew)")
    @Override
    void shouldSetTheStatusWhenAllSubtasksWithStatusNew() {
        super.shouldSetTheStatusWhenAllSubtasksWithStatusNew();
    }

    @Test
    @DisplayName("estimateStatusEpic(AllSubtasksWithStatusDone)")
    @Override
    void shouldSetTheStatusWhenAllSubtasksWithStatusDone() {
        super.shouldSetTheStatusWhenAllSubtasksWithStatusDone();
    }

    @Test
    @DisplayName("estimateStatusEpic(SubtasksWithTheStatusesNewAndDone)")
    @Override
    void shouldSetTheStatusWhenSubtasksWithTheStatusesNewAndDone() {
        super.shouldSetTheStatusWhenSubtasksWithTheStatusesNewAndDone();
    }

    @Test
    @DisplayName("estimateStatusEpic(SubtasksWithTheStatusesIN_PROGRESS)")
    @Override
    void shouldSetTheStatusWhenSubtasksWithTheStatusesIN_PROGRESS() {
        super.shouldSetTheStatusWhenSubtasksWithTheStatusesIN_PROGRESS();
    }

    @Test
    @DisplayName("estimateStartTimeEpicAndEstimateEndTimeEpicAndEstimateDurationEpic")
    void shouldReturnStartTimeAndEndTimeEpicWhenCreated2SubtasksAnd() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));
            taskManager.createSubtask(new Subtask("Название2", "Описание2", 1,
                    LocalDateTime.of(2024, 2, 7, 12, 30), 30));
            taskManager.createSubtask(new Subtask("Название3", "Описание3", 1,
                    LocalDateTime.of(2024, 2, 7, 14, 30), 30));
            LocalDateTime testStartTime = taskManager.getEpicById(1).getStartTime();
            LocalDateTime expectedStartTime = LocalDateTime.of(2024, 2, 7, 12, 30);
            LocalDateTime testEndTime = taskManager.getEpicById(1).getEndTime();
            LocalDateTime expectedEndTime = LocalDateTime.of(2024, 2, 7, 15, 0);
            long testDuration = taskManager.getEpicById(1).getDuration();
            long expectedDuration = 150;
            assertEquals(testStartTime, expectedStartTime);
            assertEquals(testEndTime, expectedEndTime);
            assertEquals(testDuration, expectedDuration);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }

    }
}