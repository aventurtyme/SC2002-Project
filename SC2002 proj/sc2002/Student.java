package sce.sc2002.alif.project;

import java.util.ArrayList;
import java.util.List;

public class Student extends User{
	//attributes
	private int yearOfStudy;
	private String major;
	private List<Application> applications = new ArrayList<>();
	private Application acceptedApplication = null;
	
	//constructor
	public Student(String userID, String name, int yearOfStudy, String major, String password) {
		super(userID, name, password);
		this.yearOfStudy = yearOfStudy;
		this.major = major;
	}
	
	//accessor
	public int getYearOfStudy() {
		return yearOfStudy;
	}
	public String getMajor() {
		return major;
	}
	public List<Application> getApplication(){
		return applications;
	}
	public Application getAcceptedApplication() {
		return acceptedApplication;
	}
	
	//method
	public boolean canApply() {
		if (acceptedApplication != null) return false;
		long active = applications.stream().filter(a -> a.getStatus()==ApplicationStatus.PENDING || a.getStatus()==ApplicationStatus.SUCCESSFUL).count();
		return active < 3;
	}
	
	protected void addApplication(Application app) {
        applications.add(app);
    }

    protected void removeApplication(Application app) {
        applications.remove(app);
    }

    protected void setAcceptedApplication(Application app) {
        this.acceptedApplication = app;
    } 
}
