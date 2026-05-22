package org.example.passwordmanager.repositories;

import org.example.passwordmanager.dto.password.PasswordResponse;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.example.passwordmanager.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PasswordRepository extends JpaRepository<Password, Integer>{
    Optional<Password> findByUserAndSite(
            User user,
            String site
    );

    ArrayList<Password> findAllByUser(User user);
}
