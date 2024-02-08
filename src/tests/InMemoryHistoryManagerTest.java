package tests;

import managers.HistoryManager;
import managers.InMemoryHistoryManager;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager manager;

    @BeforeEach
    void createdHistoryManager() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    @DisplayName("getHistory(0Tasks)")
    void shouldReturnListWith0Tasks() {
        int testSize = manager.getHistory().size();
        int expectedSize = 0;
        assertEquals(expectedSize, testSize);
    }

    @Test
    @DisplayName("getHistory(2Tasks)")
    void shouldReturnListWith2Tasks() {
        manager.add(new Task("Название1", "Описание1", 1));
        manager.add(new Task("Название2", "Описание2", 2));
        List<Task> testList = manager.getHistory();
        Task expectedTask1 = new Task("Название1", "Описание1");
        expectedTask1.setId(1);
        Task expectedTask2 = new Task("Название2", "Описание2");
        expectedTask2.setId(2);
        Task test1 = testList.get(0);
        Task test2 = testList.get(1);
        assertEquals(test1, expectedTask1);
        assertEquals(test2, expectedTask2);
    }

    @Test
    @DisplayName("add(Duplication)")
    void shouldWorkProperlyWithDuplication() {
        manager.add(new Task("Название1", "Описание1", 1));
        manager.add(new Task("Название1", "Описание1", 1));
        List<Task> testList = manager.getHistory();
        Task expectedTask = new Task("Название1", "Описание1", 1);
        Task test = testList.get(0);
        assertEquals(test, expectedTask);

    }

    @Test
    @DisplayName("add")
    void shouldWorkOutAsStandardWith2Tasks() {
        manager.add(new Task("Название1", "Описание1", 1));
        manager.add(new Task("Название2", "Описание2", 2));
        List<Task> testList = manager.getHistory();
        Task expectedTask1 = new Task("Название1", "Описание1", 1);
        Task expectedTask2 = new Task("Название2", "Описание2", 2);
        Task test1 = testList.get(0);
        Task test2 = testList.get(1);
        assertEquals(test1, expectedTask1);
        assertEquals(test2, expectedTask2);
    }

    @Test
    @DisplayName("getSize(0Task)")
    void shouldReturn0When0addTasks() {
        int test = manager.getSize();
        int expected = 0;
        assertEquals(test, expected);
    }

    @Test
    @DisplayName("getSize(2Task)")
    void shouldReturn2When2addTasks() {
        manager.add(new Task("Название1", "Описание1", 1));
        manager.add(new Task("Название2", "Описание2", 2));
        int expected = 2;
        int test = manager.getSize();
        assertEquals(test, expected);
    }

    @Test
    @DisplayName("remove(Beginning)")
    void shouldDeleteBeginning() {
        manager.add(new Task("Название1", "Описание1", 1));
        manager.add(new Task("Название2", "Описание2", 2));
        manager.add(new Task("Название3", "Описание3", 3));
        manager.remove(1);
        List<Task> testList = manager.getHistory();
        Task expectedTask1 = new Task("Название2", "Описание2", 2);
        Task expectedTask2 = new Task("Название3", "Описание3", 3);
        Task test1 = testList.get(0);
        Task test2 = testList.get(1);
        assertEquals(test1, expectedTask1);
        assertEquals(test2, expectedTask2);
    }

    @Test
    @DisplayName("remove(Middle)")
    void shouldDeleteMiddle() {
        manager.add(new Task("Название1", "Описание1", 1));
        manager.add(new Task("Название2", "Описание2", 2));
        manager.add(new Task("Название3", "Описание3", 3));
        manager.remove(2);
        List<Task> testList = manager.getHistory();
        Task expectedTask1 = new Task("Название1", "Описание1", 1);
        Task expectedTask2 = new Task("Название3", "Описание3", 3);
        Task test1 = testList.get(0);
        Task test2 = testList.get(1);
        assertEquals(test1, expectedTask1);
        assertEquals(test2, expectedTask2);
    }

    @Test
    @DisplayName("remove(End)")
    void shouldDeleteEnd() {
        manager.add(new Task("Название1", "Описание1", 1));
        manager.add(new Task("Название2", "Описание2", 2));
        manager.add(new Task("Название3", "Описание3", 3));
        manager.remove(3);
        List<Task> testList = manager.getHistory();
        Task expectedTask1 = new Task("Название1", "Описание1", 1);
        Task expectedTask2 = new Task("Название2", "Описание2", 2);
        Task test1 = testList.get(0);
        Task test2 = testList.get(1);
        assertEquals(test1, expectedTask1);
        assertEquals(test2, expectedTask2);
    }

    @Test
    @DisplayName("remove(0Task)")
    void shouldThrowExceptionWhenHistoryIsEmpty() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> manager.remove(3)
        );
        assertNull(ex.getMessage());
    }

}