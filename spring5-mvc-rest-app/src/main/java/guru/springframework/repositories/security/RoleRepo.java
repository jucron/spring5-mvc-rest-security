package guru.springframework.repositories.security;

import guru.springframework.domain.security.Permission;
import guru.springframework.domain.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
        Role findByName(String roleName);
}
