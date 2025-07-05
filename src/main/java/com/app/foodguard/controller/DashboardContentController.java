package com.app.foodguard.controller;

import com.app.foodguard.model.Lote;
import com.app.foodguard.service.LoteService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import java.util.List;

public class DashboardContentController {
    @FXML private Label greetingLabel;
    @FXML private Button notificationMainButton;

    private Popup notificationPopup;
    private NotificationDropdownController notificationDropdownController;

    @FXML
    public void initialize() {
        greetingLabel.setText("Olá, " + "Nome do Usuário");

        notificationMainButton.setOnAction(event -> {
            toggleNotificationDropdown();
        });
    }

    private void toggleNotificationDropdown() {

        if (notificationPopup != null && notificationPopup.isShowing()) {
            notificationPopup.hide();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/notification/notification-dropdown-view.fxml"));
            Parent dropdownContent = loader.load();

            notificationDropdownController = loader.getController();


            List<Lote> lotes = new LoteService().getAllLotes();

            notificationDropdownController.carregarNotificacoes(lotes);


            notificationPopup = new Popup();
            notificationPopup.getContent().add(dropdownContent);
            notificationPopup.setAutoHide(true);

            double x = notificationMainButton.localToScreen(notificationMainButton.getBoundsInLocal()).getMinX() - 20;
            double y = notificationMainButton.localToScreen(notificationMainButton.getBoundsInLocal()).getMaxY() + 5;

            notificationPopup.show(notificationMainButton, x, y);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void carregarNotificacoes(List<Lote> lotes) {
        if (notificationDropdownController != null) {
            notificationDropdownController.carregarNotificacoes(lotes);
        }
    }
}
