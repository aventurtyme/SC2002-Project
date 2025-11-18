package views;

import controllers.*;
import enums.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import models.*;
import services.*;
import utils.*;

/**
 * UI helpers dedicated to company representative actions and flows.
 * <p>Coordinates with controllers/services and prints human-readable output.</p>
 */
public class CompanyRepView {
    private CompanyRepresentative rep;
    private InternshipService internshipService;
    private UserService userService;
    private ApplicationService applicationService;
    private Scanner scanner = new Scanner(System.in);
/**
 * method view action.
 *
 * @return result or printed output indicator
 * @throws IllegalArgumentException if arguments are invalid
 */

    public CompanyRepView(CompanyRepresentative rep, InternshipService is, UserService us, ApplicationService as) { 
        this.rep = rep; 
        this.internshipService = is; 
        this.userService = us;
        this.applicationService = as;
    }
/**
 * method view action.
 *
 * @return result or printed output indicator
 * @throws IllegalArgumentException if arguments are invalid
 */

    public void show() {
        while (true) {
            System.out.println("\n--- Company Rep Menu (" + rep.getName() + ") ---");
            System.out.println("1) Create internship");
            System.out.println("2) View my internships");
            System.out.println("3) Edit internship");
            System.out.println("4) Delete internship");
            System.out.println("5) View applications for an internship");
            System.out.println("6) Approve student application");
            System.out.println("7) Reject student application");
            System.out.println("8) Confirm placement");
            System.out.println("9) Toggle internship visibility");
            System.out.println("10) Change password");
            System.out.println("11) Logout");
            System.out.print("Choice: ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1": 
                    createInternship(); 
                    break;
                case "2": 
                    viewMine(); 
                    break;
                case "3": 
                    editInternship(); 
                    break;
                case "4": 
                    deleteInternship(); 
                    break;
                case "5": 
                    viewApplications(); 
                    break;
                case "6":
                    approveApplication();
                    break;
                case "7":
                    rejectApplication();
                    break;
                case "8": 
                    confirmPlacement(); 
                    break;
                case "9":
                    toggleVisibility();
                    break;
                case "10":
                    if (userService.changePassword(rep)) {
                        System.out.println("Sign in again.");
                        return;
                    }
                    break;
                case "11":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void createInternship() {
        // max 5 internships per company
        if (rep.getInternships().size() >= 5) {
            System.out.println("You have reached the maximum of 5 internship opportunities for your company.");
            return;
        }
        System.out.println("\n--- Create Internship ---");
        System.out.print("Title: "); 
        String title = scanner.nextLine();
        System.out.print("Description: "); 
        String desc = scanner.nextLine();
        System.out.print("Level (BASIC/INTERMEDIATE/ADVANCED): "); 
        String lvl = scanner.nextLine().trim().toUpperCase();
        InternshipLevel level;
        try {
            level = InternshipLevel.valueOf(lvl);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid level. Please enter BASIC, INTERMEDIATE, or ADVANCED.");
            return;
        }
        System.out.print("Preferred Major (or blank): "); 
        String major = scanner.nextLine().trim();
        System.out.print("Opening date (yyyy-MM-dd): "); 
        LocalDate open = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("Closing date (yyyy-MM-dd): "); 
        LocalDate close = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("Slots (1-10): "); 
        int slots = Integer.parseInt(scanner.nextLine().trim());
        if (slots < 1 || slots > 10) {
            System.out.println("Invalid number of slots. Must be between 1 and 10.");
            return;
        }
        // Create internship
        new CompanyRepController(internshipService, applicationService)
                .createOpportunity(rep, title, desc, level, major, open, close, slots);
        System.out.println("Internship opportunity created successfully for company: " + rep.getCompanyName());
    }

    private void viewMine() {
        System.out.println("\n--- View My Internships ---");

        FilterHelper.FilterCriteria filters = FilterHelper.promptFilters(scanner);

        List<InternshipOpportunity> list = internshipService.getFilteredInternships(
                null, // no student filtering
                filters.status,
                filters.level,
                filters.preferredMajor,
                filters.closingBefore,
                false // show all, visibility irrelevant
        ).stream().filter(i -> i.getRepInCharge().equals(rep)).toList();

        if (list.isEmpty()) {
            System.out.println("No internships match the filters.");
            return;
        }

        for (InternshipOpportunity i : list) System.out.println(i);
    }

    private void editInternship() {
        System.out.print("Enter internship ID to edit: ");
        String id = scanner.nextLine().trim();
        InternshipOpportunity opp = internshipService.findById(id);
        if (opp == null || !opp.getRepInCharge().equals(rep)) {
            System.out.println("Invalid internship ID or you are not authorized to edit this internship.");
            return;
        }
        if (!opp.isEditable()) {
            System.out.println("This internship cannot be edited (already approved or filled).");
            return;
        }
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("Level (BASIC/INTERMEDIATE/ADVANCED): ");
        String lvl = scanner.nextLine().trim();
        System.out.print("Preferred Major (or blank): ");
        String major = scanner.nextLine().trim();
        System.out.print("Opening date (yyyy-MM-dd): ");
        LocalDate open = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("Closing date (yyyy-MM-dd): ");
        LocalDate close = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("Slots (1-10): ");
        int slots = Integer.parseInt(scanner.nextLine().trim());
        InternshipLevel level = InternshipLevel.valueOf(lvl.toUpperCase());
        new CompanyRepController(internshipService, applicationService).editOpportunity(rep, id, title, desc, level, major, open, close, slots);
    }

    private void deleteInternship() {
        System.out.print("Enter internship ID to delete: ");
        String id = scanner.nextLine().trim();
        new CompanyRepController(internshipService, applicationService).deleteOpportunity(rep, id);
    }

    private void viewApplications() {
        System.out.print("Enter internship ID to view applications: ");
        String id = scanner.nextLine().trim();
        new CompanyRepController(internshipService, applicationService).viewApplications(rep, id);
    }

    private void approveApplication() {
        System.out.print("Enter internship ID: ");
        String oppId = scanner.nextLine().trim();
        System.out.print("Enter application ID to approve: ");
        String appId = scanner.nextLine().trim();
        new CompanyRepController(internshipService, applicationService)
            .approveApplication(rep, oppId, appId);
    }

    private void rejectApplication() {
        System.out.print("Enter internship ID: ");
        String oppId = scanner.nextLine().trim();
        System.out.print("Enter application ID to reject: ");
        String appId = scanner.nextLine().trim();
        new CompanyRepController(internshipService, applicationService)
            .rejectApplication(rep, oppId, appId);
    }

    private void confirmPlacement() {
        System.out.print("Enter internship ID: ");
        String oppId = scanner.nextLine().trim();
        System.out.print("Enter application ID to confirm: ");
        String appId = scanner.nextLine().trim();
        new CompanyRepController(internshipService, applicationService).confirmPlacement(rep, oppId, appId);
    }

    private void toggleVisibility() {
        System.out.print("Enter internship ID to toggle visibility: ");
        String id = scanner.nextLine().trim();
        new CompanyRepController(internshipService, applicationService).toggleVisibility(rep, id);
    }
}