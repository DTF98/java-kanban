package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import managers.FileBackedTasksManager;
import managers.HttpTaskManager;
import managers.Managers;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer server;
    private final Gson gson;
    private final HttpTaskManager httpTaskManager;

    public HttpTaskServer(HttpTaskManager httpTaskManager) throws IOException {
        this.httpTaskManager = httpTaskManager;
        this.gson = Managers.getGson();
        this.server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handleTasks);
    }

    private void handleTasks(HttpExchange httpExchange) {
        String response = "";
        try (httpExchange) {

            String method = httpExchange.getRequestMethod();
            //String arg = httpExchange.getRequestURI().getRawQuery();
            String path = httpExchange.getRequestURI().getPath();
            switch (method) {
                case "GET" -> {
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        ArrayList<Task> A = httpTaskManager.getAllTasks();
                        response = gson.toJson(A);
                        sendText(httpExchange, response);
                    } else if (Pattern.matches("^/tasks/subtask/$", path)) {
                        response = gson.toJson(httpTaskManager.getAllSubTasks());
                        sendText(httpExchange, response);
                    } else if (Pattern.matches("^/tasks/epic/$", path)) {
                        response = gson.toJson(httpTaskManager.getAllEpics());
                        sendText(httpExchange, response);
                    } else if (Pattern.matches("^/tasks/$", path)) {
                        response = gson.toJson(httpTaskManager.getPrioritizedTasks());
                        sendText(httpExchange, response);
                    } else if (Pattern.matches("^/tasks/task/id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            response = gson.toJson(httpTaskManager.getTaskById(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Введён некорректный id");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/subtask/id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            response = gson.toJson(httpTaskManager.getSubtaskById(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Введён некорректный id");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/epic/id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            response = gson.toJson(httpTaskManager.getEpicById(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Введён некорректный id");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/subtask/epic/id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/epic/id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            response = gson.toJson(httpTaskManager.getEpicById(id).getSubtaskIds());
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Введён некорректный id");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/history", path)) {
                        response = gson.toJson(FileBackedTasksManager.historyToString(httpTaskManager.getHistoryManager()));
                        sendText(httpExchange, response);
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                }
                case "DELETE" -> {
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        httpTaskManager.deleteAllTasks();
                    } else if (Pattern.matches("^/tasks/subtask/$", path)) {
                        httpTaskManager.deleteAllSubtasks();
                    } else if (Pattern.matches("^/tasks/epic/$", path)) {
                        httpTaskManager.deleteAllEpics();
                    } else if (Pattern.matches("^/tasks/task/id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            httpTaskManager.deleteTaskById(id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Введён некорректный id");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/subtask/id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            httpTaskManager.deleteSubtaskById(id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Введён некорректный id");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/epic/id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            httpTaskManager.deleteEpicById(id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Введён некорректный id");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    }

                }
                case "POST" -> {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        Task parsedBody = gson.fromJson(body, Task.class);
                        if (!httpTaskManager.getTasks().containsKey(parsedBody.getId())) {
                            httpTaskManager.createTask(parsedBody);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpTaskManager.updateTask(parsedBody);
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                    } else if (Pattern.matches("^/tasks/epic/$", path)) {
                        Epic parsedBody = gson.fromJson(body, Epic.class);
                        if (!httpTaskManager.getEpics().containsKey(parsedBody.getId())) {
                            httpTaskManager.createEpic(parsedBody);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpTaskManager.updateEpic(parsedBody);
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                    } else {
                        Subtask parsedBody = gson.fromJson(body, Subtask.class);
                        if (!httpTaskManager.getSubtasks().containsKey(parsedBody.getId())) {
                            httpTaskManager.createSubtask(parsedBody);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpTaskManager.updateSubtask(parsedBody);
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                    }
                }
                default -> {
                    System.out.println("Ожидался метод: POST. A получен " + method);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (IOException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }

    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/tasks");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
