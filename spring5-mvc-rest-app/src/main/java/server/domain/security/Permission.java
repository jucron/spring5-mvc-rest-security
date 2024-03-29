package server.domain.security;


public enum Permission {
    READ("any:read"),
    WRITE("any:write"),
    UPDATE("any:update"),
    DELETE("any:delete");

    public final String permission;

    private Permission(String permission) {
        this.permission = permission;
    }
}
