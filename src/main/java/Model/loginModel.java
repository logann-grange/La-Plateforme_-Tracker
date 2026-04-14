package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class loginModel {

    private static final String PEPPER = System.getenv("PASSWORD_PEPPER");
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); // coût = 12
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

    // Hacher le mot de passe
    public String securePassword(String rawPassword) {
        String pepperedPassword = rawPassword + (PEPPER == null ? "" : PEPPER); //poivrage
        return encoder.encode(pepperedPassword); // hachage et salage
    }

    // Vérifier le mot de passe
    public boolean verify(String rawPassword, String hashedPassword) {
        String pepperedPassword = rawPassword + (PEPPER == null ? "" : PEPPER);
        return encoder.matches(pepperedPassword, hashedPassword);
    }

    public String[] login(String mail, String password) {
        String[] result = new String[5];
        String request = "SELECT * FROM users where mail = ?"; 
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(request);
            stmt.setString(1, mail);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println(rs.getString("first_name"));
                    if (verify(password, rs.getString("password"))) {
                        result[0] = rs.getString("id"); // int ??
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

    // test
    public static void main(String[] args) {
        System.out.println("test main");
        loginModel model = new loginModel();
        System.out.println("mdp : " + model.securePassword("test"));
    }
}