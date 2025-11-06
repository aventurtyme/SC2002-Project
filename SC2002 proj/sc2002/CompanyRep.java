package sce.sc2002.alif.project;

import java.util.ArrayList;
import java.util.List;

public class CompanyRep extends User{
	//Attributes
	private String companyName;
	private String department;
	private String position;
	private boolean isApproved = false;
	private List<Internship> internships = new ArrayList<>();
	
	//constructor
	public CompanyRep(String userID, String name, String companyName, String department, String position, String password) {
		super(userID, name, password);
		this.companyName = companyName;
		this.department = department;
		this.position = position;
	}
	
	//accessor
	public String getCompanyName() {
		return companyName;
	}
	public String getDepartment() {
		return department;
	}
	public String getPosition() {
		return position;
	}
	public boolean isApproved() {
		return isApproved;
	}
	
	//modifier
	public void setApproved(boolean approved) {
		this.isApproved = approved;
	}
	protected void addInternship(Internship i) {
		internships.add(i);
	}
	//method

	public boolean hasReachedInternshipLimit() {
		return internships.size() < 5;
	}

	public List<Internship> getInternships() {
		return internships;
	}
}
