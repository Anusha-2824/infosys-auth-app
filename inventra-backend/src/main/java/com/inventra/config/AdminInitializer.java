package com.inventra.config;

import com.inventra.model.Role;
import com.inventra.model.User;
import com.inventra.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Check if admin already exists
        if (userRepository.findByUsername("admin").isEmpty()) {

            User admin = new User();

            admin.setUsername("admin");
            admin.setEmail("admin@inventra.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);

            // Admin auto approved
            admin.setApproved(true);

            userRepository.save(admin);

            System.out.println("====================================");
            System.out.println(" DEFAULT ADMIN CREATED ");
            System.out.println(" username: admin");
            System.out.println(" password: admin123");
            System.out.println("====================================");
        }
    }
}