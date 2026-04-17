package Model;

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

    // Créé un utilisateur en BDD
    public boolean createUser(String firstName, String lastName, String mail, String password) {
        password = securePassword(password);

        int result = DatabaseConnection.executeUpdate(
            "INSERT INTO users (first_name, last_name, mail, password) VALUES (?, ?, ?, ?)",
            firstName, lastName, mail, password
        );

        if (result == -1) {
            lastErrorMessage = "Erreur lors de la création de l'utilisateur";
            return false;
        }

        lastErrorMessage = null;
        return true;
    }

    // Hash, sale et poivre le mdp
    public String securePassword(String rawPassword) {
        String pepperedPassword = rawPassword + PEPPER;
        return encoder.encode(pepperedPassword);
    }

    // Vérifie si le mdp entré correspond au mdp sécurisé
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        String pepperedPassword = rawPassword + PEPPER;
        return encoder.matches(pepperedPassword, hashedPassword);
    }

    // Connecte à un compte utilisateur
    public String[] login(String mail, String password) {
        String[] result = new String[5];

        ResultSet rs = DatabaseConnection.executeQuery(
            "SELECT * FROM users WHERE mail = ?",
            mail
        );

        try {
            if (rs != null && rs.next()) {
                if (verifyPassword(password, rs.getString("password"))) {
                    result[0] = rs.getString("id");
                    result[1] = rs.getString("first_name");
                    result[2] = rs.getString("last_name");
                    result[3] = rs.getString("mail");
                    result[4] = rs.getString("password");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}