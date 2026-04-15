package Vue.Pages;

import java.util.ArrayList;
import java.util.List;
import Vue.Components.ActionBar;
import Vue.Components.AdvencedFilterPanel;
import Vue.Components.CustomButton;
import Vue.Components.DataTable;
import Vue.Components.DataTable.StudentRow;
import Vue.Components.LabelCustom;
import Vue.Components.StatisticsPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private java.util.function.Consumer<String[]> onAddStudent;

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

        filterPanel = new AdvencedFilterPanel(
            new String[] {"Majeur", "Mineur", "Admis >=10", "Echec <10", "Excellent >=16"},
            "#FFFFFF",
            "#1A1A1A",
            280,
            420
        );
        VBox filterNode = filterPanel.build();
        filterPanel.bindToDataTable(dataTable);

        HBox centerContent = new HBox(16, dataTable.build(), filterNode);
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

    public DataTable getDataTable() {
        return dataTable;
    }

    public ActionBar getActionBar() {
        return actionBar;
    }

    public void setOnAddStudent(java.util.function.Consumer<String[]> callback) {
        this.onAddStudent = callback;
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
             if (onAddStudent != null) {
                onAddStudent.accept(new String[]{
                    firstNameField.getText(),
                    lastNameField.getText(),
                    ageField.getText(),
                    gradeField.getText()
                });
            }
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
}
