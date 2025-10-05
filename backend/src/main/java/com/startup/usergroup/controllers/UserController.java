package com.startup.usergroup.controllers;

import com.startup.usergroup.models.User;
import com.startup.usergroup.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Allow all origins for simplicity; adjust as needed
public class UserController {

    @Autowired
    protected UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody User user) {
        user = getUserWithHashedPassword(user);
        return userRepository.save(user);
    }

    private User getUserWithHashedPassword(User user) {
        String salt = BCrypt.gensalt();

        return new User(
                user.getId(),
                user.getName(),
                user.getEmail(),
                BCrypt.hashpw(user.getPassword(), salt)
        );
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/by-email")
    public User getUserByUserEmail(@RequestParam String email) {
        return userRepository.findAll().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}