package Vue.Pages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import Vue.JavaFxTestBase;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class LoginViewTest extends JavaFxTestBase {

    @Test
    void shouldExposeMainControls() {
        LoginView view = new LoginView();

        assertNotNull(view.getRoot());
        assertNotNull(view.getUsernameField());
        assertNotNull(view.getPasswordField());
        assertNotNull(view.getLoginButton());
        assertNotNull(view.getReturnButton());
        assertEquals("Se connecter", view.getLoginButton().getText());
        assertEquals("Retour", view.getReturnButton().getText());
    }

    @Test
    void shouldToggleErrorLabelVisibility() {
        LoginView view = new LoginView();

        view.showError("Erreur test");
        Label errorLabel = findLabel(view.getRoot(), "Erreur test");
        assertNotNull(errorLabel);
        assertTrue(errorLabel.isVisible());

        view.clearError();
        assertEquals("", errorLabel.getText());
        assertFalse(errorLabel.isVisible());
    }

    private Label findLabel(Pane root, String text) {
        for (Node node : root.getChildrenUnmodifiable()) {
            if (node instanceof Label label && text.equals(label.getText())) {
                return label;
            }
            if (node instanceof Pane childPane) {
                Label found = findLabel(childPane, text);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}
