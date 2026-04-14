import Controller.LoginController;
import Controller.registerController;
import Model.LoginModel;
import Vue.Components.Windows;
import Vue.Pages.AuthChoiceView;
import Vue.Pages.LoginView;
import Vue.Pages.MainDashboardView;
import Vue.Pages.Register;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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

            new registerController(view, new LoginModel(), scene, authChoiceView);

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

            new LoginController(view, new LoginModel(), scene, new MainDashboardView());

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
