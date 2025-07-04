package com.app.foodguard.repository;

import com.app.foodguard.model.Doacao;
import com.app.foodguard.utils.CsvUtil;

import java.util.List;

public class DoacaoRepository {

    private static final String FILE_PATH = "src/main/resources/csv/doacoes.csv";

    public static void save(List<Doacao> doacoes) {
        CsvUtil.save(doacoes, FILE_PATH);
    }

    public static List<Doacao> load() {
        return CsvUtil.load(FILE_PATH, Doacao.class);
    }
}
