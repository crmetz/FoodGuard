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
    private List<Lote> lotesPendentes; // buffer, caso content ainda não esteja pronto

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

    public void carregarNotificacoes(List<Lote> lotes) {
        if (dashboardContentController != null) {
            dashboardContentController.carregarNotificacoes(lotes);
        } else {
            this.lotesPendentes = lotes;
        }
    }
}
