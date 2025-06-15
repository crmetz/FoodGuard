package com.app.foodguard.service;

import com.app.foodguard.model.Receita;
import com.app.foodguard.model.ReceitaAlimento;
import com.app.foodguard.repository.ReceitaAlimentoRepository;
import com.app.foodguard.repository.ReceitaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReceitaService {
    private List<Receita> receitas;
    private List<ReceitaAlimento> vinculos;
    private ReceitaRepository receitaRepository;
    private ReceitaAlimentoRepository receitaAlimentoRepository;

    public ReceitaService() {
        receitaRepository = new ReceitaRepository();
        receitaAlimentoRepository = new ReceitaAlimentoRepository();
        receitas = receitaRepository.load();
        vinculos = receitaAlimentoRepository.load();
    }

    public List<Receita> getAllReceitas() {
        return receitas;
    }

    public void addReceita(Receita receita, List<ReceitaAlimento> alimentosReceita) {
        if (receitas == null) {
            receitas = new ArrayList<>();
        }
        receita.setId(generateNextId());
        receitas.add(receita);
        receitaRepository.save(receitas);

        // Adiciona os vínculos
        for (ReceitaAlimento ra : alimentosReceita) {
            ra.setIdReceita(receita.getId());
        }
        vinculos.addAll(alimentosReceita);
        receitaAlimentoRepository.save(vinculos);
    }

    public void updateReceita(Receita updatedReceita, List<ReceitaAlimento> novosVinculos) {
        for (int i = 0; i < receitas.size(); i++) {
            if (receitas.get(i).getId() == updatedReceita.getId()) {
                receitas.set(i, updatedReceita);
                break;
            }
        }
        receitaRepository.save(receitas);

        // Atualiza vínculos
        vinculos.removeIf(v -> v.getIdReceita() == updatedReceita.getId());
        vinculos.addAll(novosVinculos);
        receitaAlimentoRepository.save(vinculos);
    }

    public void removeReceita(Receita receita) {
        receitas.removeIf(r -> r.getId() == receita.getId());
        receitaRepository.save(receitas);

        // Remove vínculos
        vinculos.removeIf(v -> v.getIdReceita() == receita.getId());
        receitaAlimentoRepository.save(vinculos);
    }

    public List<ReceitaAlimento> getAlimentosByReceitaId(int idReceita) {
        return vinculos.stream()
                .filter(v -> v.getIdReceita() == idReceita)
                .collect(Collectors.toList());
    }

    private int generateNextId() {
        return receitas.stream().mapToInt(Receita::getId).max().orElse(0) + 1;
    }
}