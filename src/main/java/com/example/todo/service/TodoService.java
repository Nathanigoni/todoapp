package com.example.todo.service;

import com.example.todo.model.Todo;
import com.example.todo.model.User;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    private User getUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Todo createTodo(Todo todo, String usernameOrEmail) {
        User user = getUserByUsernameOrEmail(usernameOrEmail);
        todo.setUserId(user.getId());
        return todoRepository.save(todo);
    }

    public List<Todo> getTodosByUser(String usernameOrEmail) {
        User user = getUserByUsernameOrEmail(usernameOrEmail);
        return todoRepository.findByUserId(user.getId());
    }

    public Todo updateTodo(String id, Todo updated, String usernameOrEmail) {
        User user = getUserByUsernameOrEmail(usernameOrEmail);
        Todo existing = todoRepository.findById(id)
                .filter(t -> t.getUserId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Todo not found or not yours"));

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setCompleted(updated.isCompleted());
        return todoRepository.save(existing);
    }

    public void deleteTodo(String id, String usernameOrEmail) {
        User user = getUserByUsernameOrEmail(usernameOrEmail);
        Todo existing = todoRepository.findById(id)
                .filter(t -> t.getUserId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Todo not found or not yours"));

        todoRepository.delete(existing);
    }
}
