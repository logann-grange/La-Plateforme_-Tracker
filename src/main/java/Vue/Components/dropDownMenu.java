package Vue.Components;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

public class dropDownMenu {
    private final String[] options;
    private final String backgroundColor;
    private final String textColor;
    private final double width;
    private final double height;

    public dropDownMenu(String[] options) {
        this(options, "#FFFFFF", "#1A1A1A", 220, 40);
    }

    public dropDownMenu(String[] options, String backgroundColor, String textColor) {
        this(options, backgroundColor, textColor, 220, 40);
    }

    public dropDownMenu(String[] options, String backgroundColor, String textColor, double width, double height) {
        this.options = options == null ? new String[0] : options;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.width = width;
        this.height = height;
    }

    public ComboBox<String> build() {
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(options));
        comboBox.setPrefSize(width, height);
        comboBox.setStyle(
            "-fx-background-color: " + backgroundColor + ";"
                + "-fx-text-fill: " + textColor + ";"
                + "-fx-border-color: #CBD5E1;"
                + "-fx-border-radius: 10;"
                + "-fx-background-radius: 10;"
                + "-fx-font-size: 13;"
                + "-fx-padding: 4 8;"
        );

        if (options.length > 0) {
            comboBox.setValue(options[0]);
        }

        return comboBox;
    }
}