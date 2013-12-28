package pl.edu.agh.tai.security.utils;

        import org.apache.shiro.authz.Permission;
        import org.apache.shiro.authz.permission.RolePermissionResolver;

        import java.util.Collection;
        import java.util.HashMap;
        import java.util.LinkedList;

public class SimpleRoleToPermissionResolver implements RolePermissionResolver {

    protected HashMap<String, Collection<Permission>> roleToPermissions = new HashMap<String, Collection<Permission>>();

    @Override
    public Collection<Permission> resolvePermissionsInRole(String role) {
        Collection<Permission> permissions = roleToPermissions.get(role);
        if (permissions == null) {
            permissions = new LinkedList<Permission>();
            roleToPermissions.put(role, permissions);
        }
        return permissions;
    }

}
