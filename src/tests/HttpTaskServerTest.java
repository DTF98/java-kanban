package tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    KVServer kvServer;
    HttpTaskManager manager;
    HttpTaskServer taskServer;
    Gson gson;

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
    @DisplayName("GET(taskById)")
    void shouldReturnTaskByIdWhenTaskCreated() throws IOException, InterruptedException {
        manager.createTask(new Task("название1", "описание1"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200, "код ответа не 200");
        assertEquals(manager.getTaskById(1), gson.fromJson(response.body(), Task.class),
                "Не совпали задачи");
    }

    @Test
    @DisplayName("GET(epicById)")
    void shouldReturnEpicByIdWhenEpicCreated() throws IOException, InterruptedException {
        manager.createEpic(new Epic("название1", "описание1"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200, "код ответа не 200");
        assertEquals(manager.getEpicById(1), gson.fromJson(response.body(), Epic.class),
                "Не совпали задачи");
    }

    @Test
    @DisplayName("GET(epicById)")
    void shouldReturnSubtaskByIdWhenEpicAndSubtaskCreated() throws IOException, InterruptedException {
        manager.createEpic(new Epic("название1", "описание1"));
        manager.createSubtask(new Subtask("название2", "описание2", 1));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200, "код ответа не 200");
        assertEquals(manager.getSubtaskById(2), gson.fromJson(response.body(), Subtask.class),
                "Не совпали задачи");
    }

    @Test
    @DisplayName("GET(tasks)")
    void shouldReturnListOfTasksWhen2TasksCreated() throws IOException, InterruptedException {
        manager.createTask(new Task("название1", "описание1"));
        manager.createTask(new Task("название2", "описание2"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200, "код ответа не 200");
        Type type = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(response.body(), type);
        Task testTask1 = tasks.get(0);
        Task testTask2 = tasks.get(1);
        assertEquals(manager.getTaskById(2), testTask2, "Не совпали задачи");
        assertEquals(manager.getTaskById(1), testTask1, "Не совпали задачи");
    }

    @Test
    @DisplayName("GET(epics)")
    void shouldReturnListOfEpicsWhen2EpicsCreated() throws IOException, InterruptedException {
        manager.createEpic(new Epic("название1", "описание1"));
        manager.createEpic(new Epic("название2", "описание2"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200, "код ответа не 200");
        Type type = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> tasks = gson.fromJson(response.body(), type);
        Epic testTask1 = tasks.get(0);
        Epic testTask2 = tasks.get(1);
        assertEquals(manager.getEpicById(2), testTask2, "Не совпали задачи");
        assertEquals(manager.getEpicById(1), testTask1, "Не совпали задачи");
    }

    @Test
    @DisplayName("GET(epics)")
    void shouldReturnListOfSubtasksWhen2SubtasksAnd1EpicCreated() throws IOException, InterruptedException {
        manager.createEpic(new Epic("название1", "описание1"));
        manager.createSubtask(new Subtask("название2", "описание2", 1));
        manager.createSubtask(new Subtask("название3", "описание3", 1));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200, "код ответа не 200");
        Type type = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> tasks = gson.fromJson(response.body(), type);
        Subtask testTask1 = tasks.get(0);
        Subtask testTask2 = tasks.get(1);
        assertEquals(manager.getSubtaskById(3), testTask2, "Не совпали задачи");
        assertEquals(manager.getSubtaskById(2), testTask1, "Не совпали задачи");
    }

    @Test
    @DisplayName("GET(PrioritizedTasks)")
    void shouldReturnPrioritizedTasksWhen2TaskCreated() throws IOException, InterruptedException {
        manager.createTask(new Task("название1", "описание1",
                LocalDateTime.of(2024, 2, 23, 12, 30), 30));
        manager.createTask(new Task("название2", "описание2",
                LocalDateTime.of(2024, 2, 23, 14, 30), 90));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type type = new TypeToken<ArrayList<Task>>() {
        }.getType();
        TreeSet<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())).
                thenComparing(Task::getId).thenComparing(Task::getTitle).thenComparing(Task::getDescription).
                thenComparing(Task::getStatus)
        );
        ArrayList<Task> buf = gson.fromJson(response.body(), type);
        sortedTasks.addAll(buf);
        assertEquals(2, sortedTasks.size());
    }

    @Test
    @DisplayName("GET(SubtasksInEpic)")
    void shouldReturnListOfIdSubtasks() throws IOException, InterruptedException {
        manager.createEpic(new Epic("название1", "описание1"));
        manager.createSubtask(new Subtask("название2", "описание2", 1));
        manager.createSubtask(new Subtask("название3", "описание3", 1));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        List<Integer> test = gson.fromJson(response.body(), type);
        assertEquals(2, test.size());
    }

    @Test
    @DisplayName("DELETE(TaskById)")
    void shouldReturnMapOfTask1When2TasksCreated() throws IOException, InterruptedException {
        manager.createTask(new Task("название1", "описание1"));
        manager.createTask(new Task("название2", "описание2"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getAllTasks().size());
    }

    @Test
    @DisplayName("DELETE(EpicById)")
    void shouldReturnMapOfEpic1When2EpicsCreated() throws IOException, InterruptedException {
        manager.createEpic(new Epic("название1", "описание1"));
        manager.createEpic(new Epic("название2", "описание2"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getAllEpics().size());
    }

    @Test
    @DisplayName("DELETE(SubtaskById)")
    void shouldReturnMapOfSubtask1When2SubtasksCreated() throws IOException, InterruptedException {
        manager.createEpic(new Epic("название1", "описание1"));
        manager.createSubtask(new Subtask("название2", "описание2", 1));
        manager.createSubtask(new Subtask("название3", "описание3", 1));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getAllSubTasks().size());
    }

    @Test
    @DisplayName("POST(task)")
    void shouldEstimateTaskFromResponseBody() throws IOException, InterruptedException {
        Task testTask = new Task("1", "1.1");
        testTask.setId(1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String toGson = gson.toJson(testTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(toGson))
                .header("Accept", "application/json").build();
        HttpResponse<String> response = client.<String>send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(testTask, manager.getTaskById(1));
    }

    @Test
    @DisplayName("POST(subtask)")
    void shouldEstimateSubtaskFromResponseBody() throws IOException, InterruptedException {
        manager.createEpic(new Epic("1", "1.1"));
        Subtask testTask = new Subtask("2", "2.2", 1);
        testTask.setId(2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        String toGson = gson.toJson(testTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(toGson))
                .header("Accept", "application/json").build();
        HttpResponse<String> response = client.<String>send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(testTask, manager.getSubtaskById(2));
    }

    @Test
    @DisplayName("POST(epic)")
    void shouldEstimateEpicFromResponseBody() throws IOException, InterruptedException {
        Epic testTask = new Epic("1", "1.1");
        testTask.setId(1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String toGson = gson.toJson(testTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(toGson))
                .header("Accept", "application/json").build();
        HttpResponse<String> response = client.<String>send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(testTask, manager.getEpicById(1));
    }
}