package edu.ntu.ccds.sc2002.model;

import java.time.LocalDate;

public class Internship {
    private String id;                // internal id
    private String title;
    private String description;
    private InternshipLevel level;
    private String preferredMajor;    // assume 1 preferred major
    private LocalDate openingDate;
    private LocalDate closingDate;
    private InternshipStatus status;  // Pending/Approved/Rejected/Filled
    private String companyName;
    private String representativeEmail; // auto-assigned
    private int slots;                // max 10
    private boolean visible;          // toggle by company rep

    public Internship(String id, String title, String description, InternshipLevel level, String preferredMajor,
                      LocalDate openingDate, LocalDate closingDate, InternshipStatus status,
                      String companyName, String representativeEmail, int slots, boolean visible) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.status = status;
        this.companyName = companyName;
        this.representativeEmail = representativeEmail;
        this.slots = slots;
        this.visible = visible;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public InternshipLevel getLevel() { return level; }
    public String getPreferredMajor() { return preferredMajor; }
    public LocalDate getOpeningDate() { return openingDate; }
    public LocalDate getClosingDate() { return closingDate; }
    public InternshipStatus getStatus() { return status; }
    public String getCompanyName() { return companyName; }
    public String getRepresentativeEmail() { return representativeEmail; }
    public int getSlots() { return slots; }
    public boolean isVisible() { return visible; }

    public void setStatus(InternshipStatus status) { this.status = status; }
    public void setVisible(boolean visible) { this.visible = visible; }
}