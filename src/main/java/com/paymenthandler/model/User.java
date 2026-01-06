package com.paymenthandler.model;

import com.paymenthandler.enums.Role;

public class User {
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private Role role;

    public User(Long id, String name, String email){
        this.username = name;
        this.email = email;
        this.id = id;
        this.role = Role.USER;
    }

    public User(Long id, String name, String email, String passwordHash, Role role){
        this.id = id;
        this.username = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role != null ? role : Role.USER;
    }

    public Long getId() {return this.id;}
    public String getName() {return this.username;}
    public String getEmail() {return this.email;}
    public String getPasswordHash() {return this.passwordHash;}
    public Role getRole() {return this.role != null ? this.role : Role.USER;}

}
