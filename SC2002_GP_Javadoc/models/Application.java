package models;

import enums.*;
import java.time.LocalDate;

/**
 * Represents a student's internship application linking a student to an internship opportunity.
 * <p>This class is documented for API generation via the Javadoc tool.</p>
 */
public class Application {
    private static int counter = 1;
    private String id;
    private Student student;
    private InternshipOpportunity opportunity;
    private ApplicationStatus status = ApplicationStatus.PENDING;
    private boolean acceptedByStudent = false;
    private LocalDate appliedDate = LocalDate.now();
    private boolean withdrawalRequested = false;

    public Application(Student student, InternshipOpportunity opp) {
        this.id = String.format("APP%04d", counter++);
        this.student = student;
        this.opportunity = opp;
    }

    public String getId() { 
        return id; 
    }
    public Student getStudent() { 
        return student; 
    }
    public InternshipOpportunity getOpportunity() { 
        return opportunity; 
    }
    public ApplicationStatus getStatus() { 
        return status; 
    }
    public void setStatus(ApplicationStatus s) { 
        this.status = s; 
    }
    public boolean isAccepted() { 
        return acceptedByStudent; 
    }
    public void setAcceptedByStudent(boolean v) { 
        this.acceptedByStudent = v; 
    }
    public boolean isWithdrawalRequested() { 
        return withdrawalRequested; 
    }
    public void requestWithdrawal() { 
        this.withdrawalRequested = true; 
    }

    @Override
    public String toString() {
        return String.format("%s - %s applied to %s -> %s", id, student.getName(), opportunity.getTitle(), status);
    }
}