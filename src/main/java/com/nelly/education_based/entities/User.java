package com.nelly.education_based.entities;

public class User {

    public enum Role { ADMIN, INSTRUCTOR, STUDENT }

    private String username;
    private String password;
    private Role role;
    private String displayName;

    public User(String username, String password, Role role, String displayName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.displayName = displayName;
    }

    public String getUsername()    { return username; }
    public String getPassword()    { return password; }
    public Role   getRole()        { return role; }
    public String getDisplayName() { return displayName; }

    @Override
    public String toString() { return displayName + " [" + role + "]"; }
}
