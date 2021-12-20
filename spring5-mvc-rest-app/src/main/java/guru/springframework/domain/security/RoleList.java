package guru.springframework.domain.security;

import java.util.HashSet;
import java.util.Set;

import static guru.springframework.domain.security.Permission.*;

public enum RoleList {
        ADMIN(new HashSet<String>() {{
            add(READ.permission);
        }}),
        MANAGER(new HashSet<String>() {{
            add(READ.permission); add(UPDATE.permission); add(WRITE.permission);
        }}),
        USER(new HashSet<String>() {{
            add(READ.permission); add(UPDATE.permission); add(WRITE.permission); add(DELETE.permission);
        }});
        private Set<String> permissions;

        RoleList (Set<String> permissions) {
            this.permissions = permissions;
        }

        public Set<String> getPermissions () {
            return this.permissions;
        }
}
