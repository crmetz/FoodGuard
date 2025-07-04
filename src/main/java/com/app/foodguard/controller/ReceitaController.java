package com.app.foodguard.controller;

import com.app.foodguard.model.Receita;
import com.app.foodguard.service.ReceitaService;
import javafx.beans.property.SimpleIntegerProperty;
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

public class ReceitaController {

    @FXML private TableView<Receita> tabelaReceitas;
    @FXML private TableColumn<Receita, Integer> colId;
    @FXML private TableColumn<Receita, String> colNome;
    @FXML private TableColumn<Receita, Integer> colTempoPreparo;
    @FXML private TableColumn<Receita, Void> colAcoes;

    private ReceitaService receitaService;
    private ObservableList<Receita> receitas;

    public void initialize() {
        receitaService = new ReceitaService();
        receitas = FXCollections.observableArrayList(receitaService.getAllReceitas());

        tabelaReceitas.setItems(receitas);
        tabelaReceitas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        colTempoPreparo.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getTempoPreparo()).asObject());

        adicionarBotaoAcoes();
    }

    private void adicionarBotaoAcoes() {
        colAcoes.setCellFactory(coluna -> new TableCell<>() {
            private final Button btn = new Button("⋮");

            {
                btn.setOnAction(e -> {
                    Receita receita = getTableView().getItems().get(getIndex());

                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem editar = new MenuItem("Editar");
                    MenuItem excluir = new MenuItem("Excluir");
                    MenuItem executar = new MenuItem("Executar Receita");

                    editar.setOnAction(ev -> abrirModalEditar(receita));
                    excluir.setOnAction(ev -> {
                        receitaService.removeReceita(receita);
                        receitas.remove(receita);
                    });
                    executar.setOnAction(ev -> abrirModalExecutarReceita(receita)); // Novo método


                    contextMenu.getItems().addAll(editar, excluir, executar);
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
    private void abrirModalAdicionarReceita() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/receita/receita-modal-view.fxml"));
            Parent root = loader.load();

            ReceitaModalController controller = loader.getController();
            controller.setReceitaService(receitaService);
            controller.setReceitas(receitas);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Adicionar Receita");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirModalEditar(Receita receita) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/receita/receita-modal-view.fxml"));
            Parent root = loader.load();

            ReceitaModalController controller = loader.getController();
            controller.setReceitaService(receitaService);
            controller.setReceitaExistente(receita);
            controller.setReceitas(receitas);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Editar Receita");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirModalExecutarReceita(Receita receita) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/receita/receita-execucao-modal-view.fxml"));
            Parent root = loader.load();

            ReceitaExecucaoModalController controller = loader.getController();
            controller.setReceita(receita);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Executar Receita: " + receita.getNome());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}