import Vue.Components.Windows;
import Vue.Pages.AuthChoiceView;
import Vue.Pages.LoginView;
import Vue.Pages.MainDashboardView;
import Vue.Pages.Register;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        AuthChoiceView authChoiceView = new AuthChoiceView();
        Windows appWindow = new Windows(1280, 800, "#D7E8FF");
        authChoiceView.getRoot().setStyle(appWindow.getBackgroundStyle());

        Scene scene = new Scene(authChoiceView.getRoot(), appWindow.getWidth(), appWindow.getHeight());
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));

        authChoiceView.getRegisterButton().setOnAction(event -> {
            Register view = new Register();
            view.getRoot().setStyle(appWindow.getBackgroundStyle());

            view.getRegisterButton().setOnAction(registerEvent -> {
                String email = view.getEmailField().getText();
                String password = view.getPasswordField().getText();
                String confirmPassword = view.getConfirmPasswordField().getText();
                String firstName = view.getFirstNameField().getText();
                String lastName = view.getLastNameField().getText();
                System.out.println("Email saisi: " + email);
                System.out.println("Mot de passe saisi: " + password);
                System.out.println("Confirmation du mot de passe: " + confirmPassword);
                System.out.println("Prénom saisi: " + firstName);
                System.out.println("Nom saisi: " + lastName);
            });

            view.getReturnButton().setOnAction(returnEvent -> {
                scene.setRoot(authChoiceView.getRoot());
                stage.setTitle("Tracker Etudiant");
            });

            scene.setRoot(view.getRoot());
            stage.setTitle("Tracker Etudiant - Inscription");
        });

        authChoiceView.getLoginButton().setOnAction(event -> {
            LoginView view = new LoginView();
            view.getRoot().setStyle(appWindow.getBackgroundStyle());

            view.getLoginButton().setOnAction(loginEvent -> {
                String username = view.getUsernameField().getText();
                String password = view.getPasswordField().getText();
                System.out.println("Email saisi: " + username + " | Mot de passe saisi: " + password);

                MainDashboardView dashboardView = new MainDashboardView();
                dashboardView.getRoot().setStyle(appWindow.getBackgroundStyle());
                dashboardView.getReturnButton().setOnAction(backEvent -> {
                    scene.setRoot(authChoiceView.getRoot());
                    stage.setTitle("Tracker Etudiant");
                });

                scene.setRoot(dashboardView.getRoot());
                stage.setTitle("Tracker Etudiant - Tableau de bord");
            });

            view.getReturnButton().setOnAction(returnEvent -> {
                scene.setRoot(authChoiceView.getRoot());
                stage.setTitle("Tracker Etudiant");
            });

            scene.setRoot(view.getRoot());
            stage.setTitle("Tracker Etudiant - Connexion");
        });

        stage.setTitle("Tracker Etudiant");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
