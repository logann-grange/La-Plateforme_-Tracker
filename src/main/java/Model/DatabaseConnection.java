package Model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static final String URL  = "jdbc:postgresql://localhost:5432/tracker";
    private static final String USER = "postgres";
    private static final String PASS = "klemzz135";

    private static Connection instance = null;

    private DatabaseConnection() {}

    public static Connection getInstance() throws Exception {
        if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection(URL, USER, PASS);
        }
        return instance;
    }
}