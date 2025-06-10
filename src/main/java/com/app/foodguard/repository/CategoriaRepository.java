package com.app.foodguard.repository;

import com.app.foodguard.model.Categoria;
import com.app.foodguard.utils.CsvUtil;

import java.util.List;

public class CategoriaRepository {

    private static final String FILE_PATH = "src/main/resources/csv/categorias.csv";

    public void save(List<Categoria> categorias) {
        CsvUtil.save(categorias, FILE_PATH);
    }

    public List<Categoria> load() {
        return CsvUtil.load(FILE_PATH, Categoria.class);
    }
}
