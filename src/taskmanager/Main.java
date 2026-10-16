package taskmanager;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Menu-driven console UI. All user interaction lives here; all task
 * logic lives in TaskManager. main() just creates Main and calls run().
 */
public class Main {

    private static final String SAVE_FILE = "tasks.txt";

    private final TaskManager manager = new TaskManager();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        loadTasks();
        boolean running = true;
        while (running) {
            printMenu();
            // Everything is read with nextLine() (never nextInt()), so the
            // scanner never gets confused by leftover newline characters.
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": addTask(); break;
                case "2": listTasks(manager.getAllTasks()); break;
                case "3": completeTask(); break;
                case "4": deleteTask(); break;
                case "5": filterByPriority(); break;
                case "6": running = false; break;
                default: System.out.println("Please choose 1-6.");
            }
        }
        saveTasks();
        System.out.println("Tasks saved. Goodbye!");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("--- Task Manager ---");
        System.out.println("1. Add task");
        System.out.println("2. List all tasks");
        System.out.println("3. Complete a task");
        System.out.println("4. Delete a task");
        System.out.println("5. Filter by priority");
        System.out.println("6. Save and exit");
        System.out.print("Choose: ");
    }

    private void addTask() {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();
        Task.Priority priority = readPriority();
        LocalDate dueDate = readDueDate();
        manager.addTask(new Task(title, description, priority, dueDate));
        System.out.println("Task added!");
    }

    private void completeTask() {
        int id = readInt("ID of task to complete: ");
        System.out.println(manager.completeTask(id) ? "Marked as done!" : "No task with that ID.");
    }

    private void deleteTask() {
        int id = readInt("ID of task to delete: ");
        System.out.println(manager.deleteTask(id) ? "Task deleted." : "No task with that ID.");
    }

    private void filterByPriority() {
        listTasks(manager.filterByPriority(readPriority()));
    }

    private void listTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to show.");
            return;
        }
        for (Task task : tasks) {
            System.out.println(task);  // calls Task.toString() automatically
        }
    }

    /** Keep asking until the user types LOW, MEDIUM, or HIGH. */
    private Task.Priority readPriority() {
        while (true) {
            System.out.print("Priority (LOW/MEDIUM/HIGH): ");
            try {
                return Task.Priority.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Please type LOW, MEDIUM, or HIGH.");
            }
        }
    }

    /** A blank line means "no due date". */
    private LocalDate readDueDate() {
        System.out.print("Due date (YYYY-MM-DD, or blank for none): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(input);  // expects exactly YYYY-MM-DD
        } catch (DateTimeParseException e) {
            System.out.println("Couldn't understand that date -- saving without a due date.");
            return null;
        }
    }

    /** Keep asking until the user types a whole number. */
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    private void loadTasks() {
        try {
            manager.loadFromFile(SAVE_FILE);
        } catch (IOException e) {
            System.out.println("Couldn't load saved tasks: " + e.getMessage());
        }
    }

    private void saveTasks() {
        try {
            manager.saveToFile(SAVE_FILE);
        } catch (IOException e) {
            System.out.println("Couldn't save tasks: " + e.getMessage());
        }
    }
}
