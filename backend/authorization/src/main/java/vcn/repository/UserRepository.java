package vcn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vcn.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);
}
