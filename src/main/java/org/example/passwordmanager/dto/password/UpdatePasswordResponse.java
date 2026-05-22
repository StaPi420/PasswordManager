package org.example.passwordmanager.dto.password;

public record UpdatePasswordResponse(
        Integer id,
        String username,
        String site,
        String password
) {
}
