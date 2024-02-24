package tests;

import client.KVTaskClient;
import com.google.gson.Gson;
import managers.HttpTaskManager;
import managers.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    KVServer kvServer;
    HttpTaskManager manager;
    HttpTaskServer taskServer;
    Gson gson;
    String key = "MyKey";


    @BeforeEach
    void init() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = Managers.getDefault(Managers.getDefaultHistory(),
                URI.create("http://localhost:8078"), "MyKey");
        taskServer = new HttpTaskServer(manager);
        taskServer.start();
        gson = Managers.getGson();
    }

    @AfterEach
    void close() {
        kvServer.stop();
        taskServer.stop();
    }

    @Test
    @DisplayName("loadFromServerWhen")
    void loadFromServer() throws IOException {
        manager.createTask(new Task("1", "1.1"));
        manager.createTask(new Task("2", "2.2"));
        manager.createEpic(new Epic("3", "3.3"));
        String seril = manager.sirializeManager();
        HttpTaskManager manager1 = manager.desirializeManager(seril);
        assertEquals(manager1.getTaskById(1), manager.getTaskById(1));
        assertEquals(manager1.getTaskById(2), manager.getTaskById(2));
        assertEquals(manager1.getEpicById(3), manager.getEpicById(3));
    }

    @Test
    @DisplayName("getTaskByIdWhenCreated1TaskWithOutTime")
    void getTaskById() throws IOException, InterruptedException {
        manager.createTask(new Task("название1", "описание1"));
        Task testTask = manager.getTaskById(1);
        KVTaskClient client = manager.getClient();
        assertEquals(testTask, manager.desirializeManager(client.load(key, client.getAPI_TOKEN())).getTaskById(1));
    }

    @Test
    void getSubtaskByIdAndGetEpicById() throws IOException {
        manager.createEpic(new Epic("название1", "описание1"));
        manager.createSubtask(new Subtask("название2", "описание2", 1));
        Subtask testSubtask = manager.getSubtaskById(2);
        Epic testEpic = manager.getEpicById(1);
        KVTaskClient client = manager.getClient();
        assertEquals(testSubtask, manager.desirializeManager(client.load(key, client.getAPI_TOKEN())).getSubtaskById(2));
        assertEquals(testEpic, manager.desirializeManager(client.load(key, client.getAPI_TOKEN())).getEpicById(1));
    }

    @Test
    void createTaskEpicSubtask() throws IOException {
        manager.createEpic(new Epic("название1", "описание1"));
        manager.createSubtask(new Subtask("название2", "описание2", 1));
        manager.createTask(new Task("название3", "описание3"));
        Task testTask = manager.getTaskById(3);
        Subtask testSubtask = manager.getSubtaskById(2);
        Epic testEpic = manager.getEpicById(1);
        KVTaskClient client = manager.getClient();
        assertEquals(testSubtask, manager.desirializeManager(client.load(key, client.getAPI_TOKEN())).getSubtaskById(2));
        assertEquals(testEpic, manager.desirializeManager(client.load(key, client.getAPI_TOKEN())).getEpicById(1));
        assertEquals(testTask, manager.desirializeManager(client.load(key, client.getAPI_TOKEN())).getTaskById(3));
    }

    @Test
    void updateTaskSubtaskEpic() throws IOException {
        Task taskToUpdate = new Task("название3", "описание3");
        Epic epicToUpdate = new Epic("название1", "описание1");
        Subtask subtaskToUpdate = new Subtask("название2", "описание2", 1);
        manager.createEpic(epicToUpdate);
        manager.createSubtask(new Subtask("название2", "описание2", 1));
        manager.createTask(taskToUpdate);
        taskToUpdate.setId(3);
        epicToUpdate.setId(1);
        subtaskToUpdate.setId(2);
        taskToUpdate.setStartTime(LocalDateTime.of(2024, 2, 23, 12, 30));
        taskToUpdate.setDuration(30);
        manager.updateTask(taskToUpdate);
        subtaskToUpdate.setStartTime(LocalDateTime.of(2024, 2, 23, 15, 30));
        subtaskToUpdate.setDuration(90);
        manager.updateSubtask(subtaskToUpdate);
        epicToUpdate.setStartTime(LocalDateTime.of(2024, 2, 23, 15, 30));
        epicToUpdate.setDuration(90);
        manager.estimateEndTimeEpic(epicToUpdate);
        KVTaskClient client = manager.getClient();
        assertEquals(taskToUpdate, manager.desirializeManager(client.load(key, client.getAPI_TOKEN())).getTaskById(3));
        assertEquals(subtaskToUpdate, manager.desirializeManager(client.load(key, client.getAPI_TOKEN())).getSubtaskById(2));
        assertEquals(epicToUpdate, manager.desirializeManager(client.load(key, client.getAPI_TOKEN())).getEpicById(1));
    }

    @Test
    void deleteTaskEpicSubtaskById() throws IOException {
        manager.createEpic(new Epic("название1", "описание1"));
        manager.createSubtask(new Subtask("название2", "описание2", 1));
        manager.createTask(new Task("название3", "описание3"));
        manager.createTask(new Task("название4", "описание4"));
        manager.createSubtask(new Subtask("название5", "описание5", 1));
        manager.deleteTaskById(4);
        manager.deleteSubtaskById(5);
        int expectedTasks = 1;
        int expectedSubtask = 1;
        KVTaskClient client = manager.getClient();
        assertEquals(expectedTasks, manager.desirializeManager(client.load(key, client.getAPI_TOKEN())).getAllTasks().size());
        assertEquals(expectedSubtask, manager.desirializeManager(client.load(key, client.getAPI_TOKEN())).getAllSubTasks().size());
    }
}