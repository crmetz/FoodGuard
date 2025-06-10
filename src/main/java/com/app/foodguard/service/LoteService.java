package com.app.foodguard.service;

import com.app.foodguard.model.Lote;
import com.app.foodguard.repository.LoteRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LoteService {
    private final List<Lote> lotes;
    private LoteRepository loteRepository;

    public LoteService() {
        loteRepository = new LoteRepository();
        this.lotes = loteRepository.load();
    }

    public void addLote(Lote lote) {
        lote.setId(gerarProximoId());
        lotes.add(lote);
        salvarDados();
    }

    public void updateLote(Lote loteAtualizado) {
        Optional<Lote> loteExistente = lotes.stream()
                .filter(l -> l.getId() == loteAtualizado.getId())
                .findFirst();

        if (loteExistente.isPresent()) {
            Lote lote = loteExistente.get();
            lote.setAlimentoId(loteAtualizado.getAlimentoId());
            lote.setCodigo(loteAtualizado.getCodigo());
            lote.setQuantidade(loteAtualizado.getQuantidade());
            lote.setDataValidade(loteAtualizado.getDataValidade());
            salvarDados();
        }
    }

    public void removeLote(int id) {
        lotes.removeIf(l -> l.getId() == id);
        salvarDados();
    }

    private int gerarProximoId() {
        return lotes.stream()
                .mapToInt(Lote::getId)
                .max()
                .orElse(0) + 1;
    }

    private void salvarDados() {
        loteRepository.save(lotes);
    }

    public List<Lote> getLotesPorAlimento(int alimentoId) {
        return lotes.stream()
                .filter(l -> l.getAlimentoId() == alimentoId)
                .collect(Collectors.toList());
    }
}