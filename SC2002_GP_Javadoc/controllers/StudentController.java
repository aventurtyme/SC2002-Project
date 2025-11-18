package controllers;

import enums.*;
import models.*;
import services.*;

/**
 * Controller layer that orchestrates user flows for Student.
 * <p>Delegates domain logic to services and coordinates I/O with views.</p>
 */
public class StudentController {
    private UserService userService;
    private InternshipService internshipService;
    private ApplicationService applicationService;
    private WithdrawalService withdrawalService;
/**
 * method operation.
 *
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */

    public StudentController(UserService us, InternshipService is, ApplicationService as, WithdrawalService ws) {
        this.userService = us; 
        this.internshipService = is; 
        this.applicationService = as;
        this.withdrawalService = ws;
    }
/**
 * method operation.
 *
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */

    public void apply(Student s, String internshipId) {
        InternshipOpportunity opp = internshipService.findById(internshipId);
        if (opp == null) { 
            System.out.println("Internship not found."); 
            return; 
        }
        if (!opp.isVisible() || opp.getStatus() != OpportunityStatus.APPROVED) { 
            System.out.println("Not open for applications."); 
            return; 
        }
        if (!s.canApplyForLevel(opp.getLevel())) { 
            System.out.println("Your year doesn't allow applying for this level."); 
            return; 
        }
        if (s.hasReachedApplicationLimit()) { 
            System.out.println("You have reached the maximum number of active applications (3)."); 
            return; 
        }
         if (opp.availableSlots() <= 0) {
            System.out.println("No available slots for this opportunity.");
            return;
        }
        // ensure student not already applied
        boolean already = s.getApplications().stream().anyMatch(a -> a.getOpportunity().equals(opp) && (a.getStatus() == ApplicationStatus.PENDING || a.getStatus() == ApplicationStatus.SUCCESSFUL));
        if (already) {
            System.out.println("You have already applied to this opportunity.");
            return;
        }
        Application app = new Application(s, opp); // add to student's list
        applicationService.addApplication(app); // add to internship's list
        System.out.println("Application submitted: " + app.getId());
    }
/**
 * method operation.
 *
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */

    public void acceptPlacement(Student s, String appId) {
        Application chosen = s.getApplications().stream()
            .filter(a -> a.getId().equals(appId) && a.getStatus() == ApplicationStatus.SUCCESSFUL)
            .findFirst().orElse(null);
        if (chosen == null) {
            System.out.println("No successful application found with this ID.");
            return;
        }
        // Only one acceptance allowed
        if (s.hasAcceptedPlacement()) {
            System.out.println("You have already accepted a placement.");
            return;
        }
        chosen.setAcceptedByStudent(true);
        s.setAcceptedApplication(chosen);
        // withdraw all other applications
        for (Application a : s.getApplications()) {
            if (!a.equals(chosen) 
                && (a.getStatus() == ApplicationStatus.PENDING 
                    || a.getStatus() == ApplicationStatus.SUCCESSFUL)) {
                a.setStatus(ApplicationStatus.WITHDRAWN);
            }
        }
        System.out.println("Internship placement accepted successfully!");
    }
/**
 * method operation.
 *
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */

    public void requestWithdrawal(Student s, String appId) {
        Application app = s.getApplications().stream()
            .filter(a -> a.getId().equals(appId))
            .findFirst()
            .orElse(null);

        if (app == null) {
            System.out.println("Application not found.");
            return;
        }

        if (app.getStatus() == ApplicationStatus.WITHDRAWN) {
            System.out.println("This application has already been withdrawn.");
            return;
        }

        if (app.getStatus() == ApplicationStatus.WITHDRAW_REQUESTED) {
            System.out.println("Withdrawal already requested and awaiting staff approval.");
            return;
        }

        if (app.getStatus() == ApplicationStatus.UNSUCCESSFUL) {
            System.out.println("You cannot withdraw an unsuccessful application.");
            return;
        }

        WithdrawalRequest request = new WithdrawalRequest(app);
        withdrawalService.addRequest(request);

        app.setStatus(ApplicationStatus.WITHDRAW_REQUESTED);
        System.out.println("Withdrawal request submitted for staff approval.");
    }
}