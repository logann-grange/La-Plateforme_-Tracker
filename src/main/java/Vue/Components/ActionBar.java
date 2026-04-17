package Vue.Components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class ActionBar {
    private final Button addButton;
    private final Button statisticsButton;
    private final Button importButton;
    private final Button exportButton;
    private final TextField searchField;
    private final TextField idSearchField;
    private final TextField ageSearchField;
    private final TextField averageSearchField;

    public ActionBar() {
        this.addButton = new CustomButton("Ajouter un eleve", 170, 38, "#0F766E").build();
        this.statisticsButton = new CustomButton("Statistiques", 130, 38, "#0EA5E9").build();
        this.importButton = new CustomButton("Importer", 110, 38, "#334155").build();
        this.exportButton = new CustomButton("Exporter", 110, 38, "#334155").build();
        this.searchField = new SearchBar("Rechercher un eleve...", 340, 40, "#FFFFFF", "#0F172A").build();
        this.idSearchField = new SearchBar("ID", 80, 40, "#FFFFFF", "#0F172A").build();
        this.ageSearchField = new SearchBar("Age", 80, 40, "#FFFFFF", "#0F172A").build();
        this.averageSearchField = new SearchBar("Moy.", 80, 40, "#FFFFFF", "#0F172A").build();
    }

    public HBox build() {
        HBox root = new HBox(12);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(14, 16, 14, 16));
        root.setStyle(
            "-fx-background-color: linear-gradient(to right, #F8FAFC, #EFF6FF);"
                + "-fx-border-color: #D8E2EE;"
                + "-fx-border-width: 0 0 1 0;"
        );

        HBox leftBox = new HBox(10, addButton);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        HBox centerBox = new HBox(10, searchField, idSearchField, ageSearchField, averageSearchField);
        centerBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(centerBox, Priority.ALWAYS);
        searchField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox rightBox = new HBox(10, statisticsButton, importButton, exportButton);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        root.getChildren().addAll(leftBox, spacer, centerBox, rightBox);
        return root;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getStatisticsButton() {
        return statisticsButton;
    }

    public Button getImportButton() {
        return importButton;
    }

    public Button getExportButton() {
        return exportButton;
    }

    public TextField getSearchField() {
        return searchField;                        
    }

    public TextField getIdSearchField() {
        return idSearchField;
    }

    public TextField getAgeSearchField() {
        return ageSearchField;
    }

    public TextField getAverageSearchField() {
        return averageSearchField;
    }
} 