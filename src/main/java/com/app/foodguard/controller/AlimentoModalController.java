package com.app.foodguard.controller;

import com.app.foodguard.model.Alimento;
import com.app.foodguard.service.AlimentoService;
import com.app.foodguard.service.CategoriaService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;

import java.time.LocalDate;

@Setter
public class AlimentoModalController {

    @FXML private TextField txtNome;
    @FXML private DatePicker dateValidade;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtUnidadeMedida;
    @FXML private TextField txtMarca;
    @FXML private TextField txtCodigoDeBarras;
    @FXML private TextArea txtObservacoes;
    @FXML private TextField txtImagem;
    @FXML private ComboBox<String> comboCategoria;

    private AlimentoService alimentoService;
    private ObservableList<Alimento> alimentos;
    private Alimento alimentoExistente;
    private CategoriaService categoriaService = new CategoriaService();

    @FXML
    public void initialize() {
        carregarCategoriasAtivas();
    }

    private void carregarCategoriasAtivas() {
        comboCategoria.getItems().clear();
        categoriaService.getCategoriasAtivas().forEach(categoria -> comboCategoria.getItems().add(categoria.getDescricao()));
    }

    public void setAlimentoExistente(Alimento alimento) {
        this.alimentoExistente = alimento;
        preencherCampos(alimento);
    }

    private void preencherCampos(Alimento alimento) {
        txtNome.setText(alimento.getNome());
        dateValidade.setValue(alimento.getDataValidade());
        txtQuantidade.setText(String.valueOf(alimento.getQuantidade()));
        txtUnidadeMedida.setText(alimento.getUnidadeMedida());
        txtMarca.setText(alimento.getMarca());
        txtCodigoDeBarras.setText(alimento.getCodigoDeBarras());
        txtObservacoes.setText(alimento.getObservacoes());
        txtImagem.setText(alimento.getImagem());
    }

    @FXML
    private void onSalvar() {
        try {
            String nome = txtNome.getText();
            LocalDate validade = dateValidade.getValue();
            float quantidade = Float.parseFloat(txtQuantidade.getText());
            String unidadeMedida = txtUnidadeMedida.getText();
            String marca = txtMarca.getText();
            String codigo = txtCodigoDeBarras.getText();
            String observacoes = txtObservacoes.getText();
            String imagem = txtImagem.getText();
            String categoria = comboCategoria.getValue();

            if (alimentoExistente != null) {
                // Atualizar alimento existente
                alimentoExistente.setNome(nome);
                alimentoExistente.setDataValidade(validade);
                alimentoExistente.setQuantidade(quantidade);
                alimentoExistente.setUnidadeMedida(unidadeMedida);
                alimentoExistente.setMarca(marca);
                alimentoExistente.setCodigoDeBarras(codigo);
                alimentoExistente.setObservacoes(observacoes);
                alimentoExistente.setImagem(imagem);
                alimentoExistente.setCategoria(categoria);

                alimentoService.updateFood(alimentoExistente);
                tabelaAtualizar(); // força refresh
            } else {
                // Criar novo alimento
                Alimento novo = new Alimento();
                novo.setNome(nome);
                novo.setDataValidade(validade);
                novo.setQuantidade(quantidade);
                novo.setUnidadeMedida(unidadeMedida);
                novo.setMarca(marca);
                novo.setCodigoDeBarras(codigo);
                novo.setObservacoes(observacoes);
                novo.setImagem(imagem);
                novo.setCategoria(categoria);

                alimentoService.addFood(novo);
                alimentos.add(novo);
            }

            fecharJanela();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao salvar alimento", e.getMessage());
        }
    }

    private void tabelaAtualizar() {
        alimentos.setAll(alimentoService.getAllFoods());
    }

    @FXML
    private void onCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
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
