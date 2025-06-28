package com.app.foodguard.controller;

import com.app.foodguard.model.Lote;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Popup;

import java.util.List;

public class DashboardContentController {

    @FXML private Button notificationButton;

    private Popup notificationPopup;
    private NotificationDropdownController notificationDropdownController;
    private List<Lote> lotesPendentes;

    @FXML
    private void initialize() {
        notificationButton.setOnAction(event -> toggleNotificationDropdown());
    }

    private void toggleNotificationDropdown() {
        if (notificationPopup == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/notification/notification-dropdown-view.fxml"));
                Parent dropdownContent = loader.load();

                notificationDropdownController = loader.getController();

                if (lotesPendentes != null) {
                    notificationDropdownController.carregarNotificacoes(lotesPendentes);
                    lotesPendentes = null;
                }

                notificationPopup = new Popup();
                notificationPopup.getContent().add(dropdownContent);
                notificationPopup.setAutoHide(true);

                notificationPopup.show(
                        notificationButton,
                        notificationButton.getScene().getWindow().getX() + notificationButton.localToScene(0, 0).getX() + notificationButton.getScene().getX() - 70,
                        notificationButton.getScene().getWindow().getY() + notificationButton.localToScene(0, 0).getY() + notificationButton.getScene().getY() + notificationButton.getHeight() + 5
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            notificationPopup.hide();
            notificationPopup = null;
        }
    }

    public void carregarNotificacoes(List<Lote> lotes) {
        if (notificationDropdownController != null) {
            notificationDropdownController.carregarNotificacoes(lotes);
        } else {
            this.lotesPendentes = lotes; // guarda até o popup ser aberto
        }
    }
}
