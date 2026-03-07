package com.inventra.controller;

import com.inventra.model.User;
import com.inventra.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        @RequestParam String password) {

        String token = authService.login(username, password);
        return ResponseEntity.ok(token);
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username,
                                           @RequestParam String email,
                                           @RequestParam String password,
                                           @RequestParam String role) {

        String response = authService.register(username, email, password, role);
        return ResponseEntity.ok(response);
    }

    // ================= GET PENDING USERS =================
    @GetMapping("/pending")
    public ResponseEntity<List<User>> getPendingUsers() {

        return ResponseEntity.ok(authService.getPendingUsers());
    }

    // ================= APPROVE USER =================
    @PutMapping("/approve/{id}")
    public ResponseEntity<String> approveUser(@PathVariable Long id) {

        return ResponseEntity.ok(authService.approveUser(id));
    }

    // ================= REJECT USER =================
    @DeleteMapping("/reject/{id}")
    public ResponseEntity<String> rejectUser(@PathVariable Long id) {

        return ResponseEntity.ok(authService.rejectUser(id));
    }

    // ================= LOCKED USERS =================
    @GetMapping("/locked")
    public ResponseEntity<List<User>> getLockedUsers() {

        return ResponseEntity.ok(authService.getLockedUsers());
    }

    // ================= UNLOCK USER =================
    @PutMapping("/unlock/{id}")
    public ResponseEntity<String> unlockUser(@PathVariable Long id) {

        return ResponseEntity.ok(authService.unlockUser(id));
    }
}