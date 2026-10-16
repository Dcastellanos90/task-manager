package taskmanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds all tasks and the operations on them.
 *
 * OOP concept: separation of concerns. This class knows HOW tasks are
 * stored, searched, and saved. Main only handles talking to the user
 * and calls these methods -- it never touches the list directly.
 */
public class TaskManager {

    // ArrayList: a resizable list. We keep Task objects in it.
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    /** Remove the task with this id. Returns false when the id doesn't exist. */
    public boolean deleteTask(int id) {
        Task task = findById(id);
        if (task != null) {
            tasks.remove(task);
            return true;
        }
        return false;
    }

    /** Mark the task with this id as done. Returns false when the id doesn't exist. */
    public boolean completeTask(int id) {
        Task task = findById(id);
        if (task != null) {
            task.markDone();
            return true;
        }
        return false;
    }

    /** Linear search: walk the list and return the task with this id (or null). */
    public Task findById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    /** Every task. Returns a copy so callers can't mess with our internal list. */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    /** Every task with the given priority. */
    public List<Task> filterByPriority(Task.Priority priority) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getPriority() == priority) {
                result.add(task);
            }
        }
        return result;
    }

    /** Write every task to a plain-text file, one task per line. */
    public void saveToFile(String filename) throws IOException {
        Path path = Paths.get(filename);
        // try-with-resources: the writer is closed automatically, even on error
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Task task : tasks) {
                writer.write(task.toDataString());
                writer.newLine();
            }
        }
    }

    /** Read tasks back from the file. A missing file just means "no tasks yet". */
    public void loadFromFile(String filename) throws IOException {
        Path path = Paths.get(filename);
        if (!Files.exists(path)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    tasks.add(Task.fromDataString(line));
                }
            }
        }
    }
}
