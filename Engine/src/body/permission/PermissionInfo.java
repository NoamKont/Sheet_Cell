package body.permission;

public class PermissionInfo {

    public enum Permissions {
        READER,
        WRITER,
        OWNER;
        public String toString() {
            return name();
        }
    }
    public enum Status {
        PENDING,
        APPROVED,
        REJECTED;
        public String toString() {
            return name();
        }
    }


    private String username;
    private Permissions permissionType;
    private Status permissionStatus;


    public PermissionInfo(String username, Permissions permissionType, Status permissionStatus) {
        this.username = username;
        this.permissionType = permissionType;
        this.permissionStatus = permissionStatus;
    }

    public String getUsername() {
        return username;
    }

    public Permissions getPermissionType() {
        return permissionType;
    }

    public Status getPermissionStatus() {
        return permissionStatus;
    }
}
