package com.app.foodguard.controller;

import com.app.foodguard.model.Lote;
import com.app.foodguard.model.Movimentacao;
import com.app.foodguard.model.enums.TipoSaida;
import com.app.foodguard.service.LoteService;
import com.app.foodguard.service.MovimentacaoService;
import com.app.foodguard.service.ReceitaService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioMovimentacoesController {

    @FXML private ComboBox<Lote> comboLotes;
    @FXML private TableView<Movimentacao> tabelaMovimentacoes;
    @FXML private TableColumn<Movimentacao, Integer> colId;
    @FXML private TableColumn<Movimentacao, String> colTipo;
    @FXML private TableColumn<Movimentacao, Float> colQuantidade;
    @FXML private TableColumn<Movimentacao, String> colData;
    @FXML private TableColumn<Movimentacao, String> colReceita;

    private final LoteService loteService = new LoteService();
    private final MovimentacaoService movimentacaoService = new MovimentacaoService();
    private final ReceitaService receitaService = new ReceitaService();

    @FXML
    public void initialize() {
        // Carrega os lotes no ComboBox
        comboLotes.setItems(FXCollections.observableArrayList(loteService.getAllLotes()));
        comboLotes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                carregarMovimentacoes(newVal.getId());
            }
        });

        // Configura as colunas da tabela
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // CORREÇÃO: Lidar com valores nulos no tipo
        colTipo.setCellValueFactory(cellData -> {
            TipoSaida tipo = cellData.getValue().getTipo();
            return new SimpleStringProperty(tipo != null ? formatTipoSaida(tipo) : "Desconhecido");
        });

        colTipo.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String tipoStr, boolean empty) {
                super.updateItem(tipoStr, empty);
                if (empty || tipoStr == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(tipoStr);
                    Movimentacao mov = getTableView().getItems().get(getIndex());
                    if (mov != null && mov.getTipo() != null) {
                        switch (mov.getTipo()) {
                            case CONSUMO:
                                setStyle("-fx-text-fill: green;");
                                break;
                            case DESPERDICIO:
                                setStyle("-fx-text-fill: red;");
                                break;
                            case DOACAO:
                                setStyle("-fx-text-fill: blue;");
                                break;
                            default:
                                setStyle("");
                        }
                    } else {
                        setStyle(""); // Sem cor para tipo desconhecido
                    }
                }
            }
        });

        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colQuantidade.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Float quantidade, boolean empty) {
                super.updateItem(quantidade, empty);
                if (empty || quantidade == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", quantidade));
                }
            }
        });

        colData.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        colReceita.setCellValueFactory(cellData -> {
            Integer receitaId = cellData.getValue().getReceitaId();
            if (receitaId != null && receitaId > 0) {
                return new SimpleStringProperty(receitaService.getAllReceitas().stream()
                        .filter(r -> r.getId() == receitaId)
                        .findFirst()
                        .map(r -> r.getNome())
                        .orElse("N/A"));
            }
            return new SimpleStringProperty("");
        });

        // Seleciona o primeiro lote se existir
        if (!comboLotes.getItems().isEmpty()) {
            comboLotes.getSelectionModel().selectFirst();
        }
    }

    private void carregarMovimentacoes(int loteId) {
        List<Movimentacao> movimentacoes = movimentacaoService.getAllMovimentacoes().stream()
                .filter(m -> m.getLoteId() == loteId)
                .collect(Collectors.toList());

        tabelaMovimentacoes.setItems(FXCollections.observableArrayList(movimentacoes));
    }

    // CORREÇÃO: Lidar com valores nulos
    private String formatTipoSaida(TipoSaida tipo) {
        if (tipo == null) return "Desconhecido";

        switch (tipo) {
            case CONSUMO: return "Consumo";
            case DESPERDICIO: return "Desperdício";
            case DOACAO: return "Doação";
            default: return tipo.name();
        }
    }
}