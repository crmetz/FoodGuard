package com.app.foodguard.controller;

import com.app.foodguard.controller.DTO.DesperdicioDTO;
import com.app.foodguard.model.Alimento;
import com.app.foodguard.model.Lote;
import com.app.foodguard.service.AlimentoService;
import com.app.foodguard.service.LoteService;
import com.app.foodguard.service.DesperdicioService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class DesperdicioModalController {

    @FXML private ComboBox<String> comboLote;
    @FXML private Label lblDisponivel;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtMotivo;
    @FXML private TextArea txtObservacoes;

    private final LoteService loteService = new LoteService();
    @Setter
    private DesperdicioService desperdicioService;

    @Setter
    private ObservableList<DesperdicioDTO> desperdiciosControllerList;
    private List<Lote> lotesDisponiveis;
    private Lote loteSelecionado;

    @FXML
    public void initialize() {
        carregarLotes();
        comboLote.setOnAction(e -> atualizarQuantidadeDisponivel());
    }

    private void carregarLotes() {
        lotesDisponiveis = loteService.getAllLotes();
        comboLote.getItems().clear();

        for (Lote lote : lotesDisponiveis) {
            if (lote.getQtdAtual() <= 0) continue; // Ignorar lotes sem quantidade disponível
            String display = lote.getId() + " - " + lote.getCodigo();
            comboLote.getItems().add(display);
        }
    }

    private void atualizarQuantidadeDisponivel() {
        String selected = comboLote.getValue();
        if (selected == null || selected.isBlank()) return;

        int loteId = Integer.parseInt(selected.split("-")[0].trim());
        loteSelecionado = lotesDisponiveis.stream()
                .filter(l -> l.getId() == loteId)
                .findFirst()
                .orElse(null);

        List<Alimento> alimentos = new AlimentoService().getAllFoods();
        Alimento alimento = alimentos.stream()
                .filter(a -> a.getId() == loteSelecionado.getAlimentoId())
                .findFirst()
                .orElse(null);


        if (loteSelecionado != null) {
            lblDisponivel.setText("Disponível: " + loteSelecionado.getQtdAtual() + " " + alimento.getUnidadeMedida());
        }
    }

    @FXML
    private void onSalvar() {
        try {
            if (loteSelecionado == null) {
                mostrarAlerta("Erro", "Selecione um lote válido.");
                return;
            }

            float quantidade = Float.parseFloat(txtQuantidade.getText().trim());
            if (quantidade <= 0) {
                mostrarAlerta("Erro", "Quantidade deve ser maior que 0.");
                return;
            }

            if (quantidade > loteSelecionado.getQtdAtual()) {
                mostrarAlerta("Erro", "Quantidade informada excede a disponível.");
                return;
            }

            String motivo = txtMotivo.getText().trim();
            String observacoes = txtObservacoes.getText().trim();

            List<Alimento> alimentos = new AlimentoService().getAllFoods();
            Alimento alimento = alimentos.stream()
                    .filter(a -> a.getId() == loteSelecionado.getAlimentoId())
                    .findFirst()
                    .orElse(null);


            // Criar DTO
            DesperdicioDTO dto = new DesperdicioDTO();
            dto.setLoteId(loteSelecionado.getId());
            dto.setAlimento(alimento.getNome());
            dto.setQuantidade(quantidade);
            dto.setUnidadeMedida(alimento.getUnidadeMedida());
            dto.setData(LocalDate.now());
            dto.setMotivo(motivo);
            dto.setObservacao(observacoes);

            desperdicioService.addDesperdicio(dto);
            fecharJanela();
            tabelaAtualizar();
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Quantidade inválida.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao salvar", e.getMessage());
        }
    }

    private void tabelaAtualizar() {
        desperdiciosControllerList.setAll(desperdicioService.getAllDesperdicios());
    }


    @FXML
    private void onCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) comboLote.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
