package com.interview.todo.console;

import com.interview.todo.console.cache.MemCache;
import com.interview.todo.console.models.ToDo;
import com.interview.todo.console.rest.ErrorCodes;
import com.interview.todo.console.rest.RestClient;
import com.interview.todo.console.rest.RestResponse;
import com.interview.todo.console.rest.response.ErrorResponse;
import com.interview.todo.console.rest.response.ToDoResponse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ToDoPriorityListController implements Controller{
    Logger mLogger = Logger.getLogger("ToDoPriorityListController");

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
        Map<Integer, Integer> priorites = new HashMap<>();

        int minVal = 0, maxVal = 0;

        for(ToDo todo: toDoList) {
            int val = 1;
            if (priorites.containsKey(todo.getPriority())) {
                val = priorites.get(todo.getPriority());
                val++;
            }
            priorites.put(todo.getPriority(), val);

            if (minVal > todo.getPriority()) {
                minVal = todo.getPriority();
            }

            if (maxVal < todo.getPriority()) {
                maxVal = todo.getPriority();
            }
        }

        List<String> displayStrings = new LinkedList<>();
        int[] missingValues = new int[maxVal];
        for(Map.Entry entry: priorites.entrySet()) {
            displayStrings.add("Priority: "+entry.getKey()+" No of ToDos: "+entry.getValue());
            int key = (int)entry.getKey();
            missingValues[key-1] = 1;
        }
        mConsoleReader.displayStringList(displayStrings);


        mConsoleReader.displayString("Missing Priority Values...", false);
        List<String> missingPriorityStrings = new LinkedList<>();
        for(int i=0; i<missingValues.length; i++) {
            if (missingValues[i] == 0) {
                missingPriorityStrings.add("Missing Priority Value: "+(i+1));
            }
        }
        mConsoleReader.displayStringList(missingPriorityStrings);


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
