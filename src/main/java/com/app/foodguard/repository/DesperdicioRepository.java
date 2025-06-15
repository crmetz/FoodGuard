package com.app.foodguard.repository;

import com.app.foodguard.model.Desperdicio;
import com.app.foodguard.utils.CsvUtil;

import java.util.List;

public class DesperdicioRepository {

    private static final String FILE_PATH = "src/main/resources/csv/desperdicio.csv";

    public void save(List<Desperdicio> desperdicios) {
        CsvUtil.save(desperdicios, FILE_PATH);
    }

    public List<Desperdicio> load() {
        return CsvUtil.load(FILE_PATH, Desperdicio.class);
    }
}
