package com.app.foodguard.controller;

import com.app.foodguard.model.Ong;
import com.app.foodguard.service.OngService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

@Setter
public class OngModalController {

    @FXML private TextField txtNome;
    @FXML private TextField txtContato;
    @FXML private TextField txtEndereco;

    private OngService ongService;
    private ObservableList<Ong> ongs;
    private Ong ongExistente;

    public void setOngExistente(Ong ong) {
        this.ongExistente = ong;
        preencherCampos(ong);
    }

    private void preencherCampos(Ong ong) {
        txtNome.setText(ong.getNome());
        txtContato.setText(ong.getContato());
        txtEndereco.setText(ong.getEndereco());
    }

    @FXML
    private void onSalvar() {
        try {
            String nome = txtNome.getText();
            String contato = txtContato.getText();
            String endereco = txtEndereco.getText();

            if (ongExistente != null) {
                // Atualizar ONG existente
                ongExistente.setNome(nome);
                ongExistente.setContato(contato);
                ongExistente.setEndereco(endereco);

                ongService.updateOng(ongExistente);
                tabelaAtualizar(); // força refresh
            } else {
                // Criar nova ONG
                Ong nova = new Ong();
                nova.setNome(nome);
                nova.setContato(contato);
                nova.setEndereco(endereco);

                ongService.addOng(nova);
                ongs.add(nova);
            }

            fecharJanela();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao salvar ONG", e.getMessage());
        }
    }

    private void tabelaAtualizar() {
        ongs.setAll(ongService.getAllOngs());
    }

    @FXML
    private void onCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}