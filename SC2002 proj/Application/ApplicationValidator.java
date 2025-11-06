import java.time.LocalDate;
import java.util.Optional;

// requires User.java, Student.java

public class ApplicationValidator {
    private final InternshipRepository internshipRepo;
    private final ApplicationRepository applicationRepo;
    private final UserRepository userRepo;

    public ApplicationValidator(InternshipRepository iRepo, ApplicationRepository aRepo, UserRepository uRepo) {
        this.internshipRepo = iRepo; this.applicationRepo = aRepo; this.userRepo = uRepo;
    }

    public void validateStudentExists(String studentId) {
        if (!userRepo.findById(studentId).isPresent()) throw new ApplicationException("Student not found");
        User u = userRepo.findById(studentId).get();
        if (!(u instanceof Student)) throw new ApplicationException("User is not a student");
    }

    public Internship ensureInternshipExists(long internshipId) {
        Optional<Internship> oi = internshipRepo.findById(internshipId);
        if (!oi.isPresent()) throw new ApplicationException("Internship not found");
        return oi.get();
    }

    public void validateCanApply(Student s, Internship internship, ApplicationRepository aRepo) {
        if (s.getAcceptedApplicationId() != null) throw new ApplicationException("Student already accepted an internship");
        if (!s.canApplyMore()) throw new ApplicationException("Student reached max active applications (3)");
        if (internship.getStatus() != InternshipStatus.APPROVED || !internship.isVisible()) throw new ApplicationException("Internship is not open for applications");
        LocalDate today = LocalDate.now();
        if (internship.getOpenDate()!=null && today.isBefore(internship.getOpenDate())) throw new ApplicationException("Application not open yet");
        if (internship.getCloseDate()!=null && today.isAfter(internship.getCloseDate())) throw new ApplicationException("Application closed");
        if (s.getYear() <= 2 && internship.getLevel() != InternshipLevel.BASIC) throw new ApplicationException("Year 1-2 can only apply BASIC internships");
        if (applicationRepo.existsByApplicantAndInternship(s.getId(), internship.getId())) throw new ApplicationException("Duplicate application");
        // capacity check: ensure internship has slots available (count ACCEPTED)
        long confirmedCount = aRepo.findByInternshipId(internship.getId()).stream().filter(a -> a.getStatus() == ApplicationStatus.ACCEPTED).count();
        if (confirmedCount >= internship.getCapacity()) throw new ApplicationException("Internship has no remaining slots");
    }
}
