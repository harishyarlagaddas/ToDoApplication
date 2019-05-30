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

public class LoginController implements Controller{
    Logger mLogger = Logger.getLogger("LoginController");

    ConsoleReader mConsoleReader = new ConsoleReader();

    MemCache mCache = new MemCache();
    /**
     * Login Controller will start taking the
     *   email & password
     * and call /user/login rest endpoint to login an get the access token
     */
    public ControllerTypes start() {
        String email = mConsoleReader.displayStringAndGetString("Enter your email");
        String password = mConsoleReader.displayStringAndGetString("Enter your password");

        return login(email, password);
    }

    private ControllerTypes login(String email, String password) {
        UserRequest request = new UserRequest();
        request.setEmail(email);
        request.setPassword(password);

        RestClient client = new RestClient();
        RestResponse response = client.Post(Consts.USER_LOGIN_URI, null, request.toString());
        UserResponse userResponse = response.getUserResponse();
        if (null != userResponse.getError()) {
            displayError(userResponse.getError());
            return ControllerTypes.MainController;
        }

        mCache.store(Consts.CACHE_ACCESS_TOKEN_KEY, userResponse.getAccessToken());
        mCache.store(Consts.CACHE_USER_ID_KEY, userResponse.getUserId());

        mConsoleReader.displayString("User successfully logged in...", false);
        return ControllerTypes.ToDoOptionsController;
    }

    private void displayError(ErrorResponse errorResponse) {
        if (ErrorCodes.ERROR_CODE_USER_DOES_NOT_EXISTS == errorResponse.getErrorCode()) {
            mConsoleReader.displayString("User with this email doesn't exist. Please login with correct email or create new account with this email...", true);
        } else if (ErrorCodes.ERROR_CODE_INVALID_PASSWORD == errorResponse.getErrorCode()) {
            mConsoleReader.displayString("Wrong password. Please use the correct password...", true);
        } else {
            mConsoleReader.displayString("Error while login. Please try again or contact support...", true);
        }
    }
}
