package com.app.foodguard.repository;

import com.app.foodguard.model.Movimentacao;
import com.app.foodguard.utils.CsvUtil;

import java.util.List;

public class MovimentacaoRepository {

    private static final String FILE_PATH = "src/main/resources/csv/movimentacoes.csv";

    public void save(List<Movimentacao> movimentacoes) {
        CsvUtil.save(movimentacoes, FILE_PATH);
    }

    public List<Movimentacao> load() {
        return CsvUtil.load(FILE_PATH, Movimentacao.class);
    }
}
