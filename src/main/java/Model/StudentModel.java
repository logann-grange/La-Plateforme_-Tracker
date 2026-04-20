package Model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class StudentModel {

    String selectRequest;
    List<Object> selectParams;

    public StudentModel() {
        this.selectRequest = "";
        this.selectParams = new ArrayList<>();
    }

    private String[] resultSetToArray(ResultSet rs) {
        List<String> results = new ArrayList<>();
        try {
            if (rs == null) return new String[0];

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                StringBuilder row = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    if (i > 1) row.append(", ");
                    row.append(meta.getColumnName(i)).append("=").append(rs.getString(i));
                }
                results.add(row.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results.toArray(new String[0]);
    }

    // Retourne la liste de tous les élèves
    public String[] selectAllStudent() {
        ResultSet rs = DatabaseConnection.executeQuery("SELECT * FROM student");
        return resultSetToArray(rs);
    }

    // Ajouter un élève
    public void addStudent(String firstName, String lastName, int age, float grade) {
        DatabaseConnection.executeUpdate(
            "INSERT INTO student (first_name, last_name, age, grade) VALUES (?, ?, ?, ?)",
            firstName, lastName, age, grade
        );
    }

    // Modifier un élève
    public void updateStudent(int id, String firstName, String lastName, int age, float grade) {
        String request = "UPDATE student SET ";
        List<Object> params = new ArrayList<>();

        if (!firstName.isEmpty()) {
            request += "first_name = ?";
            params.add(firstName);
        }
        if (!lastName.isEmpty()) {
            if (!params.isEmpty()) request += ", ";
            request += "last_name = ?";
            params.add(lastName);
        }
        if (age != 0) {
            if (!params.isEmpty()) request += ", ";
            request += "age = ?";
            params.add(age);
        }
        if (grade != 0) {
            if (!params.isEmpty()) request += ", ";
            request += "grade = ?";
            params.add(grade);
        }

        request += " WHERE id = ?";
        params.add(id);

        DatabaseConnection.executeUpdate(request, params.toArray());
    }

    // Supprimer un élève
    public void deleteStudent(int id) {
        DatabaseConnection.executeUpdate("DELETE FROM student WHERE id = ?", id);
    }

    // Filtrer les élèves
    public String[] filter(int id, String firstName, String lastName, String age, String grade) {
        this.selectParams = new ArrayList<>();
        this.selectRequest = "SELECT * FROM student WHERE 1=1";

        // filtrage par id
        if (id != 0) {
            this.selectRequest += " AND id = ?";
            this.selectParams.add(id);
        }
        // filtrage par par prenom
        if (firstName != null && !firstName.isEmpty()) {
            this.selectRequest += " AND first_name = ?";
            this.selectParams.add(firstName);
        }
        // filtrage par nom
        if (lastName != null && !lastName.isEmpty()) {
            this.selectRequest += " AND last_name = ?";
            this.selectParams.add(lastName);
        }
        // filtrage par age
        if (age != null && !age.isEmpty()) {
            if (age.startsWith("<")) {
                this.selectRequest += " AND age < ?";
                this.selectParams.add(Integer.parseInt(age.substring(1).trim()));
            } else if (age.startsWith(">")) {
                this.selectRequest += " AND age > ?";
                this.selectParams.add(Integer.parseInt(age.substring(1).trim()));
            } else {
                this.selectRequest += " AND age = ?";
                this.selectParams.add(Integer.parseInt(age.trim()));
            }
        }
        // filtrage par note
        if (grade != null && !grade.isEmpty()) {
            if (grade.startsWith("<")) {
                this.selectRequest += " AND grade < ?";
                this.selectParams.add(Float.parseFloat(grade.substring(1).trim()));
            } else if (grade.startsWith(">")) {
                this.selectRequest += " AND grade > ?";
                this.selectParams.add(Float.parseFloat(grade.substring(1).trim()));
            } else {
                this.selectRequest += " AND grade = ?";
                this.selectParams.add(Float.parseFloat(grade.trim()));
            }
        }

        ResultSet rs = DatabaseConnection.executeQuery(this.selectRequest, this.selectParams.toArray());
        return resultSetToArray(rs);
    }

    // Trier les élèves
    public String[] sort(String column, int order) {
        if (this.selectRequest.isEmpty()) {
            this.selectRequest = "SELECT * FROM student";
        }

        String[] tabOrder = {"", "ASC", "DESC"};
        String query = this.selectRequest + " ORDER BY " + column + " " + tabOrder[order];

        ResultSet rs = DatabaseConnection.executeQuery(query, this.selectParams.toArray());

        // Reset après exécution
        this.selectRequest = "";
        this.selectParams = new ArrayList<>();

        return resultSetToArray(rs);
    }
}