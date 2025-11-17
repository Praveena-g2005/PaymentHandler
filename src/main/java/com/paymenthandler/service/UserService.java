package com.paymenthandler.service;

import java.util.*;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.paymenthandler.dao.*;
import com.paymenthandler.model.*;

@ApplicationScoped
public class UserService {

    @Inject
    @Named("InmemoryDao")
    private UserDao dao;

    @Inject // produces
    private String randomid; 

    public String getRandomId(){
        return randomid;
    }

    public User createUser(String name, String email) {
        return dao.createUser(new User(null, name, email));
    }

    public Optional<User> getUserById(Long id) {
        return dao.findById(id);
    }
    // Used Stream + Method Reference + filter + collect
    public List<String> getUsernameStartsWith(char s) {
        List<String> users = dao.findAllUsers().stream().map(User::getName).filter(n -> n.startsWith(String.valueOf(s)))
                .collect(Collectors.toList());
        return users;
    }
    //Used Optional map + method reference
    public Optional<User> updateUserName(Long id, String name){
        return dao.findById(id).map(u-> new User(u.getId(),name,u.getEmail())).flatMap(dao::updateUser);
    }

    public boolean deleteUser(Long id){
        return dao.deleteUser(id);
    }
}
