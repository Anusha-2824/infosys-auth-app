package com.inventra.controller;

import com.inventra.model.User;
import com.inventra.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // GET Pending Users
    @GetMapping("/pending-users")
    public List<User> getPendingUsers(){
        return userRepository.findByApprovedFalse();
    }

    // GET Locked Users
    @GetMapping("/locked-users")
    public List<User> getLockedUsers(){
        return userRepository.findByAccountLockedTrue();
    }

}