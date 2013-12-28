package pl.edu.agh.tai.database.model;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;
import java.util.List;

public class DbUser {

    protected String username;
    protected String passwordDigest;
    protected List<String> roles;

    public DbUser() {
    }

    public DbUser(String username, String password, String[] roles) {
        this.username = username;
        this.passwordDigest = DigestUtils.sha1Hex(password);
        if(roles == null)
            this.roles = null;
        else
            this.roles = Arrays.asList(roles);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordDigest() {
        return passwordDigest;
    }

    public void setPasswordDigest(String passwordDigest) {
        this.passwordDigest = passwordDigest;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}

