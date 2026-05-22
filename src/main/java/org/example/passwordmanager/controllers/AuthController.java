package org.example.passwordmanager.controllers;

import org.example.passwordmanager.dto.auth.LoginRequest;
import org.example.passwordmanager.dto.auth.RegistrationRequest;
import org.example.passwordmanager.entities.User;
import org.example.passwordmanager.repositories.UserRepository;
import org.example.passwordmanager.services.JWTService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final JWTService jwtService;

    public AuthController(
            UserRepository userRepository,
            JWTService jwtService
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request){
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password());
        userRepository.save(user);
        return ResponseEntity.ok("Пользователь создан");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            User user = userRepository.findByUsername(request.username())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.username()));
            if (!user.getPassword().equals(request.password())) {
                return ResponseEntity.status(401).body("Неверный пароль");
            }
            String token = jwtService.generateToken(user.getUsername());
            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity.status(400).body("Нет такого пользователя");
        }
    }
}
