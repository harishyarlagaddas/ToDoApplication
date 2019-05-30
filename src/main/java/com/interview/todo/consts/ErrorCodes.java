package com.interview.todo.consts;

public class ErrorCodes {
    public static class ErrorCode {
        public int errorCode;
        public String errorMessage;

        public ErrorCode(int errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }
    }
    public static ErrorCode ERROR_CODE_MISSING_EMAIL = new ErrorCode(2001, "Email missing in the request body.");
    public static ErrorCode ERROR_CODE_MISSING_NAME = new ErrorCode(2002, "Name missing in the request body");
    public static ErrorCode ERROR_CODE_MISSING_PASSWORD = new ErrorCode(2003, "Password missing in the request body");
    public static ErrorCode ERROR_CODE_USER_EMAIL_EXISTS = new ErrorCode(2004, "User with this email already exists. Please login with this user or use different email");
    public static ErrorCode ERROR_CODE_USER_DOES_NOT_EXISTS = new ErrorCode(2005, "No user exists with this email. Before login please create user with this email");
    public static ErrorCode ERROR_CODE_INVALID_PASSWORD = new ErrorCode(2006, "Password doesn't match. Please try again using correct password");

    public static ErrorCode ERROR_CODE_INVALID_ACCESS_TOKEN = new ErrorCode(3001, "Invalid Access Token");
    public static ErrorCode ERROR_CODE_MISSING_NOTE = new ErrorCode(3002, "ToDo Note missing in request body. It is one of the mandatory parameter");
    public static ErrorCode ERROR_CODE_MISSING_ACCESS_TOKEN = new ErrorCode(3003, "AccessToken missing in request body. It is one of the mandatory parameters");

    public static ErrorCode ERROR_CODE_MISSING_NOTE_ID = new ErrorCode(4001, "Missing noteId query parameter");
}
