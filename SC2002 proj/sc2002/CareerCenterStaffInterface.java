package sce.sc2002.alif.project;

import java.util.List;
import java.util.Scanner;

public class CareerCenterStaffInterface {
	//attributes
	private CareerCenterStaff staff;
    private final DataManager dm = DataManager.getInstance();
    private final InternshipController internshipController = new InternshipController();
    private final ApplicationController applicationController = new ApplicationController();
    private final ReportGenerator reportController = new ReportGenerator();
    private final Scanner sc = new Scanner(System.in);
	    //constructor
	public CareerCenterStaffInterface(CareerCenterStaff staff) {
		this.staff = staff;
	}
	
	public void run() {
        while (true) {
            System.out.println("\n--- Career Center Menu ("+staff.getName()+") ---");
            System.out.println("1. Approve company registration");
            System.out.println("2. Approve/reject internship");
            System.out.println("3. View withdrawal requests");
            System.out.println("4. Process withdrawal request");
            System.out.println("5. Generate report by status");
            System.out.println("0. Logout");
            System.out.print("choice> ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": approveCompany(); break;
                case "2": approveInternship(); break;
                case "3": viewWithdrawalRequests(); break;
                case "4": processWithdrawal(); break;
                case "5": genReport(); break;
                case "0": return;
                default: System.out.println("Invalid");
            }
        }
    }

    private void approveCompany() {
        List<CompanyRep> pending = dm.getReps().stream().filter(r->!r.isApproved()).toList();
        if (pending.isEmpty()) { System.out.println("No pending registrations."); return; }
        for (int i=0;i<pending.size();i++) {
            System.out.printf("[%d] %s %s (company:%s)\n", i+1, pending.get(i).getUserID(), pending.get(i).getName(), pending.get(i).getCompanyName());
        }
        System.out.print("Enter index to approve (0 to cancel): ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim())-1;
            if (idx < 0) return;
            CompanyRep rep = pending.get(idx);
            rep.setApproved(true);
            System.out.println("Approved " + rep.getUserID());
        } catch (Exception ex) { System.out.println("Invalid."); }
    }

    private void approveInternship() {
        List<Internship> pendings = dm.getInternships().stream().filter(i->i.getInternshipStatus()==InternshipStatus.PENDING).toList();
        if (pendings.isEmpty()) { System.out.println("No pending internships."); return; }
        for (int i=0;i<pendings.size();i++) {
            Internship in = pendings.get(i);
            System.out.printf("[%d] %s by %s\n", i+1, in.getTitle(), in.getCompanyName());
        }
        System.out.print("Enter index to process: ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim())-1;
            Internship in = pendings.get(idx);
            System.out.print("Approve? (y/n): ");
            boolean approve = sc.nextLine().trim().equalsIgnoreCase("y");
            internshipController.approveInternship(staff, in, approve);
            System.out.println("Done.");
        } catch (Exception ex) { System.out.println("Invalid."); }
    }

    private void viewWithdrawalRequests() {
        List<Application> list = applicationController.listWithdrawalRequests();
        if (list.isEmpty()) { System.out.println("No withdrawal requests."); return; }
        for (int i=0;i<list.size();i++) {
            Application a = list.get(i);
            System.out.printf("[%d] Student:%s Internship:%s\n", i+1, a.getStudent().getUserID(), a.getInternship().getTitle());
        }
    }

    private void processWithdrawal() {
        List<Application> list = applicationController.listWithdrawalRequests();
        if (list.isEmpty()) { System.out.println("No requests."); return; }
        viewWithdrawalRequests();
        System.out.print("Enter index to process: ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim())-1;
            Application a = list.get(idx);
            System.out.print("Approve withdrawal? (y/n): ");
            boolean approve = sc.nextLine().trim().equalsIgnoreCase("y");
            applicationController.processWithdrawal(staff, a, approve);
            System.out.println("Processed.");
        } catch (Exception ex) { System.out.println("Invalid."); }
    }

    private void genReport() {
        System.out.print("Status to filter (PENDING/APPROVED/REJECTED/FILLED): ");
        String s = sc.nextLine().trim().toUpperCase();
        InternshipStatus status = InternshipStatus.valueOf(s);
        var list = reportController.filterByStatus(status);
        if (list.isEmpty()) { System.out.println("No internships with that status."); return; }
        list.forEach(System.out::println);
    }
}
