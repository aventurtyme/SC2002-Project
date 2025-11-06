import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;;

// requires User.java, Student.java, CompanyRep.java

public class ApplicationController {
    private final InternshipRepository internshipRepo;
    private final ApplicationRepository applicationRepo;
    private final UserRepository userRepo;
    private final ApplicationValidator validator;
    private final IdGenerator idGen;

    public ApplicationController(InternshipRepository iRepo, ApplicationRepository aRepo, UserRepository uRepo, IdGenerator idGen) {
        this.internshipRepo = iRepo;
        this.applicationRepo = aRepo;
        this.userRepo = uRepo;
        this.validator = new ApplicationValidator(iRepo, aRepo, uRepo);
        this.idGen = idGen;
    }
    public boolean applyForInternship(String studentId, long internshipId, String resumePath, String coverLetter) {
        Optional<User> ou = userRepo.findById(studentId);
        if (!ou.isPresent() || !(ou.get() instanceof Student)) throw new ApplicationException("Student not found.");
        Student s = (Student) ou.get();

        Internship internship = validator.ensureInternshipExists(internshipId);
        validator.validateCanApply(s, internship, applicationRepo);

        // create application
        long appId = idGen.nextId();
        InternshipApplication app = new InternshipApplication(appId, s.getId(), internship.getId());
        app.setResumePath(resumePath);
        app.setCoverLetter(coverLetter);
        applicationRepo.save(app);

        // update model relationships
        internship.addApplicationId(app.getId());
        internshipRepo.save(internship);
        s.incrementActiveApplications();
        userRepo.save(s);

        return true;
    }
    public boolean withdrawApplication(String studentId, long applicationId) {
        Optional<User> ou = userRepo.findById(studentId);
        if (!ou.isPresent() || !(ou.get() instanceof Student)) throw new ApplicationException("Student not found.");
        Student s = (Student) ou.get();

        Optional<InternshipApplication> oa = applicationRepo.findById(applicationId);
        if (!oa.isPresent()) throw new ApplicationException("Application not found.");
        InternshipApplication app = oa.get();
        if (!app.getApplicatioId().equals(studentId)) throw new ApplicationException("Application mismatch.");

        // if altready withdrawn
        if (app.geStatus() == ApplicationStatus.WITHDRAWN) return false;
        // allow withdraw request, staff approves later
        app.setStatus(ApplicationStatus.WITHDRAW_REQUESTED);
        applicationRepo.save(app);
        return true;
    }
    public void approveApplication(String repId, long applicationId) {
        Optional<User> ur = userRepo.findById(repId);
        if (!ur.isPresent() || !(ur.get() instanceof CompanyRep)) throw new ApplicationException("Rep not found.");
        CompanyRep rep = (CompanyRep) ur.get();
        if (!rep.isApproved()) throw new ApplicationException("Rep not approved.");
        InternshipApplication app = applicationRepo.findById(applicationId).orElseThrow(() -> new ApplicationException("Application not found."));
        Internship internship = internshipRepo.findById(app.getInternshipId()).orElseThrow(() -> new ApplicationException("Internship not found."));

        // ensure rep is assigned to internship
        if (!internship.getAssignedRepIds().contains(repId)) throw new ApplicationException("Rep not in charge of this internship.");

        // update status to SUCCESSFUL
        app.setStatus(ApplicationStatus.SUCCESSFUL);
        applicationRepo.save(app);
    }
    public void rejectApplication(String repId, long applicationId) {
        Optional<User> ur = userRepo.findById(repId);
        if (!ur.isPresent() || !(ur.get() instanceof CompanyRep)) throw new ApplicationException("Rep not found.");
        CompanyRep rep = (CompanyRep) ur.get();
        if (!rep.isApproved()) throw new ApplicationException("Rep not approved.");
        InternshipApplication app = applicationRepo.findById(applicationId).orElseThrow(() -> new ApplicationException("Application not found."));
        Internship internship = internshipRepo.findById(app.getInternshipId()).orElseThrow(() -> new ApplicationException("Internship not found."));

        // ensure rep is assigned to internship
        if (!internship.getAssignedRepIds().contains(repId)) throw new ApplicationException("Rep not in charge of this internship.");

        app.setStatus(ApplicationStatus.UNSUCCESSFUL);
        applicationRepo.save(app);

        userRepo.findById(app.getApplicantId()).ifPresent(u -> {
            if (u instanceof Student) {
                Student s = (Student) u;
                s.decrementActiveApplications();
                userRepo.save(s);
            }
        });
    }
    public boolean acceptInternshipPlacement(String studentId, long applicationId) {
        Optional<User> ou = userRepo.findById(studentId);
        if (!ou.isPresent() || !(ou.get() instanceof Student)) throw new ApplicationException("Student not found.");
        Student s = (Student) ou.get();

        InternshipApplication chosen = applicationRepo.findById(applicationId).orElseThrow(() -> new ApplicationException("Application not found."));
        if (!chosen.getApplicantId().equals(studentId)) throw new ApplicationException("Not the owner.");
        if (chosen.geStatus() != ApplicationStatus.SUCCESSFUL) throw new ApplicationException("Application not SUCCESSFUL.");

        // accept chosen
        chosen.setStatus(ApplicationStatus.ACCEPTED);
        applicationRepo.save(chosen);

        // mark accepted on student
        s.setAcceptedApplicationId(chosen.getId());
        userRepo.save(s);

        // withdraw other applications of this student
        List<InternshipApplication> otherApps = applicationRepo.findByApplicantId(studentId).stream().filter(a -> a.getId() != chosen.getId()).collect(Collectors.toList());

        for (InternshipApplication a : otherApps) {
            if (a.geStatus() != ApplicationStatus.WITHDRAWN) {
                a.setStatus(ApplicationStatus.WITHDRAWN);
                applicationRepo.save(a);
                //update internship relationship and student's active count
                Internship in = internshipRepo.findById(a.getInternshipId()).orElse(null);
                if (in != null) {
                    in.removeApplicationId(a.getId());
                    internshipRepo.save(in);
                }
            }
        }
        
        // reduce student's active application counter
        int remainingActive = (int) applicationRepo.findByApplicantId(studentId).stream().filter(a -> a.getStatus() != ApplicationStatus.WITHDRAWN).count();
        while (s.getActiveApplications() > remainingActive) s.decrementActiveApplications();
        userRepo.save(s);

        // update internship confirmed count
        Internship acceptedIntern = internshipRepo.findById(chosen.getInternshipId()).orElse(null);
        if (acceptedIntern != null) {
            long confirmed = applicationRepo.findByInternshipId(acceptedIntern.getId()).stream().filter(a -> a.getStatus() == ApplicationStatus.ACCEPTED).count();
            if (confirmed >= acceptedIntern.getCapacity()) {
                acceptedIntern.setStatus(InternshipStatus.FILLED);
            }
            internshipRepo.save(acceptedIntern);
        }

        return true;
    }
    public List<InternshipApplication> getStudentApplications(String studentId) {
        return applicationRepo.findByApplicantId(studentId);
    }
    public List<InternshipApplication> getInternshipApplications(long internshipId) {
        return applicationRepo.findByInternshipId(internshipId);
    }
}
