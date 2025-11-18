package views;

import controllers.*;
import java.util.List;
import java.util.Scanner;
import models.*;
import services.*;
import utils.*;

/**
 * UI helpers dedicated to student-facing actions and flows.
 * <p>Coordinates with controllers/services and prints human-readable output.</p>
 */
public class StudentView {
    private Student student;
    private InternshipService internshipService;
    private ApplicationService applicationService;
    private UserService userService;
    private WithdrawalService withdrawalService;
    private StudentController studentController;
    private Scanner scanner = new Scanner(System.in);
/**
 * method view action.
 *
 * @return result or printed output indicator
 * @throws IllegalArgumentException if arguments are invalid
 */

    public StudentView(Student s, InternshipService is, ApplicationService as, UserService us, WithdrawalService ws) { 
        this.student = s; 
        this.internshipService = is; 
        this.applicationService = as;
        this.userService = us; 
        this.withdrawalService = ws;
        this.studentController = new StudentController(userService, internshipService, applicationService, withdrawalService);
    }
/**
 * method view action.
 *
 * @return result or printed output indicator
 * @throws IllegalArgumentException if arguments are invalid
 */

    public void show() {
        while (true) {
            System.out.println("\n--- Student Menu (" + student.getName() + ") ---");
            System.out.println("1) View available internships");
            System.out.println("2) My applications");
            System.out.println("3) Accept internship placement");
            System.out.println("4) Request withdrawal");
            System.out.println("5) Change password");
            System.out.println("6) Logout");
            System.out.print("Choice: ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1":
                    viewAvailable();
                    break;
                case "2":
                    viewApplications();
                    break;
                case "3":
                    acceptPlacement();
                    break;
                case "4":
                    requestWithdrawal();
                    break;
                case "5":
                    if (userService.changePassword(student)) {
                        System.out.println("Sign in again.");
                        return;
                    }
                    break;
                case "6":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void viewAvailable() {
        System.out.println("\n--- View Available Internships ---");

        FilterHelper.FilterCriteria filters = FilterHelper.promptFilters(scanner);

        List<InternshipOpportunity> list = internshipService.getFilteredInternships(
                student,
                filters.status,
                filters.level,
                filters.preferredMajor,
                filters.closingBefore,
                true // only visible for students
        );

        if (list.isEmpty()) {
            System.out.println("No internships match the filters.");
            return;
        }

        for (InternshipOpportunity i : list) System.out.println(i);

        System.out.print("Enter internship ID to apply or blank to return: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) return;
        studentController.apply(student, id);
    }

    private void viewApplications() {
        List<Application> apps = student.getApplications();
        if (apps.isEmpty()) {
            System.out.println("No applications yet.");
            return;
        }
        System.out.println("\n--- My Applications ---");
        for (Application a : apps) {
            System.out.println(a);
        }
    }

    private void acceptPlacement() {
        System.out.print("Enter Application ID to accept: ");
        String appId = scanner.nextLine().trim();
        studentController.acceptPlacement(student, appId);
    }

    private void requestWithdrawal() {
        System.out.print("Enter Application ID to request withdrawal: ");
        String appId = scanner.nextLine().trim();
        studentController.requestWithdrawal(student, appId);
    }
}