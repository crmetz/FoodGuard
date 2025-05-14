package com.app.foodguard.repository;

import com.app.foodguard.model.Categoria;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepository {
    private static final String DIRECTORY_PATH = "src/main/resources/csv";
    private static final String FILE_PATH = DIRECTORY_PATH + "/categorias.csv";

    public static void save(List<Categoria> categorias) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Categoria categoria : categorias) {
                writer.write(categoria.getId() + ";" + categoria.getDescricao() + ";" + categoria.getAtivo());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Categoria> load() {
        List<Categoria> categorias = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return categorias;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1); // -1 to keep empty fields
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]);
                    String descricao = parts[1];
                    String ativo = parts[2];
                    categorias.add(new Categoria(id, descricao, ativo));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return categorias;
    }
}