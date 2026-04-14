package Vue.Components;

import javafx.scene.control.Button;

public class CustomButton {
    private final String text;
    private final double width;
    private final double height;
    private final String backgroundColor;
    private final String textColor;

    public CustomButton(String text, double width, double height, String backgroundColor) {
        this(text, width, height, backgroundColor, "#FFFFFF");
    }

    public CustomButton(String text, double width, double height, String backgroundColor, String textColor) {
        this.text = text;
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    public Button build() {
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.setStyle(
            "-fx-background-color: " + backgroundColor + ";"
                + "-fx-text-fill: " + textColor + ";"
                + "-fx-font-size: 13;"
                + "-fx-font-weight: 700;"
                + "-fx-border-color: transparent;"
                + "-fx-border-radius: 10;"
                + "-fx-background-radius: 10;"
                + "-fx-padding: 8 14;"
                + "-fx-cursor: hand;"
                + "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.12), 10, 0.2, 0, 2);"
        );
        return button;
    }
}

