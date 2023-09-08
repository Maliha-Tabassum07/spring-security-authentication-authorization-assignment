package com.maliha.securitypractice.service;

import com.maliha.securitypractice.model.UserDTO;

public interface UserService {
    UserDTO createUser(UserDTO user) throws Exception;
    UserDTO getUser(String email);

    UserDTO getUserByUserId(String id) throws Exception;

}
