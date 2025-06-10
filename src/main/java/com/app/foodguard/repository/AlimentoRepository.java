package com.app.foodguard.repository;

import com.app.foodguard.model.Alimento;
import com.app.foodguard.utils.CsvUtil;

import java.util.List;

public class AlimentoRepository {

    private static final String FILE_PATH = "src/main/resources/csv/foods.csv";

    public void save(List<Alimento> alimentos) {
        CsvUtil.save(alimentos, FILE_PATH);
    }

    public List<Alimento> load() {
        return CsvUtil.load(FILE_PATH, Alimento.class);
    }
}
