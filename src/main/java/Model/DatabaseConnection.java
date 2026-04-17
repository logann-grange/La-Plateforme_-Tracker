package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseConnection {

    private static final String URL  = "jdbc:postgresql://localhost:5432/tracker";
    private static final String USER = "postgres";
    private static final String PASS = "";

    private static Connection instance = null;

    private DatabaseConnection() {}

    public static Connection getInstance() {
        try {
            if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection(URL, USER, PASS);
        }
        return instance;
        } 
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    // Ferme la connexion si elle est ouverte
    public static void close() {
        try {
            if (instance != null && !instance.isClosed()) {
                instance.close();
                instance = null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public static ResultSet executeQuery(String request, Object... params) {
        try {
            Connection conn = getInstance();
            PreparedStatement stmt = conn.prepareStatement(request);

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            ResultSet result = stmt.executeQuery();
            conn.close();
            return result;
        } 
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int executeUpdate(String request, Object... params) {
        try {
            Connection conn = getInstance();
            PreparedStatement stmt = conn.prepareStatement(request);

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            int result = stmt.executeUpdate();
            conn.close();
            return result;
        } 
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}