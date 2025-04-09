package com.app.foodguard.repository;

import com.app.foodguard.model.Alimento;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlimentoRepository {
    private static final String DIRECTORY_PATH = "src/main/resources/csv";
    private static final String FILE_PATH = DIRECTORY_PATH + "/foods.csv";

    public static void save(List<Alimento> foods) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Alimento food : foods) {
                writer.write(
                        food.getId() + ";" +
                                food.getNome() + ";" +
                                food.getDataValidade() + ";" +
                                food.getQuantidade() + ";" +
                                food.getUnidadeMedida() + ";" +
                                food.getMarca() + ";" +
                                food.getCodigoDeBarras() + ";" +
                                food.getObservacoes() + ";" +
                                food.getImagem()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Alimento> load() {
        List<Alimento> foods = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return foods;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1); // -1 to keep empty fields
                if (parts.length == 9) {
                    Alimento alimento = new Alimento();
                    alimento.setId(Integer.parseInt(parts[0]));
                    alimento.setNome(parts[1]);
                    alimento.setDataValidade(LocalDate.parse(parts[2]));
                    alimento.setQuantidade(Float.parseFloat(parts[3]));
                    alimento.setUnidadeMedida(parts[4]);
                    alimento.setMarca(parts[5]);
                    alimento.setCodigoDeBarras(parts[6]);
                    alimento.setObservacoes(parts[7]);
                    alimento.setImagem(parts[8]);
                    foods.add(alimento);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return foods;
    }
}
