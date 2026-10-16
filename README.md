# Task Manager (Java)

A console-based task tracker. Add tasks, list them, mark them complete,
delete them, filter by priority -- and everything is saved to a text file
so your tasks are still there next time you run it.

## OOP concepts demonstrated

- **Encapsulation** -- `Task`'s fields are `private`. Outside code can only
  reach them through getters/setters, and `status` can only change through
  `markDone()`. The object protects its own data.
- **Enums** -- `Task.Priority` (LOW/MEDIUM/HIGH) and `Task.Status` (TODO/DONE)
  restrict values to a fixed set, so invalid states are impossible.
- **Constructor overloading + chaining** -- one constructor builds a new task
  (auto-assigning an id), the other rebuilds a task loaded from the file.
  The first calls the second with `this(...)` to avoid duplicated code.
- **Static members** -- `nextId` is shared across all `Task` objects so every
  task gets a unique id, even after loading old tasks from the file.
- **Collections** -- `TaskManager` stores tasks in an `ArrayList<Task>` and
  returns filtered views (e.g. by priority) as new lists.
- **Separation of concerns** -- `Task` (data), `TaskManager` (logic/storage),
  and `Main` (user interaction) each have one job.

## How to compile and run

No build tools, no dependencies -- just a JDK with `javac`:

```bash
cd task-manager

# Compile all three classes
javac src/taskmanager/*.java

# Run it
java -cp src taskmanager.Main
```

Tasks are saved to `tasks.txt` in the folder you run it from.

## Example session

```
--- Task Manager ---
1. Add task
2. List all tasks
3. Complete a task
4. Delete a task
5. Filter by priority
6. Save and exit
Choose: 1
Title: Study for Java midterm
Description: Review chapters 4-6 on OOP
Priority (LOW/MEDIUM/HIGH): HIGH
Due date (YYYY-MM-DD, or blank for none): 2026-10-10
Task added!

--- Task Manager ---
1. Add task
2. List all tasks
3. Complete a task
4. Delete a task
5. Filter by priority
6. Save and exit
Choose: 1
Title: Buy groceries
Description: Milk, eggs, bread
Priority (LOW/MEDIUM/HIGH): low
Due date (YYYY-MM-DD, or blank for none):
Task added!

--- Task Manager ---
1. Add task
2. List all tasks
3. Complete a task
4. Delete a task
5. Filter by priority
6. Save and exit
Choose: 2
[ ] #1 Study for Java midterm (priority: HIGH, due: 2026-10-10)
[ ] #2 Buy groceries (priority: LOW, due: no due date)

--- Task Manager ---
1. Add task
2. List all tasks
3. Complete a task
4. Delete a task
5. Filter by priority
6. Save and exit
Choose: 5
Priority (LOW/MEDIUM/HIGH): HIGH
[ ] #1 Study for Java midterm (priority: HIGH, due: 2026-10-10)

--- Task Manager ---
1. Add task
2. List all tasks
3. Complete a task
4. Delete a task
5. Filter by priority
6. Save and exit
Choose: 3
ID of task to complete: 1
Marked as done!

--- Task Manager ---
1. Add task
2. List all tasks
3. Complete a task
4. Delete a task
5. Filter by priority
6. Save and exit
Choose: 2
[x] #1 Study for Java midterm (priority: HIGH, due: 2026-10-10)
[ ] #2 Buy groceries (priority: LOW, due: no due date)

--- Task Manager ---
1. Add task
2. List all tasks
3. Complete a task
4. Delete a task
5. Filter by priority
6. Save and exit
Choose: 6
Tasks saved. Goodbye!
```

## What I learned

- Designing with classes: splitting data (`Task`), logic (`TaskManager`),
  and UI (`Main`) instead of putting everything in one file.
- Why fields are `private` and how getters/setters control access to them.
- Using enums instead of raw strings for fixed choices like priority.
- Storing objects in an `ArrayList` and searching/filtering with loops.
- Saving state to a plain-text file (one pipe-separated line per task) and
  reading it back -- no database or serialization library needed.
- The classic `Scanner` trap: reading everything with `nextLine()` and
  parsing numbers avoids the skipped-input bug that `nextInt()` causes.
