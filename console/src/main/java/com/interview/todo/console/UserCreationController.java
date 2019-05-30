package com.interview.todo.console;

import com.interview.todo.console.cache.MemCache;
import com.interview.todo.console.rest.ErrorCodes;
import com.interview.todo.console.rest.RestClient;
import com.interview.todo.console.rest.RestResponse;
import com.interview.todo.console.rest.request.UserRequest;
import com.interview.todo.console.rest.response.ErrorResponse;
import com.interview.todo.console.rest.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

public class UserCreationController implements Controller{
    Logger mLogger = Logger.getLogger("LoginController");

    ConsoleReader mConsoleReader = new ConsoleReader();

    MemCache mCache = new MemCache();
    /**
     * Login Controller will start taking the
     *   email & password
     * and call /user/login rest endpoint to login an get the access token
     */
    public ControllerTypes start() {
        String name = mConsoleReader.displayStringAndGetString("Enter your name");
        String email = mConsoleReader.displayStringAndGetString("Enter your email");
        String password = mConsoleReader.displayStringAndGetString("Enter your password");
        return create(name, email, password);
    }

    private ControllerTypes create(String name, String email, String password) {
        UserRequest request = new UserRequest();
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);

        RestClient client = new RestClient();
        RestResponse response = client.Post(Consts.USER_CREATE_URI, null, request.toString());
        UserResponse userResponse = response.getUserResponse();
        if (null != userResponse.getError()) {
            displayError(userResponse.getError());
            return ControllerTypes.MainController;
        }

        mCache.store(Consts.CACHE_ACCESS_TOKEN_KEY, userResponse.getAccessToken());
        mCache.store(Consts.CACHE_USER_ID_KEY, userResponse.getUserId());

        return ControllerTypes.ToDoOptionsController;
    }

    private void displayError(ErrorResponse errorResponse) {
        if (ErrorCodes.ERROR_CODE_USER_EMAIL_EXISTS == errorResponse.getErrorCode()) {
            mConsoleReader.displayString("There is already an account associated with this email. Please login with this email or user new email id for account creation...", true);
        } else {
            mConsoleReader.displayString("Error while creating new account. Please try again or contact support...", true);
        }
    }
}
