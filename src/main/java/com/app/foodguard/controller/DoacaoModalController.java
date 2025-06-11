package com.app.foodguard.controller;

import com.app.foodguard.model.Doacao;
import com.app.foodguard.model.Lote;
import com.app.foodguard.model.Ong;
import com.app.foodguard.service.DoacaoService;
import com.app.foodguard.service.LoteService;
import com.app.foodguard.service.OngService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class DoacaoModalController {
    @FXML private ComboBox<Ong> comboOng;
    @FXML private ComboBox<Lote> comboLote;
    @FXML private TextField txtQuantidade;

    private DoacaoService doacaoService;
    private ObservableList<Doacao> doacoes;
    private Doacao doacaoExistente;
    private final OngService ongService = new OngService();
    private final LoteService loteService = new LoteService();

    public void setDoacaoService(DoacaoService doacaoService) {
        this.doacaoService = doacaoService;
    }

    public void setDoacoes(ObservableList<Doacao> doacoes) {
        this.doacoes = doacoes;
    }

    public void setDoacaoExistente(Doacao doacao) {
        this.doacaoExistente = doacao;
        preencherCampos();
    }

    private void preencherCampos() {
        if (doacaoExistente != null) {
            comboOng.setValue(doacaoExistente.getOng());
            comboLote.setValue(doacaoExistente.getLote());
            txtQuantidade.setText(String.valueOf(doacaoExistente.getQuantidade()));
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
        Doacao doacao = doacaoExistente != null ? doacaoExistente : new Doacao();
        doacao.setOng(comboOng.getValue());
        doacao.setLote(comboLote.getValue());
        doacao.setQuantidade(Float.parseFloat(txtQuantidade.getText()));

        if (doacaoExistente == null) {
            doacaoService.addDoacao(doacao);
            doacoes.add(doacao);
        } else {
            doacaoService.updateDoacao(doacao);
            doacoes.set(doacoes.indexOf(doacaoExistente), doacao);
        }

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
