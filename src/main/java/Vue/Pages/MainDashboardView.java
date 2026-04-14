package Vue.Pages;

import java.util.ArrayList;
import java.util.List;
import Vue.Components.ActionBar;
import Vue.Components.AdvencedFilterPanel;
import Vue.Components.CustomButton;
import Vue.Components.DataTable;
import Vue.Components.DataTable.StudentRow;
import Vue.Components.LabelCustom;
import Vue.Components.SearchBar;
import Vue.Components.StatisticsPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainDashboardView {
    private final BorderPane root;
    private final Button returnButton;
    private final ActionBar actionBar;
    private final DataTable dataTable;
    private final AdvencedFilterPanel filterPanel;

    public MainDashboardView() {
        root = new BorderPane();
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: transparent;");

        returnButton = new CustomButton("Retour", 110, 34, "#475569").build();
        Label title = new LabelCustom("Gestion des eleves", 24, "#0F172A", true).build();

        HBox titleBar = new HBox(12, returnButton, title);
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(0, 0, 6, 0));

        actionBar = new ActionBar();
        VBox topSection = new VBox(10, titleBar, actionBar.build());
        topSection.setStyle(
            "-fx-background-color: rgba(255,255,255,0.78);"
                + "-fx-border-color: #D8E2EE;"
                + "-fx-border-radius: 14;"
                + "-fx-background-radius: 14;"
                + "-fx-padding: 10;"
                + "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.08), 16, 0.1, 0, 4);"
        );

        dataTable = new DataTable(8);
        dataTable.setRows(createMockRows());
        dataTable.setOnEdit(row -> System.out.println("Modifier eleve ID=" + row.getId()));
        dataTable.setOnDelete(row -> System.out.println("Suppression confirmee ID=" + row.getId()));
        dataTable.setOnGradeClick(this::openStudentNotesWindow);

        filterPanel = new AdvencedFilterPanel(
            new String[] {"Age 18-25", "Age 26-35", "Age 36-45", "Admis >=10", "Echec <10", "Excellent >=16"},
            "#FFFFFF",
            "#1A1A1A",
            200,
            420
        );
        VBox filterNode = filterPanel.build();
        filterPanel.bindToDataTable(dataTable);

        HBox centerContent = new HBox(10, dataTable.build(), filterNode);
        centerContent.setAlignment(Pos.TOP_LEFT);
        centerContent.setPadding(new Insets(12, 0, 0, 0));
        HBox.setHgrow(dataTable.build(), Priority.ALWAYS);

        wireActionBarEvents();

        root.setTop(topSection);
        root.setCenter(centerContent);
    }

    public BorderPane getRoot() {
        return root;
    }

    public Button getReturnButton() {
        return returnButton;
    }

    private void wireActionBarEvents() {
        actionBar.getAddButton().setOnAction(event ->
            openAddStudentWindow()
        );

        actionBar.getSearchField().setOnAction(event ->
            dataTable.searchRows(actionBar.getSearchField().getText())
        );
        actionBar.getSearchField().textProperty().addListener((obs, oldValue, newValue) ->
            dataTable.searchRows(newValue)
        );

        actionBar.getStatisticsButton().setOnAction(event ->
            openStatisticsWindow()
        );

        actionBar.getImportButton().setOnAction(event ->
            System.out.println("Action test: Importer des donnees")
        );

        actionBar.getExportButton().setOnAction(event ->
            System.out.println("Action test: Exporter des donnees")
        );
    }

    private void openAddStudentWindow() {
        Label title = new LabelCustom("Ajouter un eleve", 22, "#0F172A", true).build();
        Label subtitle = new LabelCustom("Renseignez les informations de l'eleve", 13, "#64748B", false).build();

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Prenom");
        firstNameField.setPrefWidth(230);
        firstNameField.setStyle(
            "-fx-background-color: #FFFFFF;"
                + "-fx-border-color: #CBD5E1;"
                + "-fx-border-radius: 8;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 8 10;"
        );

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Nom");
        lastNameField.setPrefWidth(230);
        lastNameField.setStyle(
            "-fx-background-color: #FFFFFF;"
                + "-fx-border-color: #CBD5E1;"
                + "-fx-border-radius: 8;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 8 10;"
        );

        TextField ageField = new TextField();
        ageField.setPromptText("Age");
        ageField.setPrefWidth(230);
        ageField.setStyle(
            "-fx-background-color: #FFFFFF;"
                + "-fx-border-color: #CBD5E1;"
                + "-fx-border-radius: 8;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 8 10;"
        );

        TextField gradeField = new TextField();
        gradeField.setPromptText("Moyenne (0-20)");
        gradeField.setPrefWidth(230);
        gradeField.setStyle(
            "-fx-background-color: #FFFFFF;"
                + "-fx-border-color: #CBD5E1;"
                + "-fx-border-radius: 8;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 8 10;"
        );

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.setAlignment(Pos.CENTER);
        form.add(new LabelCustom("Prenom", 13, "#334155", true).build(), 0, 0);
        form.add(firstNameField, 1, 0);
        form.add(new LabelCustom("Nom", 13, "#334155", true).build(), 0, 1);
        form.add(lastNameField, 1, 1);
        form.add(new LabelCustom("Age", 13, "#334155", true).build(), 0, 2);
        form.add(ageField, 1, 2);
        form.add(new LabelCustom("Moyenne", 13, "#334155", true).build(), 0, 3);
        form.add(gradeField, 1, 3);

        Button cancelButton = new CustomButton("Annuler", 110, 34, "#64748B").build();
        Button validateButton = new CustomButton("Valider", 110, 34, "#0A84FF").build();
        HBox actions = new HBox(12, cancelButton, validateButton);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(4, 0, 0, 0));

        VBox formCard = new VBox(form);
        formCard.setPadding(new Insets(14));
        formCard.setStyle(
            "-fx-background-color: #F8FAFC;"
                + "-fx-border-color: #E2E8F0;"
                + "-fx-border-radius: 10;"
                + "-fx-background-radius: 10;"
        );

        VBox content = new VBox(14, title, subtitle, formCard, actions);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #FFFFFF, #F1F5F9);"
        );

        Stage addStudentStage = new Stage();
        addStudentStage.setTitle("Ajouter un eleve");
        addStudentStage.setScene(new Scene(content, 500, 380));
        addStudentStage.setResizable(false);
        addStudentStage.initModality(Modality.APPLICATION_MODAL);

        if (root.getScene() != null) {
            addStudentStage.initOwner(root.getScene().getWindow());
        }

        cancelButton.setOnAction(event -> addStudentStage.close());
        validateButton.setOnAction(event -> {
            System.out.println(
                "Ajout eleve (a connecter): "
                    + firstNameField.getText() + " "
                    + lastNameField.getText() + ", age="
                    + ageField.getText() + ", moyenne="
                    + gradeField.getText()
            );
            addStudentStage.close();
        });

        addStudentStage.showAndWait();
    }

    private void openStatisticsWindow() {
        StatisticsPanel statisticsPanel = new StatisticsPanel("Statistiques des eleves", "#FFFFFF", "#1A1A1A");
        statisticsPanel.setRows(dataTable.getFilteredRows());

        Scene statisticsScene = new Scene(statisticsPanel.build(), 620, 780);
        Stage statisticsStage = new Stage();
        statisticsStage.setTitle("Statistiques");
        statisticsStage.setScene(statisticsScene);
        statisticsStage.setResizable(false);
        statisticsStage.initModality(Modality.APPLICATION_MODAL);

        if (root.getScene() != null) {
            statisticsStage.initOwner(root.getScene().getWindow());
        }

        statisticsStage.show();
    }

    private void openStudentNotesWindow(StudentRow student) {
        ObservableList<NoteRow> allNotes = FXCollections.observableArrayList(createMockNotesForStudent(student.getId()));
        ObservableList<NoteRow> visibleNotes = FXCollections.observableArrayList(allNotes);
        final String[] currentQuery = {""};

        Label title = new LabelCustom(
            "Notes - " + student.getFirstName() + " " + student.getLastName(),
            20,
            "#0F172A",
            true
        ).build();
        Label subtitle = new LabelCustom(
            "Moyenne actuelle: " + String.format("%.2f", student.getGrade()),
            13,
            "#475569",
            false
        ).build();

        TextField searchField = new SearchBar("Rechercher une note...", 260, 36, "#FFFFFF", "#0F172A").build();
        searchField.setMaxWidth(Double.MAX_VALUE);
        searchField.setStyle(
            searchField.getStyle()
                + "-fx-background-color: #FFFFFF;"
                + "-fx-border-color: #BFDBFE;"
                + "-fx-border-radius: 9;"
                + "-fx-background-radius: 9;"
        );

        TableView<NoteRow> notesTable = new TableView<>();
        notesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        notesTable.setPlaceholder(new LabelCustom("Aucune note pour cet eleve.", 13, "#64748B", false).build());
        notesTable.getSelectionModel().clearSelection();

        TableColumn<NoteRow, String> subjectCol = new TableColumn<>("Matiere");
        subjectCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getSubject()));

        TableColumn<NoteRow, Double> gradeCol = new TableColumn<>("Note");
        gradeCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getGrade()));
        gradeCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                    return;
                }

                setText(String.format("%.2f", item));
                if (item >= 10.0) {
                    setStyle("-fx-text-fill: #166534; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-text-fill: #B91C1C; -fx-font-weight: bold;");
                }
            }
        });

        TableColumn<NoteRow, String> examDateCol = new TableColumn<>("Date examen");
        examDateCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getExamDate()));

        TableColumn<NoteRow, String> createdAtCol = new TableColumn<>("Ajoute le");
        createdAtCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getCreatedAt()));

        notesTable.getColumns().addAll(subjectCol, gradeCol, examDateCol, createdAtCol);
        notesTable.getItems().setAll(visibleNotes);
        notesTable.setPrefHeight(340);
        notesTable.setStyle(
            "-fx-background-color: #F8FBFF;"
                + "-fx-border-color: #BFDBFE;"
                + "-fx-border-radius: 10;"
                + "-fx-background-radius: 10;"
                + "-fx-background-insets: 0;"
        );

        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            currentQuery[0] = newValue == null ? "" : newValue.trim().toLowerCase();
            refreshVisibleNotes(allNotes, visibleNotes, notesTable, currentQuery[0]);
        });

        Button addNoteButton = new CustomButton("Ajouter une note", 150, 34, "#0F766E").build();
        Button editNoteButton = new CustomButton("Modifier la note", 140, 34, "#0EA5E9").build();
        Button closeButton = new CustomButton("Fermer", 120, 34, "#334155").build();

        addNoteButton.setOnAction(event -> {
            NoteRow newNote = openNoteFormWindow(student, null);
            if (newNote == null) {
                return;
            }

            allNotes.add(newNote);
            refreshVisibleNotes(allNotes, visibleNotes, notesTable, currentQuery[0]);
        });

        editNoteButton.setOnAction(event -> {
            NoteRow selectedNote = notesTable.getSelectionModel().getSelectedItem();
            if (selectedNote == null) {
                showNoteAlert("Selection requise", "Selectionne d'abord une note a modifier.");
                return;
            }

            NoteRow updatedNote = openNoteFormWindow(student, selectedNote);
            if (updatedNote == null) {
                return;
            }

            int selectedIndex = allNotes.indexOf(selectedNote);
            if (selectedIndex >= 0) {
                allNotes.set(selectedIndex, updatedNote);
            }

            refreshVisibleNotes(allNotes, visibleNotes, notesTable, currentQuery[0]);
        });

        HBox noteActions = new HBox(10, addNoteButton, editNoteButton, closeButton);
        noteActions.setAlignment(Pos.CENTER_RIGHT);

        VBox contentCard = new VBox(12, title, subtitle, searchField, notesTable, noteActions);
        contentCard.setPadding(new Insets(16));
        contentCard.setStyle(
            "-fx-background-color: rgba(255,255,255,0.90);"
                + "-fx-border-color: #D8E2EE;"
                + "-fx-border-radius: 14;"
                + "-fx-background-radius: 14;"
                + "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.10), 18, 0.12, 0, 4);"
        );

        BorderPane shell = new BorderPane(contentCard);
        shell.setPadding(new Insets(14));
        shell.setStyle("-fx-background-color: linear-gradient(to bottom right, #DCEEFF, #ECF5FF);");

        Stage notesStage = new Stage();
        notesStage.setTitle("Notes de l'eleve");
        notesStage.setScene(new Scene(shell, 730, 520));
        notesStage.setResizable(false);
        notesStage.initModality(Modality.APPLICATION_MODAL);

        if (root.getScene() != null) {
            notesStage.initOwner(root.getScene().getWindow());
        }

        closeButton.setOnAction(event -> notesStage.close());
        notesStage.showAndWait();
    }

    private void refreshVisibleNotes(
        ObservableList<NoteRow> allNotes,
        ObservableList<NoteRow> visibleNotes,
        TableView<NoteRow> notesTable,
        String query
    ) {
        String normalizedQuery = query == null ? "" : query.trim().toLowerCase();
        visibleNotes.setAll(
            allNotes.stream()
                .filter(note -> normalizedQuery.isEmpty()
                    || note.getSubject().toLowerCase().contains(normalizedQuery)
                    || note.getExamDate().toLowerCase().contains(normalizedQuery)
                    || String.valueOf(note.getGrade()).contains(normalizedQuery))
                .toList()
        );
        notesTable.setItems(FXCollections.observableArrayList(visibleNotes));
    }

    private NoteRow openNoteFormWindow(StudentRow student, NoteRow existingNote) {
        boolean isEdit = existingNote != null;

        Label title = new LabelCustom(
            isEdit ? "Modifier une note" : "Ajouter une note",
            22,
            "#0F172A",
            true
        ).build();
        Label subtitle = new LabelCustom(
            isEdit ? "Ajuste les informations de la note." : "Renseigne les informations de la nouvelle note.",
            13,
            "#64748B",
            false
        ).build();

        TextField subjectField = new TextField();
        subjectField.setPromptText("Matiere");
        subjectField.setPrefWidth(240);
        subjectField.setStyle(
            "-fx-background-color: #FFFFFF;"
                + "-fx-border-color: #CBD5E1;"
                + "-fx-border-radius: 8;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 8 10;"
        );

        TextField gradeField = new TextField();
        gradeField.setPromptText("Note (0-20)");
        gradeField.setPrefWidth(240);
        gradeField.setStyle(
            "-fx-background-color: #FFFFFF;"
                + "-fx-border-color: #CBD5E1;"
                + "-fx-border-radius: 8;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 8 10;"
        );

        TextField examDateField = new TextField();
        examDateField.setPromptText("Date examen (YYYY-MM-DD)");
        examDateField.setPrefWidth(240);
        examDateField.setStyle(
            "-fx-background-color: #FFFFFF;"
                + "-fx-border-color: #CBD5E1;"
                + "-fx-border-radius: 8;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 8 10;"
        );

        if (isEdit) {
            subjectField.setText(existingNote.getSubject());
            gradeField.setText(String.valueOf(existingNote.getGrade()));
            examDateField.setText(existingNote.getExamDate());
        }

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.setAlignment(Pos.CENTER);
        form.add(new LabelCustom("Matiere", 13, "#334155", true).build(), 0, 0);
        form.add(subjectField, 1, 0);
        form.add(new LabelCustom("Note", 13, "#334155", true).build(), 0, 1);
        form.add(gradeField, 1, 1);
        form.add(new LabelCustom("Date examen", 13, "#334155", true).build(), 0, 2);
        form.add(examDateField, 1, 2);

        Button cancelButton = new CustomButton("Annuler", 110, 34, "#64748B").build();
        Button saveButton = new CustomButton(isEdit ? "Modifier" : "Ajouter", 110, 34, "#0A84FF").build();
        HBox actions = new HBox(12, cancelButton, saveButton);
        actions.setAlignment(Pos.CENTER);

        VBox formCard = new VBox(form);
        formCard.setPadding(new Insets(14));
        formCard.setStyle(
            "-fx-background-color: #F8FAFC;"
                + "-fx-border-color: #E2E8F0;"
                + "-fx-border-radius: 10;"
                + "-fx-background-radius: 10;"
        );

        VBox content = new VBox(14, title, subtitle, formCard, actions);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle(
            "-fx-background-color: rgba(255,255,255,0.92);"
                + "-fx-border-color: #D8E2EE;"
                + "-fx-border-radius: 14;"
                + "-fx-background-radius: 14;"
                + "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.10), 18, 0.12, 0, 4);"
        );

        BorderPane shell = new BorderPane(content);
        shell.setPadding(new Insets(14));
        shell.setStyle("-fx-background-color: linear-gradient(to bottom right, #DCEEFF, #ECF5FF);");

        Stage formStage = new Stage();
        formStage.setTitle(isEdit ? "Modifier une note" : "Ajouter une note");
        formStage.setScene(new Scene(shell, 560, 410));
        formStage.setResizable(false);
        formStage.initModality(Modality.APPLICATION_MODAL);

        if (root.getScene() != null) {
            formStage.initOwner(root.getScene().getWindow());
        }

        final NoteRow[] result = {null};

        cancelButton.setOnAction(event -> formStage.close());
        saveButton.setOnAction(event -> {
            String subject = subjectField.getText() == null ? "" : subjectField.getText().trim();
            String gradeText = gradeField.getText() == null ? "" : gradeField.getText().trim();
            String examDate = examDateField.getText() == null ? "" : examDateField.getText().trim();

            if (subject.isBlank() || gradeText.isBlank() || examDate.isBlank()) {
                showNoteAlert("Champs requis", "Tous les champs de la note sont obligatoires.");
                return;
            }

            double gradeValue;
            try {
                gradeValue = Double.parseDouble(gradeText.replace(',', '.'));
            } catch (NumberFormatException exception) {
                showNoteAlert("Note invalide", "La note doit etre un nombre valide.");
                return;
            }

            if (gradeValue < 0 || gradeValue > 20) {
                showNoteAlert("Note invalide", "La note doit etre comprise entre 0 et 20.");
                return;
            }

            result[0] = new NoteRow(
                student.getId(),
                subject,
                gradeValue,
                examDate,
                isEdit && existingNote != null ? existingNote.getCreatedAt() : "2026-04-14 12:00"
            );
            formStage.close();
        });

        formStage.showAndWait();
        return result[0];
    }

    private void showNoteAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private List<NoteRow> createMockNotesForStudent(int studentId) {
        List<NoteRow> notes = new ArrayList<>();

        notes.add(new NoteRow(studentId, "Maths", 14.5, "2026-03-28", "2026-04-01 10:20"));
        notes.add(new NoteRow(studentId, "Francais", 12.0, "2026-03-25", "2026-04-01 10:21"));
        notes.add(new NoteRow(studentId, "Histoire", 15.0, "2026-03-20", "2026-04-01 10:22"));
        notes.add(new NoteRow(studentId, "Physique", 13.5, "2026-03-18", "2026-04-01 10:23"));

        return notes;
    }

    private List<StudentRow> createMockRows() {
        List<StudentRow> rows = new ArrayList<>();
        rows.add(new StudentRow(1, "Lina", "Martin", 17, 14.5, "2026-04-01 10:00"));
        rows.add(new StudentRow(2, "Noah", "Petit", 18, 9.2, "2026-04-01 10:03"));
        rows.add(new StudentRow(3, "Emma", "Bernard", 20, 16.8, "2026-04-01 10:07"));
        rows.add(new StudentRow(4, "Adam", "Robert", 16, 11.0, "2026-04-01 10:10"));
        rows.add(new StudentRow(5, "Ines", "Richard", 19, 7.8, "2026-04-01 10:12"));
        rows.add(new StudentRow(6, "Leo", "Dubois", 21, 13.3, "2026-04-01 10:15"));
        rows.add(new StudentRow(7, "Maya", "Moreau", 18, 17.4, "2026-04-01 10:19"));
        rows.add(new StudentRow(8, "Sami", "Simon", 17, 10.1, "2026-04-01 10:22"));
        rows.add(new StudentRow(9, "Jade", "Laurent", 22, 15.0, "2026-04-01 10:25"));
        rows.add(new StudentRow(10, "Yanis", "Michel", 19, 8.4, "2026-04-01 10:30"));
        rows.add(new StudentRow(11, "Nora", "Garcia", 18, 12.6, "2026-04-01 10:34"));
        rows.add(new StudentRow(12, "Ilyes", "Roux", 17, 16.1, "2026-04-01 10:38"));
        rows.add(new StudentRow(13, "Sara", "Fournier", 20, 18.0, "2026-04-01 10:42"));
        rows.add(new StudentRow(14, "Amine", "Girard", 16, 6.9, "2026-04-01 10:46"));
        rows.add(new StudentRow(15, "Yara", "Andre", 19, 11.5, "2026-04-01 10:50"));
        return rows;
    }

    private static class NoteRow {
        private final int studentId;
        private final String subject;
        private final double grade;
        private final String examDate;
        private final String createdAt;

        private NoteRow(int studentId, String subject, double grade, String examDate, String createdAt) {
            this.studentId = studentId;
            this.subject = subject;
            this.grade = grade;
            this.examDate = examDate;
            this.createdAt = createdAt;
        }

        public int getStudentId() {
            return studentId;
        }

        public String getSubject() {
            return subject;
        }

        public double getGrade() {
            return grade;
        }

        public String getExamDate() {
            return examDate;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}
