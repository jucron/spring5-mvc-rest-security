package guru.springframework.services.security;

import guru.springframework.domain.security.Level;
import guru.springframework.domain.security.Role;
import guru.springframework.domain.security.User;
import guru.springframework.model.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO saveUser(UserDTO userDTO);
    Role saveRole(Role role);
    void addRoleToUser(String username, Level level);
    UserDTO getUser (String username);
    List<UserDTO> getUsers();

    User getTrueUser(String username);
}
