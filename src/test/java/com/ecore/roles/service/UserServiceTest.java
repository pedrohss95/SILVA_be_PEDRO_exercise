package com.ecore.roles.service;

import com.ecore.roles.client.UserClient;
import com.ecore.roles.client.model.User;
import com.ecore.roles.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.ecore.roles.utils.TestData.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserClient userClient;

    @Test
    void shouldGetUserWhenUserIdExists() {
        User gianniUser = GIANNI_USER();
        when(userClient.getUser(UUID_1))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(gianniUser));

        assertNotNull(userService.getUserById(UUID_1));
    }

    @Test
    void shouldNotGetUserWhenTeamIdDoesNotExist() {
        when(userClient.getUser(UUID_1))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null));
        assertNull(userService.getUserById(UUID_1));
    }

    @Test
    void shouldGetAllUsers() {
        List<User> userList = List.of(GIANNI_USER());
        when(userClient.getAllUsers())
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(userList));
        assertNotNull(userService.getAllUsers());
    }

    @Test
    void shouldNotGetAllUsers() {
        when(userClient.getAllUsers())
                .thenReturn(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null));
        assertNull(userService.getAllUsers());
    }
}
