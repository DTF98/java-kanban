package client;

import server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String API_TOKEN;
    private final String url;
    private final HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(String url) throws IOException {
        this.url = url;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                API_TOKEN = response.body();
            } else {
                System.out.println("Что-то пошло не так(метод register не вернул API_TOKEN). Сервер вернул код состояния: " + response.statusCode()); //TODO key + API token
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }

    }

    public void put(String key, String json) {
        URI putUrl = URI.create(url + "/save/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(putUrl)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Всё получилось! Сервер вернул код состояния: " + response.statusCode());
            } else {
                System.out.println("Что-то пошло не так (метод put). Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key, String token) {
        String result;
        URI loadUrl = URI.create(url + "/load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(loadUrl)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                result = response.body();
                return result;
            } else {
                System.out.println("Что-то пошло не так(client метод load). Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        result = "В далекой, далекой галактике(load)";
        return result;
    }

    public String getAPI_TOKEN() {
        return API_TOKEN;
    }

}
