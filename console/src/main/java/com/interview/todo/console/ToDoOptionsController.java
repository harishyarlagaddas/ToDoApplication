package com.interview.todo.console;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToDoOptionsController implements Controller{
    Logger mLogger = Logger.getLogger("ToDoOptionsController");

    ConsoleReader mConsoleReader = new ConsoleReader();

    /**
     * MainController starts with first set of options
     *  Login
     *  Create New Account
     */
    public ControllerTypes start() {
        while (true) {
            int option = mConsoleReader.displayOptionsAndGetInput(new LinkedList<>(Arrays.asList(
                    "List All ToDos",
                    "Create new ToDo item",
                    "Delete ToDo item",
                    "Get Priorities",
                    "Exit")));

            if (0 == option) {
                //mLogger.log(Level.INFO, "Starting ToDoListController");
                return ControllerTypes.ToDoListController;
            } else if (1 == option) {
                //mLogger.log(Level.INFO, "Starting ToDoCreationController");
                return ControllerTypes.ToDoCreationController;
            }else if (2 == option){
                //mLogger.log(Level.INFO, "Starting ToDoDeletionController");
                return ControllerTypes.ToDoDeletionController;
            } else if (3 == option) {
                //mLogger.log(Level.INFO, "Starting ToDoListController");
                return ControllerTypes.ToDoGetPrioritiesController;
            } else if (4 == option) {
                return ControllerTypes.StopController;
            } else {
                continue;
            }
        }
    }
}
