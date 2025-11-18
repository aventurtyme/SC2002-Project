package models;

import enums.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an internship posting with metadata such as level, major, visibility, and closing date.
 * <p>This class is documented for API generation via the Javadoc tool.</p>
 */
public class InternshipOpportunity {
    private static int counter = 1;
    private String id;
    private String title;
    private String description;
    private InternshipLevel level;
    private String preferredMajor;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private OpportunityStatus status = OpportunityStatus.PENDING;
    private String companyName;
    private CompanyRepresentative repInCharge;
    private int slots;
    private boolean visible = false;
    private List<Application> applicants = new ArrayList<>();

    public InternshipOpportunity(String title, String description, InternshipLevel level, String preferredMajor, LocalDate opening, LocalDate closing, CompanyRepresentative rep, int slots) {
        this.id = String.format("INT%04d", counter++);
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openingDate = opening;
        this.closingDate = closing;
        this.repInCharge = rep;
        this.companyName = rep.getCompanyName();
        this.slots = Math.min(slots, 10);
    }

    public String getId() { 
        return id; 
    }
    public String getTitle() { 
        return title; 
    }
    public String getDescription() {
        return description;
    }
    public InternshipLevel getLevel() { 
        return level; 
    }
    public String getPreferredMajor() { 
        return preferredMajor; 
    }
    public LocalDate getOpeningDate() { 
        return openingDate; 
    }
    public LocalDate getClosingDate() { 
        return closingDate; 
    }
    public OpportunityStatus getStatus() { 
        return status; 
    }
    public void setStatus(OpportunityStatus s) { 
        this.status = s; 
    }
    public CompanyRepresentative getRepInCharge() { 
        return repInCharge; 
    }
    public int getSlots() { 
        return slots; 
    }
    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean v) { 
        this.visible = v; 
    }
    public List<Application> getApplicants() { 
        return applicants; 
    }

    public boolean isEditable() {
        return this.status != OpportunityStatus.APPROVED && this.status != OpportunityStatus.FILLED;
    }

    public boolean updateDetails(String title, String description, InternshipLevel level, String preferredMajor, LocalDate opening, LocalDate closing, int slots) {
        if (!isEditable()) return false;
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openingDate = opening;
        this.closingDate =  closing;
        this.slots = Math.min(slots, 10);
        return true;
    }

    public int confirmedSlots() {
        int confirmed = 0;
        for (Application a : applicants) {
            if (a.getStatus() == ApplicationStatus.SUCCESSFUL && a.isAccepted()) {
                confirmed++;
            }
        }
        return confirmed;
    }

    public int availableSlots() {
        int avail = slots - confirmedSlots();
        return Math.max(avail, 0);
    }

    public void evaluateFillStatus() {
        if (availableSlots() <= 0) {
            setStatus(OpportunityStatus.FILLED);
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - %s to %s\nCompany: %s | Rep: %s\nMajor: %s | Slots: %d (Avail: %d)\nStatus: %s | Visible: %b",
            id, title, level, openingDate, closingDate,
            companyName, repInCharge != null ? repInCharge.getName() : "N/A",
            preferredMajor != null ? preferredMajor : "Any",
            slots, availableSlots(), status, visible
            );
    }
}