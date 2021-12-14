package guru.springframework.repositories.security;

import guru.springframework.domain.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
