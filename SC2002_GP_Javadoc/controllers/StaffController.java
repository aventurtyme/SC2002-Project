package controllers;

import enums.*;
import models.*;
import services.*;

/**
 * Controller layer that orchestrates user flows for Staff.
 * <p>Delegates domain logic to services and coordinates I/O with views.</p>
 */
public class StaffController {
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

    public StaffController(UserService us, InternshipService is, ApplicationService as, WithdrawalService ws) { 
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

    public void approveCompanyRep(CompanyRepresentative rep) {
        rep.setRegistrationStatus(RequestStatus.APPROVED);
        System.out.println("Approved rep: " + rep.getName());
    }
/**
 * method operation.
 *
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */

    public void approveInternship(InternshipOpportunity opp) {
        opp.setStatus(OpportunityStatus.APPROVED);
        opp.setVisible(true);
        System.out.println("Approved internship: " + opp.getId());
    }
/**
 * method operation.
 *
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */

    public void rejectInternship(InternshipOpportunity opp) {
        opp.setStatus(OpportunityStatus.REJECTED);
        opp.setVisible(false);
        System.out.println("Rejected internship: " + opp.getId());
    }

    // Process withdrawal requests (approve or reject)
/**
 * processWithdrawal operation.
 *
 * @param req parameter
 * @param approve parameter
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */
    public void processWithdrawal(WithdrawalRequest req, boolean approve) {
        if (req == null) return;
        if (approve) {
            req.getApplication().setStatus(ApplicationStatus.WITHDRAWN);
            req.setStatus(RequestStatus.APPROVED);
            System.out.println("Withdrawal approved: " + req.getApplication().getId());
        } else {
            req.getApplication().setStatus(ApplicationStatus.PENDING);
            req.setStatus(RequestStatus.REJECTED);
            System.out.println("Withdrawal rejected: " + req.getApplication().getId());
        }
    }
}