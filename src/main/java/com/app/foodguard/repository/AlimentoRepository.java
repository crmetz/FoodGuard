package com.app.foodguard.repository;

import com.app.foodguard.model.Alimento;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AlimentoRepository {
    private static final String DIRECTORY_PATH = "src/main/resources/csv"; // Path to CSV folder
    private static final String FILE_PATH = DIRECTORY_PATH + "/foods.csv";

    public static void save(List<Alimento> foods) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Alimento food : foods) {
                writer.write(food.getName() + ";" + food.getQuantity() + ";" + food.getCategory());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Alimento> load() {
        List<Alimento> foods = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    foods.add(new Alimento(parts[0], Integer.parseInt(parts[1]), parts[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foods;
    }
}
