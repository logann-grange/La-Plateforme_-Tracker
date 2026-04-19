package Vue.Pages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import Vue.JavaFxTestBase;
import javafx.scene.layout.VBox;

public class AuthChoiceViewTest extends JavaFxTestBase {

    @Test
    void shouldBuildAuthChoiceViewWithExpectedButtons() {
        AuthChoiceView view = new AuthChoiceView();

        assertNotNull(view.getRoot());
        assertNotNull(view.getLoginButton());
        assertNotNull(view.getRegisterButton());
        assertEquals("Se connecter", view.getLoginButton().getText());
        assertEquals("S'inscrire", view.getRegisterButton().getText());

        VBox root = view.getRoot();
        assertEquals(3, root.getChildren().size());
    }
}
