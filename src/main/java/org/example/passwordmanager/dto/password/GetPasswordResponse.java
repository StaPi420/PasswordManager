package org.example.passwordmanager.dto.password;

public record GetPasswordResponse(
        Integer id,
        String username,
        String site,
        String password
) {
}
