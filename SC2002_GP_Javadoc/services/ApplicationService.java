package services;

import enums.*;
import java.util.ArrayList;
import java.util.List;
import models.*;

/**
 * Business operations for application lifecycle (submit, confirm, list, etc.).
 * <p>This class is documented for API generation via the Javadoc tool.</p>
 */
public class ApplicationService {
    private List<Application> applications = new ArrayList<>();
/**
 * method operation.
 *
 * @return result of the operation
 * @throws IllegalArgumentException if invalid arguments
 */

    public void addApplication(Application a) {
        applications.add(a);
        a.getStudent().getApplications().add(a);
        a.getOpportunity().getApplicants().add(a);
    }

    public List<Application> getAll() { 
        return applications; 
    }
    public List<Application> getByStudent(Student s) {
        List<Application> out = new ArrayList<>();
        for (Application a : applications) if (a.getStudent().equals(s)) out.add(a);
        return out;
    }

    public List<Application> getByOpportunity(InternshipOpportunity opp) {
        List<Application> out = new ArrayList<>();
        for (Application a : applications) if (a.getOpportunity().equals(opp)) out.add(a);
        return out;
    }

    // Accept/confirm a placement (usually by company rep). This makes the Application SUCCESSFUL,
    // sets the student's acceptedApplication, withdraws other active applications for the student,
    // updates opportunity fill status.
    public void confirmPlacement(Application acceptedApp) {
        if (acceptedApp == null) return;
        Student s = acceptedApp.getStudent();
        InternshipOpportunity opp = acceptedApp.getOpportunity();

        // set as successful and mark accepted by student false for now (student may confirm)
        acceptedApp.setStatus(ApplicationStatus.SUCCESSFUL);
        acceptedApp.setAcceptedByStudent(true); // mark accepted (company confirmed; treat as accepted)
        s.setAcceptedApplication(acceptedApp);

        // Withdraw all other active applications of the student
        for (Application a : new ArrayList<>(applications)) {
            if (a.getStudent().equals(s) && !a.getId().equals(acceptedApp.getId())) {
                if (a.getStatus() == ApplicationStatus.PENDING || a.getStatus() == ApplicationStatus.SUCCESSFUL) {
                    a.setStatus(ApplicationStatus.WITHDRAWN);
                }
            }
        }

        // Update opportunity fill status and visibility if filled
        opp.evaluateFillStatus();
    }

    // Student withdraws their application (or staff approves a withdrawal)
    public void withdrawApplication(Application app) {
        if (app == null) return;
        app.setStatus(ApplicationStatus.WITHDRAWN);
        // If this was a successful application that freed a slot, update opp status
        InternshipOpportunity opp = app.getOpportunity();
        if (opp.getStatus() == OpportunityStatus.FILLED) {
            // re-evaluate; if slots freed, set back to APPROVED
            if (opp.availableSlots() > 0) {
                opp.setStatus(OpportunityStatus.APPROVED);
                opp.setVisible(true);
            }
        }
    }
}