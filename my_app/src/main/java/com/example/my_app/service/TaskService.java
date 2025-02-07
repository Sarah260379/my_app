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

    // Create a Task
    public Task createTask(String title, String description, String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setUser(user);
        return taskRepository.save(task);
    }


    // Get Tasks for the Authenticated User
    public List<Task> getUserTasks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());

        return taskRepository.findByUserId(user.getId());
    }

    // Update a Task
    public Task updateTask(Long id, String title, String description) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());

        // Ensure user is authenticated
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        //  Find the task
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        //
        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: You cannot update this task");
        }

        // Update the task
        task.setTitle(title);
        task.setDescription(description);
        return taskRepository.save(task);
    }



    // Delete a Task
    public void deleteTask(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());

        // Find the task in the database
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Ensure the task belongs to the logged-in user
        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this task");
        }

        // Delete the task
        taskRepository.delete(task);
    }

}
