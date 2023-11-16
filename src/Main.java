import java.util.Scanner;

public class Main {
//    Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.
//    Распечатайте списки эпиков, задач и подзадач, через System.out.println(..)
//    Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
//    И, наконец, попробуйте удалить одну из задач и один из эпиков.

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();
        manager.createTask("Название1", "Описание1");//1
        manager.createTask("Название2", "Описание2");//2
        manager.createEpic("Название3", "Описание3");//3
        manager.createSubtask("Название4", "Описание4", 3);//4
        manager.createSubtask("Название5", "Описание5", 3);//5
        manager.createEpic("Название6", "Описание6");//6
        manager.createSubtask("Название7", "Описание7", 6);//7
        System.out.println(manager.tasks.get(1).getTitle());
        System.out.println(manager.tasks.get(2).getTitle());
        System.out.println(manager.epics.get(3).getTitle());
        System.out.println(manager.subtasks.get(4).getTitle());
        System.out.println(manager.subtasks.get(5).getTitle());
        System.out.println(manager.epics.get(6).getTitle());
        System.out.println(manager.subtasks.get(7).getTitle());
        manager.updateTask(1, "Название11", "Описание11", "DONE");//8
        System.out.println(manager.tasks.get(8).getStatus());
        manager.updateSubtask(7, "Название7", "Описание7", "DONE", 6);//9
        System.out.println(manager.epics.get(6).getStatus()); // Показал работу изменения статуса NEW на DONE в Эпиках
        manager.updateSubtask(4, "Название8", "Описание8", "DONE", 3);
        System.out.println(manager.epics.get(3).getStatus());// Показал работу изменения статуса NEW на IN_PROGRESS, при присутствии DONE в Эпиках
        manager.deleteTaskById(2);
        System.out.println(manager.tasks.size());// До удаления было 2 - стало 1
        manager.deleteEpicById(3);
        System.out.println(manager.epics.size());// До удаления было 2 - стало 1
        System.out.println(manager.subtasks.size());// До удаления было 3 - стало 1
        while (true) {
            System.out.println("Выберите что хотите сделать:");
            printMenu();
            int command = scanner.nextInt();
            scanner.nextLine();
            switch (command) {
                case 1:
                    manager.printAllTasks();
                    break;
                case 2:
                    manager.printAllEpics();
                    break;
                case 3:
                    manager.printAllSubTasks();
                    break;
                case 4:
                    System.out.println("Введите id Эпика:");
                    int id1 = scanner.nextInt();
                    manager.getSubtaskInEpic(id1);
                    break;
                case 5:
                    manager.deleteAlltasks();
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
                    manager.getTaskById(id2);
                    break;
                case 9:
                    System.out.println("Введите id Эпика:");
                    int id3 = scanner.nextInt();
                    manager.getEpicById(id3);
                    break;
                case 10:
                    System.out.println("Введите id Подзадачи:");
                    int id4 = scanner.nextInt();
                    manager.getSubtaskById(id4);
                    break;
                case 11:
                    System.out.println("Введите название задачи");
                    String title1 = scanner.nextLine();
                    System.out.println("Введите описание задачи");
                    String description1 = scanner.nextLine();
                    manager.createTask(title1, description1);
                    break;
                case 12:
                    System.out.println("Введите название Эпика");
                    String title2 = scanner.nextLine();
                    System.out.println("Введите описание Эпика");
                    String description2 = scanner.nextLine();
                    manager.createEpic(title2, description2);
                    break;
                case 13:
                    System.out.println("Введите название подзадачи");
                    String title3 = scanner.nextLine();
                    System.out.println("Введите описание подзадачи");
                    String description3 = scanner.nextLine();
                    System.out.println("Введите id Эпика, к которому эта подзадача относится");
                    int idEpic1 = scanner.nextInt();
                    manager.createSubtask(title3, description3, idEpic1);
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
                    manager.updateTask(idTask1, title4, description4, status1);
                    break;
                case 15:
                    System.out.println("Введите обновлённый id Эпика");
                    int idEpic2 = scanner.nextInt();
                    System.out.println("Введите обновлённое название Эпика");
                    String title5 = scanner.nextLine();
                    System.out.println("Введите обновлённое описание Эпика");
                    String description5 = scanner.nextLine();
                    manager.updateEpic(idEpic2, title5, description5);
                    break;
                case 16:
                    System.out.println("Введите id Эпика, к которому эта подзадача относится");
                    int idEpic3 = scanner.nextInt();
                    System.out.println("Введите обновлённый id подзадачи");
                    int idSubtask1 = scanner.nextInt();
                    System.out.println("Введите обновлённое название подзадачи");
                    String title6 = scanner.nextLine();
                    System.out.println("Введите обновлённое описание подзадачи");
                    String description6 = scanner.nextLine();
                    System.out.println("Введите обновлённый статус подзадачи");
                    String status2 = scanner.nextLine();
                    manager.updateSubtask(idSubtask1, title6, description6, status2, idEpic3);
                    break;
                case 17:
                    System.out.println("Введите id задачи для удаления");
                    int idTask2 = scanner.nextInt();
                    manager.deleteTaskById(idTask2);
                    break;
                case 18:
                    System.out.println("Введите id Эпика для данной подзадачи");
                    int idEpic4 = scanner.nextInt();
                    manager.deleteEpicById(idEpic4);
                    break;
                case 19:
                    System.out.println("Введите id подзадачи для удаления");
                    int idSubtask2 = scanner.nextInt();
                    System.out.println("Введите id Эпика для данной подзадачи");
                    int idEpic5 = scanner.nextInt();
                    manager.deleteSubtaskById(idSubtask2, idEpic5);
                    break;
                case 20:
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
        System.out.println("Выход из программы - 20");
    }
}
