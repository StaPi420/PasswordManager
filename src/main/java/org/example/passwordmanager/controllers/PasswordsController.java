package org.example.passwordmanager.controllers;


import org.example.passwordmanager.PasswordManagerApplication;
import org.example.passwordmanager.dto.password.GetPasswordResponse;
import org.example.passwordmanager.dto.password.PasswordAddRequest;
import org.example.passwordmanager.dto.password.PasswordResponse;
import org.example.passwordmanager.dto.password.UpdatePasswordResponse;
import org.example.passwordmanager.entities.Password;
import org.example.passwordmanager.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/password")
public class PasswordsController {
    private final UserService userService;

    public PasswordsController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<PasswordResponse> getPasswords(Principal principal){
        String username = principal.getName();
        return userService.getPasswordListByUsername(username);
    }

    @PostMapping
    public ResponseEntity<PasswordResponse> addNewPassword(
            @RequestBody PasswordAddRequest request,
            Principal principal
            ){
        String username = principal.getName();
        Password password = userService.addNewPassword(request, username);
        System.out.println(password.getPassword());
        return ResponseEntity.status(201).body(
                new PasswordResponse(
                        password.getId(),
                        password.getSite(),
                        password.getUsername()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPasswordResponse> getPassword(
            @PathVariable Integer id,
            Principal principal
            ) {
        Password password = userService.getPasswordById(
                id,
                principal.getName()
        );
        GetPasswordResponse getPasswordResponse = new GetPasswordResponse(
                password.getId(),
                password.getUsername(),
                password.getSite(),
                password.getPassword()
        );
        return ResponseEntity.status(200).body(getPasswordResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdatePasswordResponse> updatePassword(
            @PathVariable Integer id,
            @RequestBody PasswordAddRequest passwordAddRequest,
            Principal principal
    ) {
        Password updated = userService.updatePassword(
                id,
                passwordAddRequest,
                principal.getName()
        );
        UpdatePasswordResponse response = new UpdatePasswordResponse(
                id,
                updated.getUsername(),
                updated.getSite(),
                updated.getPassword()
        );

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassword(
            @PathVariable Integer id,
            Principal principal
    ) {
        userService.deletePassword(id, principal.getName());

        return ResponseEntity.noContent().build();
    }
}
