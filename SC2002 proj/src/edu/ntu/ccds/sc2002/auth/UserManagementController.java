package edu.ntu.ccds.sc2002.auth;

import edu.ntu.ccds.sc2002.model.*;
import java.util.*;

public class UserManagementController {
    private final Map<String, User> userMap;

    public UserManagementController(Collection<User> users) {
        userMap = new HashMap<>();
        for (User u : users) userMap.put(u.getId(), u);
    }

    public Optional<User> findUser(String idOrEmail) {
        return Optional.ofNullable(userMap.get(idOrEmail));
    }

    public boolean registerCompanyRep(CompanyRepresentative rep) {
        if (userMap.containsKey(rep.getId())) {
            System.out.println("[Error] Representative already exists.");
            return false;
        }
        userMap.put(rep.getId(), rep);
        System.out.println("[Registered] Pending approval by Career Center Staff.");
        return true;
    }

    public boolean updateUser(User updated) {
        if (!userMap.containsKey(updated.getId())) {
            System.out.println("[Error] User not found.");
            return false;
        }
        userMap.put(updated.getId(), updated);
        return true;
    }

    public Collection<User> allUsers() { return userMap.values(); }
}