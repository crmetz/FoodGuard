package com.app.foodguard.controller;

import com.app.foodguard.controller.DTO.DesperdicioDTO;
import com.app.foodguard.service.DesperdicioService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DesperdicioController {

    @FXML private TableView<DesperdicioDTO> tabelaDesperdicios;
    @FXML private TableColumn<DesperdicioDTO, Integer> colId;
    @FXML private TableColumn<DesperdicioDTO, String> colLoteId;
    @FXML private TableColumn<DesperdicioDTO, String> colNomeAlimento;
    @FXML private TableColumn<DesperdicioDTO, String> colQuantidade;
    @FXML private TableColumn<DesperdicioDTO, String> colUnidadeMedida;
    @FXML private TableColumn<DesperdicioDTO, String> colData;
    @FXML private TableColumn<DesperdicioDTO, String> colMotivo;
    @FXML private TableColumn<DesperdicioDTO, String> colObservacoes;
    @FXML private TableColumn<DesperdicioDTO, Void> colAcoes;

    private DesperdicioService DesperdicioService;
    private ObservableList<DesperdicioDTO> desperdicios;

    public void initialize() {
        DesperdicioService = new DesperdicioService();
        desperdicios = FXCollections.observableArrayList(DesperdicioService.getAllDesperdicios());

        tabelaDesperdicios.setItems(desperdicios);
        tabelaDesperdicios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLoteId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getLoteId())));
        colNomeAlimento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAlimento()));
        colQuantidade.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getQuantidade())));
        colUnidadeMedida.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUnidadeMedida()));
        colData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getData().toString()));
        colMotivo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMotivo()));
        colObservacoes.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getObservacao()));

        adicionarBotaoAcoes();
    }

    private void adicionarBotaoAcoes() {
        colAcoes.setCellFactory(coluna -> new TableCell<>() {
            private final Button btn = new Button("⋮");

            {
                btn.setOnAction(e -> {
                    DesperdicioDTO desperdicio = getTableView().getItems().get(getIndex());

                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem excluir = new MenuItem("Excluir");

                    excluir.setOnAction(ev -> {
                        DesperdicioService.removeDesperdicio(desperdicio);
                        desperdicios.remove(desperdicio);
                    });

                    contextMenu.getItems().addAll(excluir);
                    contextMenu.show(btn, Side.BOTTOM, 0, 0);
                });
            }

            @Override
            protected void updateItem(Void item, boolean vazio) {
                super.updateItem(item, vazio);
                if (vazio) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    @FXML
    private void abrirModalAdicionarDesperdicio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/desperdicio/desperdicio-modal-view.fxml"));
            Parent root = loader.load();

            DesperdicioModalController controller = loader.getController();
            controller.setDesperdicioService(DesperdicioService);
            controller.setDesperdiciosControllerList(desperdicios);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Adicionar Desperdicio");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
