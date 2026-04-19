package Vue.Pages;

import Vue.Components.CustomButton;
import Vue.Components.LabelCustom;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

// Vue d'accueil qui propose le choix entre connexion et inscription.
public class AuthChoiceView {
    private final VBox root;
    private final Label title;
    private final Button loginButton;
    private final Button registerButton;

    public AuthChoiceView() {
        // Elements principaux de l'ecran d'accueil d'authentification.
        title = new LabelCustom("Bienvenue !", 30, "#0F172A", true).build();
        loginButton = new CustomButton("Se connecter", 190, 44, "#0F766E").build();
        registerButton = new CustomButton("S'inscrire", 190, 44, "#0EA5E9").build();
        loginButton.setMaxWidth(Region.USE_PREF_SIZE);
        registerButton.setMaxWidth(Region.USE_PREF_SIZE);

        // Carte centrale qui contient le titre et les deux actions utilisateur.
        root = new VBox(18);
        root.setAlignment(Pos.CENTER);
        root.setFillWidth(false);
        root.setPadding(new Insets(28));

        // Style visuel de la carte (fond semi-transparent, bord arrondi, ombre).
        root.setStyle(
            "-fx-background-color: rgba(255,255,255,0.82);"
                + "-fx-border-color: #DDE7F3;"
                + "-fx-border-radius: 14;"
                + "-fx-background-radius: 14;"
                + "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.12), 20, 0.15, 0, 6);"
        );
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
