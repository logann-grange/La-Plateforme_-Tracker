package Vue.Components;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class AdvencedFilterPanel {
    private final String[] filterOptions;
    private final String backgroundColor;
    private final String textColor;
    private final double width;
    private final double height;

    private ComboBox<String> sortByMenu;
    private ComboBox<String> sortOrderMenu;
    private Button applyButton;
    private final List<CheckBox> optionCheckBoxes;

    public AdvencedFilterPanel(String[] filterOptions) {
        this(filterOptions, "#FFFFFF", "#1A1A1A", 300, 200);
    }

    public AdvencedFilterPanel(String[] filterOptions, String backgroundColor, String textColor, double width, double height) {
        this.filterOptions = filterOptions == null ? new String[0] : filterOptions;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.width = width;
        this.height = height;
        this.optionCheckBoxes = new ArrayList<>();
    }

    public VBox build() {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.TOP_LEFT);
        panel.setPrefSize(width, height);
        panel.setStyle(
            "-fx-background-color: " + backgroundColor + ";"
                + "-fx-border-color: #CFCFCF;"
                + "-fx-border-radius: 8;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 15;"
        );

        optionCheckBoxes.clear();

        for (String option : filterOptions) {
            CheckBox checkBox = new CheckBox(option);
            checkBox.setStyle(
                "-fx-text-fill: " + textColor + ";"
                    + "-fx-font-size: 13;"
            );
            optionCheckBoxes.add(checkBox);
            panel.getChildren().add(checkBox);
        }

        panel.getChildren().add(new LabelCustom("Tri par", 13, textColor, true).build());
        sortByMenu = new dropDownMenu(
            new String[] {"Nom", "Prenom", "Age", "Moyenne"},
            "#FFFFFF",
            textColor,
            width - 30,
            36
        ).build();
        panel.getChildren().add(sortByMenu);

        panel.getChildren().add(new LabelCustom("Ordre", 13, textColor, true).build());
        sortOrderMenu = new dropDownMenu(
            new String[] {"Croissant", "Decroissant"},
            "#FFFFFF",
            textColor,
            width - 30,
            36
        ).build();
        panel.getChildren().add(sortOrderMenu);

        applyButton = new CustomButton("Appliquer", 120, 34, "#0A84FF").build();
        panel.getChildren().add(applyButton);

        return panel;
    }

    public String[] getFilterOptions() {
        return filterOptions;
    }

    public List<String> getSelectedOptions() {
        List<String> selectedOptions = new ArrayList<>();
        for (CheckBox checkBox : optionCheckBoxes) {
            if (checkBox.isSelected()) {
                selectedOptions.add(checkBox.getText());
            }
        }
        return selectedOptions;
    }

    public String getSelectedSortBy() {
        return sortByMenu == null ? null : sortByMenu.getValue();
    }

    public String getSelectedSortOrder() {
        return sortOrderMenu == null ? null : sortOrderMenu.getValue();
    }

    public Button getApplyButton() {
        return applyButton;
    }

    public void bindToDataTable(DataTable dataTable) {
        if (dataTable == null || applyButton == null) {
            return;
        }

        applyButton.setOnAction(event -> {
            dataTable.applyFilterAndSort(
                getSelectedOptions(),
                getSelectedSortBy(),
                getSelectedSortOrder()
            );
        });
    }
    
}
