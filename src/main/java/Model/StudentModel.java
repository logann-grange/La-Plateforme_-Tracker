package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentModel {

    String selectRequest;
    //String orderRequest;
    List<Object> selectParams;

    public StudentModel() {
        this.selectRequest = "";
        this.selectParams = new ArrayList<>();
        //this.orderRequest = "";
    }

    public String[] selectAllStudent() {
        String request = "SELECT * FROM student";

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

    // Ajouter un élève
    public void addStudent(String firstName, String lastName, int age, float grade) {
        String request = "INSERT INTO student (first_name, last_name, age, grade) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(request);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setInt(3, age);
            stmt.setFloat(4, grade);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Modifier un élève
    public void updateStudent(int id, String firstName, String lastName, int age, float grade) {
        String request = "UPDATE student SET ";
        List<Object> params = new ArrayList<>();

        if (!firstName.isEmpty()) {
            if (!params.isEmpty()) request += ", ";
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

    // Supprimer un élève
    public void deleteStudent(int id) {
        String request = "DELETE FROM student WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(request);
            stmt.setObject(1, id);
            stmt.executeUpdate();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Filtrer les élèves
    public String[] filter(int id, String firstName, String lastName, String age, String grade) {
        this.selectParams = new ArrayList<>();
        this.selectRequest = "SELECT * FROM student WHERE 1=1";

        // filtrage sur l'id
        if (id != 0) {
            this.selectRequest += " AND id = ?";
            this.selectParams.add(id);
        }
        // filtrage sur le prénom
        if (firstName != null && !firstName.isEmpty()) {
            this.selectRequest += " AND first_name = ?";
            this.selectParams.add(firstName);
        }
        // filtrage sur le nom
        if (lastName != null && !lastName.isEmpty()) {
            this.selectRequest += " AND last_name = ?";
            this.selectParams.add(lastName);
        }
        // filtrage sur l'âge
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
        // filtrage sur la moyenne
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

        List<String> results = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(this.selectRequest);
            for (int i = 0; i < this.selectParams.size(); i++) {
                stmt.setObject(i + 1, this.selectParams.get(i));
            }

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

    // Trier les élèves
    public String[] sort(String column, int order) {
        if (this.selectRequest.isEmpty()) {
            this.selectRequest = "SELECT * FROM student";
        }

        String[] tabOrder = {"", "ASC", "DESC"};
        String strOrder = tabOrder[order];

        String query = this.selectRequest + " ORDER BY " + column + " " + strOrder;

        List<String> results = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(query);

            for (int i = 0; i < this.selectParams.size(); i++) {
                stmt.setObject(i + 1, this.selectParams.get(i));
            }

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

        // Reset après exécution
        this.selectRequest = "";
        this.selectParams = new ArrayList<>();

        return results.toArray(new String[0]);
    }

    // Test
    public static void main(String[] args) {
        System.out.println("test main");
        StudentModel model = new StudentModel();

        // Trier sans filtre
        //System.out.println(Arrays.toString(model.sort("age", 1)));

        // Filtrer puis trier
        //model.filter(0, "", "", ">50", "");
        //System.out.println(Arrays.toString(model.sort("grade", 1)));
        System.out.println(Arrays.toString(model.selectAllStudent()));
    }
}