package Controller;
import Vue.Pages.AuthChoiceView;
import Vue.Pages.Register;
import Model.loginModel;
import javafx.scene.Scene;

public class registerController {
    
    private Register vue;
    private loginModel model;
    private Scene scene;
    private AuthChoiceView authChoiceView;

    public registerController(Register vue, loginModel model, Scene scene, AuthChoiceView authChoiceView) {
        this.vue = vue;
        this.model = model;
        this.scene = scene;
        this.authChoiceView = authChoiceView;

        this.vue.getRegisterButton().setOnAction(e -> handleRegister());
    }

    
    private void handleRegister(){
        String email = vue.getEmailField().getText();
        String password = vue.getPasswordField().getText();
        String confirmPassword = vue.getConfirmPasswordField().getText();
        String firstName = vue.getFirstNameField().getText();
        String lastName = vue.getLastNameField().getText();
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty() ||
            firstName == null || firstName.trim().isEmpty() ||
            lastName == null || lastName.trim().isEmpty()) {
            vue.showError("Tous les champs sont requis.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            vue.showError("Les mots de passe ne correspondent pas.");
            return;
        }

        boolean created = model.createUser(firstName, lastName, email, password);
        if (!created) {
            String dbError = model.getLastErrorMessage();
            if (dbError == null || dbError.isBlank()) {
                vue.showError("Echec d'inscription: verifie la connexion PostgreSQL ou les doublons (email/prenom).");
            } else {
                vue.showError("Echec d'inscription: " + dbError);
            }
            return;
        }

        System.out.println("Utilisateur créé !");
        scene.setRoot(authChoiceView.getRoot());
    }
}