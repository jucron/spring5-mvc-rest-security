package guru.springframework.controllers.v1;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.domain.security.Level;
import guru.springframework.domain.security.Role;
import guru.springframework.domain.security.User;
import guru.springframework.services.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(UserController.BASE_URL)
@RequiredArgsConstructor
public class UserController {
    public static final String BASE_URL = "/api/v1/users";
    private final UserService userService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return userService.getUsers();
    }
    @PostMapping("/user/save")
    @ResponseStatus(HttpStatus.CREATED)
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
    @PostMapping("/role/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Role saveUser(@RequestBody Role role) {
        return userService.saveRole(role);
    }
    @PostMapping("/role/addtouser")
    @ResponseStatus(HttpStatus.OK)
    public void addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getLevel());
    }
    @GetMapping ("/token/refresh")
    @ResponseStatus(HttpStatus.OK)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) { //This must be expected and implemented in the frontend
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); //Use the same secret as signing (Authenticating) user.
                //JWTVerifier assert Token and Signatures
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);

                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);

                String access_token = JWT.create()
                        .withSubject(user.getUsername()) //Should choose something unique that identifies (in this app usernames are uniques)
                        .withExpiresAt(new Date(System.currentTimeMillis() +10*60*1000)) // Token expires in 10 minutes
                        .withIssuer(request.getRequestURL().toString()) //Company name or author of this
                        .withClaim("roles", user.getRoles().stream() //Roles of this specific user
                                .map(Role::getLevel).collect(Collectors.toList()))
                        .sign(algorithm); //must sign the Token with the algorithm created


                // Response to the Front-end, giving them headers
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);


            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                // Send a JSON with the error embedded
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
@Data
class RoleToUserForm {
    private String username;
    private Level level;
}
