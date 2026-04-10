package Vue.Pages;

import java.util.ArrayList;
import java.util.List;
import Vue.Components.ActionBar;
import Vue.Components.AdvencedFilterPanel;
import Vue.Components.CustomButton;
import Vue.Components.DataTable;
import Vue.Components.DataTable.StudentRow;
import Vue.Components.LabelCustom;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MainDashboardView {
    private final BorderPane root;
    private final Button returnButton;
    private final ActionBar actionBar;
    private final DataTable dataTable;
    private final AdvencedFilterPanel filterPanel;

    public MainDashboardView() {
        root = new BorderPane();
        root.setPadding(new Insets(16));

        returnButton = new CustomButton("Retour", 110, 34, "#475569").build();
        Label title = new LabelCustom("Gestion des eleves", 22, "#1A1A1A", true).build();

        HBox titleBar = new HBox(12, returnButton, title);
        titleBar.setAlignment(Pos.CENTER_LEFT);

        actionBar = new ActionBar();
        VBox topSection = new VBox(10, titleBar, actionBar.build());

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
            System.out.println("Action test: Ajouter un eleve")
        );

        actionBar.getSearchField().setOnAction(event ->
            dataTable.searchRows(actionBar.getSearchField().getText())
        );
        actionBar.getSearchField().textProperty().addListener((obs, oldValue, newValue) ->
            dataTable.searchRows(newValue)
        );

        actionBar.getStatisticsButton().setOnAction(event ->
            System.out.println("Action test: Ouvrir statistiques")
        );

        actionBar.getImportButton().setOnAction(event ->
            System.out.println("Action test: Importer des donnees")
        );

        actionBar.getExportButton().setOnAction(event ->
            System.out.println("Action test: Exporter des donnees")
        );
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
