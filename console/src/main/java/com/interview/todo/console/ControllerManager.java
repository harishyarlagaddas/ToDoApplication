package com.interview.todo.console;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

public class ControllerManager {
    Logger mLogger = Logger.getLogger("ControllerManager");

    ConsoleReader mConsoleReader = new ConsoleReader();

    public void start(ControllerTypes controllerName){
        ControllerTypes controller = controllerName;
        while (ControllerTypes.StopController != controller) {
            controller = startController(controller);
        }
        mConsoleReader.displayString("Exiting the application....", false);
    }

    private ControllerTypes startController(ControllerTypes controllerName) {
        switch (controllerName) {
            case MainController:
                MainController mainController = new MainController();
                return mainController.start();
            case LoginController:
                LoginController loginController = new LoginController();
                return loginController.start();
            case UserCreationController:
                UserCreationController userCreationController = new UserCreationController();
                return userCreationController.start();
            case ToDoListController:
                ToDoListController listController = new ToDoListController();
                return listController.start();
            case ToDoOptionsController:
                ToDoOptionsController optionsController = new ToDoOptionsController();
                return optionsController.start();
            case ToDoCreationController:
                ToDoCreationController creationController = new ToDoCreationController();
                return creationController.start();
            case ToDoDeletionController:
                ToDoDeletionController deletionController = new ToDoDeletionController();
                return deletionController.start();
            case ToDoGetPrioritiesController:
                ToDoPriorityListController priorityListController = new ToDoPriorityListController();
                return priorityListController.start();

                default:
                    return ControllerTypes.StopController;
        }
    }
}
