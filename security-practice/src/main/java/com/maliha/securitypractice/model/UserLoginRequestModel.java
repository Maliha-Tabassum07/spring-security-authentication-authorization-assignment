package com.maliha.securitypractice.model;

public class UserLoginRequestModel {
    private String email;
    private String passwords;

    public UserLoginRequestModel(String email, String passwords) {
        this.email = email;
        this.passwords = passwords;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }
}
