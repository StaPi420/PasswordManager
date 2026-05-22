package org.example.passwordmanager.dto.auth;

public record RegistrationRequest(
    String username,
    String password
) {}
