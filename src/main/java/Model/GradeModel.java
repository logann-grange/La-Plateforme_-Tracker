package Model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class GradeModel {

    // renvoie une liste à partir des resultat d'une requete
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

    // Retourne la liste de toutes les notes
    public String[] selectAllGrade() {
        ResultSet rs = DatabaseConnection.executeQuery("SELECT * FROM grade");
        return resultSetToArray(rs);
    }

    // Retourne la liste des notes d'un élève
    public String[] getGrades(int studentId) {
        ResultSet rs = DatabaseConnection.executeQuery(
            "SELECT * FROM grade INNER JOIN student ON grade.student_id = student.id WHERE grade.student_id = ?",
            studentId
        );
        return resultSetToArray(rs);
    }

    // Ajoute une note à la BDD
    public void insertGrade(int studentId, String subject, double grade, LocalDate date) {
        DatabaseConnection.executeUpdate(
            "INSERT INTO grade (student_id, subject, grade, exam_date) VALUES (?, ?, ?, ?)",
            studentId, subject, grade, date
        );
    }

    // Modifie une note de la BDD
    public void updateGrade(int id, String subject, double grade, LocalDate date) {
        String request = "UPDATE grade SET ";
        List<Object> params = new ArrayList<>();

        // modif du sujet
        if (!subject.isEmpty()) {
            request += "subject = ?";
            params.add(subject);
        }
        // modif de la note
        if (grade != 0) {
            if (!params.isEmpty()) request += ", ";
            request += "grade = ?";
            params.add(grade);
        }
        // modif de la date
        if (date != null) {
            if (!params.isEmpty()) request += ", ";
            request += "exam_date = ?";
            params.add(date);
        }

        request += " WHERE id = ?";
        params.add(id);

        DatabaseConnection.executeUpdate(request, params.toArray());
    }

    // Supprime une note de la BDD
    public void deleteGrade(int id) {
        DatabaseConnection.executeUpdate("DELETE FROM grade WHERE id = ?", id);
    }
}