package com.example.apple.todolist.data;

/**
 * Created by mark on 7/4/17.
 */

public class ToDoItem {
    private String description;
    private String dueDate;
    private boolean done;
    private String category;

    public ToDoItem(String description, String dueDate, boolean done, String category) {
        this.description = description;
        this.dueDate = dueDate;
        this.done = done;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isDone() {return done;}

    public void setDone(boolean done) { this.done = done;}

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }
}
