package com.app.foodguard.controller;

import com.app.foodguard.model.Usuario;
import com.app.foodguard.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class UsuarioController {
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private Button btnSalvar;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    public void initialize() {
        // Carrega usuário existente se houver
        Usuario usuario = usuarioService.getUsuario();
        if (usuario != null) {
            txtNome.setText(usuario.getNome());
            txtEmail.setText(usuario.getEmail());
        }

        // Configura ação do botão salvar
        btnSalvar.setOnAction(event -> salvarUsuario());
    }

    private void salvarUsuario() {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();

        if (nome.isEmpty() || email.isEmpty()) {
            mostrarAlerta("Campos obrigatórios", "Preencha todos os campos");
            return;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            mostrarAlerta("Email inválido", "Digite um email válido");
            return;
        }

        // Cria e salva o usuário
        Usuario usuario = new Usuario(0, nome, email);
        usuarioService.saveUsuario(usuario);

        mostrarAlerta("Sucesso", "Usuário salvo com sucesso!", Alert.AlertType.INFORMATION);
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        mostrarAlerta(titulo, mensagem, Alert.AlertType.WARNING);
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}