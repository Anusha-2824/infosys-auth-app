package com.inventra.service;

import com.inventra.exception.InvalidOperationException;
import com.inventra.exception.ResourceNotFoundException;
import com.inventra.model.Role;
import com.inventra.model.User;
import com.inventra.repository.UserRepository;
import com.inventra.security.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ================= REGISTER =================
    public String register(String username,
                           String email,
                           String password,
                           String role) {

        if (userRepository.findByUsername(username).isPresent())
            throw new InvalidOperationException("Username already exists");

        if (userRepository.findByEmail(email).isPresent())
            throw new InvalidOperationException("Email already exists");

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.valueOf(role.toUpperCase()));

        // Admin auto approved
        if (role.equalsIgnoreCase("ADMIN")) {
            user.setApproved(true);
        } else {
            user.setApproved(false);
        }

        user.setFailedAttempts(0);
        user.setAccountLocked(false);

        userRepository.save(user);

        return "Registration successful. Wait for admin approval.";
    }

    // ================= LOGIN =================
    public String login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // Check approval
        if (!user.isApproved())
            throw new InvalidOperationException("Account not approved by admin");

        // Check lock
        if (user.isAccountLocked())
            throw new InvalidOperationException("Account locked. Contact admin.");

        // Password validation
        if (!passwordEncoder.matches(password, user.getPassword())) {

            user.setFailedAttempts(user.getFailedAttempts() + 1);

            if (user.getFailedAttempts() >= 3) {
                user.setAccountLocked(true);
                user.setLockTime(LocalDateTime.now());
            }

            userRepository.save(user);

            throw new InvalidOperationException("Invalid password");
        }

        // Reset failed attempts
        user.setFailedAttempts(0);
        userRepository.save(user);

        return jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()
        );
    }

    // ================= GET PENDING USERS =================
    public List<User> getPendingUsers() {
        return userRepository.findByApprovedFalse();
    }

    // ================= APPROVE USER =================
    public String approveUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setApproved(true);
        userRepository.save(user);

        return "User approved successfully";
    }

    // ================= REJECT USER =================
    public String rejectUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        userRepository.delete(user);

        return "User rejected successfully";
    }

    // ================= LOCKED USERS =================
    public List<User> getLockedUsers() {
        return userRepository.findByAccountLockedTrue();
    }

    // ================= UNLOCK USER =================
    public String unlockUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setAccountLocked(false);
        user.setFailedAttempts(0);
        user.setLockTime(null);

        userRepository.save(user);

        return "User unlocked successfully";
    }

    // ================= GENERATE RESET TOKEN =================
    public String generateResetToken(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Email not found"));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        userRepository.save(user);

        return "Reset Token: " + token;
    }

    // ================= RESET PASSWORD =================
    public String resetPassword(String token, String newPassword) {

        User user = userRepository.findByResetToken(token)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid reset token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);

        userRepository.save(user);

        return "Password Reset Successful";
    }
}