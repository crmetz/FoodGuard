package com.app.foodguard.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.List;
import com.app.foodguard.model.Lote;

public class DashboardController {

    @FXML private StackPane dynamicContent;

    private DashboardContentController dashboardContentController;
    private List<Lote> lotesPendentes;

    @FXML
    public void initialize() {
        loadDashboardContent();
    }

    @FXML
    private void loadDashboardContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/dashboard/dashboard-content-view.fxml"));
            Parent dashboardView = loader.load();

            dashboardContentController = loader.getController();
            dynamicContent.getChildren().setAll(dashboardView);

            if (lotesPendentes != null) {
                dashboardContentController.carregarNotificacoes(lotesPendentes);
                lotesPendentes = null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Métodos para carregar as telas
    @FXML
    private void loadAlimentos() {
        loadView("/com/app/foodguard/alimento/alimento-view.fxml");
    }

    @FXML
    private void loadCategorias() {
        loadView("/com/app/foodguard/categoria/categoria-view.fxml");
    }

    @FXML
    private void loadEstoque() {
        loadView("/com/app/foodguard/lote/lote-view.fxml");
    }

    @FXML
    private void loadReceita() {
        loadView("/com/app/foodguard/receita/receita-view.fxml");
    }

    @FXML
    private void loadDesperdicios() {
        loadView("/com/app/foodguard/desperdicio/desperdicio-view.fxml");
    }

    @FXML
    private void loadDoacoes() {
        loadView("/com/app/foodguard/Estoque/estoque-view.fxml");
    }

    @FXML
    private void loadOngs() {
        loadView("/com/app/foodguard/ongs/ongs-view.fxml");
    }

    @FXML
    private void loadRelatorios() {
        loadView("/com/app/foodguard/relatorio/relatorio-view.fxml");
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
        loadView("/com/app/foodguard/usuario/usuario-view.fxml");
    }
}
