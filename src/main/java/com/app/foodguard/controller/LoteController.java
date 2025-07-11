package com.app.foodguard.controller;

import com.app.foodguard.model.Lote;
import com.app.foodguard.service.AlimentoService;
import com.app.foodguard.service.LoteService;
import com.app.foodguard.utils.TableViewUtils;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class LoteController {

    @FXML private ComboBox<String> comboAlimentos;
    @FXML private TableView<Lote> tabelaLotes;
    @FXML private TableColumn<Lote, String> colCodigo;
    @FXML private TableColumn<Lote, Float> colQtdInicial;
    @FXML private TableColumn<Lote, Float> colQtdAtual;
    @FXML private TableColumn<Lote, String> colDataValidade;
    @FXML private TableColumn<Lote, String> colDataEntrada;
    @FXML private TableColumn<Lote, Void> colAcoes;

    private final LoteService loteService = new LoteService();
    private ObservableList<Lote> lotes;

    @FXML
    public void initialize() {
        configurarTabela();
        carregarComboAlimentos();
        configurarListeners();
    }

    private void configurarTabela() {
        lotes = FXCollections.observableArrayList();
        tabelaLotes.setItems(lotes);
        tabelaLotes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        colCodigo.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCodigo()));
        colCodigo.setMaxWidth(1f * Integer.MAX_VALUE);
        colQtdInicial.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQtdInicial()));
        colQtdAtual.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQtdAtual()));
        colDataValidade.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDataValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        ));
        colDataEntrada.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDataEntrada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        ));


        TableViewUtils.alinharColunaAEsquerda(colQtdInicial);
        TableViewUtils.alinharColunaAEsquerda(colQtdAtual);
        TableViewUtils.alinharColunaAEsquerda(colCodigo);
        TableViewUtils.alinharColunaAEsquerda(colDataValidade);
        TableViewUtils.alinharColunaAEsquerda(colDataEntrada);


        adicionarBotaoAcoes();
    }

    private void carregarComboAlimentos() {
        comboAlimentos.setItems(FXCollections.observableArrayList(
                new AlimentoService().getAllFoods().stream()
                        .map(a -> a.getId() + " - " + a.getNome())
                        .collect(Collectors.toList())
        ));
    }

    private void configurarListeners() {
        comboAlimentos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                int alimentoId = Integer.parseInt(newVal.split("-")[0].trim());
                atualizarTabelaLotes(alimentoId);
                tabelaLotes.setVisible(true);
            }
        });
    }

    private void atualizarTabelaLotes(int alimentoId) {
        lotes.setAll(loteService.getLotesPorAlimento(alimentoId));
    }

    private void adicionarBotaoAcoes() {
        colAcoes.setCellFactory(coluna -> new TableCell<>() {
            private final Button btn = new Button("⋮");

            {
                btn.setOnAction(e -> {
                    Lote lote = getTableView().getItems().get(getIndex());

                    ContextMenu menu = new ContextMenu();
                    MenuItem editar = new MenuItem("Editar");
                    MenuItem excluir = new MenuItem("Excluir");

                    editar.setOnAction(ev -> abrirModalEditarLote(lote));
                    excluir.setOnAction(ev -> {
                        loteService.removeLote(lote.getId());
                        lotes.remove(lote);
                    });

                    menu.getItems().addAll(editar, excluir);
                    menu.show(btn, Side.BOTTOM, 0, 0);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });
    }

    @FXML
    private void abrirModalAdicionarLote() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/lote/lote-modal-view.fxml"));
            Parent root = loader.load();

            LoteModalController controller = loader.getController();
            controller.setLotes(lotes);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Novo Lote");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirModalEditarLote(Lote lote) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/lote/lote-modal-view.fxml"));
            Parent root = loader.load();

            LoteModalController controller = loader.getController();
            controller.setLoteExistente(lote);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Editar Lote");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            atualizarTabelaLotes(lote.getAlimentoId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}