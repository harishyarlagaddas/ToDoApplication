package com.interview.todo.console.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.todo.console.rest.response.ToDoResponse;
import com.interview.todo.console.rest.response.UserResponse;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResponse {

    @JsonProperty("statusCode")
    public int StatusCode;

    @JsonProperty("response")
    public String Response;

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public UserResponse getUserResponse() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            UserResponse response = mapper.readValue(getResponse(), UserResponse.class);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ToDoResponse getToDoResponse() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ToDoResponse response = mapper.readValue(getResponse(), ToDoResponse.class);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

