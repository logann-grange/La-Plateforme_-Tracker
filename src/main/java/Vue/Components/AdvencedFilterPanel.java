package Vue.Components;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

// Panneau de filtres avances applique au tableau des eleves.
public class AdvencedFilterPanel {
    private final String[] filterOptions;
    private final String backgroundColor;
    private final String textColor;
    private final double width;
    private final double height;

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
                + "-fx-border-color: #D8E2EE;"
                + "-fx-border-radius: 12;"
                + "-fx-background-radius: 12;"
                + "-fx-padding: 16;"
                + "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.08), 14, 0.1, 0, 4);"
        );

        optionCheckBoxes.clear();

        for (String option : filterOptions) {
            CheckBox checkBox = new CheckBox(option);
            checkBox.setStyle(
                "-fx-text-fill: " + textColor + ";"
                    + "-fx-font-size: 13;"
                    + "-fx-font-weight: 600;"
            );
            optionCheckBoxes.add(checkBox);
            panel.getChildren().add(checkBox);
        }

        applyButton = new CustomButton("Appliquer", 130, 36, "#0F766E").build();
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

    public Button getApplyButton() {
        return applyButton;
    }

    public void bindToDataTable(DataTable dataTable) {
        if (dataTable == null || applyButton == null) {
            return;
        }

        applyButton.setOnAction(event -> {
            dataTable.applyFilters(getSelectedOptions());
        });
    }
    
}
