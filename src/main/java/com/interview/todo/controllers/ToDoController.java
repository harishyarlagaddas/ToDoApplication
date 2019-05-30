package com.interview.todo.controllers;

import com.interview.todo.cache.MemCache;
import com.interview.todo.consts.ErrorCodes;
import com.interview.todo.entity.ToDo;
import com.interview.todo.entity.ToDoStatus;
import com.interview.todo.models.ToDoModel;
import com.interview.todo.repositories.ToDoRepository;
import com.interview.todo.response.ToDoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/todo")
public class ToDoController {

    @Autowired
    private MemCache mCache;

    @Autowired
    private ToDoRepository mToDoRepository;

    @RequestMapping("")
    public ResponseEntity getAllToDos(@RequestHeader("Authorization") String accessToken) {
        ToDoResponse response = new ToDoResponse();

        if (null == accessToken) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(ErrorCodes.ERROR_CODE_MISSING_ACCESS_TOKEN);
        } else {
            String userId = mCache.validateAccessToken(accessToken);
            if (null == userId) {
                response.setStatus(HttpStatus.UNAUTHORIZED);
                response.setError(ErrorCodes.ERROR_CODE_INVALID_ACCESS_TOKEN);
            } else {
                List<ToDo> toDoList = mToDoRepository.findAllByUserId(userId);

                response.setStatus(HttpStatus.OK);
                response.setMessage("Successfully added todo item");
                response.setToDoItems(toDoList);
            }
        }
        return new ResponseEntity<>(response.toString(), response.getStatus());
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity addToDo(@RequestHeader("Authorization") String accessToken, @RequestBody ToDoModel toDo) {
        ToDoResponse response = new ToDoResponse();

        if (null == accessToken) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(ErrorCodes.ERROR_CODE_MISSING_ACCESS_TOKEN);
        }else if (null == toDo.getNote()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(ErrorCodes.ERROR_CODE_MISSING_NOTE);
        } else {
            String userId = mCache.validateAccessToken(accessToken);
            if (null == userId) {
                response.setStatus(HttpStatus.UNAUTHORIZED);
                response.setError(ErrorCodes.ERROR_CODE_INVALID_ACCESS_TOKEN);
            } else {
                ToDo toDoToStore = new ToDo();
                toDoToStore.setNote(toDo.getNote());
                toDoToStore.setCreatedOn(new Date());
                toDoToStore.setStatus(ToDoStatus.TODO.getValue());
                toDoToStore.setUserId(userId);
                toDoToStore.setNoteId(UUID.randomUUID().toString());
                toDoToStore.setPriority(toDo.getPriority());
                if (null != toDo.getCompleteBy()) {
                    toDoToStore.setCompleteBy(toDo.getCompleteBy());
                }
                mToDoRepository.save(toDoToStore);

                response.setStatus(HttpStatus.CREATED);
                response.setMessage("Successfully added todo item");
                response.setToDoItems(new LinkedList<ToDo>(Arrays.asList(toDoToStore)));
            }
        }

        return new ResponseEntity<>(response.toString(), response.getStatus());
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity addToDo(@RequestHeader("Authorization") String accessToken, @RequestParam String noteId) {
        ToDoResponse response = new ToDoResponse();
        if (null == accessToken) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(ErrorCodes.ERROR_CODE_MISSING_ACCESS_TOKEN);
        } else if (null == noteId) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(ErrorCodes.ERROR_CODE_MISSING_NOTE_ID);
        } else {
            String userId = mCache.validateAccessToken(accessToken);
            if (null == userId) {
                response.setStatus(HttpStatus.UNAUTHORIZED);
                response.setError(ErrorCodes.ERROR_CODE_INVALID_ACCESS_TOKEN);
            } else {
                ToDo item = mToDoRepository.findAllByNoteId(noteId);
                mToDoRepository.delete(item);

                response.setStatus(HttpStatus.OK);
                response.setMessage("Successfully deleted");
            }
        }
        return new ResponseEntity<>(response.toString(), response.getStatus());
    }
}
