package com.app.foodguard.controller;

import com.app.foodguard.model.Doacao;
import com.app.foodguard.model.Movimentacao;
import com.app.foodguard.model.Ong;
import com.app.foodguard.service.DoacaoService;
import com.app.foodguard.service.MovimentacaoService;
import com.app.foodguard.service.OngService;
import javafx.beans.property.SimpleObjectProperty;
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

public class DoacaoController {

    @FXML private TableView<Doacao> tabelaDoacoes;
    @FXML private TableColumn<Doacao, Integer> colId;
    @FXML private TableColumn<Doacao, String> colNomeOng;
    @FXML private TableColumn<Doacao, String> colLoteid;
    @FXML private TableColumn<Doacao, Float> colQuantidade;
    @FXML private TableColumn<Doacao, Void> colAcoes;

    private DoacaoService doacaoService;
    private ObservableList<Doacao> doacoes;

    public void initialize() {
        doacaoService = new DoacaoService();
        doacoes = FXCollections.observableArrayList(doacaoService.getAllDoacao());
        tabelaDoacoes.setItems(doacoes);
        tabelaDoacoes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNomeOng.setCellValueFactory(data -> {
            Ong ong = new OngService().getOngById(data.getValue().getOngId());
            return new SimpleStringProperty(ong != null ? ong.getNome() : "N/A");
        });

        colLoteid.setCellValueFactory(data -> {
            Movimentacao movimentacao = new MovimentacaoService().getMovimentacaoById(data.getValue().getMovimentacaoId());
            return new SimpleStringProperty(movimentacao != null ? String.valueOf(movimentacao.getLoteId()) : "N/A");
        });

        colQuantidade.setCellValueFactory(data -> {
            Movimentacao movimentacao = new MovimentacaoService().getMovimentacaoById(data.getValue().getMovimentacaoId());
            return new SimpleObjectProperty<>(movimentacao != null ? movimentacao.getQuantidade() : 0f);
        });


        adicionarBotaoAcoes();
    }

    private void adicionarBotaoAcoes() {
        colAcoes.setCellFactory(coluna -> new TableCell<>() {
            private final Button btn = new Button("⋮");

            {
                btn.setOnAction(e -> {
                    Doacao doacao = getTableView().getItems().get(getIndex());

                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem editar = new MenuItem("Editar");
                    MenuItem excluir = new MenuItem("Excluir");

                    editar.setOnAction(ev -> abrirModalEditar(doacao));
                    excluir.setOnAction(ev -> {
                        doacaoService.removeDoacao(doacao);
                        doacoes.remove(doacao);
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
    private void abrirModalAdicionarDoacao() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/doacao/doacao-modal-view.fxml"));
            Parent root = loader.load();

            DoacaoModalController controller = loader.getController();
            controller.setDoacaoService(doacaoService);
            controller.setDoacoes(doacoes);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Adicionar Doação");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirModalEditar(Doacao doacao) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/doacao/doacao-modal-view.fxml"));
            Parent root = loader.load();

            DoacaoModalController controller = loader.getController();
            controller.setDoacaoExistente(doacao);
            controller.setDoacaoService(doacaoService);
            controller.setDoacoes(doacoes);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Editar Doação");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

