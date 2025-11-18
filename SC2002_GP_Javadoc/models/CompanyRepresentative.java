package models;

import enums.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a company-side user who can create and manage internship opportunities.
 * <p>This class is documented for API generation via the Javadoc tool.</p>
 */
public class CompanyRepresentative extends User {
    private RequestStatus registrationStatus = RequestStatus.PENDING;
    private String companyName;
    private String department;
    private String position;
    private String email;
    private List<InternshipOpportunity> internships = new ArrayList<>();

    public CompanyRepresentative(String userID, String name, String password, String companyName, String department, String position, String email) {
        super(userID, name, password);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.email = email;
        this.registrationStatus = RequestStatus.PENDING;
    }

    public RequestStatus getRegistrationStatus() { 
        return registrationStatus; 
    }
    public void setRegistrationStatus(RequestStatus s) { 
        this.registrationStatus = s; 
    }
    public String getCompanyName() { 
        return companyName; 
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getDepartment() { 
        return department; 
    }
    public String getPosition() { 
        return position; 
    }
    public String getEmail() { 
        return email; 
    }
    public List<InternshipOpportunity> getInternships() { 
        return internships; 
    }
    public void addInternship(InternshipOpportunity opp) { 
        internships.add(opp); 
    }
    public void removeInternship(InternshipOpportunity opp) { 
        internships.remove(opp); 
    }
    public boolean isApproved() {
        return registrationStatus == RequestStatus.APPROVED;
    }
    @Override
    public String toString() {
        return String.format("CompanyRep: %s | Company: %s | Status: %s", getName(), companyName, registrationStatus);
    }
}