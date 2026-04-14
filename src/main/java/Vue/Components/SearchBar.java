package Vue.Components;

import javafx.scene.control.TextField;

public class SearchBar {
    private final String placeholder;
    private final double width;
    private final double height;
    private final String backgroundColor;
    private final String textColor;
    public SearchBar(String placeholder) {
        this(placeholder, 300, 40, "#FFFFFF", "#1A1A1A");
    }

    public SearchBar(String placeholder, double width, double height, String backgroundColor, String textColor) {
        this.placeholder = placeholder;
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    public TextField build() {
        TextField searchField = new TextZone(
            width,
            height,
            placeholder,
            backgroundColor,
            textColor,
            "#CFCFCF"
        ).build();

        searchField.setStyle(searchField.getStyle() + "-fx-font-size: 13;");
        return searchField;
    }
    
}
