package com.app.foodguard.controller;

import com.app.foodguard.model.Notification;
import com.app.foodguard.model.Lote;
import com.app.foodguard.service.NotificationService;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.util.List;

public class NotificationDropdownController {
    @FXML
    private VBox unreadMessagesContainer;

    @FXML
    private VBox readMessagesContainer;

    private NotificationService notificationService = new NotificationService();

    public void carregarNotificacoes(List<Lote> lotes) {
        List<Notification> notificacoes = notificationService.verificarValidade(lotes);

        for (Notification notificacao : notificacoes) {
            Label label = new Label(notificacao.getMensagem());
            label.getStyleClass().add("message-label");

            if (notificacao.isLido()) {
                readMessagesContainer.getChildren().add(label);
            } else {
                unreadMessagesContainer.getChildren().add(label);
            }
        }
    }
}