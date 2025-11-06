package edu.ntu.ccds.sc2002.model;

public class CareerCenterStaff extends User {
    private String staffDepartment;

    public CareerCenterStaff(String staffId, String name, String staffDepartment, String password) {
        super(staffId, name, password, Role.CAREER_CENTER_STAFF);
        this.staffDepartment = staffDepartment;
    }

    public String getStaffDepartment() { return staffDepartment; }
    public void setStaffDepartment(String staffDepartment) { this.staffDepartment = staffDepartment; }
}