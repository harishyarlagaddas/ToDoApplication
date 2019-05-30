package com.interview.todo.console;

import com.interview.todo.console.cache.MemCache;
import com.interview.todo.console.models.ToDo;
import com.interview.todo.console.rest.ErrorCodes;
import com.interview.todo.console.rest.RestClient;
import com.interview.todo.console.rest.RestResponse;
import com.interview.todo.console.rest.request.ToDoRequest;
import com.interview.todo.console.rest.response.ErrorResponse;
import com.interview.todo.console.rest.response.ToDoResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ToDoCreationController implements Controller{
    Logger mLogger = Logger.getLogger("ToDoCreationController");

    ConsoleReader mConsoleReader = new ConsoleReader();

    MemCache mCache = new MemCache();

    /**
     * Login Controller will start taking the
     *   email & password
     * and call /user/login rest endpoint to login an get the access token
     */
    public ControllerTypes start() {
        String todo = mConsoleReader.displayStringAndGetString("Please Enter ToDo Note");
        int priority = mConsoleReader.displayStringAndGetInt("Please enter the priority");
        return create(todo, priority);
    }

    private ControllerTypes create(String todo, int priority) {
        Map<String, String> headers = new HashMap<>();
        headers.put(Consts.HEADER_AUTHORIZATION_KEY, mCache.get(Consts.CACHE_ACCESS_TOKEN_KEY));

        ToDoRequest request = new ToDoRequest();
        request.setNote(todo);
        request.setPriority(priority);

        RestClient client = new RestClient();
        RestResponse response = client.Post(Consts.TODO_CREATE_URI, headers, request.toString());
        ToDoResponse toDoResponse = response.getToDoResponse();
        if (null != toDoResponse.getError()) {
            displayError(toDoResponse.getError());
            return ControllerTypes.LoginController;
        }
        mConsoleReader.displayString("ToDo item created successfully!!", false);

        return ControllerTypes.ToDoOptionsController;
    }

    private void displayError(ErrorResponse errorResponse) {
        if (ErrorCodes.ERROR_CODE_INVALID_ACCESS_TOKEN == errorResponse.getErrorCode()) {
            mConsoleReader.displayString("Looks like access token is invalid or expired. Please login to proceed further...", true);
        } else {
            mConsoleReader.displayString("Error while creating ToDo Item. Please try again or contact support...", true);
        }
    }
}
