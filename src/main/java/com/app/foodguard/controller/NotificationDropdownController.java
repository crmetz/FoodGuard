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
import java.time.LocalDate;
import java.util.List;

public class NotificationDropdownController {
    public VBox notificationsContainer;

    private NotificationService notificationService = new NotificationService();

    public void carregarNotificacoes(List<Lote> lotes) {
        notificationsContainer.getChildren().clear();
        List<Notification> notificacoes = notificationService.verificarValidade(lotes);
        for (Notification notificacao : notificacoes) {
            Lote lote = notificacao.getLote();
            Button notificationButton = new Button("Lote: " + lote.getCodigo() + "\nData de Validade: " + lote.getDataValidade());
            notificationButton.getStyleClass().add("notification-button");
            notificationButton.setMaxWidth(Double.MAX_VALUE);
            notificationsContainer.getChildren().add(notificationButton);
        }
    }
}