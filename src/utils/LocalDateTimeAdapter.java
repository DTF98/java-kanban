package utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd--MM--yyyy HH:mm");
    //private static final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
        if (localDate != null)
            jsonWriter.value(localDate.format(formatter));
        else jsonWriter.value("null");
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        String next = jsonReader.nextString();
        if (next.equals("null")) {
            return null;
        }
        return LocalDateTime.parse(next, formatter);

    }
}