package managers;

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
            oldTail.setNext(newNode);
        }
        size++;
        return newNode;
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node<Task> current = head;
        for (int i = 0; i < size; i++) {
            tasks.add(current.getData());
            current = current.getNext();
        }
        return tasks;
    }

    private void removeNode(Node<Task> a) {
        if (a.getPrev() != null && a.getNext() != null) {
            a.getPrev().setNext(a.getNext());
            a.getNext().setPrev(a.getPrev());
        } else if (a.getPrev() != null) {
            a.getPrev().setNext(null);
            tail = a.getPrev();
        } else if (a.getNext() != null) {
            a.getNext().setPrev(null);
            head = a.getNext();
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

    public int getSize() {
        return size;
    }
}

