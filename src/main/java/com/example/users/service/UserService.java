package com.example.users.service;

import com.example.users.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService {

    private final Map<Integer, User> users = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1); // Auto ID

    // Add a new user
    public User addUser(User user) {
        int id = idCounter.getAndIncrement();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    // Get all users
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    // Get a user by ID
    public User getUserById(int id) {
        return users.get(id);
    }

    // Delete a user by ID
    public void deleteUser(int id) {
        users.remove(id);
    }
}

