package com.app.foodguard.service;

import com.app.foodguard.controller.DTO.DesperdicioDTO;
import com.app.foodguard.model.Alimento;
import com.app.foodguard.model.Desperdicio;
import com.app.foodguard.model.Lote;
import com.app.foodguard.model.Movimentacao;
import com.app.foodguard.repository.DesperdicioRepository;

import java.util.ArrayList;
import java.util.List;

public class DesperdicioService {
    private List<DesperdicioDTO> desperdicioList;
    private final DesperdicioRepository desperdicioRepository;

    public DesperdicioService() {
        desperdicioRepository = new DesperdicioRepository();
        desperdicioList = loadDesperdicios(); // Carrega na inicialização
    }

    public List<DesperdicioDTO> getAllDesperdicios() {
        // Retorna lista atualizada
        desperdicioList = loadDesperdicios();
        return desperdicioList;
    }

    public void addDesperdicio(DesperdicioDTO desperdicioDTO) {
        // salvar movimentacao
        int movimentacaoId = new MovimentacaoService().addMovimentacao(new Movimentacao(desperdicioDTO));

        // salvar desperdicio
        desperdicioDTO.setMovimentacaoId(movimentacaoId);
        desperdicioDTO.setId(generateNextId());

        List<Desperdicio> newList = desperdicioRepository.load();
        newList.add(new Desperdicio(desperdicioDTO));
        desperdicioRepository.save(newList);

        // atualizar quantidade atual do lote
        new LoteService().updateQuantidadeAtual(desperdicioDTO.getLoteId(), desperdicioDTO.getQuantidade(), true);

        // Atualiza lista em memória
        desperdicioList = loadDesperdicios();
    }

    public void removeDesperdicio(DesperdicioDTO desperdicioDTO) {
        // remove movimentacao
        new MovimentacaoService().removeMovimentacao(desperdicioDTO.getMovimentacaoId());

        // remove desperdício
        List<Desperdicio> newList = desperdicioRepository.load();
        newList.removeIf(a -> a.getId() == desperdicioDTO.getId());
        desperdicioRepository.save(newList);

        // atualizar quantidade atual do lote
        new LoteService().updateQuantidadeAtual(desperdicioDTO.getLoteId(), desperdicioDTO.getQuantidade(), false);

        // Atualiza lista em memória
        desperdicioList = loadDesperdicios();
    }

    private List<DesperdicioDTO> loadDesperdicios() {
        List<Desperdicio> desperdicios = desperdicioRepository.load();
        List<Movimentacao> movimentacoes = new MovimentacaoService().getAllMovimentacoes();
        List<Lote> lotes = new LoteService().getAllLotes();
        List<Alimento> alimentos = new AlimentoService().getAllFoods();

        List<DesperdicioDTO> desperdicioDTOs = new ArrayList<>();

        for (Desperdicio desperdicio : desperdicios) {
            DesperdicioDTO dto = new DesperdicioDTO();
            Movimentacao movimentacao = movimentacoes.stream()
                    .filter(m -> m.getId() == desperdicio.getMovimentacaoId())
                    .findFirst()
                    .orElse(null);

            if (movimentacao == null) continue;

            Lote lote = lotes.stream()
                    .filter(l -> l.getId() == movimentacao.getLoteId())
                    .findFirst()
                    .orElse(null);

            if (lote == null) continue;

            Alimento alimento = alimentos.stream()
                    .filter(x -> x.getId() == lote.getAlimentoId())
                    .findFirst()
                    .orElse(null);

            if (alimento == null) continue;

            dto.setAlimento(alimento.getNome());
            dto.setQuantidade(movimentacao.getQuantidade());
            dto.setData(movimentacao.getData());
            dto.setUnidadeMedida(alimento.getUnidadeMedida());
            dto.setLoteId(movimentacao.getLoteId());
            dto.setObservacao(desperdicio.getObservacao());
            dto.setMotivo(desperdicio.getMotivo());
            dto.setId(desperdicio.getId());
            dto.setMovimentacaoId(desperdicio.getMovimentacaoId());

            desperdicioDTOs.add(dto);
        }

        return desperdicioDTOs;
    }

    private int generateNextId() {
        List<Desperdicio> all = desperdicioRepository.load();
        return all.stream().mapToInt(Desperdicio::getId).max().orElse(0) + 1;
    }
}
