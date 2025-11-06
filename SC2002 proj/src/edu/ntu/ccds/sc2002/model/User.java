package edu.ntu.ccds.sc2002.model;

import java.util.Objects;

public abstract class User {
    protected String id;              // Student: UxxxxxxxX; Rep: email; Staff: staff id
    protected String name;
    protected String password;        // default: "password"
    protected Role role;

    protected User(String id, String name, String password, Role role) {
        this.id = id;
        this.name = name;
        this.password = (password == null || password.isEmpty()) ? "password" : password;
        this.role = role;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }

    @Override public String toString() { return role + "{" + id + ", " + name + "}"; }
    @Override public boolean equals(Object o) { return (o instanceof User u) && Objects.equals(id, u.id); }
    @Override public int hashCode() { return Objects.hash(id); }
}