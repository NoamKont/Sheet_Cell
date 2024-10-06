package body.permission;

import java.util.Objects;

public class PermissionInfo {

    public enum Permissions {
        NO_PERMISSION{
            public String toString() {
                return "NONE";
            }
        },
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
    private String sheetName;


    public PermissionInfo(String username, Permissions permissionType, Status permissionStatus, String sheetName) {
        this.username = username;
        this.permissionType = permissionType;
        this.permissionStatus = permissionStatus;
        this.sheetName = sheetName;
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

    public String getSheetName() {
        return sheetName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PermissionInfo that = (PermissionInfo) obj;
        return username.equals(that.username) && permissionType == that.permissionType && permissionStatus == that.permissionStatus;
    }
    @Override
    public int hashCode() {
        return Objects.hash(username, permissionType, permissionStatus);
    }
}
