package org.example.passwordmanager.dto.password;

public record PasswordResponse(
        Integer id,
        String site,
        String username
) {
}
