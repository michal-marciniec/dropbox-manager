package pl.edu.agh.tai.database;

public class MongoDbException extends Exception {
    public MongoDbException() {
        super();
    }

    public MongoDbException(Exception e) {
        super(e);
    }

    public MongoDbException(String e) {
        super(e);
    }
}
