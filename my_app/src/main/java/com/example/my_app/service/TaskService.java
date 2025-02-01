package com.example.my_app.service;

import com.example.my_app.model.Task;
import com.example.my_app.model.User;
import com.example.my_app.repository.TaskRepository;
import com.example.my_app.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(String title, String description) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setUser(user);
        return taskRepository.save(task);
    }

    public List<Task> getUserTasks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());

        return taskRepository.findByUserId(user.getId());
    }
}
