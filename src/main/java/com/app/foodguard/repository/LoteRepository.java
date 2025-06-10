package com.app.foodguard.repository;

import com.app.foodguard.model.Lote;
import com.app.foodguard.utils.CsvUtil;

import java.util.List;

public class LoteRepository {

    private static final String FILE_PATH = "src/main/resources/csv/lotes.csv";

    public void save(List<Lote> lotes) {
        CsvUtil.save(lotes, FILE_PATH);
    }

    public List<Lote> load() {
        return CsvUtil.load(FILE_PATH, Lote.class);
    }
}
