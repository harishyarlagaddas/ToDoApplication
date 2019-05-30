package com.interview.todo.console;

import com.interview.todo.console.cache.MemCache;
import com.interview.todo.console.models.ToDo;
import com.interview.todo.console.rest.ErrorCodes;
import com.interview.todo.console.rest.RestClient;
import com.interview.todo.console.rest.RestResponse;
import com.interview.todo.console.rest.request.ToDoRequest;
import com.interview.todo.console.rest.request.UserRequest;
import com.interview.todo.console.rest.response.ErrorResponse;
import com.interview.todo.console.rest.response.ToDoResponse;
import com.interview.todo.console.rest.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ToDoListController implements Controller{
    Logger mLogger = Logger.getLogger("ToDoListController");

    ConsoleReader mConsoleReader = new ConsoleReader();

    MemCache mCache = new MemCache();

    /**
     * Login Controller will start taking the
     *   email & password
     * and call /user/login rest endpoint to login an get the access token
     */
    public ControllerTypes start() {
        return list();
    }

    private ControllerTypes list() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Consts.HEADER_AUTHORIZATION_KEY, mCache.get(Consts.CACHE_ACCESS_TOKEN_KEY));

        RestClient client = new RestClient();
        RestResponse response = client.Get(Consts.TODO_GET_ALL_URI, headers);
        ToDoResponse toDoResponse = response.getToDoResponse();
        if (null == toDoResponse || null != toDoResponse.getError()) {
            displayError(toDoResponse.getError());
            return ControllerTypes.LoginController;
        }

        mConsoleReader.displayString("Current list of todo items...", false);

        List<ToDo> toDoList = toDoResponse.getToDoItems();
        List<String> toDoListStrings = new LinkedList<>();
        for(ToDo todo: toDoList) {
            toDoListStrings.add("Note: "+todo.getNote()+"  Priority: "+todo.getPriority());
        }
        mConsoleReader.displayStringList(toDoListStrings);

        return ControllerTypes.ToDoOptionsController;
    }

    private void displayError(ErrorResponse errorResponse) {
        if (ErrorCodes.ERROR_CODE_INVALID_ACCESS_TOKEN == errorResponse.getErrorCode()) {
            mConsoleReader.displayString("Looks like access token is invalid or expired. Please login to proceed further...", true);
        } else {
            mConsoleReader.displayString("Error while login. Please try again or contact support...", true);
        }
    }
}
