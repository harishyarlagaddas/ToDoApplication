package com.interview.todo.console;

public class Consts {
    public static String BASE_URI = "http://127.0.0.1:8080";
    public static String USER_CREATE_URI = BASE_URI+"/user/create";
    public static String USER_LOGIN_URI = BASE_URI+"/user/login";
    public static String TODO_GET_ALL_URI = BASE_URI+"/todo/";
    public static String TODO_CREATE_URI = BASE_URI+"/todo/create";
    public static String TODO_DELETE_URI = BASE_URI+"/todo/delete";

    public static final String CACHE_ACCESS_TOKEN_KEY = "ACCESS_TOKEN";
    public static final String CACHE_USER_ID_KEY = "USER_ID";
    public static final String CACHE_TODO_NOTE_ID = "NOTE_ID";

    public static final String HEADER_AUTHORIZATION_KEY = "Authorization";
    public static final String QUERY_PARAM_NOTE_ID = "noteId";
}
