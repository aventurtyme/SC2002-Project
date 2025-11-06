package sce.sc2002.alif.project;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class CompanyRepInterface {
	//attributes
	private CompanyRep rep;
    private final InternshipController internshipController = new InternshipController();
    private final ApplicationController applicationController = new ApplicationController();
    private final AuthController authController = new AuthController();
    private final Scanner sc = new Scanner(System.in);
    
    public CompanyRepInterface(CompanyRep rep) {
        this.rep = rep;
    }
    
    public void run() {
        while (true) {
            System.out.println("\n--- CompanyRep Menu ("+rep.getName()+") approved:" + rep.isApproved() + " ---");
            System.out.println("1. Create internship");
            System.out.println("2. View my internships");
            System.out.println("3. View applications for an internship");
            System.out.println("4. Approve application");
            System.out.println("5. Reject application");
            System.out.println("6. Toggle internship visibility");
            System.out.println("7. Change password");
            System.out.println("0. Logout");
            System.out.print("choice> ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": createInternship(); break;
                case "2": viewMyInternships(); break;
                case "3": viewApplications(); break;
                case "4": changeAppStatus(true); break;
                case "5": changeAppStatus(false); break;
                case "6": toggleVisibility(); break;
                case "7": changePassword(); break;
                case "0": return;
                default: System.out.println("Invalid");
            }
        }
    }

    private void createInternship() {
        if (!rep.hasReachedInternshipLimit()) { System.out.println("Reached max internships (5)."); return; }
        System.out.print("Title: "); String title = sc.nextLine().trim();
        System.out.print("Description: "); String desc = sc.nextLine().trim();
        System.out.print("Level (BASIC/INTERMEDIATE/ADVANCED): "); InternshipLevel level = InternshipLevel.valueOf(sc.nextLine().trim().toUpperCase());
        System.out.print("Company Name: "); String name = sc.nextLine().trim();
        System.out.print("Preferred major: "); String major = sc.nextLine().trim();
        System.out.print("Opening date (YYYY-MM-DD): "); LocalDate open = LocalDate.parse(sc.nextLine().trim());
        System.out.print("Closing date (YYYY-MM-DD): "); LocalDate close = LocalDate.parse(sc.nextLine().trim());
        System.out.print("Slots (1-10): "); int slots = Integer.parseInt(sc.nextLine().trim());
        Internship i = internshipController.createInternship(title, desc, name, major, level, open, close, rep, slots);
        if (i == null) System.out.println("Failed to create internship.");
        else System.out.println("Created internship (pending approval): " + i);
    }

    private void viewMyInternships() {
        List<Internship> list = rep.getInternships();
        if (list.isEmpty()) { System.out.println("No internships."); return; }
        list.forEach(System.out::println);
    }

    private void viewApplications() {
        viewMyInternships();
        System.out.print("Enter internship id to see applications: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            Internship i = DataManager.getInstance().findInternshipById(id);
            if (i==null || !i.getCompanyRep().equals(rep)) { System.out.println("Not found or not yours."); return; }
            List<Application> apps = applicationController.listApplicationsForInternship(rep, i);
            if (apps.isEmpty()) { System.out.println("No applications."); return; }
            for (int j=0;j<apps.size();j++) {
                Application a = apps.get(j);
                System.out.printf("[%d] Student: %s status:%s withdrawal:%b\n", j+1, a.getStudent().toString(), a.getStatus(), a.isWithdrawalRequested());
            }
        } catch (Exception ex) { System.out.println("Invalid input."); }
    }

    private void changeAppStatus(boolean approve) {
        viewApplications();
        System.out.print("Enter internship id: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            Internship i = DataManager.getInstance().findInternshipById(id);
            if (i==null || !i.getCompanyRep().equals(rep)) { System.out.println("Not found or not yours."); return; }
            List<Application> apps = i.getApplications();
            if (apps.isEmpty()) { System.out.println("No applications."); return; }
            System.out.print("Enter application index: ");
            int idx = Integer.parseInt(sc.nextLine().trim())-1;
            Application app = apps.get(idx);
            if (approve) applicationController.approveApplication(rep, app);
            else applicationController.rejectApplication(rep, app);
            System.out.println("Done.");
        } catch (Exception ex) { System.out.println("Invalid."); }
    }

    private void toggleVisibility() {
        viewMyInternships();
        System.out.print("Enter internship id to toggle visibility: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            Internship i = DataManager.getInstance().findInternshipById(id);
            boolean ok = internshipController.toggleVisibility(rep, i);
            System.out.println(ok ? "Toggled." : "Failed.");
        } catch (Exception ex) { System.out.println("Invalid."); }
    }

    private void changePassword() {
        System.out.print("New password: ");
        String np = sc.nextLine().trim();
        authController.changePassword(rep, np);
        System.out.println("Password changed.");
    }
}
