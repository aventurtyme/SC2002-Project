package sce.sc2002.alif.project;

import java.util.List;
import java.util.Scanner;

public class StudentInterface {
	private final Student student;
    private final InternshipController internshipController = new InternshipController();
    private final ApplicationController applicationController = new ApplicationController();
    private final AuthController authController = new AuthController();
    private final Scanner sc = new Scanner(System.in);
    
    public StudentInterface(Student s) {
    	this.student = s;
    }
    public void run() {
        while (true) {
            System.out.println("\n--- Student Menu ("+student.getName()+") ---");
            System.out.println("1. View available internships");
            System.out.println("2. View my applications");
            System.out.println("3. Apply for internship");
            System.out.println("4. Request withdrawal");
            System.out.println("5. Accept placement");
            System.out.println("6. Change password");
            System.out.println("0. Logout");
            System.out.print("choice> ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": viewAvailable(); break;
                case "2": viewMyApps(); break;
                case "3": apply(); break;
                case "4": requestWithdrawal(); break;
                case "5": acceptPlacement(); break;
                case "6": changePassword(); break;
                case "0": return;
                default: System.out.println("Invalid");
            }
        }
    }

    private void viewAvailable() {
        List<Internship> list = internshipController.listVisibleForStudent(student);
        if (list.isEmpty()) {
            System.out.println("No internships available for you right now.");
            return;
        }
        list.forEach(i -> System.out.println(i.toString()));
    }

    private void viewMyApps() {
        var apps = student.getApplication();
        if (apps.isEmpty()) { System.out.println("You have no applications."); return; }
        for (int idx=0; idx<apps.size(); idx++) {
            Application a = apps.get(idx);
            System.out.printf("[%d] %s -> %s : %s (withdrawal:%b)\n", idx+1, a.getInternship().toString(), a.getStudent().getName(), a.getStatus(), a.isWithdrawalRequested());
        }
    }

    private void apply() {
        System.out.print("Enter internship ID to apply: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            Internship i = DataManager.getInstance().findInternshipById(id);
            String res = applicationController.apply(student, i);
            System.out.println(res);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id.");
        }
    }

    private void requestWithdrawal() {
        viewMyApps();
        System.out.print("Enter application index to request withdrawal: ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim()) -1;
            if (idx < 0 || idx >= student.getApplication().size()) { System.out.println("Invalid index"); return; }
            Application app = student.getApplication().get(idx);
            String res = applicationController.requestWithdrawal(student, app);
            System.out.println(res);
        } catch (NumberFormatException ex) { System.out.println("Invalid."); }
    }

    private void acceptPlacement() {
        viewMyApps();
        System.out.print("Enter application index to accept (must be SUCCESSFUL): ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim()) -1;
            Application app = student.getApplication().get(idx);
            String res = applicationController.acceptPlacement(student, app);
            System.out.println(res);
        } catch (Exception ex) { System.out.println("Invalid selection."); }
    }

    private void changePassword() {
        System.out.print("New password: ");
        String np = sc.nextLine().trim();
        authController.changePassword(student, np);
        System.out.println("Password changed.");
    }
}
