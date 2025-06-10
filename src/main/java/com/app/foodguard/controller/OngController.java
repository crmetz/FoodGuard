package com.app.foodguard.controller;

import com.app.foodguard.model.Ong;
import com.app.foodguard.service.OngService;
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

public class OngController {

    @FXML private TableView<Ong> tabelaOngs;
    @FXML private TableColumn<Ong, Integer> colId;
    @FXML private TableColumn<Ong, String> colNome;
    @FXML private TableColumn<Ong, String> colContato;
    @FXML private TableColumn<Ong, String> colEndereco;
    @FXML private TableColumn<Ong, Void> colAcoes;

    private OngService ongService;
    private ObservableList<Ong> ongs;

    public void initialize() {
        ongService = new OngService();
        ongs = FXCollections.observableArrayList(ongService.getAllOngs());
        tabelaOngs.setItems(ongs);
        tabelaOngs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        colContato.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContato()));
        colEndereco.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndereco()));

        adicionarBotaoAcoes();
    }

    private void adicionarBotaoAcoes() {
        colAcoes.setCellFactory(coluna -> new TableCell<>() {
            private final Button btn = new Button("⋮");

            {
                btn.setOnAction(e -> {
                    Ong ong = getTableView().getItems().get(getIndex());

                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem editar = new MenuItem("Editar");
                    MenuItem excluir = new MenuItem("Excluir");

                    editar.setOnAction(ev -> abrirModalEditar(ong));
                    excluir.setOnAction(ev -> {
                        ongService.removeOng(ong);
                        ongs.remove(ong);
                    });

                    contextMenu.getItems().addAll(editar, excluir);
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
    private void abrirModalAdicionarOng() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/ongs/ongs-modal-view.fxml"));
            Parent root = loader.load();

            OngModalController controller = loader.getController();
            controller.setOngService(ongService);
            controller.setOngs(ongs);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Adicionar ONG");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirModalEditar(Ong ong) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/ong/ong-modal-view.fxml"));
            Parent root = loader.load();

            OngModalController controller = loader.getController();
            controller.setOngExistente(ong);
            controller.setOngService(ongService);
            controller.setOngs(ongs);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Editar ONG");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}