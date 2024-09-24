package body.permission;


import java.util.HashMap;
import java.util.Map;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class PermissionManager {

    // Holds the permissions of the users "User:Permission"
    private final Map<String,String> permissions;

    public PermissionManager() {
        permissions = new HashMap<>();
    }

    public synchronized void addPermission(String user,String permission){
        if(permissions.containsKey(user)){
            permissions.replace(user,permission);
        }else {
            permissions.put(user, permission);
        }
    }

    public synchronized void removeSheet(String user){
        permissions.remove(user);

    }

    public synchronized Map<String, String> getPermissions() {
        return permissions;
    }

    public boolean isPermissionExists(String user) {
        return permissions.containsKey(user);

    }
}
