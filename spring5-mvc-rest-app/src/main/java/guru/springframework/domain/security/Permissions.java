package guru.springframework.domain.security;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Permissions {
    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER"),
    ADMIN("ROLE_ADMIN");

    private String permission;

}
