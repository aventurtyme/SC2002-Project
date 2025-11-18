package controllers;

import enums.*;
import java.time.LocalDate;
import java.util.Optional;
import models.*;
import services.*;

/**
 * Controller layer that orchestrates user flows for CompanyRep.
 * <p>Delegates domain logic to services and coordinates I/O with views.</p>
 */
public class CompanyRepController {
    private InternshipService internshipService;
    private ApplicationService applicationService;
/**
 * method operation.
 *
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */

    public CompanyRepController(InternshipService is, ApplicationService as) { 
        this.internshipService = is; 
        this.applicationService = as;
    }
/**
 * method operation.
 *
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */

    public void createOpportunity(CompanyRepresentative rep, String title, String desc, InternshipLevel level, String preferredMajor, java.time.LocalDate open, java.time.LocalDate close, int slots) {
        // limit creation of internship opportunities up to 5
        if (rep.getInternships().size() >= 5) { 
            System.out.println("You have reached your limit of 5 internships."); 
            return; 
        }
        // create internship opportunity
        InternshipOpportunity opp = new InternshipOpportunity(title, desc, level, preferredMajor, open, close, rep, slots);
        // add to rep's list and global internship service
        rep.getInternships().add(opp);
        internshipService.addOpportunity(opp);
        System.out.println("Internship created (pending approval): " + opp.getId());
    }
    
    // edit only if not approved/filled
/**
 * editOpportunity operation.
 *
 * @param rep parameter
 * @param oppId parameter
 * @param title parameter
 * @param desc parameter
 * @param level parameter
 * @param preferredMajor parameter
 * @param open parameter
 * @param close parameter
 * @param slots parameter
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */
    public void editOpportunity(CompanyRepresentative rep, String oppId, String title, String desc, InternshipLevel level, String preferredMajor, LocalDate open, LocalDate close, int slots) {
        InternshipOpportunity opp = internshipService.findById(oppId);
        if (opp == null || !opp.getRepInCharge().equals(rep)) {
            System.out.println("Opportunity not found or not yours.");
            return;
        }
        if (!opp.isEditable()) {
            System.out.println("Cannot edit an approved or filled opportunity.");
            return;
        }
        boolean ok = opp.updateDetails(title, desc, level, preferredMajor, open, close, slots);
        System.out.println(ok ? "Updated opportunity: " + opp.getId() : "Failed to update.");
    }

    // delete only if not approved/filled
/**
 * deleteOpportunity operation.
 *
 * @param rep parameter
 * @param oppId parameter
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */
    public void deleteOpportunity(CompanyRepresentative rep, String oppId) {
        InternshipOpportunity opp = internshipService.findById(oppId);
        if (opp == null || !opp.getRepInCharge().equals(rep)) {
            System.out.println("Opportunity not found or not yours.");
            return;
        }
        if (!opp.isEditable()) {
            System.out.println("Cannot delete an approved or filled opportunity.");
            return;
        }
        rep.getInternships().remove(opp);
        internshipService.removeOpportunity(opp);
        System.out.println("Opportunity removed: " + oppId);
    }

    // view applications for an opportunity (always allowed for owner)
/**
 * viewApplications operation.
 *
 * @param rep parameter
 * @param oppId parameter
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */
    public void viewApplications(CompanyRepresentative rep, String oppId) {
        InternshipOpportunity opp = internshipService.findById(oppId);
        if (opp == null || !opp.getRepInCharge().equals(rep)) {
            System.out.println("Opportunity not found or not yours.");
            return;
        }
        if (opp.getApplicants().isEmpty()) {
            System.out.println("No applications for this opportunity.");
            return;
        }
        for (Application a : opp.getApplicants()) {
            System.out.println(a);
        }
    }

    // Approve application
/**
 * approveApplication operation.
 *
 * @param rep parameter
 * @param oppId parameter
 * @param appId parameter
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */
    public void approveApplication(CompanyRepresentative rep, String oppId, String appId) {
        InternshipOpportunity opp = internshipService.findById(oppId);
        if (opp == null || !opp.getRepInCharge().equals(rep)) {
            System.out.println("Opportunity not found or not yours.");
            return;
        }
        Application app = opp.getApplicants().stream()
            .filter(a -> a.getId().equals(appId))
            .findFirst().orElse(null);
        if (app == null) { System.out.println("Application not found."); return; }
        if (app.getStatus() != ApplicationStatus.PENDING) {
            System.out.println("Application already processed."); return;
        }
        app.setStatus(ApplicationStatus.SUCCESSFUL);
        System.out.println("Application approved.");
    }

    // Reject application
/**
 * rejectApplication operation.
 *
 * @param rep parameter
 * @param oppId parameter
 * @param appId parameter
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */
    public void rejectApplication(CompanyRepresentative rep, String oppId, String appId) {
        InternshipOpportunity opp = internshipService.findById(oppId);
        if (opp == null || !opp.getRepInCharge().equals(rep)) {
            System.out.println("Opportunity not found or not yours.");
            return;
        }
        Application app = opp.getApplicants().stream()
            .filter(a -> a.getId().equals(appId))
            .findFirst().orElse(null);
        if (app == null) { System.out.println("Application not found."); return; }
        if (app.getStatus() != ApplicationStatus.PENDING) {
            System.out.println("Application already processed."); return;
        }
        app.setStatus(ApplicationStatus.UNSUCCESSFUL);
        System.out.println("Application rejected.");
    }

    // confirm a placement for an applicant (by app id)
/**
 * confirmPlacement operation.
 *
 * @param rep parameter
 * @param oppId parameter
 * @param appId parameter
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */
    public void confirmPlacement(CompanyRepresentative rep, String oppId, String appId) {
        InternshipOpportunity opp = internshipService.findById(oppId);
        if (opp == null || !opp.getRepInCharge().equals(rep)) {
            System.out.println("Opportunity not found or not yours.");
            return;
        }
        Optional<Application> maybe = opp.getApplicants().stream().filter(a -> a.getId().equals(appId)).findFirst();
        if (!maybe.isPresent()) {
            System.out.println("Application not found.");
            return;
        }
        Application app = maybe.get();
        if (opp.availableSlots() <= 0) {
            System.out.println("No available slots.");
            return;
        }        
        // confirm placement via ApplicationService
        applicationService.confirmPlacement(app);
        System.out.println("Placement confirmed for application " + app.getId() + " student " + app.getStudent().getName());        
        // after confirming, re-evaluate opp status
        opp.evaluateFillStatus();
    }

    // toggle internship visibility
/**
 * toggleVisibility operation.
 *
 * @param rep parameter
 * @param oppId parameter
 * @return result of the operation
 * @throws IllegalArgumentException if arguments are invalid
 */
    public void toggleVisibility(CompanyRepresentative rep, String oppId) {
        InternshipOpportunity opp = internshipService.findById(oppId);
        if (opp == null || !opp.getRepInCharge().equals(rep)) {
            System.out.println("Opportunity not found or not yours.");
            return;
        }
        // Only allow toggle if the opportunity is approved
        if (opp.getStatus() != OpportunityStatus.APPROVED) {
            System.out.println("Visibility can only be changed for approved opportunities.");
            return;
        }
        opp.setVisible(!opp.isVisible());
        System.out.println("Visibility for " + opp.getId() + " set to: " + (opp.isVisible() ? "ON" : "OFF"));
    }
}