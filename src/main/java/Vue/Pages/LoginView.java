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

public class LoginView {

	private final BorderPane root;
	private final TextField usernameField;
	private final PasswordField passwordField;
	private final Button loginButton;
    private final Button returnButton;
	private final Label errorLabel;

	public LoginView() {
		Label title = new LabelCustom("Connexion", 24, "#1A1A1A", true).build();

		usernameField = new TextZone(250, 30, "Email").build();
		passwordField = new PasswordZone(250, 30, "Mot de passe").build();
		loginButton = new CustomButton("Se connecter", 110, 30, "#10d551").build();
        returnButton = new CustomButton("Retour", 110, 30, "#10d551").build();
		usernameField.setMaxWidth(Region.USE_PREF_SIZE);
		passwordField.setMaxWidth(Region.USE_PREF_SIZE);
		loginButton.setMaxWidth(Region.USE_PREF_SIZE);
		returnButton.setMaxWidth(Region.USE_PREF_SIZE);

		errorLabel = new LabelCustom("", 12, "#D92D20", false).build();
		errorLabel.setVisible(false);

		VBox formBox = new VBox(12);
		formBox.setAlignment(Pos.CENTER);
		formBox.setFillWidth(false);
		formBox.getChildren().addAll(title, usernameField, passwordField, loginButton, errorLabel);

		HBox topBar = new HBox(returnButton);
		topBar.setAlignment(Pos.TOP_LEFT);

		root = new BorderPane();
		root.setPadding(new Insets(24));
		root.setTop(topBar);
		root.setCenter(formBox);
	}

	public BorderPane getRoot() {
		return root;
	}

	public Button getReturnButton() {
		return returnButton;
	}

	public TextField getUsernameField() {
		return usernameField;
	}

	public PasswordField getPasswordField() {
		return passwordField;
	}

	public Button getLoginButton() {
		return loginButton;
	}

	public void showError(String message) {
		errorLabel.setText(message == null ? "Erreur de connexion." : message);
		errorLabel.setVisible(true);
	}

	public void clearError() {
		errorLabel.setText("");
		errorLabel.setVisible(false);
	}
}
