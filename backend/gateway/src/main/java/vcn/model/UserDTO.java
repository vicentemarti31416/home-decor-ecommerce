package vcn.model;

public record UserDTO(String username, String details, String roles) {
    public static final UserDTO ANONYMOUS = new UserDTO("ANONYMOUS", "Usuario anónimo", "NONE");
}
