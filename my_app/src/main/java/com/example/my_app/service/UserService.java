package com.example.my_app.service;

import com.example.my_app.model.User;
import com.example.my_app.repository.UserRepository;
import com.example.my_app.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // REGISTER (CREATE) a new user with password policy
    public void registerUser(String username, String email, String password) {
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain a mix of letters and numbers.");
        }
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email already in use.");
        }

        String encodedPassword = encoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    // ✅ AUTHENTICATE user and return JWT Token
    public String authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !encoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
        return jwtUtil.generateToken(email);  // Return JWT Token
    }

    // READ (Get user by ID)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // READ (Get all users)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // UPDATE user
    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                if (!isValidPassword(updatedUser.getPassword())) {
                    throw new IllegalArgumentException("Password must be at least 8 characters long and contain letters and numbers.");
                }
                user.setPassword(encoder.encode(updatedUser.getPassword()));
            }
            return userRepository.save(user);
        });
    }

    // DELETE user
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //  Password Policy: Minimum 8 chars, must contain letters & numbers
    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8 && password.matches(".*\\d.*") && password.matches(".*[a-zA-Z].*");
    }
}
