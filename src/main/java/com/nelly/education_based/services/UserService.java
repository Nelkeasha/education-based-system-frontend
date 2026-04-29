package com.nelly.education_based.services;

import com.nelly.education_based.entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserService {

    private final Map<String, User> users = new HashMap<>();
    private User currentUser;

    public UserService() {
        users.put("admin",      new User("admin",      "admin123", User.Role.ADMIN,      "Administrator"));
        users.put("nelly",      new User("nelly",      "nelly123", User.Role.ADMIN,      "Nelly"));
        users.put("instructor", new User("instructor", "inst123",  User.Role.INSTRUCTOR, "Dr. Smith"));
        users.put("student",    new User("student",    "stud123",  User.Role.STUDENT,    "John Doe"));
    }

    public Optional<User> login(String username, String password) {
        User user = users.get(username.toLowerCase());
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public void logout()              { currentUser = null; }
    public User getCurrentUser()      { return currentUser; }
    public boolean isLoggedIn()       { return currentUser != null; }
}
