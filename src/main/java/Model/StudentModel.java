package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentModel {

    //Ajouter un élève
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
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // modifier un élève
    public void updateStudent(int id, String firstName, String lastName, int age, float grade) {
        String request = "UPDATE student SET ";
        List<Object> params = new ArrayList<>();

        //ajouter à la requete si les parametres ne sont pas vides
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

        // connection à la BDD et execution de la requete
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

    // Suppression d'un éleve
    public void deleteStudent(int id) {
        //préparation de la requete
        String request = "DELETE FROM student WHERE id = ?";
        // connection à la BDD et execution de la requete
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(request);
            stmt.setObject(1, id);
            stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] filter(int id, String firstName, String lastName, String age, String grade) {
        List<Object> params = new ArrayList<>();
        String request = "SELECT * FROM student WHERE 1=1";

        // filtrage sur l'id
        if (id != 0) {
            request += " AND id = ?";
            params.add(id);
        }
        // filtrage sur le prénom
        if (firstName != null && !firstName.isEmpty()) {
            request += " AND first_name = ?";
            params.add(firstName);
        }
        // filtrage sur le nom
        if (lastName != null && !lastName.isEmpty()) {
            request += " AND last_name = ?";
            params.add(lastName);
        }
        // filtrage sur l'âge
            if (age.startsWith("<")) {
                request += " AND age < ?";
                params.add(Integer.parseInt(age.substring(1).trim()));
            } else if (age.startsWith(">")) {
                request += " AND age > ?";
                params.add(Integer.parseInt(age.substring(1).trim()));
            } else {
                request += " AND age = ?";
                params.add(Integer.parseInt(age.substring(1).trim()));
        }
        // filtrage sur la moyenne
        if (grade != null && !grade.isEmpty()) {
            if (grade.startsWith("<")) {
                request += " AND grade < ?";
                params.add(Float.parseFloat(grade.substring(1).trim()));
            } else if (grade.startsWith(">")) {
                request += " AND grade > ?";
                params.add(Float.parseFloat(grade.substring(1).trim()));
            } else {
                request += " AND grade = ?";
                params.add(Float.parseFloat(grade.substring(1).trim()));
            }
        }
        List<String> results = new ArrayList<>();

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(request);
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
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
    //Test
    public static void main(String[] args) {
        System.out.println("test main");
        StudentModel model = new StudentModel();
        //model.addStudent("A", "B", 66, 5);
        //model.updateStudent(1, "test3", "TEST3", 0, 0);
        System.out.println(Arrays.toString(model.filter(0, "", "", ">50", "")));
    }

}
