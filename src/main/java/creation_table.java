import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class creation_table {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/tracker";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "mdp123";
    private static final String ADMIN_DB_URL = "jdbc:postgresql://localhost:5432/postgres";

    public static void main(String[] args) {
        String url = DB_URL;
        String user = DB_USER;
        String password = DB_PASSWORD;
        String databaseName = extractDatabaseName(DB_URL);

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL introuvable. Lance le script via Maven avec la dependance postgresql.");
            e.printStackTrace();
            return;
        }

        String sql = """
            CREATE TABLE IF NOT EXISTS student (
                id SERIAL PRIMARY KEY,
                first_name VARCHAR(255) NOT NULL,
                last_name VARCHAR(255) NOT NULL,
                age INT NOT NULL,
                grade FLOAT NOT NULL CHECK ,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

        String noteSql = """
            CREATE TABLE IF NOT EXISTS grade (
                id SERIAL PRIMARY KEY,
                student_id INT NOT NULL REFERENCES student(id) ON DELETE CASCADE,
                subject VARCHAR(100) NOT NULL,
                grade FLOAT NOT NULL ,
                exam_date DATE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

        String userSql = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                first_name VARCHAR(255) NOT NULL UNIQUE,
                last_name VARCHAR(255) NOT NULL,
                mail VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(500) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

        try {
            ensureDatabaseExists(databaseName, user, password);
        } catch (SQLException e) {
            System.err.println("Erreur creation base PostgreSQL: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            statement.execute(noteSql);
            statement.execute(userSql);
            System.out.println("Table 'student' creee ou deja existante.");
            System.out.println("Table 'note' creee ou deja existante.");
            System.out.println("Table 'users' creee ou deja existante.");
        } catch (SQLException e) {
            System.err.println("Erreur JDBC PostgreSQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void ensureDatabaseExists(String databaseName, String user, String password) throws SQLException {
        String checkDbSql = "SELECT 1 FROM pg_database WHERE datname = '" + databaseName.replace("'", "''") + "'";

        try (Connection adminConnection = DriverManager.getConnection(ADMIN_DB_URL, user, password);
             Statement adminStatement = adminConnection.createStatement();
             ResultSet resultSet = adminStatement.executeQuery(checkDbSql)) {

            if (!resultSet.next()) {
                String createDbSql = "CREATE DATABASE \"" + databaseName.replace("\"", "\"\"") + "\"";
                adminStatement.execute(createDbSql);
                System.out.println("Base '" + databaseName + "' creee.");
            } else {
                System.out.println("Base '" + databaseName + "' deja existante.");
            }
        }
    }

    private static String extractDatabaseName(String jdbcUrl) {
        int lastSlash = jdbcUrl.lastIndexOf('/');
        if (lastSlash == -1 || lastSlash == jdbcUrl.length() - 1) {
            throw new IllegalArgumentException("DB_URL invalide: " + jdbcUrl);
        }

        String dbPart = jdbcUrl.substring(lastSlash + 1);
        int queryIndex = dbPart.indexOf('?');
        return queryIndex >= 0 ? dbPart.substring(0, queryIndex) : dbPart;
    }
}
