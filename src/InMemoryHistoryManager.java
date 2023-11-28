import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        int counterOfUnnecessaryTasks = history.size() - 10;
        if (counterOfUnnecessaryTasks > 0) {
            for (int i = 0; i < counterOfUnnecessaryTasks; i++) {
                history.remove(i);
            }
        }
        return history;
    }
}
