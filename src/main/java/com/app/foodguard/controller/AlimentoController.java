package com.app.foodguard.controller;

import com.app.foodguard.model.Alimento;
import com.app.foodguard.model.Categoria;
import com.app.foodguard.service.AlimentoService;
import com.app.foodguard.service.CategoriaService;
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
import java.util.Map;
import java.util.stream.Collectors;

public class AlimentoController {

    @FXML private TableView<Alimento> tabelaAlimentos;
    @FXML private TableColumn<Alimento, Integer> colId;
    @FXML private TableColumn<Alimento, String> colNome;
    @FXML private TableColumn<Alimento, String> colDataValidade;
    @FXML private TableColumn<Alimento, String> colUnidadeMedida;
    @FXML private TableColumn<Alimento, String> colMarca;
    @FXML private TableColumn<Alimento, String> colCategoria;
    @FXML private TableColumn<Alimento, String> colCodigoDeBarras;
    @FXML private TableColumn<Alimento, String> colObservacoes;
    @FXML private TableColumn<Alimento, String> colImagem;
    @FXML private TableColumn<Alimento, Void> colAcoes;

    private AlimentoService alimentoService;
    private CategoriaService categoriaService = new CategoriaService();
    private ObservableList<Alimento> alimentos;

    public void initialize() {
        alimentoService = new AlimentoService();
        alimentos = FXCollections.observableArrayList(alimentoService.getAllFoods());

        tabelaAlimentos.setItems(alimentos);
        tabelaAlimentos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        // Mapear IDs de categorias para suas descrições
        Map<Integer, String> categoriaMap = categoriaService.getAllCategorias().stream()
                .collect(Collectors.toMap(Categoria::getId, Categoria::getDescricao));

        // Configurar a coluna de categoria para exibir a descrição
        colCategoria.setCellValueFactory(data -> {
            Integer categoriaId = data.getValue().getCategoriaId();
            return new SimpleStringProperty(categoriaMap.getOrDefault(categoriaId, ""));
        });


        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        colDataValidade.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDataValidade().toString()));
        colUnidadeMedida.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUnidadeMedida()));
        colMarca.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMarca()));
        colCodigoDeBarras.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodigoDeBarras()));
        colObservacoes.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getObservacoes()));
        colImagem.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getImagem()));

        adicionarBotaoAcoes();
    }

    private void adicionarBotaoAcoes() {
        colAcoes.setCellFactory(coluna -> new TableCell<>() {
            private final Button btn = new Button("⋮");

            {
                btn.setOnAction(e -> {
                    Alimento alimento = getTableView().getItems().get(getIndex());

                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem editar = new MenuItem("Editar");
                    MenuItem excluir = new MenuItem("Excluir");

                    editar.setOnAction(ev -> abrirModalEditar(alimento));
                    excluir.setOnAction(ev -> {
                        alimentoService.removeFood(alimento);
                        alimentos.remove(alimento);
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
    private void abrirModalAdicionarAlimento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/alimento/alimento-modal-view.fxml"));
            Parent root = loader.load();

            AlimentoModalController controller = loader.getController();
            controller.setAlimentoService(alimentoService);
            controller.setAlimentos(alimentos);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Adicionar Alimento");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirModalEditar(Alimento alimento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/alimento/alimento-modal-view.fxml"));
            Parent root = loader.load();

            AlimentoModalController controller = loader.getController();
            controller.setAlimentoExistente(alimento);
            controller.setAlimentoService(alimentoService);
            controller.setAlimentos(alimentos);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Editar Alimento");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
