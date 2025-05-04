package com.app.foodguard.controller;

import com.app.foodguard.utils.IconManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class DashboardController {

    @FXML private StackPane dynamicContent;
    @FXML private Label greetingLabel;
    @FXML private Button notificationButton;
    @FXML private Button profileButton;

    @FXML
    public void initialize() {
        // Exemplo: Carregar nome do usuário (substitua com lógica real)
        greetingLabel.setText("Olá, " + "Nome do Usuário");
        setupIcons();
    }

    private void setupIcons() {
        // Notification bell icon
        notificationButton.setGraphic(
                IconManager.getIcon(FontAwesomeIcon.BELL, "#2c3e50", 1.5)
        );

        // Profile user icon (in the sidebar)
        profileButton.setGraphic(
                IconManager.getIcon(FontAwesomeIcon.USER, "white", 1.2)
        );
        profileButton.setText("Perfil"); // Remove text if you only want the icon

    }

    // Métodos para carregar as telas
    @FXML
    private void loadAlimentos() {
        loadView("/com/app/foodguard/alimento/alimento-view.fxml");
    }

    @FXML
    private void loadEstoque() {
        loadView("/com/app/foodguard/Estoque/estoque-view.fxml");
    }

    @FXML
    private void loadReceita() {
        loadView("/com/app/foodguard/Estoque/estoque-view.fxml");
    }

    @FXML
    private void loadDesperdicios() {
        loadView("/com/app/foodguard/Estoque/estoque-view.fxml");
    }

    @FXML
    private void loadDoacoes() {
        loadView("/com/app/foodguard/Estoque/estoque-view.fxml");
    }

    @FXML
    private void loadOngs() {
        loadView("/com/app/foodguard/Estoque/estoque-view.fxml");
    }

    @FXML
    private void loadRelatorios() {
        loadView("/com/app/foodguard/Estoque/estoque-view.fxml");
    }


    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            dynamicContent.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadPerfil() {
        // Implemente a lógica para carregar a tela de perfil
    }
}