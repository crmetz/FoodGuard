package com.app.foodguard.controller;

import com.app.foodguard.model.Categoria;
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

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriaController {

    @FXML private TextField txtDescricao;
    @FXML private ToggleButton toggleAtivo;
    @FXML private TableView<Categoria> tabelaCategorias;
    @FXML private TableColumn<Categoria, Integer> colId;
    @FXML private TableColumn<Categoria, String> colDescricao;
    @FXML private TableColumn<Categoria, String> colAtivo;
    @FXML private TableColumn<Categoria, Void> colAcoes;

    private final ObservableList<Categoria> listaCategorias = FXCollections.observableArrayList();
    private final List<Integer> idsDisponiveis = new ArrayList<>();
    private final String CSV_PATH = "src/main/resources/csv/categorias.csv";

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
                        listaCategorias.remove(categoria);
                        idsDisponiveis.add(categoria.getId());
                        Collections.sort(idsDisponiveis);
                        salvarCSV();
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
                        atualizarBotao(categoria, btnAtivo);
                        salvarCSV();
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

        tabelaCategorias.setItems(listaCategorias);
        carregarCSV();
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
                int novoId = idsDisponiveis.isEmpty() ? listaCategorias.size() + 1 : idsDisponiveis.remove(0);
                novaCategoria.setId(novoId);
                listaCategorias.add(novaCategoria);
                salvarCSV();
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

    private void limparCampos() {
        txtDescricao.clear();
        toggleAtivo.setSelected(true);
        toggleAtivo.setText("Sim");
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void carregarCSV() {
        File file = new File(CSV_PATH);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            listaCategorias.clear();
            idsDisponiveis.clear();
            List<String> linhas = br.lines().skip(1).collect(Collectors.toList());
            for (String linha : linhas) {
                String[] campos = linha.split(",");
                int id = Integer.parseInt(campos[0]);
                String descricao = campos[1];
                String ativo = campos[2];
                listaCategorias.add(new Categoria(id, descricao, ativo));
            }

            for (int i = 1; i <= listaCategorias.size(); i++) {
                int finalI = i;
                if (listaCategorias.stream().noneMatch(c -> c.getId() == finalI)) {
                    idsDisponiveis.add(finalI);
                }
            }
            Collections.sort(idsDisponiveis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salvarCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_PATH))) {
            bw.write("id,descricao,ativo\n");
            for (Categoria cat : listaCategorias) {
                bw.write(cat.toCSV() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}