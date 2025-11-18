package views;

import enums.RequestStatus;
import java.util.Scanner;
import models.*;
import services.*;

/**
 * Console-based menus and user prompts for the internship management system.
 * <p>Coordinates with controllers/services and prints human-readable output.</p>
 */
public class MenuView {
    private Scanner scanner = new Scanner(System.in);
    private UserService userService;
    private InternshipService internshipService;
    private ApplicationService applicationService;
    private WithdrawalService withdrawalService;
/**
 * method view action.
 *
 * @return result or printed output indicator
 * @throws IllegalArgumentException if arguments are invalid
 */

    public MenuView(UserService us, InternshipService is, ApplicationService as, WithdrawalService ws) {
        this.userService = us; this.internshipService = is; this.applicationService = as; this.withdrawalService = ws;
    }
/**
 * method view action.
 *
 * @return result or printed output indicator
 * @throws IllegalArgumentException if arguments are invalid
 */

    public void showMainMenu() {
        while (true) {
            System.out.println("\n=== Internship Management System ===");
            System.out.println("1) Login");
            System.out.println("2) Register as Company Representative");
            System.out.println("3) Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    login();
                    break;
                case "2":
                    registerCompanyRep();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void login() {
        System.out.print("UserID: "); 
        String id = scanner.nextLine().trim();
        System.out.print("Password: "); 
        String pw = scanner.nextLine().trim();
        User u = userService.authenticate(id, pw);
        if (u == null) { 
            System.out.println("Invalid credentials."); 
            return; 
        }
        System.out.println("Welcome " + u.getName());
        if (u instanceof Student) 
            new StudentView((Student) u, internshipService, applicationService, userService, withdrawalService).show();
        else if (u instanceof CompanyRepresentative) 
            new CompanyRepView((CompanyRepresentative) u, internshipService, userService, applicationService).show();
        else if (u instanceof CareerCenterStaff) 
            new StaffView((CareerCenterStaff) u, userService, internshipService, applicationService, withdrawalService).show();
    }

    private void registerCompanyRep() {
        System.out.println("\n--- Company Representative Registration ---");
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Company Name: ");
        String companyName = scanner.nextLine().trim();
        System.out.print("Department: ");
        String department = scanner.nextLine().trim();
        System.out.print("Position: ");
        String position = scanner.nextLine().trim();
        System.out.print("Email (UserID): ");
        String userID = scanner.nextLine().trim();
        String defaultPassword = "password";

        // create CompanyRep object
        CompanyRepresentative cr = new CompanyRepresentative(userID, name, defaultPassword, companyName, department, position, userID);
        cr.setRegistrationStatus(RequestStatus.PENDING);
        userService.addUser(cr);

        System.out.println("Registration successful! Default password: 'password'. Waiting for approval.");
    }
}