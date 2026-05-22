package org.example.passwordmanager.dto.auth;

public record LoginRequest(
        String username,
        String password
) {}
