package com.app.foodguard.controller;

import com.app.foodguard.model.Alimento;
import com.app.foodguard.model.Receita;
import com.app.foodguard.model.ReceitaAlimento;
import com.app.foodguard.service.AlimentoService;
import com.app.foodguard.service.ReceitaService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
public class ReceitaModalController {

    @FXML private TextField txtNome;
    @FXML private TextField txtTempoPreparo;
    @FXML private TableView<AlimentoRow> tabelaAlimentos;
    @FXML private TableColumn<AlimentoRow, String> colAlimento;
    @FXML private TableColumn<AlimentoRow, String> colQuantidade;
    @FXML private TableColumn<AlimentoRow, String> colUnidade;
    @FXML private TableColumn<AlimentoRow, Void> colRemover;
    @FXML private ComboBox<Alimento> comboAlimentos;
    @FXML private TextField txtQuantidade;

    private ReceitaService receitaService;
    private ObservableList<Receita> receitas;
    private Receita receitaExistente;
    private AlimentoService alimentoService = new AlimentoService();
    private List<ReceitaAlimento> alimentosVinculados = new ArrayList<>();

    // Classe auxiliar para exibição na tabela
    public static class AlimentoRow {
        private final Alimento alimento;
        private final float quantidade;

        public AlimentoRow(Alimento alimento, float quantidade) {
            this.alimento = alimento;
            this.quantidade = quantidade;
        }

        public Alimento getAlimento() {
            return alimento;
        }

        public float getQuantidade() {
            return quantidade;
        }
    }

    @FXML
    public void initialize() {
        // Configurar colunas da tabela
        colAlimento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAlimento().getNome()));
        colQuantidade.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getQuantidade())));
        colUnidade.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAlimento().getUnidadeMedida()));

        // Configurar botão de remoção
        colRemover.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Remover");

            {
                btn.setOnAction(event -> {
                    AlimentoRow row = getTableView().getItems().get(getIndex());
                    alimentosVinculados.removeIf(ra -> ra.getIdAlimento() == row.getAlimento().getId());
                    atualizarTabelaAlimentos();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        // Carregar ComboBox com alimentos
        comboAlimentos.getItems().addAll(alimentoService.getAllFoods());
        comboAlimentos.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Alimento item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNome());
                }
            }
        });
    }

    public void setReceitaExistente(Receita receita) {
        this.receitaExistente = receita;
        preencherCampos(receita);

        // Carregar alimentos vinculados
        if (receita != null) {
            alimentosVinculados = receitaService.getAlimentosByReceitaId(receita.getId());
            atualizarTabelaAlimentos();
        }
    }

    private void preencherCampos(Receita receita) {
        txtNome.setText(receita.getNome());
        txtTempoPreparo.setText(String.valueOf(receita.getTempoPreparo()));
    }

    private void atualizarTabelaAlimentos() {
        ObservableList<AlimentoRow> itens = FXCollections.observableArrayList();
        for (ReceitaAlimento vinculo : alimentosVinculados) {
            alimentoService.getAllFoods().stream()
                    .filter(a -> a.getId() == vinculo.getIdAlimento())
                    .findFirst()
                    .ifPresent(a -> itens.add(new AlimentoRow(a, vinculo.getQuantidade())));
        }
        tabelaAlimentos.setItems(itens);
    }

    @FXML
    private void onAdicionarAlimento() {
        Alimento alimentoSelecionado = comboAlimentos.getSelectionModel().getSelectedItem();
        String quantidadeStr = txtQuantidade.getText();

        if (alimentoSelecionado == null) {
            mostrarAlerta("Erro", "Selecione um alimento");
            return;
        }

        try {
            float quantidade = Float.parseFloat(quantidadeStr);
            alimentosVinculados.add(new ReceitaAlimento(0, alimentoSelecionado.getId(), quantidade));
            atualizarTabelaAlimentos();
            txtQuantidade.clear();
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Quantidade inválida");
        }
    }

    @FXML
    private void onSalvar() {
        try {
            String nome = txtNome.getText();
            int tempoPreparo = Integer.parseInt(txtTempoPreparo.getText());

            if (receitaExistente != null) {
                // Atualizar receita existente
                receitaExistente.setNome(nome);
                receitaExistente.setTempoPreparo(tempoPreparo);
                receitaService.updateReceita(receitaExistente, alimentosVinculados);
                receitas.setAll(
                        receitas.stream()
                                .map(x -> x.getId() == receitaExistente.getId() ? receitaExistente : x)
                                .collect(Collectors.toList())
                );
            } else {
                // Criar nova receita
                Receita nova = new Receita();
                nova.setNome(nome);
                nova.setTempoPreparo(tempoPreparo);
                receitaService.addReceita(nova, alimentosVinculados);
                receitas.add(nova);
            }

            fecharJanela();
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Tempo de preparo inválido");
        }
    }

    @FXML
    private void onCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}