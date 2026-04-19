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

public class RegisterViewTest extends JavaFxTestBase {

    @Test
    void shouldExposeRegisterFormControls() {
        Register view = new Register();

        assertNotNull(view.getRoot());
        assertNotNull(view.getEmailField());
        assertNotNull(view.getPasswordField());
        assertNotNull(view.getConfirmPasswordField());
        assertNotNull(view.getFirstNameField());
        assertNotNull(view.getLastNameField());
        assertNotNull(view.getRegisterButton());
        assertNotNull(view.getReturnButton());
        assertEquals("S'inscrire", view.getRegisterButton().getText());
        assertEquals("Retour", view.getReturnButton().getText());
    }

    @Test
    void shouldToggleErrorLabelVisibility() {
        Register view = new Register();

        view.showError("Erreur inscription");
        Label errorLabel = findLabel(view.getRoot(), "Erreur inscription");
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
