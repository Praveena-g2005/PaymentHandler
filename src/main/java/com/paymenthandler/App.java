package com.paymenthandler;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import com.paymenthandler.model.User;
import com.paymenthandler.service.UserService;

public class App {
    public static void main(String[] args) {
        SeContainer container =SeContainerInitializer.newInstance().initialize();
        UserService service=container.select(UserService.class).get();

        User u1=service.createUser("Praveena", "praveena@gmail.com");
        User u2=service.createUser("Alice", "alice@gmail.com");
        service.createUser("Peter", "peter@example.com");

        System.out.println("All users (names starting with 'P'):");
        service.getUsernameStartsWith('P').forEach(System.out::println); // method reference

        System.out.println("\nGet user by id:");
        service.getUserById(u1.getId()).ifPresent(u -> System.out.println(u.getName()+" "+u.getEmail()));

        System.out.println("\nUpdate Name:");
        service.updateUserName(u1.getId(), "praveena")
           .ifPresent(u -> System.out.println("Updated: " + u.getName()));

        System.out.println("\nDelete user 2:");
        boolean deleted = service.deleteUser(u2.getId());
        System.out.println("Deleted user2? " + deleted);

        System.out.println("\nAll users now:");
        service.getUsernameStartsWith('P').forEach(System.out::println);

        System.out.println("Random id : "+service.getRandomId());

        container.close();
    }
}
