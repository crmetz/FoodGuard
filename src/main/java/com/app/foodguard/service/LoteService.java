package com.app.foodguard.service;

import com.app.foodguard.model.Lote;
import com.app.foodguard.repository.LoteRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LoteService {
    private final List<Lote> lotes;
    private final LoteRepository loteRepository;

    public LoteService() {
        loteRepository = new LoteRepository();
        this.lotes = loteRepository.load();
    }

    public List<Lote> getAllLotes() {
        return lotes;
    }

    public Lote getLoteById(int id) {
        return getAllLotes().stream()
                .filter(l -> l.getId() == id)
                .findFirst()
                .orElse(null);
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
            lote.setQtdInicial(loteAtualizado.getQtdInicial());
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

    public void updateQuantidadeAtual(int loteId, float quantidade, boolean isSubtraction) {
        Optional<Lote> loteOptional = lotes.stream()
                .filter(l -> l.getId() == loteId)
                .findFirst();

        if (loteOptional.isPresent()) {
            Lote lote = loteOptional.get();
            if (isSubtraction) {
                if (lote.getQtdAtual() < quantidade) {
                    throw new IllegalArgumentException("Quantidade insuficiente no lote para a subtração.");
                }
                lote.setQtdAtual(lote.getQtdAtual() - quantidade);
            } else {
                lote.setQtdAtual(lote.getQtdAtual() + quantidade);
            }
            salvarDados();
        }
    }
}