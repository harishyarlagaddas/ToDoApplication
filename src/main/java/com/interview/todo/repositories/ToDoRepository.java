package com.interview.todo.repositories;

import com.interview.todo.entity.ToDo;
import com.interview.todo.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ToDoRepository extends CrudRepository<ToDo, Long> {
    List<ToDo> findAllByUserId(String userId);
    ToDo findAllByNoteId(String noteId);
    void deleteByNoteId(String noteId);
}
