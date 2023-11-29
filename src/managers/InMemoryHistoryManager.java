package managers;

import managers.HistoryManager;
import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        history.addLast(task);
    }

    @Override
    public List<Task> getHistory() {
        int counterOfUnnecessaryTasks = history.size() - 10;
        if (counterOfUnnecessaryTasks > 0) {
            for (int i = 0; i < counterOfUnnecessaryTasks; i++) {
                history.removeFirst();
            }
        }
        return history;
    }
}
