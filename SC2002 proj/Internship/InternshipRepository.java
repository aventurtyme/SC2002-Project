import java.util.Optional;

public interface InternshipRepository {
    Optional<Internship> findById(long id);
    void save(Internship internship); // insert or update
}
