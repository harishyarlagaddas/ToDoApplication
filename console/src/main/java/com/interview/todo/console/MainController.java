package com.interview.todo.console;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Logger;

public class MainController implements Controller{
    Logger mLogger = Logger.getLogger("MainController");

    ConsoleReader mConsoleReader = new ConsoleReader();

    /**
     * MainController starts with first set of options
     *  Login
     *  Create New Account
     */
    public ControllerTypes start() {
        while (true) {
            int option = mConsoleReader.displayOptionsAndGetInput(new LinkedList<>(Arrays.asList("Login", "Create New Account", "Exit")));
            if (0 == option) {
                return ControllerTypes.LoginController;
            } else if (1 == option) {
                return ControllerTypes.UserCreationController;
            } else if (2 == option) {
                return ControllerTypes.StopController;
            } else {
                continue;
            }
        }
    }
}
