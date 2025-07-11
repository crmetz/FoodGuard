package com.app.foodguard.controller;

import com.app.foodguard.model.Lote;
import com.app.foodguard.service.LoteService;
import com.app.foodguard.model.Desperdicio;
import com.app.foodguard.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.io.FileReader;

public class DashboardContentController {
    @FXML private Label greetingLabel;
    @FXML private Button notificationMainButton;
    @FXML private TableView<Desperdicio> tabelaLotes;
    @FXML private TableColumn<Desperdicio, String> colAlimento;
    @FXML private TableColumn<Desperdicio, String> colQtdDes;
    @FXML private Label labelTotalDoacoes;

    private Popup notificationPopup;
    private NotificationDropdownController notificationDropdownController;

    @FXML
    public void initialize() {
        String usuario =  new UsuarioService().getUsuario().getNome();

        greetingLabel.setText("Olá, " + usuario);

        notificationMainButton.setOnAction(event -> {
            toggleNotificationDropdown();
        });

        carregarTabelaDesperdicio();

        carregarTotalDoacoes();
    }

    private void toggleNotificationDropdown() {
        System.out.println("clicado");
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
            System.out.println("chegou aqui?");

        } catch (Exception e) {
            System.out.println("deu pau");
            e.printStackTrace();
        }
    }

    public void carregarNotificacoes(List<Lote> lotes) {
        if (notificationDropdownController != null) {
            notificationDropdownController.carregarNotificacoes(lotes);
        }
    }

    private void carregarTabelaDesperdicio() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/csv/movimentacoes.csv"))) {

            ObservableList<Desperdicio> lista = FXCollections.observableArrayList();

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length >= 5 && "DESPERDICIO".equalsIgnoreCase(partes[3])) {
                    String lote = partes[1];
                    String quantidade = partes[4];

                    lista.add(new Desperdicio(0, 0, lote, quantidade));
                }
            }

            colAlimento.setCellValueFactory(new PropertyValueFactory<>("motivo"));
            colQtdDes.setCellValueFactory(new PropertyValueFactory<>("observacao"));
            tabelaLotes.setItems(lista);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregarTotalDoacoes() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/csv/movimentacoes.csv"))) {

            float totalDoacoes = 0;

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length >= 5 && "DOACAO".equalsIgnoreCase(partes[3])) {
                    float quantidade = Float.parseFloat(partes[4]);
                    totalDoacoes += quantidade;
                }
            }

            labelTotalDoacoes.setText(String.valueOf(totalDoacoes));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
