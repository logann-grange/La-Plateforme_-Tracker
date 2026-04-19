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

// Vue d'inscription pour creer un nouveau compte utilisateur.
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
        title = new LabelCustom("Inscription", 28, "#0F172A", true).build();
        passwordField = new PasswordZone(290, 38, "Mot de passe", "#FFFFFF", "#0F172A", "#CBD5E1").build();
        confirmPasswordField = new PasswordZone(290, 38, "Confirmer le mot de passe", "#FFFFFF", "#0F172A", "#CBD5E1").build();
        firstNameField = new TextZone(290, 38, "Prénom", "#FFFFFF", "#0F172A", "#CBD5E1").build();
        lastNameField = new TextZone(290, 38, "Nom", "#FFFFFF", "#0F172A", "#CBD5E1").build();
        emailField = new TextZone(290, 38, "Email", "#FFFFFF", "#0F172A", "#CBD5E1").build();
        registerButton = new CustomButton("S'inscrire", 170, 40, "#0F766E").build();
        returnButton = new CustomButton("Retour", 120, 36, "#334155").build();
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
        formBox.setPadding(new Insets(22));
        formBox.setStyle(
            "-fx-background-color: rgba(255,255,255,0.88);"
                + "-fx-border-color: #DDE7F3;"
                + "-fx-border-radius: 14;"
                + "-fx-background-radius: 14;"
                + "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.12), 20, 0.15, 0, 6);"
        );
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
