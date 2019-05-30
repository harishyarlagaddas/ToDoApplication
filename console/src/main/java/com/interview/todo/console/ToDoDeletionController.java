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

public class ToDoDeletionController implements Controller{
    Logger mLogger = Logger.getLogger("ToDoDeletionController");

    ConsoleReader mConsoleReader = new ConsoleReader();

    MemCache mCache = new MemCache();

    /**
     * Login Controller will start taking the
     *   email & password
     * and call /user/login rest endpoint to login an get the access token
     */
    public ControllerTypes start() {
        while(true) {
            List<ToDo> toDoList =  getToDoList();
            if (null == toDoList) {
                return ControllerTypes.LoginController;
            }

            List<String> toDoListStrings = toDoList.stream().map(todo -> todo.getNote()).collect(Collectors.toList());
            mConsoleReader.displayString("Select the ToDo Item to delete", false);
            int option = mConsoleReader.displayOptionsAndGetInput(toDoListStrings);

            ToDo deleteToDo = toDoList.get(option);
            return delete(deleteToDo);
        }
    }

    private List<ToDo> getToDoList() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Consts.HEADER_AUTHORIZATION_KEY, mCache.get(Consts.CACHE_ACCESS_TOKEN_KEY));

        RestClient client = new RestClient();
        RestResponse response = client.Get(Consts.TODO_GET_ALL_URI, headers);
        ToDoResponse toDoResponse = response.getToDoResponse();
        if (null == toDoResponse || null != toDoResponse.getError()) {
            displayError(toDoResponse.getError());
            return null;
        }

        List<ToDo> toDoList = toDoResponse.getToDoItems();
        return toDoList;
    }

    private ControllerTypes delete(ToDo deleteToDo) {
        Map<String, String> headers = new HashMap<>();
        headers.put(Consts.HEADER_AUTHORIZATION_KEY, mCache.get(Consts.CACHE_ACCESS_TOKEN_KEY));

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(Consts.QUERY_PARAM_NOTE_ID, deleteToDo.getNoteId());

        ToDoRequest request = new ToDoRequest();
        request.setNoteId(deleteToDo.getNoteId());

        RestClient client = new RestClient();
        RestResponse response = client.Delete(Consts.TODO_DELETE_URI, headers, queryParams, null);
        ToDoResponse toDoResponse = response.getToDoResponse();
        if (null != toDoResponse.getError()) {
            displayError(toDoResponse.getError());
            return ControllerTypes.ToDoOptionsController;
        }

        mConsoleReader.displayString("Successfully deleted the ToDo Item", false);

        return ControllerTypes.ToDoOptionsController;

    }
    private void displayError(ErrorResponse errorResponse) {
        if (ErrorCodes.ERROR_CODE_INVALID_ACCESS_TOKEN == errorResponse.getErrorCode()) {
            mConsoleReader.displayString("Looks like access token is invalid or expired. Please login to proceed further...", true);
        } else {
            mConsoleReader.displayString("Error while deleting the ToDo Item. Please try again or contact support...", true);
        }
    }
}
