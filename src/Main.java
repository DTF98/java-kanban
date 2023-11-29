import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        manager.createTask(new Task("Название1", "Описание1", 1));//1
        manager.createTask(new Task("Название2", "Описание2", 2));//2
        manager.createEpic(new Epic("Название3", "Описание3", 3));//3
        manager.createSubtask(new Subtask("Название4", "Описание4", 3));//4
        manager.createSubtask(new Subtask("Название5", "Описание5", 3));//5
        manager.createEpic(new Epic("Название6", "Описание6", 6));//6
        manager.createSubtask(new Subtask("Название7", "Описание7", 6));//7
        System.out.println(manager.getTasks().get(1).getTitle());
        System.out.println(manager.getTasks().get(2).getTitle());
        System.out.println(manager.getEpics().get(3).getTitle());
        System.out.println(manager.getSubtasks().get(4).getTitle());
        System.out.println(manager.getSubtasks().get(5).getTitle());
        System.out.println(manager.getEpics().get(6).getTitle());
        System.out.println(manager.getSubtasks().get(7).getTitle());
        manager.updateTask(new Task(1, "Название11", "Описание11", "DONE"));
        System.out.println(manager.getTasks().get(1).getStatus());
        manager.updateSubtask(new Subtask(7, "Название7", "Описание7", "DONE", 6));
        System.out.println(manager.getEpics().get(6).getStatus()); // Показал работу изменения статуса NEW на DONE в Эпиках
        manager.updateSubtask(new Subtask(4, "Название8", "Описание8", "DONE", 3));
        System.out.println(manager.getEpics().get(3).getStatus());// Показал работу изменения статуса NEW на IN_PROGRESS,
        // при присутствии DONE в Эпиках
        manager.deleteTaskById(2);
        System.out.println(manager.getTasks().size());// До удаления было 2 - стало 1
        manager.deleteEpicById(3);
        System.out.println(manager.getEpics().size());// До удаления было 2 - стало 1
        System.out.println(manager.getSubtasks().size());// До удаления было 3 - стало 1
        manager.createTask(new Task("Название20", "Описание20", 8));//8
        manager.createTask(new Task("Название21", "Описание21", 9));//9
        manager.createTask(new Task("Название22", "Описание22", 10));//10
        manager.createTask(new Task("Название23", "Описание23", 11));//11
        System.out.println("Проверка истории");
        historyManager.add(manager.getTaskById(1));//1
        historyManager.add(manager.getTaskById(1));//2
        historyManager.add(manager.getEpicById(6));//3
        historyManager.add(manager.getTaskById(1));//4
        historyManager.add(manager.getTaskById(1));//5
        historyManager.add(manager.getSubtaskById(7));//6
        historyManager.add(manager.getTaskById(1));//7
        historyManager.add(manager.getTaskById(8));//8
        historyManager.add(manager.getTaskById(9));//9
        historyManager.add(manager.getTaskById(10));//10
        for (Task task : historyManager.getHistory()) {
            System.out.println(task.getId());
        }
        System.out.println("Проверка удаления лишних элементов истории");
        historyManager.add(manager.getTaskById(11));//11
        for (Task task : historyManager.getHistory()) {
            System.out.println(task.getId());
        }

        while (true) {
            System.out.println("Выберите что хотите сделать:");
            printMenu();
            int command = scanner.nextInt();
            scanner.nextLine();
            switch (command) {
                case 1:
                    ArrayList<Task> Tasks = new ArrayList<>(manager.getAllTasks());
                    break;
                case 2:
                    ArrayList<Epic> Epics = new ArrayList<>(manager.getAllEpics());
                    break;
                case 3:
                    ArrayList<Subtask> Subtasks1 = new ArrayList<>(manager.getAllSubTasks());
                    break;
                case 4:
                    System.out.println("Введите id Эпика:");
                    int id1 = scanner.nextInt();
                    ArrayList<Subtask> Subtasks2 = new ArrayList<>(manager.getSubtaskInEpic(id1));
                    break;
                case 5:
                    manager.deleteAllTasks();
                    break;
                case 6:
                    manager.deleteAllEpics();
                    break;
                case 7:
                    manager.deleteAllSubtasks();
                    break;
                case 8:
                    System.out.println("Введите id Задачи:");
                    int id2 = scanner.nextInt();
                    System.out.println(manager.getTaskById(id2).getTitle());
                    break;
                case 9:
                    System.out.println("Введите id Эпика:");
                    int id3 = scanner.nextInt();
                    System.out.println(manager.getEpicById(id3));
                    break;
                case 10:
                    System.out.println("Введите id Подзадачи:");
                    int id4 = scanner.nextInt();
                    System.out.println(manager.getSubtaskById(id4));
                    break;
                case 11:
                    System.out.println("Введите название задачи");
                    String title1 = scanner.nextLine();
                    System.out.println("Введите описание задачи");
                    String description1 = scanner.nextLine();
                    manager.createTask(new Task(title1, description1));
                    break;
                case 12:
                    System.out.println("Введите название Эпика");
                    String title2 = scanner.nextLine();
                    System.out.println("Введите описание Эпика");
                    String description2 = scanner.nextLine();
                    manager.createEpic(new Epic(title2, description2));
                    break;
                case 13:
                    System.out.println("Введите название подзадачи");
                    String title3 = scanner.nextLine();
                    System.out.println("Введите описание подзадачи");
                    String description3 = scanner.nextLine();
                    System.out.println("Введите id Эпика, к которому эта подзадача относится");
                    int idEpic1 = scanner.nextInt();
                    manager.createSubtask(new Subtask(title3, description3, idEpic1));
                    break;
                case 14:
                    System.out.println("Введите  id задачи, которую хотите изменить");
                    int idTask1 = scanner.nextInt();
                    System.out.println("Введите обновлённое название задачи");
                    String title4 = scanner.nextLine();
                    System.out.println("Введите обновлённое описание задачи");
                    String description4 = scanner.nextLine();
                    System.out.println("Введите обновлённый статус задачи");
                    String status1 = scanner.nextLine();
                    manager.updateTask(new Task(idTask1, title4, description4, status1));
                    break;
                case 15:
                    System.out.println("Введите id Эпика");
                    int idEpic2 = scanner.nextInt();
                    System.out.println("Введите обновлённое название Эпика");
                    String title5 = scanner.nextLine();
                    System.out.println("Введите обновлённое описание Эпика");
                    String description5 = scanner.nextLine();
                    manager.updateEpic(new Epic(title5, description5, idEpic2));
                    break;
                case 16:
                    System.out.println("Введите id Эпика, к которому эта подзадача относится");
                    int idEpic3 = scanner.nextInt();
                    System.out.println("Введите id подзадачи");
                    int idSubtask1 = scanner.nextInt();
                    System.out.println("Введите обновлённое название подзадачи");
                    String title6 = scanner.nextLine();
                    System.out.println("Введите обновлённое описание подзадачи");
                    String description6 = scanner.nextLine();
                    System.out.println("Введите обновлённый статус подзадачи");
                    String status2 = scanner.nextLine();
                    manager.updateSubtask(new Subtask(idSubtask1, title6, description6, status2, idEpic3));
                    break;
                case 17:
                    System.out.println("Введите id задачи для удаления");
                    int idTask2 = scanner.nextInt();
                    manager.deleteTaskById(idTask2);
                    break;
                case 18:
                    System.out.println("Введите id Эпика");
                    int idEpic4 = scanner.nextInt();
                    manager.deleteEpicById(idEpic4);
                    break;
                case 19:
                    System.out.println("Введите id подзадачи для удаления");
                    int idSubtask2 = scanner.nextInt();
                    manager.deleteSubtaskById(idSubtask2);
                    break;
                case 20:
                    historyManager.getHistory();
                    break;
                case 21:
                    return;
                default:
                    System.out.println("Введена неправильная команда");
            }

        }
    }

    static void printMenu() {
        System.out.println("Получение списка всех задач - 1");
        System.out.println("Получение списка всех Эпиков - 2");
        System.out.println("Получение списка всех подзадач - 3");
        System.out.println("Получение списка всех подзадач у определенного Эпика - 4");
        System.out.println("Удаление всех задач - 5");
        System.out.println("Удаление всех Эпиков - 6");
        System.out.println("Удаление всех подзадач - 7");
        System.out.println("Получение по идентификатору задачи - 8");
        System.out.println("Получение по идентификатору Эпика - 9");
        System.out.println("Получение по идентификатору подзадачи - 10");
        System.out.println("Создание задачи - 11");
        System.out.println("Создание Эпика - 12");
        System.out.println("Создание подзадачи для конкретного Эпика - 13");
        System.out.println("Обновление задачи - 14");
        System.out.println("Обновление Эпика - 15");
        System.out.println("Обновление подзадачи для конкретного Эпика - 16");
        System.out.println("Удаление по идентификатору задачи - 17");
        System.out.println("Удаление по идентификатору Эпика - 18");
        System.out.println("Удаление по идентификатору подзадачи для конкретного Эпика - 19");
        System.out.println("Запросить историю просмотренных задач - 20");
        System.out.println("Выход из программы - 21");
    }
}
