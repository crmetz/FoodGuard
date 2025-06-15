package com.app.foodguard.repository;

import com.app.foodguard.model.Doacao;
import com.app.foodguard.model.Lote;
import com.app.foodguard.model.Ong;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DoacaoRepository {
    private static final String DIRECTORY_PATH = "src/main/resources/csv";
    private static final String FILE_PATH = DIRECTORY_PATH + "/doacoes.csv";

    public static void save(List<Doacao> doacoes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Doacao doacao : doacoes) {
                writer.write(
                        doacao.getId() + ";" +
                        doacao.getOng().getId() + ";" +
                        doacao.getLote().getId() + ";" +
                        doacao.getQuantidade()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Doacao> load() {
        List<Doacao> doacoes = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return doacoes;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1);
                if (parts.length == 4) {
                    Doacao doacao = new Doacao();
                    doacao.setId(Integer.parseInt(parts[0]));
                    
                    // Carrega apenas o ID da ONG - os dados completos podem ser carregados quando necessário
                    Ong ong = new Ong();
                    ong.setId(Integer.parseInt(parts[1]));
                    ong.setNome(parts[2]);
                    doacao.setOng(ong);

                    // Carrega apenas o ID do Lote - os dados completos podem ser carregados quando necessário
                    Lote lote = new Lote();
                    lote.setId(Integer.parseInt(parts[3]));
                    doacao.setLote(lote);

                    doacao.setQuantidade(Float.parseFloat(parts[4]));
                    doacoes.add(doacao);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return doacoes;
    }
}

