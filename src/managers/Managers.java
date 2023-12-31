package managers;

import managers.HistoryManager;
import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;

public class Managers {
    public static TaskManager getDefault(HistoryManager newHistory) {
        return new InMemoryTaskManager(newHistory);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
