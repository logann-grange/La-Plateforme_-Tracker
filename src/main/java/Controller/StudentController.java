package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import Model.GradeModel;
import Model.StudentModel;
import Vue.Components.DataTable.StudentRow;
import Vue.Pages.MainDashboardView;
import Vue.Pages.MainDashboardView.NoteFormData;
import javafx.scene.Scene;
import javafx.stage.FileChooser;

public class StudentController {

    private final MainDashboardView vue;
    private final StudentModel model;
    private final GradeModel gradeModel;
    private final Scene scene;

    public StudentController(MainDashboardView vue, StudentModel model, Scene scene) {
        this(vue, model, new GradeModel(), scene);
    }

    public StudentController(MainDashboardView vue, StudentModel model, GradeModel gradeModel, Scene scene) {
        this.vue = vue;
        this.model = model;
        this.gradeModel = gradeModel;
        this.scene = scene;

        loadStudents();
        branchAdd();
        branchEditDelete();
        branchNotes();
        branchOnExport();
        branchOnImport();
    }

    private void loadStudents() {
        String[] results = model.sort("id", 1);
        vue.getDataTable().setRows(parseRows(results));
    }

    private void branchAdd() {
        vue.setOnAddStudent(data -> {
            String firstName = data[0];
            String lastName  = data[1];
            String ageStr    = data[2];
            String gradeStr  = data[3];

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

    private void branchOnExport() {
        vue.setOnExport(data -> openFileChooser(data[0], false));
    }

    private void branchOnImport() {
        vue.setOnImport(data -> openFileChooser(data[0], true));
    }

    private void branchEditDelete() {
        vue.setOnEditStudentRequested(row -> {
            model.updateStudent(
                row.getId(),
                row.getFirstName(),
                row.getLastName(),
                row.getAge(),
                (float) row.getGrade()
            );
            loadStudents();
        });

        vue.setOnDeleteStudentRequested(row -> {
            model.deleteStudent(row.getId());
            loadStudents();
        });
    }

    private void branchNotes() {
        vue.setOnNoteCreated(this::addNote);
        vue.setStudentNotesProvider(this::loadNotesForStudent);
    }

    private void addNote(NoteFormData note) {
        if (note == null) {
            return;
        }

        String subject = note.getSubject().trim();
        if (subject.isEmpty() || note.getExamDate().trim().isEmpty()) {
            System.out.println("Erreur : sujet et date de la note sont obligatoires.");
            return;
        }

        try {
            LocalDate examDate = LocalDate.parse(note.getExamDate().trim());
            gradeModel.insertGrade(note.getStudentId(), subject, note.getGrade(), examDate);
            updateStudentAverage(note.getStudentId());
        } catch (DateTimeParseException e) {
            System.out.println("Erreur : date de note invalide (format attendu YYYY-MM-DD).");
        }
    }

    private void updateStudentAverage(int studentId) {
        Double average = calculateStudentAverage(studentId);
        if (average == null) {
            return;
        }

        model.updateStudentAverage(studentId, average);
        loadStudents();
    }

    private Double calculateStudentAverage(int studentId) {
        String[] results = gradeModel.selectAllGrade();
        double sum = 0.0;
        int count = 0;

        for (String result : results) {
            try {
                String[] parts = result.split(", ");
                int currentStudentId = 0;
                double grade = 0.0;

                for (String part : parts) {
                    String[] kv = part.split("=", 2);
                    if (kv.length < 2) {
                        continue;
                    }

                    switch (kv[0].trim()) {
                        case "student_id" -> currentStudentId = Integer.parseInt(kv[1].trim());
                        case "grade" -> grade = Double.parseDouble(kv[1].trim());
                    }
                }

                if (currentStudentId == studentId) {
                    sum += grade;
                    count++;
                }
            } catch (Exception e) {
                System.out.println("Erreur calcul moyenne pour l'eleve " + studentId + " : " + result);
            }
        }

        if (count == 0) {
            return null;
        }

        return sum / count;
    }

    private List<NoteFormData> loadNotesForStudent(int studentId) {
        String[] results = gradeModel.selectAllGrade();
        List<NoteFormData> notes = new ArrayList<>();

        for (String result : results) {
            try {
                String[] parts = result.split(", ");
                int currentStudentId = 0;
                String subject = "";
                double grade = 0;
                String examDate = "";
                String createdAt = "";

                for (String part : parts) {
                    String[] kv = part.split("=", 2);
                    if (kv.length < 2) {
                        continue;
                    }

                    switch (kv[0].trim()) {
                        case "student_id" -> currentStudentId = Integer.parseInt(kv[1].trim());
                        case "subject" -> subject = kv[1].trim();
                        case "grade" -> grade = Double.parseDouble(kv[1].trim());
                        case "exam_date" -> examDate = kv[1].trim();
                        case "created_at" -> createdAt = kv[1].trim();
                    }
                }

                if (currentStudentId == studentId) {
                    notes.add(new NoteFormData(currentStudentId, subject, grade, examDate, createdAt));
                }
            } catch (Exception e) {
                System.out.println("Erreur parsing note : " + result);
            }
        }

        return notes;
    }
    
    public void openFileChooser(String format, boolean isImport) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(format.toUpperCase(), "*." + format)
        );

        if (isImport) {
            fileChooser.setTitle("Importer un fichier " + format.toUpperCase());
            File file = fileChooser.showOpenDialog(scene.getWindow());
            if (file != null) importFile(file, format);
        } else {
            fileChooser.setTitle("Exporter en " + format.toUpperCase());
            fileChooser.setInitialFileName("eleves." + format);
            File file = fileChooser.showSaveDialog(scene.getWindow());
            if (file != null) exportFile(file, format);
        }
    }

    private void importFile(File file, String format) {
        try {
            switch (format) {
                case "csv"  -> importCSV(file);
                case "xml"  -> importXML(file);
                case "json" -> importJSON(file);
            }
            loadStudents();
        } catch (Exception e) {
            System.out.println("Erreur import : " + e.getMessage());
        }
    }

    private void importCSV(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        boolean firstLine = true;
        while ((line = reader.readLine()) != null) {
            if (firstLine) { firstLine = false; continue; }
            String[] cols = line.split(",");
            if (cols.length < 4) continue;
            String firstName = cols[0].trim();
            String lastName  = cols[1].trim();
            int age          = Integer.parseInt(cols[2].trim());
            float grade      = Float.parseFloat(cols[3].trim());
            model.addStudent(firstName, lastName, age, grade);
        }
        reader.close();
    }

    private void importXML(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line.trim());
        reader.close();

        String content = sb.toString();
        String[] entries = content.split("<student>");
        for (int i = 1; i < entries.length; i++) {
            String entry = entries[i];
            String firstName = extractXMLTag(entry, "first_name");
            String lastName  = extractXMLTag(entry, "last_name");
            int age          = Integer.parseInt(extractXMLTag(entry, "age"));
            float grade      = Float.parseFloat(extractXMLTag(entry, "grade"));
            model.addStudent(firstName, lastName, age, grade);
        }
    }

    private String extractXMLTag(String content, String tag) {
        int start = content.indexOf("<" + tag + ">") + tag.length() + 2;
        int end   = content.indexOf("</" + tag + ">");
        return content.substring(start, end).trim();
    }

    private void importJSON(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line.trim());
        reader.close();

        String content = sb.toString();
        String[] entries = content.split("\\{");
        for (int i = 1; i < entries.length; i++) {
            String entry = entries[i].replace("}", "").replace("]", "");
            String firstName = extractJSONField(entry, "first_name");
            String lastName  = extractJSONField(entry, "last_name");
            String ageStr    = extractJSONField(entry, "age");
            String gradeStr  = extractJSONField(entry, "grade");
            if (firstName == null || lastName == null || ageStr == null || gradeStr == null) continue;
            model.addStudent(firstName, lastName, Integer.parseInt(ageStr), Float.parseFloat(gradeStr));
        }
    }

    private String extractJSONField(String content, String key) {
        String search = "\"" + key + "\"";
        int idx = content.indexOf(search);
        if (idx == -1) return null;
        int colonIdx = content.indexOf(":", idx) + 1;
        int end = content.indexOf(",", colonIdx);
        if (end == -1) end = content.length();
        return content.substring(colonIdx, end).trim().replace("\"", "");
    }


    private void exportFile(File file, String format) {
        String[] results = model.sort("id", 1);
        List<StudentRow> rows = parseRows(results);

        try {
            switch (format) {
                case "csv"  -> exportCSV(file, rows);
                case "xml"  -> exportXML(file, rows);
                case "json" -> exportJSON(file, rows);
            }
        } catch (Exception e) {
            System.out.println("Erreur export : " + e.getMessage());
        }
    }

    private void exportCSV(File file, List<StudentRow> rows) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("first_name,last_name,age,grade");
        writer.newLine();
        for (StudentRow row : rows) {
            writer.write(row.getFirstName() + "," + row.getLastName() + "," + row.getAge() + "," + row.getGrade());
            writer.newLine();
        }
        writer.close();
    }

    private void exportXML(File file, List<StudentRow> rows) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("<students>");
        writer.newLine();
        for (StudentRow row : rows) {
            writer.write("  <student>");
            writer.newLine();
            writer.write("    <first_name>" + row.getFirstName() + "</first_name>");
            writer.newLine();
            writer.write("    <last_name>" + row.getLastName() + "</last_name>");
            writer.newLine();
            writer.write("    <age>" + row.getAge() + "</age>");
            writer.newLine();
            writer.write("    <grade>" + row.getGrade() + "</grade>");
            writer.newLine();
            writer.write("  </student>");
            writer.newLine();
        }
        writer.write("</students>");
        writer.close();
    }

    private void exportJSON(File file, List<StudentRow> rows) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("[");
        writer.newLine();
        for (int i = 0; i < rows.size(); i++) {
            StudentRow row = rows.get(i);
            writer.write("  {");
            writer.newLine();
            writer.write("    \"first_name\": \"" + row.getFirstName() + "\",");
            writer.newLine();
            writer.write("    \"last_name\": \"" + row.getLastName() + "\",");
            writer.newLine();
            writer.write("    \"age\": " + row.getAge() + ",");
            writer.newLine();
            writer.write("    \"grade\": " + row.getGrade());
            writer.newLine();
            writer.write("  }" + (i < rows.size() - 1 ? "," : ""));
            writer.newLine();
        }
        writer.write("]");
        writer.close();
    }

    private List<StudentRow> parseRows(String[] results) {
        List<StudentRow> rows = new ArrayList<>();
        for (String result : results) {
            try {
                String[] parts   = result.split(", ");
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