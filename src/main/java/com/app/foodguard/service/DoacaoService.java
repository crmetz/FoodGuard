package com.app.foodguard.service;

import com.app.foodguard.model.Doacao;
import com.app.foodguard.model.Lote;
import com.app.foodguard.model.Movimentacao;
import com.app.foodguard.model.enums.TipoSaida;
import com.app.foodguard.repository.DoacaoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DoacaoService {
    private List<Doacao> doacaoList;

    public DoacaoService() {
        doacaoList = DoacaoRepository.load(); // Carregar dados existentes
    }

    public List<Doacao> getAllDoacao() {
        return doacaoList;
    }

    public void addDoacao(int ongId, Lote lote, float quantidade) {
        // Criar movimentação
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setLoteId(lote.getId());
        movimentacao.setReceitaId(0);
        movimentacao.setTipo(TipoSaida.DOACAO);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setData(LocalDate.now());

        int movimentacaoId = new MovimentacaoService().addMovimentacao(movimentacao);

        // Atualizar lote
        new LoteService().updateQuantidadeAtual(lote.getId(), quantidade, true);

        // Criar doação
        Doacao doacao = new Doacao();
        doacao.setId(generateNextId());
        doacao.setOngId(ongId);
        doacao.setMovimentacaoId(movimentacaoId);

        doacaoList.add(doacao);
        DoacaoRepository.save(doacaoList);
    }

    public void removeDoacao(Doacao doacao) {

        Movimentacao movimentacao = new MovimentacaoService().getMovimentacaoById(doacao.getMovimentacaoId());
        if (movimentacao != null) {
            new MovimentacaoService().removeMovimentacao(doacao.getMovimentacaoId());
            new LoteService().updateQuantidadeAtual(movimentacao.getLoteId(), movimentacao.getQuantidade(), false);
        }

        doacaoList.removeIf(o -> o.getId() == doacao.getId());
        DoacaoRepository.save(doacaoList);
    }


    public void updateDoacao(Doacao updatedDoacao) {
        for (int i = 0; i < doacaoList.size(); i++) {
            if (doacaoList.get(i).getId() == updatedDoacao.getId()) {
                doacaoList.set(i, updatedDoacao);
                break;
            }
        }
        DoacaoRepository.save(doacaoList);
    }

    private int generateNextId() {
        return doacaoList.stream().mapToInt(Doacao::getId).max().orElse(0) + 1;
    }
}

