package com.springbootsample.jpa.cache.hazelcast.web.rest;

import com.springbootsample.jpa.cache.hazelcast.domain.User;
import com.springbootsample.jpa.cache.hazelcast.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;


    @GetMapping("/add")
    public User add() {
        User user = new User();
        user.setFirstName("hello");
        user.setLastName("com");
        user.setEmail("hello@abc.com");
        return userRepository.save(user);
    }

    @GetMapping("/find")
    public User findBy() {
        return userRepository.findOneByEmailIgnoreCase("hello@abc.com").get();
    }

}
