package com.interview.todo.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interview.todo.consts.ErrorCodes;
import org.springframework.http.HttpStatus;


public class UserResponse extends Response{

    @JsonIgnore
    public HttpStatus status;

    public ErrorCodes.ErrorCode error;

    public String message;

    public String userId;

    public String accessToken;

    public UserResponse() {
        this.status = HttpStatus.OK;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public ErrorCodes.ErrorCode getError() {
        return error;
    }

    public void setError(ErrorCodes.ErrorCode error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
