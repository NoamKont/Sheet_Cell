package body.permission;


import javafx.scene.control.Label;

import java.security.Permission;
import java.util.HashMap;
import java.util.Map;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class PermissionManager {

    // Holds the permissions of the users "User:Permission"

    private String sheetName;
    private final Map<String, PermissionInfo> permissions;

    public PermissionManager() {
        permissions = new HashMap<>();
    }

    public synchronized void addPermission(String user,PermissionInfo.Permissions permission, PermissionInfo.Status status){
        if(permissions.containsKey(user)){
            permissions.replace(user,new PermissionInfo(user, permission, status, sheetName));
        }else {
            PermissionInfo permissionInfo = new PermissionInfo(user, permission, status, sheetName);
            permissions.put(user, permissionInfo);
        }
    }

    public synchronized void removePermission(String user){
        permissions.remove(user);

    }

    public synchronized Map<String, PermissionInfo> getPermissions() {
        return permissions;
    }

    public boolean isPermissionExists(String user) {
        return permissions.containsKey(user);

    }

    public PermissionInfo getPermissionInfo(String user){
        return permissions.get(user);
    }

    public String getUserPermissionStatus(String user){
        return permissions.get(user).getPermissionStatus().toString();
    }

    public String getUserPermissionType(String user){
        return permissions.get(user).getPermissionType().toString();
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}
