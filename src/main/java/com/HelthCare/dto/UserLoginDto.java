package com.HelthCare.dto;

import lombok.Data;

@Data
public class UserLoginDto {
    private String username;
    private String password;
    
    // Manually added getters and setters to fix compilation errors
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}