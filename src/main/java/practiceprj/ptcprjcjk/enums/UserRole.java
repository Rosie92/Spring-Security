package practiceprj.ptcprjcjk.enums;

import java.util.Arrays;

public enum UserRole {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    private String role;

    private UserRole(String role) {
        this.role = role;
    }

    public String getRole() { return role; }

    public static UserRole find() {
        return Arrays.stream(UserRole.values())
                .filter(user -> !user.getRole().contains("ADMIN"))
                .findAny()
                .orElse(ROLE_USER);
    }

    public static UserRole findAdmin() {
        return Arrays.stream(UserRole.values())
                .filter(user -> !user.getRole().contains("USER"))
                .findAny()
                .orElse(ROLE_ADMIN);
    }
}
