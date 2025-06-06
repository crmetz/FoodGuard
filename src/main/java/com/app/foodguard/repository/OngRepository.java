package com.app.foodguard.repository;

import com.app.foodguard.model.Ong;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OngRepository {
    private static final String DIRECTORY_PATH = "src/main/resources/csv";
    private static final String FILE_PATH = DIRECTORY_PATH + "/ongs.csv";

    public static void save(List<Ong> ongs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Ong ong : ongs) {
                writer.write(
                        ong.getId() + ";" +
                                ong.getNome() + ";" +
                                ong.getContato() + ";" +
                                ong.getEndereco()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Ong> load() {
        List<Ong> ongs = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return ongs;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1); // -1 para manter campos vazios
                if (parts.length == 4) {
                    Ong ong = new Ong();
                    ong.setId(Integer.parseInt(parts[0]));
                    ong.setNome(parts[1]);
                    ong.setContato(parts[2]);
                    ong.setEndereco(parts[3]);
                    ongs.add(ong);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ongs;
    }
}