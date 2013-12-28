package pl.edu.agh.tai.database.model;

import java.util.Arrays;
import java.util.List;

public class DbRolePermissions {
    protected String role;
    protected List<String> permissions;

    public DbRolePermissions(String role, String[] permissions) {
        this.role = role;
        if(permissions == null)
            this.permissions = null;
        else
            this.permissions = Arrays.asList(permissions);
    }

    public DbRolePermissions() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
