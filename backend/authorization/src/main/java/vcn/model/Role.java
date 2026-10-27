package vcn.model;

public enum Role {
    ROLE_USER, ROLE_ADMIN;

    public String deletePrefix() {
        return name().replace("ROLE_", "");
    }
}
