package Vue.Pages;

import Vue.Components.CustomButton;
import Vue.Components.LabelCustom;
import Vue.Components.PasswordZone;
import Vue.Components.TextZone;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Register {
    private final BorderPane root;
    private final Label title;
    private final Button registerButton;
    private final Button returnButton;
    private final Label errorLabel;
    private final TextField emailField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final TextField firstNameField;
    private final TextField lastNameField;

    public Register() {
        title = new LabelCustom("Inscription", 24, "#1A1A1A", true).build();
        passwordField = new PasswordZone(250, 30, "Mot de passe").build();
        confirmPasswordField = new PasswordZone(250, 30, "Confirmer le mot de passe").build();
        firstNameField = new TextZone(250, 30, "Prénom").build();
        lastNameField = new TextZone(250, 30, "Nom").build();
        emailField = new TextZone(250, 30, "Email").build();
        registerButton = new CustomButton("S'inscrire", 150, 40, "#10d551").build();
        returnButton = new CustomButton("Retour", 150, 40, "#10d551").build();
        errorLabel = new LabelCustom("", 12, "#D92D20", false).build();
        registerButton.setMaxWidth(Region.USE_PREF_SIZE);
        returnButton.setMaxWidth(Region.USE_PREF_SIZE);
        passwordField.setMaxWidth(Region.USE_PREF_SIZE);
        confirmPasswordField.setMaxWidth(Region.USE_PREF_SIZE);
        firstNameField.setMaxWidth(Region.USE_PREF_SIZE);
        lastNameField.setMaxWidth(Region.USE_PREF_SIZE);
        emailField.setMaxWidth(Region.USE_PREF_SIZE);
        
        HBox topBar = new HBox(returnButton);
		topBar.setAlignment(Pos.TOP_LEFT);

        VBox formBox = new VBox(16);
        formBox.setAlignment(Pos.CENTER);
        formBox.setFillWidth(false);
        formBox.getChildren().addAll(title, emailField, passwordField, confirmPasswordField, firstNameField, lastNameField, registerButton, errorLabel);

        root = new BorderPane();
        root.setPadding(new Insets(24));
        root.setTop(topBar);
        root.setCenter(formBox);
        errorLabel.setVisible(false);
    }

    public BorderPane getRoot() {
        return root;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public PasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public TextField getFirstNameField() {
        return firstNameField;
    }

    public TextField getLastNameField() {
        return lastNameField;
    }

    public void showError(String message) {
        errorLabel.setText(message == null ? "Erreur d'inscription." : message);
        errorLabel.setVisible(true);
    }

    public void clearError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }
    
}
