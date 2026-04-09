package Vue.Pages;

import Vue.Components.CustomButton;
import Vue.Components.LabelCustom;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AuthChoiceView {
    private final VBox root;
    private final Label title;
    private final Button loginButton;
    private final Button registerButton;

    public AuthChoiceView() {
        title = new LabelCustom("Bienvenue !", 24, "#1A1A1A", true).build();
        loginButton = new CustomButton("Se connecter", 150, 40, "#10d551").build();
        registerButton = new CustomButton("S'inscrire", 150, 40, "#10d551").build();
        loginButton.setMaxWidth(Region.USE_PREF_SIZE);
        registerButton.setMaxWidth(Region.USE_PREF_SIZE);
        root = new VBox(16);
        root.setAlignment(Pos.CENTER);
        root.setFillWidth(false);
        root.setPadding(new Insets(24));
        root.getChildren().addAll(title, loginButton, registerButton);
    }

    public VBox getRoot() {
        return root;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getRegisterButton() {
        return registerButton;

    }
    
}
