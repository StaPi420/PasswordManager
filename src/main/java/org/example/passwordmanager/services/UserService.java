package org.example.passwordmanager.services;

import org.example.passwordmanager.dto.password.PasswordAddRequest;
import org.example.passwordmanager.dto.password.PasswordResponse;
import org.example.passwordmanager.entities.Password;
import org.example.passwordmanager.repositories.*;
import org.example.passwordmanager.entities.User;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;

    public UserService(UserRepository userRepository, PasswordRepository passwordRepository){

        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
    }

    public UserDetails loadUserByUsername(@NonNull String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    public ArrayList<PasswordResponse> getPasswordListByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        ArrayList<Password> passwords = passwordRepository.findAllByUser(user);
        ArrayList<PasswordResponse> response = new ArrayList<>();
        for (Password password : passwords){
            response.add(new PasswordResponse(
                    password.getId(),
                    password.getSite(),
                    password.getUsername()
                    ));
        }
        return response;
    }

    public Password addNewPassword(
            PasswordAddRequest request,
            String username
    ){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        if (passwordRepository.findByUserAndSite(user, request.site()).isPresent()) {
            throw new RuntimeException("Password already exists");
        }
        Password password = new Password(
                request.site(),
                request.username(),
                request.password(),
                user
        );
        return passwordRepository.save(password);
    }

    public Password getPasswordById(int id, String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        System.out.println("Not found");
        Password password = passwordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "password not found"
                ));

        System.out.println("found");

        if (!user.getUsername().equals(password.getUser().getUsername())){
            throw new AccessDeniedException("Forbidden");
        }

        return password;
    }

    public Password updatePassword(int id, PasswordAddRequest request, String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        Password password = passwordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "password not found"
                ));

        if (!user.getUsername().equals(password.getUser().getUsername())){
            throw new AccessDeniedException("Forbidden");
        }

        password.setUsername(request.username());
        password.setSite(request.site());
        password.setPassword(request.password());

        return passwordRepository.save(password);
    }

    public void deletePassword(Integer id, String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        Password password = passwordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "password not found"
                ));

        if (!user.getUsername().equals(password.getUser().getUsername())){
            throw new AccessDeniedException("Forbidden");
        }

        passwordRepository.delete(password);
    }
}
