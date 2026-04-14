package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;


public class GradeModel {


    public String[] selectAllGrade() {
        String request = "SELECT * FROM grade";

        List<String> results = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(request);
            ResultSet rs = stmt.executeQuery();
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

        } 
        catch (Exception e) {
            e.printStackTrace();
        }

        return results.toArray(new String[0]);

    }
    
    public String[] getGrades(int studentId) {
        String request = "SELECT * FROM grade INNER JOIN student ON grade.student_id = student.id WHERE grade.student_id = ?";
        
        List<String> results = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(request);
            stmt.setInt(1, studentId);

            ResultSet rs = stmt.executeQuery();
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

    public void insertGrade(int studentId, String subject, double grade, LocalDate date) {

        String request = "INSERT INTO grade (student_id, subject, grade, exam_date) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(request);
            stmt.setInt(1, studentId);
            stmt.setString(2, subject);
            stmt.setDouble(3, grade);
            stmt.setObject(4, date);
            stmt.executeUpdate();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateGrade(int id, String subject, double grade, LocalDate date) {
        String request = "UPDATE grade SET ";
        List<Object> params = new ArrayList<>();

        if (!subject.isEmpty()) {
            if (!params.isEmpty()) request += ", ";
            request += "subject = ?";
            params.add(subject);
        }
        if (grade != 0) {
            if (!params.isEmpty()) request += ", ";
            request += "grade = ?";
            params.add(grade);
        }
        if (date != null) {
            if (!params.isEmpty()) request += ", ";
            request += "date = ?";
            params.add(date);
        }

        request += " WHERE id = ?";
        params.add(id);

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(request);
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //test
    public static void main(String[] args) {
        GradeModel model = new GradeModel();
        //model.insertGrade(1, "subject", 10, LocalDate.of(2026, 3, 10));
        //System.out.println(Arrays.toString(model.getGrades(1)));
        model.updateGrade(1, "SUBJECT", 2.2, null);
    }
}
