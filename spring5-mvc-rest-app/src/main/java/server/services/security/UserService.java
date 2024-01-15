package server.services.security;

import server.api.v1.model.UserDTO;
import server.domain.security.Role;
import server.domain.security.User;

import java.util.List;

public interface UserService {
    UserDTO saveUser(UserDTO userDTO);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    UserDTO getUser (String username);
    List<UserDTO> getUsers();

    User getTrueUser(String username);
}
