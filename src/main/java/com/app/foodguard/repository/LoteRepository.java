package com.app.foodguard.repository;

import com.app.foodguard.model.Lote;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoteRepository {
    private static final String FILE_PATH = "src/main/resources/csv/lotes.csv";

    public static void save(List<Lote> lotes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Lote lote : lotes) {
                writer.write(lote.getId() + ";" + lote.getAlimentoId() + ";" + lote.getQuantidade() + ";" +
                        lote.getDataValidade() + ";" + lote.getDataEntrada() + ";" + lote.getCodigo());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Lote> load() {
        List<Lote> lotes = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return lotes;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    Lote lote = new Lote();
                    lote.setId(Integer.parseInt(parts[0]));
                    lote.setAlimentoId(Integer.parseInt(parts[1]));
                    lote.setQuantidade(Float.parseFloat(parts[2]));
                    lote.setDataValidade(LocalDate.parse(parts[3]));
                    lote.setDataEntrada(LocalDate.parse(parts[4]));
                    lote.setCodigo(parts[5]);
                    lotes.add(lote);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lotes;
    }
}