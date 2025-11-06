import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class DataManager implements UserRepository, InternshipRepository, ApplicationRepository, IdGenerator {
    private static final DataManager INSTANCE = new DataManager();
    public static DataManager getInstance() { return INSTANCE; }

    private final Map<String, User> users = new LinkedHashMap<>();
    private final Map<Long, Internship> internships = new LinkedHashMap<>();
    private final Map<Long, InternshipApplication> applications = new LinkedHashMap<>();
    private final AtomicLong idSeq = new AtomicLong(1000);

    private DataManager() {}

    @Override public long nextId() { return idSeq.incrementAndGet(); }

    @Override public Optional<User> findById(String id) { return Optional.ofNullable(users.get(id)); }
    @Override public void save(User user) { users.put(user.getUserID(), user); }

    @Override public Optional<Internship> findById(long id) { return Optional.ofNullable(internships.get(id)); }
    @Override public void save(Internship i) { internships.put(i.getId(), i); }

    @Override public Optional<InternshipApplication> findByIdApp(long id) { return Optional.ofNullable(applications.get(id)); }
    @Override public void save(InternshipApplication app) { applications.put(app.getId(), app); }

    @Override public List<InternshipApplication> findByApplicantId(String studentId) {
        return applications.values().stream().filter(a -> a.getApplicantId().equals(studentId)).toList();
    }
    @Override public List<InternshipApplication> findByInternshipId(long internshipId) {
        return applications.values().stream().filter(a -> a.getInternshipId() == internshipId).toList();
    }
    @Override public boolean existsByApplicantAndInternship(String applicantId, long internshipId) {
        return applications.values().stream().anyMatch(a -> a.getApplicantId().equals(applicantId) && a.getInternshipId()==internshipId);
    }

    public List<Internship> allInternships() { return new ArrayList<>(internships.values()); }
}
