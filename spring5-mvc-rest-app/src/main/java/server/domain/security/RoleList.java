package server.domain.security;

import java.util.HashSet;
import java.util.Set;

import static server.domain.security.Permission.*;

public enum RoleList {
        USER(new HashSet<String>() {{
            add(READ.permission);
        }}),
        MANAGER(new HashSet<String>() {{
            add(READ.permission); add(UPDATE.permission); add(WRITE.permission);
        }}),
        ADMIN(new HashSet<String>() {{
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
