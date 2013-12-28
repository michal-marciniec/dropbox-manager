package pl.edu.agh.tai.database;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import pl.edu.agh.tai.database.model.DbRolePermissions;
import pl.edu.agh.tai.database.model.DbUser;

import java.net.UnknownHostException;

/**
 * Class to store database connection information and to access database.
 */
public final class DbAccess {

    private static final String DB_USER = "mongo";
    private static final String DB_PASS = "mango";
    private static final String DB_NAME = "taidb";
    private static final String DB_ADDRESS = "localhost";
    private static final int DB_PORT = 27017;
    private static final String DB_USERS_COLLECTION = "users";
    private static final String DB_ROLES_COLLECTION = "roles";
    protected MongoClient mongo;
    protected DB database;

    /**
     * Established connection with a database.
     * @throws MongoDbException
     */
    public void connect() throws MongoDbException {
        try {

            mongo = new MongoClient(DB_ADDRESS, DB_PORT);
            database = mongo.getDB(DB_NAME);
            authenticate();

        } catch (UnknownHostException e) {
            throw new MongoDbException(e);
        }
    }

    /**
     * Closes connection with a database.
     */
    public void disconnect() {
        mongo.close();
    }

    /**
     * Restores default database content.
     */
    public void setUpDbContent() {
        DbUser adminUser = new DbUser("admin", "admin", new String[]{"administrator", "viewer"});
        DbUser userUser = new DbUser("user", "user", new String[]{"viewer"});

        DbRolePermissions administratorRole = new DbRolePermissions("administrator", new String[]{"*"});
        DbRolePermissions viewerRole = new DbRolePermissions("viewer", new String[]{"*:view"});

        getUsersCollection().drop();
        registerUser(adminUser);
        registerUser(userUser);

        getRolesCollection().drop();
        addRole(administratorRole);
        addRole(viewerRole);
    }

    /**
     * Returns database collection containing information about users.
     * @return database collection
     */
    public DBCollection getUsersCollection() {
        final DBCollection collection = database.getCollection(DB_USERS_COLLECTION);
        return collection;
    }

    /**
     * Returns database collection containing information about roles and theirs permissions.
     * @return database collection
     */
    public DBCollection getRolesCollection() {
        final DBCollection collection = database.getCollection(DB_ROLES_COLLECTION);
        return collection;
    }

    protected void authenticate() throws MongoDbException {
        if (database != null) {
            final boolean auth = database.authenticate(DB_USER, DB_PASS.toCharArray());
            if (auth)
                return;
        }
        throw new MongoDbException("Database authentication failed");
    }

    protected void registerUser(DbUser userToRegister) {
        insertObject(userToRegister, getUsersCollection());
    }

    protected void addRole(DbRolePermissions role) {
        insertObject(role, getRolesCollection());
    }

    protected void insertObject(Object obj, DBCollection collection) {
        Gson gson = new Gson();
        String jsonObj = gson.toJson(obj);
        DBObject dbObject = (DBObject) JSON.parse(jsonObj);
        collection.insert(dbObject);
    }

}