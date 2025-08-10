package com.example.todo.service;

import com.example.todo.model.Todo;
import com.example.todo.model.User;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<Todo> getAllTodosForCurrentUser() {
        User user = getCurrentUser();
        return todoRepository.findByUserId(user.getId());
    }

    public Todo createTodo(Todo todo) {
        User user = getCurrentUser();
        todo.setUserId(user.getId());
        return todoRepository.save(todo);
    }

    public Optional<Todo> getTodoById(String id) {
        return todoRepository.findById(id).filter(t -> t.getUserId().equals(getCurrentUser().getId()));
    }

    public Optional<Todo> updateTodo(String id, Todo updated) {
        return todoRepository.findById(id).filter(t -> t.getUserId().equals(getCurrentUser().getId())).map(t -> {
            t.setTitle(updated.getTitle());
            t.setDescription(updated.getDescription());
            t.setCompleted(updated.isCompleted());
            return todoRepository.save(t);
        });
    }

    public boolean deleteTodo(String id) {
        return todoRepository.findById(id).filter(t -> t.getUserId().equals(getCurrentUser().getId())).map(t -> {
            todoRepository.delete(t);
            return true;
        }).orElse(false);
    }
}