package com.interview.todo.console.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.interview.todo.console.models.ToDo;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ToDoResponse {
    private ErrorResponse error;

    private String message;

    private List<ToDo> toDoItems;

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ToDo> getToDoItems() {
        return toDoItems;
    }

    public void setToDoItems(List<ToDo> toDoItems) {
        this.toDoItems = toDoItems;
    }

    public void addToDoItem(ToDo item) {
        if (null == this.toDoItems) {
            this.toDoItems = new ArrayList<>();
        }
        this.toDoItems.add(item);
    }
}
