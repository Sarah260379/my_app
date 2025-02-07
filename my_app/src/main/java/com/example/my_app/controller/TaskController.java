package com.example.my_app.controller;

import com.example.my_app.model.Task;
import com.example.my_app.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //  Create Task
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); //  Get the authenticated user's email from JWT

        return ResponseEntity.ok(taskService.createTask(task.getTitle(), task.getDescription(), email));
    }


    //  Get User Tasks
    @GetMapping
    public ResponseEntity<List<Task>> getUserTasks() {
        return ResponseEntity.ok(taskService.getUserTasks());
    }

    //  Update Task
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task updatedTask) {
        return ResponseEntity.ok(taskService.updateTask(id, updatedTask.getTitle(), updatedTask.getDescription()));
    }

    //  Delete Task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
