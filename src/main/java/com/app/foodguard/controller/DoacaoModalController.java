package com.app.foodguard.controller;

import com.app.foodguard.model.Doacao;
import com.app.foodguard.model.Lote;
import com.app.foodguard.model.Movimentacao;
import com.app.foodguard.model.Ong;
import com.app.foodguard.service.DoacaoService;
import com.app.foodguard.service.LoteService;
import com.app.foodguard.service.MovimentacaoService;
import com.app.foodguard.service.OngService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

import java.time.LocalDate;


public class DoacaoModalController {
    @FXML private ComboBox<Ong> comboOng;
    @FXML private ComboBox<Lote> comboLote;
    @FXML private TextField txtQuantidade;

    @Setter
    private DoacaoService doacaoService;
    @Setter
    private ObservableList<Doacao> doacoes;
    private Doacao doacaoExistente;
    private final OngService ongService = new OngService();
    private final LoteService loteService = new LoteService();

    public void setDoacaoExistente(Doacao doacao) {
        this.doacaoExistente = doacao;
        preencherCampos();
    }

    private void preencherCampos() {
        if (doacaoExistente != null) {
            // Buscar ONG pelo ID
            Ong ong = ongService.getOngById(doacaoExistente.getOngId());
            comboOng.setValue(ong);

            // Buscar movimentação e depois o lote relacionado
            Movimentacao movimentacao = new MovimentacaoService().getMovimentacaoById(doacaoExistente.getMovimentacaoId());
            if (movimentacao != null) {
                Lote lote = loteService.getLoteById(movimentacao.getLoteId());
                comboLote.setValue(lote);
                txtQuantidade.setText(String.valueOf(movimentacao.getQuantidade()));
            }
        }
    }

    @FXML
    private void initialize() {
        // Populate ONGs ComboBox
        comboOng.setItems(FXCollections.observableArrayList(ongService.getAllOngs()));
        
        // Populate Lotes ComboBox
        comboLote.setItems(FXCollections.observableArrayList(loteService.getAllLotes()));
    }

    @FXML
    private void salvar() {
        Ong ong = comboOng.getValue();
        Lote lote = comboLote.getValue();
        float quantidade = Float.parseFloat(txtQuantidade.getText());

        // Cria doação com movimentação
        doacaoService.addDoacao(ong.getId(), lote, quantidade);

        // Atualiza tabela (recarrega)
        doacoes.setAll(doacaoService.getAllDoacao());
        fecharModal();
    }

    @FXML
    private void cancelar() {
        fecharModal();
    }

    private void fecharModal() {
        ((Stage) txtQuantidade.getScene().getWindow()).close();
    }
}
