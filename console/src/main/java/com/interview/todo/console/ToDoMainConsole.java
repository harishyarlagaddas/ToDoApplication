package com.interview.todo.console;

public class ToDoMainConsole {
    public static void main ( String [] arguments )
    {
        ControllerManager manager = new ControllerManager();
        manager.start(ControllerTypes.MainController);
    }
}
