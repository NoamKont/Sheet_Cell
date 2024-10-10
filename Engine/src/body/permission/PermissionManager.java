package body.permission;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class PermissionManager {

    // Holds the permissions of the users "User:Permission"
    private String sheetName;
    //private final Map<String, PermissionInfo> permissions;
    private final List<PermissionInfo> permissions;
    private final Map<String, PermissionInfo> acceptedPermissions;
    private final Map<String, PermissionInfo> pendingPermissions;
    private final Map<String, PermissionInfo> rejectedPermissions;


    public PermissionManager() {
        //permissions = new HashMap<>();
        permissions = new ArrayList<>();

        acceptedPermissions = new HashMap<>();
        pendingPermissions = new HashMap<>();
        rejectedPermissions = new HashMap<>();
    }

    public synchronized void addPermission(String user,PermissionInfo.Permissions permission, PermissionInfo.Status status){
        PermissionInfo permissionInfo = new PermissionInfo(user, permission, status, sheetName);

        PermissionInfo existingPermissions = pendingPermissions.get(user);
        if(existingPermissions != null){
            permissions.remove(existingPermissions);
            pendingPermissions.remove(user);
        }

        if(status == PermissionInfo.Status.PENDING){
            pendingPermissions.put(user,permissionInfo);
        }
        if(status == PermissionInfo.Status.APPROVED){
            acceptedPermissions.put(user,permissionInfo);
        }
        if(status == PermissionInfo.Status.REJECTED){
            rejectedPermissions.put(user,permissionInfo);
        }
        permissions.add(permissionInfo);


//        if(permissions.containsKey(user)){
//            permissions.replace(user,new PermissionInfo(user, permission, status, sheetName));
//        }else {
//            permissions.put(user, permissionInfo);
//        }
    }

//    public synchronized Map<String, PermissionInfo> getPermissions() {
//        return permissions;
//    }

    public synchronized List<PermissionInfo> getPermissions() {
        return permissions;
    }

    public synchronized Map<String, PermissionInfo> getAcceptedPermissions() {
        return acceptedPermissions;
    }

    public PermissionInfo getPermissionInfo(String user){
        for(PermissionInfo permissionInfo : permissions){
            if(permissionInfo.getUsername().equals(user)){
                return permissionInfo;
            }
        }

        //return permissions.get(user);
        return null;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public synchronized void removePermission(String user){
        permissions.remove(user);

    }

//    public boolean isPermissionExists(String user) {
//        return permissions.containsKey(user);
//
//    }
//
//    public String getUserPermissionStatus(String user){
//        return permissions.get(user).getPermissionStatus().toString();
//    }
//
//    public String getUserPermissionType(String user){
//        return permissions.get(user).getPermissionType().toString();
//    }
}
