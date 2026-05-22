package org.example.passwordmanager.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "passwords")
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String site;

    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    public Password() {};

    public Password(String site, String username, String password, User user){
        this.site = site;
        this.password = password;
        this.user = user;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public String getSite() {
        return site;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public User getUser() {
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
