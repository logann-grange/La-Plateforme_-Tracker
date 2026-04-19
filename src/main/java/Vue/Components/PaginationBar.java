package Vue.Components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

// Composant de pagination pour naviguer entre les pages du tableau.
public class PaginationBar {
    private final int currentPage;
    private final int totalPages;
    private final double width;
    private final double height;

    private Button prevButton;
    private Button nextButton;
    private Label pageLabel;

    public PaginationBar(int currentPage, int totalPages) {
        this(currentPage, totalPages, 400, 40);
    }

    public PaginationBar(int currentPage, int totalPages, double width, double height) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.width = width;
        this.height = height;
    }

    public HBox build() {
        HBox container = new HBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPrefSize(width, height);
        container.setStyle(
            "-fx-padding: 8 10;"
                + "-fx-background-color: #DCEBFF;"
                + "-fx-border-color: #A4C4EA;"
                + "-fx-border-radius: 10;"
                + "-fx-background-radius: 10;"
        );

        prevButton = new CustomButton("< Precedent", 120, 32, "#1D4ED8").build();
        prevButton.setDisable(currentPage <= 1);

        pageLabel = new LabelCustom("Page " + currentPage + " / " + totalPages, 14, "#1E3A8A", true).build();

        nextButton = new CustomButton("Suivant >", 120, 32, "#1D4ED8").build();
        nextButton.setDisable(currentPage >= totalPages);

        container.getChildren().addAll(prevButton, pageLabel, nextButton);
        return container;
    }

    public Button getPrevButton() {
        return prevButton;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public Label getPageLabel() {
        return pageLabel;
    }
}
