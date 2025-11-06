import java.util.List;

public class ApplicationController {
    private final InternshipRepository iRepo;
    private final ApplicationRepository aRepo;
    private final IdGenerator ids;

    public ApplicationController() {
        DataManager dm = DataManager.getInstance();
        this.iRepo = dm; this.aRepo = dm; this.ids = dm;
    }

    public String apply(Student s, Internship i) {
        if (!i.isVisible() || i.getStatus()!=InternshipStatus.APPROVED) return "Not open for application.";
        if (s.getYearOfStudy() <= 2 && i.getLevel()!=InternshipLevel.BASIC) return "Y1–2 can apply Basic only.";
        if (!i.getPreferredMajor().equalsIgnoreCase(s.getMajor())) return "Major not eligible.";
        if (aRepo.existsByApplicantAndInternship(s.getUserID(), i.getId())) return "Already applied.";
        long active = aRepo.findByApplicantId(s.getUserID()).stream()
                .filter(a -> a.getStatus()==ApplicationStatus.PENDING || a.getStatus()==ApplicationStatus.SUCCESSFUL)
                .count();
        if (active >= 3) return "You can only hold up to 3 active applications.";

        InternshipApplication app = new InternshipApplication(ids.nextId(), s.getUserID(), i.getId());
        aRepo.save(app);
        s.addApplication(app);
        return "Application submitted (Pending).";
    }
}
