package com.interview.todo.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interview.todo.entity.ToDo;
import com.interview.todo.consts.ErrorCodes;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ToDoResponse extends Response{

    @JsonIgnore
    public HttpStatus status;

    public ErrorCodes.ErrorCode error;

    public String message;

    public List<ToDo> toDoItems;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public ErrorCodes.ErrorCode getError() {
        return error;
    }

    public void setError(ErrorCodes.ErrorCode error) {
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
