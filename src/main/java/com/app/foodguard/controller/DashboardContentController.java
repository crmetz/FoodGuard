package com.app.foodguard.controller;

import com.app.foodguard.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardContentController {
    @FXML private Label greetingLabel;

    @FXML
    public void initialize() {
        // Exemplo: Carregar nome do usuário (substitua com lógica real)

        String usuario =  new UsuarioService().getUsuario().getNome();

        greetingLabel.setText("Olá, " + usuario);
    }

}
