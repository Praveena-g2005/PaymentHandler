package com.paymenthandler.dao;

import com.paymenthandler.model.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped //CDI bean
@Named("InmemoryDao")
public class InMemoryUserDao implements UserDao{
    Map<Long,User> users = new HashMap<>();
    AtomicLong generateid = new AtomicLong(1);
    @Override
    public User createUser(User u) {
        Long id = generateid.getAndIncrement();
        User user= new User(id,u.getName(),u.getEmail());
        users.put(id,user);
        return user;
    }
    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }
    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }
    @Override
    public Optional<User> updateUser(User user) {
        return Optional.ofNullable(users.computeIfPresent(user.getId(),(k,v)->new User(k,user.getName(),user.getEmail())));
    }
    @Override
    public boolean deleteUser(Long id) {
        return users.remove(id)!=null;
    }
}
