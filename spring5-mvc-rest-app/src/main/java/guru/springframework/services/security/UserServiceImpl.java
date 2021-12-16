package guru.springframework.services.security;

import guru.springframework.api.v1.mapper.UserMapper;
import guru.springframework.domain.security.Level;
import guru.springframework.domain.security.Role;
import guru.springframework.domain.security.User;
import guru.springframework.model.UserDTO;
import guru.springframework.repositories.security.RoleRepo;
import guru.springframework.repositories.security.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user==null) {
            log.info("User not found in database");
            throw new UsernameNotFoundException("User not found in database");
        } else {
            log.info("User found in database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles()
                .forEach(role ->
                        authorities.add(new SimpleGrantedAuthority(role.getLevel().toString())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        log.info("Attempting to save new user {} to database", userDTO.getName());
        //Checking username in repo:
        User existingUser = userRepo.findByUsername(userDTO.getUsername());
        if (existingUser!=null) {
            log.info("Username {} already exists in DataBase. User not saved.",userDTO.getUsername());
            return new UserDTO();
        }
        User newUser = userMapper.userDTOToUser(userDTO);
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setRoles(new ArrayList<>());
        userRepo.save(newUser);

        userDTO.setPassword("hidden");
        return userDTO;
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to database", role.getLevel());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, Level level) {
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByLevel(level);
        user.getRoles().add(role);
        userRepo.save(user);
        log.info("Adding role {} to user {}", level, username);
    }

    @Override
    public UserDTO getUser(String username) {
        log.info("Fetching user {}", username);
        UserDTO userDTO = userMapper.userToUserDTO(userRepo.findByUsername(username));
        userDTO.setPassword("hidden");
        return userDTO;
    }
    @Override
    public User getTrueUser(String username) {
        log.info("Fetching user {}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<UserDTO> getUsers() {
        log.info("Fetching all users");
        Iterable<User> usersList = userRepo.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : usersList) {
            UserDTO userDTO = userMapper.userToUserDTO(user);
            userDTO.setPassword("hidden");
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }
}