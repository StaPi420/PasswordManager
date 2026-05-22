package org.example.passwordmanager.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    public User(){};

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
