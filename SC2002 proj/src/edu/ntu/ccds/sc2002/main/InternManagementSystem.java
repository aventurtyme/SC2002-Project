package edu.ntu.ccds.sc2002.main;

import edu.ntu.ccds.sc2002.auth.*;
import edu.ntu.ccds.sc2002.io.*;
import edu.ntu.ccds.sc2002.model.*;

import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class InternManagementSystem {

    private final FileHandler fileHandler = new FileHandler();
    private LoginService loginService;
    private UserManagementController userController;

    private List<Student> students = new ArrayList<>();
    private List<CompanyRepresentative> reps = new ArrayList<>();
    private List<CareerCenterStaff> staff = new ArrayList<>();

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        InternManagementSystem sys = new InternManagementSystem();
        sys.init();
        sys.startCli();
    }

    public void init() {
        System.out.println("[System] Initializing...");
        try {
            Path base = Paths.get("data");
            students = fileHandler.loadStudents(base.resolve("students.csv"));
            reps = fileHandler.loadCompanyReps(base.resolve("companyreps.csv"));
            staff = fileHandler.loadCareerCenterStaff(base.resolve("companystaff.csv"));
        } catch (Exception e) {
            System.err.println("[Error] Failed to load data: " + e.getMessage());
        }
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(students);
        allUsers.addAll(reps);
        allUsers.addAll(staff);
        userController = new UserManagementController(allUsers);
        loginService = new LoginService(allUsers, Paths.get("data/credentials.csv"));
    }

    public void startCli() {
        System.out.println("=============================");
        System.out.println(" NTU Internship Management CLI");
        System.out.println("=============================");

        while (true) {
            System.out.println("\n1) Login\n2) Change Password\n3) Exit");
            System.out.print("Select: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> handleLogin();
                case "2" -> handleChangePassword();
                case "3" -> {
                    System.out.println("[System] Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void handleLogin() {
        System.out.print("Enter ID/Email: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String pwd = scanner.nextLine().trim();
        if (loginService.login(id, pwd)) {
            loginService.currentUser().ifPresent(this::roleMenu);
        }
    }

    private void handleChangePassword() {
        System.out.print("Enter ID/Email: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Old Password: ");
        String oldPwd = scanner.nextLine().trim();
        System.out.print("Enter New Password: ");
        String newPwd = scanner.nextLine().trim();
        loginService.changePassword(id, oldPwd, newPwd);
    }

    private void roleMenu(User user) {
        switch (user.getRole()) {
            case STUDENT -> studentMenu((Student) user);
            case COMPANY_REPRESENTATIVE -> repMenu((CompanyRepresentative) user);
            case CAREER_CENTER_STAFF -> staffMenu((CareerCenterStaff) user);
        }
        loginService.logout();
    }

    private void studentMenu(Student s) {
        System.out.println("\n--- Student Dashboard ---");
        System.out.println("Welcome, " + s.getName() + " (Year " + s.getYearOfStudy() + ", " + s.getMajor() + ")");
        System.out.println("[Note] Internship viewing/applying handled in ApplicationController (Hannah's module).");
    }

    private void repMenu(CompanyRepresentative rep) {
        System.out.println("\n--- Company Representative Dashboard ---");
        System.out.println("Welcome, " + rep.getName() + " from " + rep.getCompanyName());
        System.out.println("[Note] Internship creation/visibility handled in InternshipController (Shyshy's module).");
    }

    private void staffMenu(CareerCenterStaff ccs) {
        System.out.println("\n--- Career Center Staff Dashboard ---");
        System.out.println("Welcome, " + ccs.getName() + " from " + ccs.getStaffDepartment());
        System.out.println("[Note] Approval/report handled in ReportGenerator (Shyshy module).");
    }
}
