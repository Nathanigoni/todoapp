package com.example.todo.payload;

import jakarta.validation.constraints.NotBlank;

public class AuthRequest {
    @NotBlank
    private String login; // can be username or email

    @NotBlank
    private String password;

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
