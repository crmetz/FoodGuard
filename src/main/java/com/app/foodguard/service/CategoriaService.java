package com.app.foodguard.service;

import com.app.foodguard.model.Categoria;
import com.app.foodguard.repository.CategoriaRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriaService {
    private List<Categoria> categoriaList;
    private final List<Integer> idsDisponiveis = new ArrayList<>();

    public CategoriaService() {
        categoriaList = CategoriaRepository.load(); // Carrega os dados existentes
        atualizarIdsDisponiveis();
    }

    public List<Categoria> getAllCategorias() {
        return categoriaList;
    }

    public List<Categoria> getCategoriasAtivas() {
        return categoriaList.stream()
                .filter(categoria -> "Sim".equals(categoria.getAtivo()))
                .collect(Collectors.toList());
    }

    public void addCategoria(Categoria categoria) {
        if (categoriaList == null) {
            categoriaList = new ArrayList<>();
        }
        categoria.setId(generateNextId());
        categoriaList.add(categoria);
        CategoriaRepository.save(categoriaList);
    }

    public void removeCategoria(Categoria categoria) {
        categoriaList.removeIf(c -> c.getId() == categoria.getId());
        idsDisponiveis.add(categoria.getId());
        Collections.sort(idsDisponiveis);
        CategoriaRepository.save(categoriaList);
    }

    public void updateCategoria(Categoria updatedCategoria) {
        for (int i = 0; i < categoriaList.size(); i++) {
            if (categoriaList.get(i).getId() == updatedCategoria.getId()) {
                categoriaList.set(i, updatedCategoria);
                break;
            }
        }
        CategoriaRepository.save(categoriaList);
    }

    private int generateNextId() {
        if (!idsDisponiveis.isEmpty()) {
            return idsDisponiveis.remove(0); // Usa o primeiro ID disponível
        }
        return categoriaList.stream().mapToInt(Categoria::getId).max().orElse(0) + 1;
    }

    private void atualizarIdsDisponiveis() {
        idsDisponiveis.clear();
        int maxId = categoriaList.stream().mapToInt(Categoria::getId).max().orElse(0);
        for (int i = 1; i <= maxId; i++) {
            int finalI = i;
            if (categoriaList.stream().noneMatch(c -> c.getId() == finalI)) {
                idsDisponiveis.add(finalI);
            }
        }
        Collections.sort(idsDisponiveis);
    }
}