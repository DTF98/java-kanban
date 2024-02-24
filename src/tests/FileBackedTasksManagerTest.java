package tests;

import managers.FileBackedTasksManager;
import model.Epic;
import exception.TimeIntersectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import static managers.Managers.getDefaultHistory;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    File fileSave = new File("./src/Files/Data.csv");

    @BeforeEach
    void createTaskManager() {
        taskManager = new FileBackedTasksManager(getDefaultHistory(), fileSave);
    }

    @Test
    @DisplayName("AnEmptyTaskList")
    void shouldReturnTheBlankSheetsWhenAnEmptyTaskListLoad() {
        try {
            taskManager = FileBackedTasksManager.loadFromFile(fileSave);
            int expectedSizeTask = 0;
            int expectedSizeHistory = 0;
            int testSizeTask = taskManager.getTasks().size();
            int testHistory = taskManager.getHistoryManager().getSize();
            assertEquals(expectedSizeTask, testSizeTask);
            assertEquals(expectedSizeHistory, testHistory);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("AnEmptyHistoryList")
    void shouldThrowExceptionWhenAnEmptyHistoryList() {
        NumberFormatException numberFormatException = assertThrows(
                NumberFormatException.class,
                () -> taskManager = FileBackedTasksManager.loadFromFile(fileSave)
        );
    }

    @Test
    @DisplayName("EpicWithOutSubtasks")
    void shouldReturnEpicsListWhen1EpicInHistory() {
        try {
            taskManager.createEpic(new Epic("Название1", "Описание1"));//id = 1
            Epic test = taskManager.getEpicById(1);
            taskManager = FileBackedTasksManager.loadFromFile(fileSave);
            int expectedEpics = 1;
            int expectedSubtask = 0;
            int testEpics = taskManager.getAllEpics().size();
            int testSubtask = taskManager.getAllSubTasks().size();
            assertEquals(expectedEpics, testEpics);
            assertEquals(expectedSubtask, testSubtask);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("AnEmptyTaskListAndHistory")
    void shouldThrowExceptionWhenAnEmptyTaskAndHistoryListSave() {
        taskManager.save();
        NumberFormatException numberFormatException = assertThrows(
                NumberFormatException.class,
                () -> taskManager = FileBackedTasksManager.loadFromFile(fileSave)
        );
        assertEquals("For input string: \"null\"", numberFormatException.getMessage());
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

}