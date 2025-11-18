package models;

import java.util.Objects;

public abstract class User {
    protected String userID;
    protected String name;
    protected String password;
    protected String email;

    public User(String userID, String name, String email) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = "password"; // default
    }

    public String getUserID() { 
        return userID; 
    }
    public String getName() { 
        return name; 
    }
    public String getEmail() { 
        return email; 
    }
    public String getPassword() { 
        return password; 
    }
    public void setPassword(String newPass) { 
        this.password = newPass; 
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, userID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(userID, user.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID);
    }
}