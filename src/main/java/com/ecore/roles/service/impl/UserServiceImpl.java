package com.ecore.roles.service.impl;

import com.ecore.roles.client.UserClient;
import com.ecore.roles.client.model.User;
import com.ecore.roles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserClient userClient;

    @Autowired
    public UserServiceImpl(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public User getUserById(UUID id) {
        return userClient.getUser(id).getBody();
    }

    @Override
    public List<User> getAllUsers() {
        return userClient.getAllUsers().getBody();
    }

}
