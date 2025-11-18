package services;

import enums.RequestStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import models.*;

/**
 * User lookup and profile utilities.
 * <p>This class is documented for API generation via the Javadoc tool.</p>
 */
public class UserService {
    private Map<String, User> users = new HashMap<>();
/**
 * method operation.
 *
 * @return result of the operation
 * @throws IllegalArgumentException if invalid arguments
 */

    public void addUser(User u) { 
        users.put(u.getUserID(), u); 
    }
    public User findById(String id) { 
        return users.get(id); 
    }

    public User authenticate(String id, String password) {
        for (User u : users.values()) {
            if (u.getUserID().equals(id) && u.getPassword().equals(password)) {
                // Only allow company representative if approveds
                if (u instanceof CompanyRepresentative cr) {
                    if (cr.getRegistrationStatus() != RequestStatus.APPROVED) {
                        System.out.println("Account pending approval.");
                        return null;
                    }
                }
                return u;
            }
        }
        return null;
    }

    public Map<String, User> getAllUsers() { 
        return users; 
    }

    public boolean  changePassword(User user) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter current password: ");
        String current = sc.nextLine();
        if (!current.equals(user.getPassword())) {
            System.out.println("Incorrect password.");
            return false;
        }
        System.out.println("Enter new password: ");
        String newPass = sc.nextLine();
        if (newPass.trim().isEmpty()) {
            System.out.println("Password cannot be empty.");
            return false;
        }
        user.setPassword(newPass);
        System.out.println("Password changed successfully. Please log in again.");
        return true;
    }
}