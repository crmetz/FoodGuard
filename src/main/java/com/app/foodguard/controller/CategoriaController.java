package com.app.foodguard.controller;

import com.app.foodguard.model.Categoria;
import com.app.foodguard.service.CategoriaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CategoriaController {

    @FXML private TextField txtDescricao;
    @FXML private ToggleButton toggleAtivo;
    @FXML private TableView<Categoria> tabelaCategorias;
    @FXML private TableColumn<Categoria, Integer> colId;
    @FXML private TableColumn<Categoria, String> colDescricao;
    @FXML private TableColumn<Categoria, String> colAtivo;
    @FXML private TableColumn<Categoria, Void> colAcoes;

    private final ObservableList<Categoria> listaCategorias = FXCollections.observableArrayList();
    private final CategoriaService categoriaService = new CategoriaService();

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colAtivo.setCellValueFactory(new PropertyValueFactory<>("ativo"));

        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnDelete = new Button("Excluir");
            private final HBox hBox = new HBox(btnDelete);

            {
                btnDelete.setOnAction(event -> {
                    Categoria categoria = getTableView().getItems().get(getIndex());
                    if (categoria != null) {
                        categoriaService.removeCategoria(categoria);
                        listaCategorias.remove(categoria);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hBox);
            }
        });

        colAtivo.setCellFactory(param -> new TableCell<>() {
            private final Button btnAtivo = new Button();

            {
                btnAtivo.setOnAction(event -> {
                    Categoria categoria = getTableView().getItems().get(getIndex());
                    if (categoria != null) {
                        categoria.setAtivo(categoria.getAtivo().equals("Sim") ? "Não" : "Sim");
                        categoriaService.updateCategoria(categoria);
                        atualizarBotao(categoria, btnAtivo);
                    }
                });
            }

            @Override
            protected void updateItem(String ativo, boolean empty) {
                super.updateItem(ativo, empty);
                if (empty || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    Categoria categoria = getTableView().getItems().get(getIndex());
                    atualizarBotao(categoria, btnAtivo);
                    setGraphic(btnAtivo);
                }
            }
        });

        listaCategorias.addAll(categoriaService.getAllCategorias());
        tabelaCategorias.setItems(listaCategorias);
    }

    private void atualizarBotao(Categoria categoria, Button botao) {
        if (categoria.getAtivo().equals("Sim")) {
            botao.setText("Ativo");
            botao.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        } else {
            botao.setText("Inativo");
            botao.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        }
    }

    @FXML
    public void adicionarCategoria(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/foodguard/categoria/categoria-modal-view.fxml"));
            Parent root = loader.load();

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Adicionar Categoria");
            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();

            CategoriaModalController controller = loader.getController();
            Categoria novaCategoria = controller.getNovaCategoria();
            if (novaCategoria != null) {
                categoriaService.addCategoria(novaCategoria);
                listaCategorias.add(novaCategoria);
                tabelaCategorias.refresh();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void atualizarToggle() {
        toggleAtivo.setText(toggleAtivo.isSelected() ? "Sim" : "Não");
    }
}