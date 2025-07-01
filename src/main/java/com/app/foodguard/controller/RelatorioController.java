package com.app.foodguard.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class RelatorioController {

    @FXML
    private void abrirRelatorioMovimentacoes(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/relatorio/relatorio-movimentacoes-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Relatório de Movimentações por Lote");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}