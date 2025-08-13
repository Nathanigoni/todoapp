package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // Create Todo
    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo, Authentication authentication) {
        String username = authentication.getName(); // from JWT
        return ResponseEntity.ok(todoService.createTodo(todo, username));
    }

    // Get all Todos for the logged-in user
    @GetMapping
    public ResponseEntity<List<Todo>> getUserTodos(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(todoService.getTodosByUser(username));
    }

    // Update Todo
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable String id, @RequestBody Todo todo, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(todoService.updateTodo(id, todo, username));
    }

    // Delete Todo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String id, Authentication authentication) {
        String username = authentication.getName();
        todoService.deleteTodo(id, username);
        return ResponseEntity.noContent().build();
    }
}
