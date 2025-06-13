package com.app.foodguard.utils;

import java.io.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

public class CsvUtil {

    public static <T> void save(List<T> list, String filePath) {
        if (list == null || list.isEmpty()) return;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T obj : list) {
                List<String> values = new ArrayList<>();
                for (Field field : obj.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    values.add(value != null ? value.toString() : "");
                }
                writer.write(String.join(";", values));
                writer.newLine();
            }
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> load(String filePath, Class<T> clazz) {
        List<T> resultList = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return resultList;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Field> fields = Arrays.asList(clazz.getDeclaredFields());

            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(";", -1); // -1 mantém campos vazios
                T obj = clazz.getDeclaredConstructor().newInstance();

                for (int i = 0; i < tokens.length && i < fields.size(); i++) {
                    Field field = fields.get(i);
                    field.setAccessible(true);
                    Object value = convertValue(field.getType(), tokens[i]);
                    field.set(obj, value);
                }

                resultList.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    private static Object convertValue(Class<?> type, String value) {
        if (value == null || value.isEmpty()) return null;
        if (type == int.class || type == Integer.class) return Integer.parseInt(value);
        if (type == float.class || type == Float.class) return Float.parseFloat(value);
        if (type == double.class || type == Double.class) return Double.parseDouble(value);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
        if (type == long.class || type == Long.class) return Long.parseLong(value);
        if (type == LocalDate.class) return LocalDate.parse(value);
        if (type == String.class) return value;
        return null; // ou lançar exceção se quiser evitar silenciosamente ignorar campos não suportados
    }
}
