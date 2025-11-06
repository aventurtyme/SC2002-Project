import java.util.List;
import java.util.Optional;

public interface ApplicationRepository {
    Optional<InternshipApplication> findById(long id);
    void save(InternshipApplication app);
    List<InternshipApplication> findByApplicantId(String studentId);
    List<InternshipApplication> findByInternshipId(long internshipId);
    boolean existsByApplicantAndInternship(String applicantId, long internshipId);
}
