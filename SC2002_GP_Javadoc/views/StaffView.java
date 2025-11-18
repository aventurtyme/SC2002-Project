package views;

import controllers.*;
import enums.*;
import java.util.List;
import java.util.Scanner;
import models.*;
import services.*;
import utils.*;

/**
 * UI helpers dedicated to career center staff actions and flows.
 * <p>Coordinates with controllers/services and prints human-readable output.</p>
 */
public class StaffView {
    private CareerCenterStaff staff;
    private UserService userService;
    private InternshipService internshipService;
    private ApplicationService applicationService;
    private WithdrawalService withdrawalService;
    private Scanner scanner = new Scanner(System.in);
/**
 * method view action.
 *
 * @return result or printed output indicator
 * @throws IllegalArgumentException if arguments are invalid
 */

    public StaffView(CareerCenterStaff s, UserService us, InternshipService is, ApplicationService as, WithdrawalService ws) { 
        this.staff = s; 
        this.userService = us; 
        this.internshipService = is; 
        this.applicationService = as;
        this.withdrawalService = ws;
    }
/**
 * method view action.
 *
 * @return result or printed output indicator
 * @throws IllegalArgumentException if arguments are invalid
 */

    public void show() {
        while (true) {
            System.out.println("\n--- Staff Menu (" + staff.getName() + ") ---");
            System.out.println("1) Approve company reps");
            System.out.println("2) Approve internships");
            System.out.println("3) View report by status");
            System.out.println("4) View internships");
            System.out.println("5) Process withdrawals");
            System.out.println("6) Change password");
            System.out.println("7) Logout");
            System.out.print("Choice: ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1": 
                    approveReps(); 
                    break;
                case "2": 
                    approveInternships(); 
                    break;
                case "3": 
                    ReportService.printOpportunitiesByStatus(internshipService.getAll()); 
                    break;
                case "4": 
                    viewInternships(); 
                    break;
                case "5": 
                    processWithdrawals(); 
                    break;
                case "6":
                    if (userService.changePassword(staff)) {
                        System.out.println("Sign in again.");
                        return;
                    }
                    break;
                case "7": 
                    System.out.println("Logging out..."); 
                    return;
                default: 
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void approveReps() {
        for (User u : userService.getAllUsers().values()) {
            if (u instanceof CompanyRepresentative) {
                CompanyRepresentative rep = (CompanyRepresentative) u;
                if (rep.getRegistrationStatus() == RequestStatus.PENDING) {
                    System.out.println(rep);
                    System.out.print("Approve this rep? (y/n): ");
                    String ans = scanner.nextLine().trim();
                    if (ans.equalsIgnoreCase("y")) new StaffController(userService, internshipService, applicationService, withdrawalService).approveCompanyRep(rep);
                }
            }
        }
    }

    private void approveInternships() {
        for (InternshipOpportunity i : internshipService.getAll()) {
            if (i.getStatus() == OpportunityStatus.PENDING) {
                System.out.println(i);
                System.out.print("Approve this internship? (y/n): ");
                String ans = scanner.nextLine().trim();
                if (ans.equalsIgnoreCase("y")) new StaffController(userService, internshipService, applicationService, withdrawalService).approveInternship(i);
            }
        }
    }

    private void processWithdrawals() {
        for (WithdrawalRequest r : withdrawalService.getAll()) {
            if (r.getStatus() == RequestStatus.PENDING) {
                System.out.println(r);
                System.out.print("Approve? (y/n): ");
                String ans = scanner.nextLine().trim();
                StaffController sc = new StaffController(userService, internshipService, applicationService, withdrawalService);
                if (ans.equalsIgnoreCase("y")) sc.processWithdrawal(r, true);
                else sc.processWithdrawal(r, false);
            }
        }
    }

    private void viewInternships() {
        System.out.println("\n--- Filter Internships ---");

        FilterHelper.FilterCriteria filters = FilterHelper.promptFilters(scanner);

        List<InternshipOpportunity> list = internshipService.getFilteredInternships(
                null,
                filters.status,
                filters.level,
                filters.preferredMajor,
                filters.closingBefore,
                false // staff can see all
        );

        if (list.isEmpty()) {
            System.out.println("No internships match the filters.");
            return;
        }

        for (InternshipOpportunity i : list) System.out.println(i);
    }
}