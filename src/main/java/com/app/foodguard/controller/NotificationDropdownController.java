package com.app.foodguard.controller;

import com.app.foodguard.model.Lote;
import com.app.foodguard.model.Notification;
import com.app.foodguard.service.NotificationService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class NotificationDropdownController {
    public VBox notificationsContainer;

    private NotificationService notificationService = new NotificationService();

    public void carregarNotificacoes(List<Lote> lotes) {
        List<Notification> notificacoes = notificationService.verificarValidade(lotes);

        for (Notification notificacao : notificacoes) {
            Button notificationButton = new Button(notificacao.getMensagem());
            notificationButton.getStyleClass().add("notification-button");
            notificationButton.setOnAction(event -> abrirModal(notificacao));
            notificationsContainer.getChildren().add(notificationButton);
        }
    }

    private void abrirModal(Notification notificacao) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/notification/notification-modal-view.fxml"));
            VBox modalRoot = loader.load();

            NotificationModalController modalController = loader.getController();
            modalController.setNotification(notificacao);

            Stage modalStage = new Stage();
            modalStage.setScene(new Scene(modalRoot));
            modalStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}