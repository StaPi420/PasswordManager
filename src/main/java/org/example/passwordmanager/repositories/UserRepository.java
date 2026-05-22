package org.example.passwordmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.passwordmanager.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByUsername(String username);
}
