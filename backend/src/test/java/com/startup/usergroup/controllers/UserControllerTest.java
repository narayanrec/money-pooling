package com.startup.usergroup.controllers;

import com.startup.usergroup.models.User;
import com.startup.usergroup.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    UserController target;
    @Mock
    UserRepository userRepository;
    @BeforeEach
    void setUp() {
        target = new UserController();
        target.userRepository = userRepository;
    }

    @Test
    void getUserByUserEmail_userFound() {
        when(userRepository.findAll()).thenReturn(List.of(new User(1l, "xyz", "xyz@gmail.com", "hashedpassword")));
        var user = target.getUserByUserEmail("xyz@gmail.com");
        assertNotNull(user);
        assertEquals("xyz@gmail.com", user.getEmail());
    }

    @Test
    void getUserByUserEmail_userNotFound() {
        when(userRepository.findAll()).thenReturn(List.of(new User(1l, "xyz", "xyz1@gmail.com", "hashedpassword")));
        var user = target.getUserByUserEmail("xyz@gmail.com");
        assertNull(user);
    }
}