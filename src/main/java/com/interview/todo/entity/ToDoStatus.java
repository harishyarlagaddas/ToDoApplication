package com.interview.todo.entity;

public enum ToDoStatus {
    TODO (1),
    COMPLETED (2);

    int value;
    ToDoStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
