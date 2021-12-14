package guru.springframework.services;

import guru.springframework.domain.security.Level;
import guru.springframework.domain.security.Role;
import guru.springframework.domain.security.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, Level level);
    User getUser (String username);
    List<User> getUsers();
}
