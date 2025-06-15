package com.app.foodguard.service;

import com.app.foodguard.model.Movimentacao;
import com.app.foodguard.repository.MovimentacaoRepository;

import java.util.List;

public class MovimentacaoService {
    private final MovimentacaoRepository repository;
    private List<Movimentacao> movimentacaoList;

    public MovimentacaoService() {
        this.repository = new MovimentacaoRepository();
        this.movimentacaoList = getAllMovimentacoes(); // Load existing data
    }


    public List<Movimentacao> getAllMovimentacoes() {
        return repository.load();
    }

    public int addMovimentacao(Movimentacao movimentacao) {
        if (movimentacaoList == null) {
            movimentacaoList = repository.load();
        }
        movimentacao.setId(generateNextId());
        movimentacaoList.add(movimentacao);
        repository.save(movimentacaoList);
        return movimentacao.getId();
    }

    public void removeMovimentacao(int id) {
        movimentacaoList.removeIf(m -> m.getId() == id);
        repository.save(movimentacaoList);
    }

    // generateNextId method to generate a new ID for the movimentacao
    private int generateNextId() {
        return movimentacaoList.stream().mapToInt(Movimentacao::getId).max().orElse(0) + 1;
    }


}
