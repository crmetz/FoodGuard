package com.app.foodguard.repository;

import com.app.foodguard.model.Receita;
import com.app.foodguard.utils.CsvUtil;

import java.util.List;

public class ReceitaRepository {
    private static final String FILE_PATH = "src/main/resources/csv/receitas.csv";

    public void save(List<Receita> receitas) {
        CsvUtil.save(receitas, FILE_PATH);
    }

    public List<Receita> load() {
        return CsvUtil.load(FILE_PATH, Receita.class);
    }
}