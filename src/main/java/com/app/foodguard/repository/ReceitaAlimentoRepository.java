package com.app.foodguard.repository;

import com.app.foodguard.model.ReceitaAlimento;
import com.app.foodguard.utils.CsvUtil;

import java.util.List;

public class ReceitaAlimentoRepository {
    private static final String FILE_PATH = "src/main/resources/csv/receita_alimentos.csv";

    public void save(List<ReceitaAlimento> vinculos) {
        CsvUtil.save(vinculos, FILE_PATH);
    }

    public List<ReceitaAlimento> load() {
        return CsvUtil.load(FILE_PATH, ReceitaAlimento.class);
    }
}