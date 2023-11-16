import java.util.Objects;

public class Task {
    private String title; /*Название задачи*/
    private String description; /*Описание задачи*/
    private int id;  /*Уникальный идентификационный номер задачи*/
    private String status; /*Мы будем выделять следующие этапы жизни задачи:
                            a. NEW — задача только создана, но к её выполнению ещё не приступили.
                            b. IN_PROGRESS — над задачей ведётся работа.
                            c. DONE — задача выполнена.*/

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = "NEW";
    }

    public Task(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
