package Vue;

import java.util.regex.Pattern;

public class Windows {
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#?([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$");

    private double width;
    private double height;
    private String colors;

    public Windows(double width, double height, String colors) {
        this.width = width;
        this.height = height;
        this.colors = normalizeHexColor(colors);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public String getColors() {
        return colors;
    }

    public String getBackgroundStyle() {
        return "-fx-background-color: " + colors + ";";
    }

    private static String normalizeHexColor(String rawColor) {
        if (rawColor == null) {
            throw new IllegalArgumentException("La couleur ne peut pas etre nulle.");
        }

        String trimmed = rawColor.trim();
        if (!HEX_COLOR_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("Couleur hex invalide. Formats attendus: #RRGGBB ou #RRGGBBAA");
        }

        return trimmed.startsWith("#") ? trimmed.toUpperCase() : ("#" + trimmed.toUpperCase());
    }
}

