package com.example.my_app.dto;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;

    //  Add a no-args constructor (needed for Spring Boot)
    public RegisterRequest() {}

    //  Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
