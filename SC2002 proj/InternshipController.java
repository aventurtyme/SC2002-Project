import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class InternshipController {
    private final InternshipRepository iRepo;
    private final IdGenerator ids;

    public InternshipController() {
        DataManager dm = DataManager.getInstance();
        this.iRepo = dm; this.ids = dm;
    }

    public Internship createInternship(String title, String desc, String company, String preferredMajor,
                                       InternshipLevel level, LocalDate open, LocalDate close,
                                       CompanyRep owner, int slots) {
        if (!owner.isApproved()) return null;
        if (slots < 1 || slots > 10) return null;
        if (close.isBefore(open)) return null;
        if (!owner.hasReachedInternshipLimit()) return null;

        Internship i = new Internship(ids.nextId(), title, desc, company, preferredMajor, level, open, close, owner, slots);
        iRepo.save(i);
        owner.addInternship(i);
        return i;
    }

    public void approveInternship(CareerCenterStaff staff, long internshipId) {
        Internship i = iRepo.findById(internshipId).orElseThrow(() -> new ApplicationException("Internship not found"));
        i.setStatus(InternshipStatus.APPROVED);
        i.setVisible(true);
        iRepo.save(i);
    }

    public void rejectInternship(CareerCenterStaff staff, long internshipId) {
        Internship i = iRepo.findById(internshipId).orElseThrow(() -> new ApplicationException("Internship not found"));
        i.setStatus(InternshipStatus.REJECTED);
        i.setVisible(false);
        iRepo.save(i);
    }

    public void toggleVisibility(CompanyRep owner, long internshipId, boolean visible) {
        Internship i = iRepo.findById(internshipId).orElseThrow(() -> new ApplicationException("Internship not found"));
        if (!i.getOwner().getUserID().equals(owner.getUserID())) throw new ApplicationException("Not owner");
        i.setVisible(visible);
        iRepo.save(i);
    }

    public List<Internship> listVisibleFor(Student s) {
        LocalDate today = LocalDate.now();
        return DataManager.getInstance().allInternships().stream()
                .filter(i -> i.isVisible() && i.getStatus()==InternshipStatus.APPROVED)
                .filter(i -> !today.isBefore(i.getOpenDate()) && !today.isAfter(i.getCloseDate()))
                .filter(i -> s.getYearOfStudy() >= 3 || i.getLevel()==InternshipLevel.BASIC)
                .filter(i -> i.getPreferredMajor().equalsIgnoreCase(s.getMajor()))
                .collect(Collectors.toList());
    }
}
