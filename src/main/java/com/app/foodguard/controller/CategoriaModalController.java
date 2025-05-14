package com.app.foodguard.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import com.app.foodguard.model.Categoria;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CategoriaModalController {

    @FXML
    private TextField txtDescricao;

    @FXML
    private ToggleButton toggleAtivo;

    private Categoria novaCategoria;

    public Categoria getNovaCategoria() {
        return novaCategoria;
    }

    @FXML
    private void atualizarToggle() {
        toggleAtivo.setText(toggleAtivo.isSelected() ? "Sim" : "Não");
    }

    @FXML
    private void salvarCategoria() {
        String descricao = txtDescricao.getText().trim();
        String ativo = toggleAtivo.isSelected() ? "Sim" : "Não";

        if (!descricao.isEmpty()) {
            novaCategoria = new Categoria(0, descricao, ativo); // ID será gerado no CategoriaController
        } else {
            // Exibe um alerta se a descrição estiver vazia
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("A descrição não pode estar vazia.");
            alert.showAndWait();
        }

        fecharJanela();
    }

    @FXML
    private void cancelar() {
        novaCategoria = null;
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) txtDescricao.getScene().getWindow();
        stage.close();
    }
}
