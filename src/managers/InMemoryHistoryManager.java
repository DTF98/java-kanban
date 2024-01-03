package managers;

import managers.HistoryManager;
import model.Node;
import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    private final Map<Integer, Node<Task>> history = new HashMap<>();

    private Node<Task> linkLast(Task element) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(tail, element, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
        return newNode;
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node<Task> current = head;
        for (int i = 0; i < size; i++) {
            tasks.add(current.data);
            current = current.next;
        }
        return tasks;
    }

    private void removeNode(Node<Task> a) {
        if (a.prev != null && a.next != null) {
            a.prev.next = a.next;
            a.next.prev = a.prev;
        } else if (a.prev != null) {
            a.prev.next = null;
            tail = a.prev;
        } else if (a.next != null) {
            a.next.prev = null;
            head = a.next;
        } else {
            head = null;
            tail = null;
        }
        size--;
    }

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            removeNode(history.get(task.getId()));
            history.remove(task.getId());
        }
        history.put(task.getId(), linkLast(task));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
        history.remove(id);
    }
}

