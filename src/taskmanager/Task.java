package taskmanager;

import java.time.LocalDate;

/**
 * Represents a single to-do task.
 *
 * OOP concepts used here:
 * - Encapsulation: fields are private, so outside code must go through
 *   the getters/setters below. That keeps the object's data valid.
 * - Enums: Priority and Status are fixed sets of allowed values, which
 *   prevents typos like "higgh" from ever becoming a priority.
 */
public class Task {

    /** How urgent a task is. */
    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    /** Where a task is in its lifecycle. */
    public enum Status {
        TODO, DONE
    }

    // Shared across ALL Task objects: hands out a unique id to each new task.
    private static int nextId = 1;

    private final int id;  // final: an id never changes once assigned
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private LocalDate dueDate;  // may be null when there is no due date

    /** Build a brand-new task. It always starts as TODO. */
    public Task(String title, String description, Priority priority, LocalDate dueDate) {
        this(nextId, title, description, priority, Status.TODO, dueDate);
    }

    /** Rebuild a task that was saved to the file (see fromDataString). */
    public Task(int id, String title, String description, Priority priority,
                Status status, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
        nextId = Math.max(nextId, id + 1);  // keep future ids unique after loading
    }

    // ---- getters and setters: the controlled doorway to the private fields ----
    public int getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Status getStatus() { return status; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    /** The only way a task becomes DONE. */
    public void markDone() {
        this.status = Status.DONE;
    }

    /** How a task looks when printed in the menu. */
    @Override
    public String toString() {
        String mark = (status == Status.DONE) ? "[x]" : "[ ]";
        String due = (dueDate == null) ? "no due date" : dueDate.toString();
        return String.format("%s #%d %s (priority: %s, due: %s)",
                mark, id, title, priority, due);
    }

    /**
     * Pack this task into one pipe-separated line for the save file.
     * Pipes are used instead of commas because titles may contain commas.
     */
    public String toDataString() {
        String safeTitle = title.replace("|", " ");
        String safeDescription = description.replace("|", " ");
        String due = (dueDate == null) ? "" : dueDate.toString();
        return id + "|" + safeTitle + "|" + safeDescription + "|"
                + priority + "|" + status + "|" + due;
    }

    /** Rebuild a Task from one line of the save file. */
    public static Task fromDataString(String line) {
        String[] parts = line.split("\\|", -1);  // -1 keeps empty trailing fields
        int id = Integer.parseInt(parts[0]);
        Priority priority = Priority.valueOf(parts[3]);
        Status status = Status.valueOf(parts[4]);
        LocalDate due = parts[5].isEmpty() ? null : LocalDate.parse(parts[5]);
        return new Task(id, parts[1], parts[2], priority, status, due);
    }
}
