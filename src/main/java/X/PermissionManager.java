package X;

import java.util.ArrayList;

public class PermissionManager {

    public static boolean hasLogInPermission(User user){
      return hasPermission("login", user);
    }

    public static boolean hasCreateProjectPermission(User user){
        return hasPermission("create_project", user);
    }

    public static boolean hasControlPanelPermission(User user){
        return hasPermission("control_panel", user);
    }

    public static boolean hasChangeUsernamePermission(User user){ return hasPermission("change_username", user);}

    public static boolean hasChangeRoleManagementPermission(User user) {return hasPermission("role_management", user);}

    public static boolean hasPermission(String permission, User user){
        ArrayList<Role> roles = user.getRoles();
        for (int i=0; i<roles.size(); i++){
            ArrayList<String> rolePermission = roles.get(i).getPermissions();
            for (int j=0; j<rolePermission.size(); j++){
                if (rolePermission.get(j).equals(permission)){
                    return true;
                }
            }
        }
        return false;
    }
}
