package pl.edu.agh.tai.database;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authz.SimpleAuthorizationInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * Uses database to perform operations of authentication and authorization.
 */
public class AuthenticationManager {

    protected DbAccess db;

    /**
     * Initialization. Opens connection with a database.
     * @throws MongoDbException
     */
    public void initialize() throws MongoDbException {
        db = new DbAccess();
        db.connect();
    }

    /**
     * Closes connection with a database.
     */
    public void close() {
        db.disconnect();
    }

    /**
     * Checks if specified credentials are valid.
     * @param username application user's name
     * @param password user's password
     * @return true/false
     */
    public boolean validateUser(String username, char[] password) {
        String passwordDigest = DigestUtils.sha1Hex(String.valueOf(password));
        BasicDBObject query = new BasicDBObject();
        query.put("username", username);
        query.put("passwordDigest", passwordDigest);

        DBCursor iterator = db.getUsersCollection().find(query);
        if (iterator.hasNext())
            return true;

        return false;
    }

    /**
     * Fetches information about user's roles and theirs permissions.
     * @param username application user's name
     * @return AuthorizationInfo filled with fetched information
     */
    public SimpleAuthorizationInfo getUsersAuthorizationInfo(String username) {
        SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();
        List<String> roles = getUsersRoles(username);
        for (String role : roles) {
            List<String> permissions = getRolePermissions(role);
            sai.addRole(role);
            sai.addStringPermissions(permissions);
        }
        return sai;
    }

    protected List<String> getUsersRoles(String username) {
        List<String> roles = new LinkedList<String>();

        BasicDBObject query = new BasicDBObject();
        query.put("username", username);

        DBCursor iterator = db.getUsersCollection().find(query);
        if (iterator.hasNext()) {
            BasicDBObject userInfo = (BasicDBObject) iterator.next();
            roles = (List<String>) userInfo.get("roles");
        }
        return roles;
    }

    protected List<String> getRolePermissions(String role) {
        List<String> permissions = new LinkedList<String>();

        BasicDBObject query = new BasicDBObject();
        query.put("role", role);

        DBCursor iterator = db.getRolesCollection().find(query);
        if (iterator.hasNext()) {
            BasicDBObject userInfo = (BasicDBObject) iterator.next();
            permissions = (List<String>) userInfo.get("permissions");
        }
        return permissions;
    }
}
