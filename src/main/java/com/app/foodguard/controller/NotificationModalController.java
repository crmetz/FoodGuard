package com.app.foodguard.controller;

import com.app.foodguard.model.Notification;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class NotificationModalController {
    @FXML
    private Label messageLabel;

    public void setNotification(Notification notificacao) {
        messageLabel.setText(notificacao.getMensagem());
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.close();
    }
}