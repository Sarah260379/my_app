package com.example.my_app.controller;

import com.example.my_app.model.Task;
import com.example.my_app.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        return ResponseEntity.ok(taskService.createTask(task.getTitle(), task.getDescription()));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getUserTasks() {
        return ResponseEntity.ok(taskService.getUserTasks());
    }
}
