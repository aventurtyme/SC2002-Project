package models;

import enums.*;
/**
 * Represents a student's request to withdraw a submitted application, with status and timestamps.
 * <p>This class is documented for API generation via the Javadoc tool.</p>
 */
public class WithdrawalRequest {
    private static int counter = 1;
    private String id;
    private Application application;
    private RequestStatus status = RequestStatus.PENDING;

    public WithdrawalRequest(Application application) {
        this.id = String.format("WR%04d", counter++);
        this.application = application;
    }

    public String getId() { 
        return id; 
    }
    public Application getApplication() { 
        return application; 
    }
    public RequestStatus getStatus() { 
        return status; 
    }
    public void setStatus(RequestStatus s) { 
        this.status = s; 
    }

    @Override
    public String toString() {
        return String.format("ApplicationID: %s | Student: %s | Internship: %s | Status: %s",
                application.getId(),
                application.getStudent().getName(),
                application.getOpportunity().getTitle(),
                status);
    }
}