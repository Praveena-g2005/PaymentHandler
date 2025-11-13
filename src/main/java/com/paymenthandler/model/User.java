package com.paymenthandler.model;

public class User {
    private Long id;
    private String username;
    private String email;

    public User(Long id ,String name, String email){
        this.username=name;
        this.email=email;
        this.id=id;
    }

    public Long getId() {return this.id;}
    public String getName() {return this.username;}
    public String getEmail() {return this.email;}

}
