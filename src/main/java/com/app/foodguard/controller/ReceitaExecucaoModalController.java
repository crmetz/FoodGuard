package com.app.foodguard.controller;

import com.app.foodguard.model.*;
import com.app.foodguard.model.enums.TipoSaida;
import com.app.foodguard.service.*;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReceitaExecucaoModalController {

    @FXML private TableView<ItemExecucao> tabelaAlimentos;
    @FXML private TableColumn<ItemExecucao, String> colAlimento;
    @FXML private TableColumn<ItemExecucao, Float> colQuantidade;
    @FXML private TableColumn<ItemExecucao, Lote> colLote;

    private Receita receita;
    private final ReceitaService receitaService = new ReceitaService();
    private final AlimentoService alimentoService = new AlimentoService();
    private final LoteService loteService = new LoteService();
    private final MovimentacaoService movimentacaoService = new MovimentacaoService();

    private final ObservableList<ItemExecucao> itensExecucao = FXCollections.observableArrayList();

    public void setReceita(Receita receita) {
        this.receita = receita;
        carregarDados();
    }

    private void carregarDados() {
        // Obter alimentos da receita
        List<ReceitaAlimento> alimentosReceita = receitaService.getAlimentosByReceitaId(receita.getId());

        // Converter para itens de execução
        for (ReceitaAlimento ra : alimentosReceita) {
            Alimento alimento = alimentoService.getAlimentoById(ra.getIdAlimento());
            if (alimento != null) {
                List<Lote> lotesDisponiveis = loteService.getLotesPorAlimento(alimento.getId()).stream()
                        .filter(l -> l.getQtdAtual() >= ra.getQuantidade())
                        .sorted(Comparator.comparing(Lote::getDataValidade)) // Ordenar por validade mais próxima
                        .collect(Collectors.toList());

                itensExecucao.add(new ItemExecucao(
                        alimento,
                        ra.getQuantidade(),
                        lotesDisponiveis
                ));
            }
        }

        // Configurar tabela
        colAlimento.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getAlimento().getNome()));

        colQuantidade.setCellValueFactory(data ->
                new SimpleFloatProperty(data.getValue().getQuantidadeNecessaria()).asObject());

        colLote.setCellFactory(tc -> new TableCell<>() {
            private final ComboBox<Lote> comboBox = new ComboBox<>();

            {
                comboBox.setConverter(new StringConverter<Lote>() {
                    @Override
                    public String toString(Lote lote) {
                        if (lote == null) return "";
                        return lote.getCodigo() + " (Val: " + lote.getDataValidade() + ")";
                    }

                    @Override
                    public Lote fromString(String string) {
                        return null;
                    }
                });

                comboBox.setOnAction(event -> {
                    ItemExecucao item = getTableView().getItems().get(getIndex());
                    item.setLoteSelecionado(comboBox.getValue());
                });
            }

            @Override
            protected void updateItem(Lote item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ItemExecucao linha = getTableView().getItems().get(getIndex());
                    comboBox.setItems(linha.getLotesDisponiveis());
                    comboBox.setValue(linha.getLoteSelecionado());
                    setGraphic(comboBox);
                }
            }
        });

        tabelaAlimentos.setItems(itensExecucao);
    }

    @FXML
    private void salvar() {
        // Validar seleções
        for (ItemExecucao item : itensExecucao) {
            if (item.getLoteSelecionado() == null) {
                mostrarAlerta("Seleção incompleta", "Selecione um lote para todos os alimentos");
                return;
            }

            // Validar quantidade disponível
            if (item.getLoteSelecionado().getQtdAtual() < item.getQuantidadeNecessaria()) {
                mostrarAlerta("Quantidade insuficiente",
                        "O lote selecionado para " + item.getAlimento().getNome() +
                                " não possui quantidade suficiente. Disponível: " +
                                item.getLoteSelecionado().getQtdAtual() +
                                ", Necessário: " + item.getQuantidadeNecessaria());
                return;
            }
        }

        // Processar execução
        for (ItemExecucao item : itensExecucao) {
            Lote lote = item.getLoteSelecionado();
            float quantidade = item.getQuantidadeNecessaria();

            // Atualizar lote
            loteService.updateQuantidadeAtual(lote.getId(), quantidade, true);

            // Criar movimentação
            Movimentacao mov = new Movimentacao();
            mov.setTipo(TipoSaida.CONSUMO);
            mov.setQuantidade(quantidade);
            mov.setData(LocalDate.now());
            mov.setLoteId(lote.getId());

            movimentacaoService.addMovimentacao(mov);
        }

        // Fechar modal
        ((Stage) tabelaAlimentos.getScene().getWindow()).close();
    }

    @FXML
    private void cancelar() {
        ((Stage) tabelaAlimentos.getScene().getWindow()).close();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    // Classe para representar os itens na tabela
    public static class ItemExecucao {
        private final Alimento alimento;
        private final float quantidadeNecessaria;
        private final ObservableList<Lote> lotesDisponiveis;
        private Lote loteSelecionado;

        public ItemExecucao(Alimento alimento, float quantidadeNecessaria, List<Lote> lotesDisponiveis) {
            this.alimento = alimento;
            this.quantidadeNecessaria = quantidadeNecessaria;
            this.lotesDisponiveis = FXCollections.observableArrayList(lotesDisponiveis);
            if (!lotesDisponiveis.isEmpty()) {
                this.loteSelecionado = lotesDisponiveis.get(0); // Seleciona o primeiro por padrão
            }
        }

        // Getters e Setters
        public Alimento getAlimento() {
            return alimento;
        }

        public float getQuantidadeNecessaria() {
            return quantidadeNecessaria;
        }

        public ObservableList<Lote> getLotesDisponiveis() {
            return lotesDisponiveis;
        }

        public Lote getLoteSelecionado() {
            return loteSelecionado;
        }

        public void setLoteSelecionado(Lote loteSelecionado) {
            this.loteSelecionado = loteSelecionado;
        }
    }
}