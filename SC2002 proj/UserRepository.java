import java.util.Optional;

// requires User.java

public interface UserRepository {
    Optional<User> findById(String id);
    void save(User user);
}