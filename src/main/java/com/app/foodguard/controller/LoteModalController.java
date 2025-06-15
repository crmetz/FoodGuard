package com.app.foodguard.controller;

import com.app.foodguard.model.Lote;
import com.app.foodguard.service.AlimentoService;
import com.app.foodguard.service.LoteService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class LoteModalController {

    @FXML private ComboBox<String> comboAlimento;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtQuantidade;
    @FXML private DatePicker dateValidade;

    private final LoteService loteService = new LoteService();
    private final AlimentoService alimentoService = new AlimentoService();
    private Lote loteExistente;

    public void setLoteExistente(Lote lote) {
        this.loteExistente = lote;
        preencherCampos(lote);
    }

    @FXML
    public void initialize() {
        carregarAlimentos();
    }

    private void carregarAlimentos() {
        List<String> alimentosFormatados = alimentoService.getAllFoods().stream()
                .map(a -> a.getId() + " - " + a.getNome())
                .collect(Collectors.toList());
        comboAlimento.setItems(FXCollections.observableArrayList(alimentosFormatados));
    }

    private void preencherCampos(Lote lote) {
        if (lote != null) {
            String alimentoFormatado = alimentoService.getAllFoods().stream()
                    .filter(a -> a.getId() == lote.getAlimentoId())
                    .findFirst()
                    .map(a -> a.getId() + " - " + a.getNome())
                    .orElse("");

            comboAlimento.setValue(alimentoFormatado);
            txtCodigo.setText(lote.getCodigo());
            txtQuantidade.setText(String.valueOf(lote.getQtdInicial()));
            dateValidade.setValue(lote.getDataValidade());
        }
    }

    @FXML
    private void onSalvar() {
        try {
            String alimentoSelecionado = comboAlimento.getValue();
            if (alimentoSelecionado == null) throw new Exception("Selecione um alimento!");

            int alimentoId = Integer.parseInt(alimentoSelecionado.split("-")[0].trim());
            String codigo = txtCodigo.getText();
            float quantidade = Float.parseFloat(txtQuantidade.getText());
            LocalDate validade = dateValidade.getValue();

            if (codigo.isEmpty()) throw new Exception("Código é obrigatório!");
            if (quantidade <= 0) throw new Exception("Quantidade inválida!");
            if (validade == null) throw new Exception("Data de validade obrigatória!");

            if (loteExistente != null) {
                loteExistente.setAlimentoId(alimentoId);
                loteExistente.setCodigo(codigo);
                loteExistente.setQtdInicial(quantidade);
                loteExistente.setDataValidade(validade);
                loteService.updateLote(loteExistente);
            } else {
                Lote novoLote = new Lote(
                        0,
                        alimentoId,
                        quantidade,
                        quantidade,
                        validade,
                        LocalDate.now(),
                        codigo
                );
                loteService.addLote(novoLote);
            }

            fecharJanela();

        } catch (Exception e) {
            mostrarErro("Erro ao salvar lote", e.getMessage());
        }
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void onCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) comboAlimento.getScene().getWindow();
        stage.close();
    }
}