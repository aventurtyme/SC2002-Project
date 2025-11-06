package sce.sc2002.alif.project;

public class CareerCenterStaff extends User{
	//attributes
	private String department;
	
	//constructor
	public CareerCenterStaff(String userID, String name, String department, String password) {
		super(userID, name, password);
		this.department = department;
	}
	
	//accessor
	public String getDepartment() {
		return department;
	}
	
//	//modifier
//	public void approveCompantRep(CompanyRep rep) {
//		return;
//	}
//	public void approveInternshipOpp(Internships i) {
//		return;
//	}
//	public void rejectInternshipOpp(Internships i) {
//		return;
//	}
//	public processWithdrawalReq(WithdrawalReq request) {
//		return;
//	}
//	
//	//display
//	public String generateInternshipReport(ReportFilter filter) {
//		return;
//	}
}
