package com.maliha.securitypractice.controller;

import com.maliha.securitypractice.constants.AppConstants;
import com.maliha.securitypractice.entity.UserEntity;
import com.maliha.securitypractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/join")
    public String joinGroup(@RequestBody UserEntity user) {
        user.setRoles(AppConstants.DEFAULT_ROLE);//USER
        String encryptedPwd = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPwd);
        repository.save(user);
        return "Hi " + user.getUserId() + " welcome to group !";
    }
    //If loggedin user is ADMIN -> ADMIN OR MODERATOR
    //If loggedin user is MODERATOR -> MODERATOR

    @GetMapping("/access/{userId}/{userRole}")
    //@Secured("ROLE_ADMIN")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public String giveAccessToUser(@PathVariable long userId, @PathVariable String userRole, Principal principal) {
        UserEntity user = repository.findById(userId).get();
        List<String> activeRoles = getRolesByLoggedInUser(principal);
        String newRole = "";
        if (activeRoles.contains(userRole)) {
            newRole = user.getRoles() + "," + userRole;
            user.setRoles(newRole);
        }
        repository.save(user);
        return "Hi " + user.getUserId() + " New Role assign to you by " + principal.getName();
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserEntity> loadUsers() {
        return repository.findAll();
    }

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String testUserAccess() {
        return "user can only access this !";
    }

    private List<String> getRolesByLoggedInUser(Principal principal) {
        String roles = getLoggedInUser(principal).getRoles();
        List<String> assignRoles = Arrays.stream(roles.split(",")).collect(Collectors.toList());
        if (assignRoles.contains("ROLE_ADMIN")) {
            return Arrays.stream(AppConstants.ADMIN_ACCESS).collect(Collectors.toList());
        }
        if (assignRoles.contains("ROLE_MODERATOR")) {
            return Arrays.stream(AppConstants.MODERATOR_ACCESS).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private UserEntity getLoggedInUser(Principal principal) {
        return repository.findByUserId(principal.getName()).get();
    }
}