package Controller;

import java.util.ArrayList;
import java.util.List;

import Model.StudentModel;
import Vue.Components.DataTable.StudentRow;
import Vue.Pages.MainDashboardView;
import javafx.scene.Scene;

public class StudentController {

    private final MainDashboardView vue;
    private final StudentModel model;
    private final Scene scene;

    public StudentController(MainDashboardView vue, StudentModel model, Scene scene) {
        this.vue = vue;
        this.model = model;
        this.scene = scene;

        loadStudents();
        branchAdd();
        branchEditDelete();
    }

    private void loadStudents() {
        String[] results = model.sort("id", 1);
        vue.getDataTable().setRows(parseRows(results));
    }

    private void branchAdd() {
        vue.setOnAddStudent(data -> {
            String firstName = data[0] == null ? "" : data[0].trim();
            String lastName  = data[1] == null ? "" : data[1].trim();
            String ageStr    = data[2] == null ? "" : data[2].trim();
            String gradeStr  = data[3] == null ? "" : data[3].trim().replace(',', '.');

            if (firstName.isEmpty() || lastName.isEmpty() || ageStr.isEmpty() || gradeStr.isEmpty()) {
                System.out.println("Erreur : tous les champs sont obligatoires.");
                return;
            }

            try {
                int age     = Integer.parseInt(ageStr.trim());
                float grade = Float.parseFloat(gradeStr.trim());
                model.addStudent(firstName, lastName, age, grade);
                loadStudents();
            } catch (NumberFormatException e) {
                System.out.println("Erreur : age ou moyenne invalide.");
            }
        });
    }

    private void branchEditDelete() {
        vue.getDataTable().setOnEdit(row -> {
            model.updateStudent(
                row.getId(),
                row.getFirstName(),
                row.getLastName(),
                row.getAge(),
                (float) row.getGrade()
            );
            loadStudents();
        });

        vue.getDataTable().setOnDelete(row -> {
            model.deleteStudent(row.getId());
            loadStudents();
        });
    }

    private List<StudentRow> parseRows(String[] results) {
        List<StudentRow> rows = new ArrayList<>();
        for (String result : results) {
            try {
                String[] parts = result.split(", ");
                int id           = 0;
                String firstName = "";
                String lastName  = "";
                int age          = 0;
                float grade      = 0;
                String createdAt = "";

                for (String part : parts) {
                    String[] kv = part.split("=", 2);
                    if (kv.length < 2) continue;
                    switch (kv[0].trim()) {
                        case "id"         -> id        = Integer.parseInt(kv[1].trim());
                        case "first_name" -> firstName = kv[1].trim();
                        case "last_name"  -> lastName  = kv[1].trim();
                        case "age"        -> age       = Integer.parseInt(kv[1].trim());
                        case "grade"      -> grade     = Float.parseFloat(kv[1].trim());
                        case "created_at" -> createdAt = kv[1].trim();
                    }
                }
                rows.add(new StudentRow(id, firstName, lastName, age, grade, createdAt));
            } catch (Exception e) {
                System.out.println("Erreur parsing ligne : " + result);
            }
        }
        return rows;
    }
}