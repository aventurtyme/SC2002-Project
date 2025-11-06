package edu.ntu.ccds.sc2002.model;

public class Student extends User {
    private int yearOfStudy;     // 1..4
    private String major;        // CSC, EEE, MAE, ...

    public Student(String id, String name, int yearOfStudy, String major, String password) {
        super(id, name, password, Role.STUDENT);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
    }

    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
}