package edu.ntu.ccds.sc2002.main;

import edu.ntu.ccds.sc2002.auth.*;
import edu.ntu.ccds.sc2002.io.*;
import edu.ntu.ccds.sc2002.model.*;
import edu.ntu.ccds.sc2002.util.InternshipFilter;

import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class InternManagementSystem {

    // --- services & I/O ---
    private final FileHandler fileHandler = new FileHandler();
    private LoginService loginService;
    private UserManagementController userController;

    // --- in-memory data ---
    private List<Student> students = new ArrayList<>();
    private List<CompanyRepresentative> reps = new ArrayList<>();
    private List<CareerCenterStaff> staff = new ArrayList<>();
    private List<Internship> internships = new ArrayList<>();

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        InternManagementSystem sys = new InternManagementSystem();
        sys.init();
        sys.startCli();
    }

    // Load CSV data & bootstrap services
    public void init() {
        System.out.println("[System] Initializing...");
        try {
            Path base = Paths.get("data");
            students     = fileHandler.loadStudents(base.resolve("students.csv"));
            reps         = fileHandler.loadCompanyReps(base.resolve("companyreps.csv"));
            staff        = fileHandler.loadCareerCenterStaff(base.resolve("companystaff.csv"));
            internships  = fileHandler.loadInternships(base.resolve("internships.csv"));
        } catch (Exception e) {
            System.err.println("[Error] Failed to load data: " + e.getMessage());
        }

        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(students);
        allUsers.addAll(reps);
        allUsers.addAll(staff);

        // user controller（供后续团队模块使用）
        userController = new UserManagementController(allUsers);
        // credentials.csv 用于持久化改密
        loginService = new LoginService(allUsers, Paths.get("data/credentials.csv"));

        System.out.printf("[Loaded] students=%d, reps=%d, staff=%d, internships=%d%n",
                students.size(), reps.size(), staff.size(), internships.size());
    }

    // Main CLI loop
    public void startCli() {
        System.out.println("\nNTU Internship Management CLI");
        System.out.println("================================");

        while (true) {
            System.out.println("\n1) Login");
            System.out.println("2) Change Password");
            System.out.println("3) Exit");
            System.out.println("4) View Internships (demo)"); // new: demo uses InternshipFilter
            System.out.print("Select: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> handleLogin();
                case "2" -> handleChangePassword();
                case "3" -> {
                    System.out.println("[System] Exiting...");
                    return;
                }
                case "4" -> demoViewInternships();
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ---- Menu handlers ----

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
        // after a role visit, log out to return to main menu

    }

    private void studentMenu(Student s) {
        System.out.println("\n—— Student Dashboard ——");
        System.out.println("Welcome, " + s.getName() + " (Year " + s.getYearOfStudy() + ", " + s.getMajor() + ")");
        System.out.println("[Note] Internship viewing/applying handled in ApplicationController (Hannah's module).");
    }

    private void repMenu(CompanyRepresentative rep) {
        System.out.println("\n—— Company Representative Dashboard ——");
        System.out.println("Welcome, " + rep.getName() + " from " + rep.getCompanyName());
        System.out.println("[Note] Internship creation/visibility handled in InternshipController (teammate module).");
    }

    private void staffMenu(CareerCenterStaff ccs) {
        System.out.println("\n—— Career Center Staff Dashboard ——");
        System.out.println("Welcome, " + ccs.getName());
        System.out.println("[Note] Approval/report handled in ReportGenerator (teammate module).");
    }

    // ---- Demo: use InternshipFilter without teammate modules ----
    private void demoViewInternships() {
        var viewer = loginService.currentUser().orElse(null); // may be null (not logged in)
        var list = InternshipFilter.filter(
                internships,
                Optional.of("APPROVED"),              // status
                Optional.empty(),                     // preferred major (e.g., Optional.of("CSC"))
                Optional.empty(),                     // level
                Optional.of(LocalDate.now().plusYears(5)), // closing date not passed
                Optional.of(true),                    // only visible
                Optional.ofNullable(viewer)           // eligibility by viewer
        );

        if (list.isEmpty()) {
            System.out.println("[No internships match]");
            return;
        }
        System.out.println("\nID | Title | Major | Level | Close | Visible");
        System.out.println("-------------------------------------------------------------");
        for (Internship i : list) {
            System.out.printf("%s | %s | %s | %s | %s | %s%n",
                    i.getId(),
                    i.getTitle(),
                    i.getPreferredMajor(),
                    i.getLevel(),
                    i.getClosingDate(),
                    i.isVisible());
        }

        // 同时给出一个“按标题排序”的视图
        var titles = list.stream()
                .map(Internship::getTitle)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
        System.out.println("\n[Sorted Titles] " + titles);
    }
}
