package Vue.Components;

import javafx.scene.control.Alert;

// Fenetre d'alerte simple pour afficher des messages utilisateur.
public class AlertBox {
    private final String title;
    private final String headerText;
    private final String contentText;

    public AlertBox(String title, String headerText, String contentText) {
        this.title = title;
        this.headerText = headerText;
        this.contentText = contentText;
    }

    public Alert build() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert;
    }
}
