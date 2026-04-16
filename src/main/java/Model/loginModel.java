package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class LoginModel {

    private static final String PEPPER = System.getenv("PASSWORD_PEPPER");
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private String lastErrorMessage;

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public boolean createUser(String firstName, String lastName, String mail, String password) {
        String request = "INSERT INTO users (first_name, last_name, mail, password) VALUES (?, ?, ?, ?)";
        
        password = securePassword(password);

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(request);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, mail);
            stmt.setString(4, password);
            stmt.executeUpdate();
            stmt.close();
            lastErrorMessage = null;
            return true;
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
            return false;
        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public String securePassword(String rawPassword) {
        String pepperedPassword = rawPassword + PEPPER;
        return encoder.encode(pepperedPassword);
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        String pepperedPassword = rawPassword + PEPPER;
        return encoder.matches(pepperedPassword, hashedPassword);
    }

    public String[] login(String mail, String password) {
        String[] result = new String[5];
        
        String request = "SELECT * FROM users WHERE mail = ?";

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(request);
            stmt.setString(1, mail);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    if (verifyPassword(password, rs.getString("password"))) {
                        result[0] = rs.getString("id");
                        result[1] = rs.getString("first_name");
                        result[2] = rs.getString("last_name");
                        result[3] = rs.getString("mail");
                        result[4] = rs.getString("password");
                    }
                }
            }
            stmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println("test main");
        LoginModel model = new LoginModel();
        //String passwordSecure = model.securePassword("test");
        //System.out.println("mdp : " + passwordSecure);
        //System.out.println(model.verifyPassword("test", passwordSecure));
        model.createUser("T" ,"T","t@laplateforme.io", "1");

    }
}