package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class loginModel {

    private static final String URL  = "jdbc:postgresql://localhost:5432/tracker";
    private static final String USER = "postgres";
    private static final String PASS = "klemzz135";
    private static final String PEPPER = System.getenv("PASSWORD_PEPPER");
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); // coût = 12

    public void createUser(String firstName, String lastName, String mail, String password) {
        String request = "INSERT INTO users (first_name, last_name, mail, password) VALUES (?, ?, ?, ?)";
        
        password = securePassword(password);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = conn.prepareStatement(request)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, mail);
            stmt.setString(4, password);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hacher le mot de passe
    public String securePassword(String rawPassword) {
        String pepperedPassword = rawPassword + PEPPER; //poivrage
        return encoder.encode(pepperedPassword); // hachage et salage
    }

    // Vérifier le mot de passe
    public boolean verify(String rawPassword, String hashedPassword) {
        String pepperedPassword = rawPassword + PEPPER;
        return encoder.matches(pepperedPassword, hashedPassword);
    }

    public String[] login(String mail, String password) {
        String[] result = new String[5];
        String request = "SELECT * FROM users where mail = ?"; 
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
        PreparedStatement stmt = conn.prepareStatement(request)) {
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