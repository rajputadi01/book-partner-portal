package com.capg.portal.auth.controller;

import com.capg.portal.auth.dto.UserRegistrationDto;
import com.capg.portal.auth.entity.Role;
import com.capg.portal.auth.entity.User;
import com.capg.portal.auth.repository.RoleRepository;
import com.capg.portal.auth.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthRestController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username is already taken!"));
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Default to Staff for safety
        Role staffRole = roleRepository.findByName("ROLE_STAFF")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found in Database."));
        user.setRoles(Set.of(staffRole));

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
    }

    // The Frontend will call this with Basic Auth to verify credentials
    @GetMapping("/me")
    public ResponseEntity<?> verifyLogin(Authentication authentication) {
        return ResponseEntity.ok(authentication);
    }
}