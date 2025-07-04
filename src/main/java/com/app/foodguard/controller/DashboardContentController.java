package com.app.foodguard.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardContentController {
    @FXML private Label greetingLabel;

    @FXML
    public void initialize() {
        // Exemplo: Carregar nome do usuário (substitua com lógica real)
        greetingLabel.setText("Olá, " + "Nome do Usuário");
    }

}
