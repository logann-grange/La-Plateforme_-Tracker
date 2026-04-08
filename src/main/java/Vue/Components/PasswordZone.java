package Vue.Components;

import javafx.scene.control.PasswordField;
import java.util.regex.Pattern;

public class PasswordZone {
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#?([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$");

    private final double width;
    private final double height;
    private final String placeholder;
    private final String backgroundColor;
    private final String textColor;
    private final String borderColor;

    public PasswordZone(double width, double height, String placeholder) {
        this(width, height, placeholder, "#FFFFFF", "#1A1A1A", "#CFCFCF");
    }

    public PasswordZone(
        double width,
        double height,
        String placeholder,
        String backgroundColor,
        String textColor,
        String borderColor
    ) {
        this.width = width;
        this.height = height;
        this.placeholder = placeholder == null ? "" : placeholder;
        this.backgroundColor = normalizeHexColor(backgroundColor);
        this.textColor = normalizeHexColor(textColor);
        this.borderColor = normalizeHexColor(borderColor);
    }

    public PasswordField build() {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(placeholder);
        passwordField.setPrefSize(width, height);
        passwordField.setStyle(
            "-fx-background-color: " + backgroundColor + ";"
                + "-fx-text-fill: " + textColor + ";"
                + "-fx-prompt-text-fill: #8A8A8A;"
                + "-fx-border-color: " + borderColor + ";"
                + "-fx-border-radius: 8;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 8 10 8 10;"
        );
        return passwordField;
    }

    private static String normalizeHexColor(String rawColor) {
        if (rawColor == null) {
            throw new IllegalArgumentException("La couleur ne peut pas etre nulle.");
        }

        String trimmed = rawColor.trim();
        if (!HEX_COLOR_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("Couleur hex invalide. Formats attendus: #RRGGBB ou #RRGGBBAA");
        }
        return trimmed.startsWith("#") ? trimmed : "#" + trimmed;
    }
}
