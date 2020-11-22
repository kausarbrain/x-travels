package com.xtravels.models.dto;

import com.xtravels.utils.FieldMatch;

import javax.validation.constraints.*;

@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
public class UserRegistrationDto {

    @NotBlank(message = "Full Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;


    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Confirm Password is required")
    private String confirmPassword;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRegistrationDto(){
    }





}
