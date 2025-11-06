package edu.ntu.ccds.sc2002.auth;

import edu.ntu.ccds.sc2002.model.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LoginService implements LoginInterface {
    private final Map<String, User> userMap;        // id/email -> User
    private final Map<String, String> credentials;  // id/email -> password
    private User currentUser;
    private final Path credentialsFile;

    public LoginService(Collection<User> users, Path credentialsFile) {
        this.userMap = new HashMap<>();
        this.credentials = new HashMap<>();
        this.credentialsFile = credentialsFile;
        users.forEach(u -> userMap.put(u.getId(), u));
        loadCredentials();
    }

    @Override
    public boolean login(String idOrEmail, String password) {
        if (!userMap.containsKey(idOrEmail)) {
            System.out.println("[Error] User not found.");
            return false;
        }
        User user = userMap.get(idOrEmail);
        if (user instanceof CompanyRepresentative rep && !rep.isApproved()) {
            System.out.println("[Error] Company Representative not approved yet.");
            return false;
        }
        String stored = credentials.getOrDefault(idOrEmail, user.getPassword());
        if (!Objects.equals(stored, password)) {
            System.out.println("[Error] Incorrect password.");
            return false;
        }
        currentUser = user;
        System.out.println("[Login Successful] Welcome, " + user.getName() + " (" + user.getRole() + ")");
        return true;
    }

    @Override
    public void logout() {
        if (currentUser != null) {
            System.out.println("[Logout] Bye, " + currentUser.getName());
            currentUser = null;
        }
    }

    @Override
    public boolean changePassword(String idOrEmail, String oldPwd, String newPwd) {
        if (!userMap.containsKey(idOrEmail)) {
            System.out.println("[Error] User not found.");
            return false;
        }
        String stored = credentials.getOrDefault(idOrEmail, userMap.get(idOrEmail).getPassword());
        if (!stored.equals(oldPwd)) {
            System.out.println("[Error] Old password incorrect.");
            return false;
        }
        credentials.put(idOrEmail, newPwd);
        userMap.get(idOrEmail).setPassword(newPwd);
        saveCredentials();
        System.out.println("[Password Changed] Please log in again.");
        return true;
    }

    @Override
    public Optional<User> currentUser() { return Optional.ofNullable(currentUser); }

    // ------------------- Helpers -------------------
    private void loadCredentials() {
        if (!Files.exists(credentialsFile)) return;
        try (BufferedReader br = Files.newBufferedReader(credentialsFile)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",", 2);
                if (parts.length == 2) credentials.put(parts[0].trim(), parts[1].trim());
            }
        } catch (IOException e) {
            System.err.println("[Warning] Failed to load credentials: " + e.getMessage());
        }
    }

    private void saveCredentials() {
        try (BufferedWriter bw = Files.newBufferedWriter(credentialsFile)) {
            for (Map.Entry<String, String> e : credentials.entrySet()) {
                bw.write(e.getKey() + "," + e.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("[Warning] Failed to save credentials: " + e.getMessage());
        }
    }
}
