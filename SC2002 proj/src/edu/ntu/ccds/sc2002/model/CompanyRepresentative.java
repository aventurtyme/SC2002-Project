package edu.ntu.ccds.sc2002.model;

public class CompanyRepresentative extends User {
    private String companyName;
    private String department;
    private String position;
    private boolean approved; // must be approved by CCS before login

    public CompanyRepresentative(String email, String name, String companyName,
                                 String department, String position, boolean approved, String password) {
        super(email, name, password, Role.COMPANY_REPRESENTATIVE);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.approved = approved;
    }

    public String getCompanyName() { return companyName; }
    public String getDepartment() { return department; }
    public String getPosition() { return position; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
}