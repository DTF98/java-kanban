package managers;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import utils.LocalDateTimeAdapter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Managers {
    public static HttpTaskManager getDefault(HistoryManager newHistory, URI url, String key) throws IOException {
        return new HttpTaskManager(url, newHistory, key);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileManager() {
        return new FileBackedTasksManager(getDefaultHistory(), new File("./src/Files/Data.csv"));
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }
}

