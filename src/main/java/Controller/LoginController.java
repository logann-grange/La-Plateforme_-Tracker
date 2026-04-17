package Controller;
import Model.LoginModel;
import Model.StudentModel;
import Vue.Pages.LoginView;
import Vue.Pages.MainDashboardView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {

    private LoginView vue;
    private LoginModel model;
    private Scene scene;
    private MainDashboardView dashboardView;

    public LoginController(LoginView vue, LoginModel model, Scene scene, MainDashboardView dashboardView) {
        this.vue = vue;
        this.model = model;
        this.scene = scene;
        this.dashboardView = dashboardView;

        this.vue.getLoginButton().setOnAction(e -> handleLogin());
    }

    private boolean validateInput(String mail, String password) {
        if (mail == null || mail.trim().isEmpty()) {
            vue.showError("Le champ email est requis.");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            vue.showError("Le champ mot de passe est requis.");
            return false;
        }
        return true;
    }

    private void handleLogin() {
        String mail = vue.getUsernameField().getText();
        String password = vue.getPasswordField().getText();

        if (!validateInput(mail, password)) {
            return;
        }

        String[] resultat = model.login(mail, password);

        if (resultat[0] != null) {
            new StudentController(dashboardView, new StudentModel(), scene);
            scene.setRoot(dashboardView.getRoot());
            if (scene.getWindow() instanceof Stage stage) {
                stage.setTitle("Tracker Etudiant");
            }
        } else {
            vue.showError("Mail ou mot de passe incorrect");
        }
    }
}
