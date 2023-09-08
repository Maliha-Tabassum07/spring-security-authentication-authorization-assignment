package com.maliha.securitypractice.model;

public class UserDTO {
        private long id;
        private String userId;
        private String email;
        private String password;

        public UserDTO() {
        }

        public UserDTO(long id, String userId, String email, String password) {
            this.id = id;
            this.userId = userId;
            this.email = email;
            this.password = password;
        }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

