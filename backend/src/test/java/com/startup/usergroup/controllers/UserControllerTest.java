package com.startup.usergroup.controllers;

import com.startup.usergroup.models.User;
import com.startup.usergroup.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    UserController target;
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
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

    @Test
    void createUser_rejectsMissingOrBlankPasswordBeforePersistence() throws Exception {
        String[] invalidBodies = {
                "{\"name\":\"xyz\",\"email\":\"xyz@gmail.com\"}",
                "{\"name\":\"xyz\",\"email\":\"xyz@gmail.com\",\"password\":null}",
                "{\"name\":\"xyz\",\"email\":\"xyz@gmail.com\",\"password\":\"\"}",
                "{\"name\":\"xyz\",\"email\":\"xyz@gmail.com\",\"password\":\"   \"}"
        };

        for (String body : invalidBodies) {
            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        verifyNoInteractions(userRepository);
    }

    @Test
    void createUser_rejectsInvalidNameAndEmail() throws Exception {
        String[] invalidBodies = {
                "{\"name\":\" \",\"email\":\"xyz@gmail.com\",\"password\":\"password\"}",
                "{\"name\":\"xyz\",\"email\":\"not-an-email\",\"password\":\"password\"}"
        };

        for (String body : invalidBodies) {
            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        verifyNoInteractions(userRepository);
    }

    @Test
    void createUser_hashesNonblankPasswordBeforeSaving() throws Exception {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"xyz\",\"email\":\"xyz@gmail.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk());

        var savedUser = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(savedUser.capture());
        assertTrue(BCrypt.checkpw("password", savedUser.getValue().getPassword()));
    }
}