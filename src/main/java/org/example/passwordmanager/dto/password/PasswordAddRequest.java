package org.example.passwordmanager.dto.password;

public record PasswordAddRequest(
        String site,
        String username,
        String password
) {
}
