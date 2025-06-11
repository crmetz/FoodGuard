package com.app.foodguard.service;

import com.app.foodguard.model.Doacao;
import com.app.foodguard.model.Ong;
import com.app.foodguard.repository.DoacaoRepository;
import com.app.foodguard.repository.OngRepository;

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

    public void addDoacao(Doacao doacao) {
        if (doacaoList == null) {
            doacaoList = new ArrayList<>();
        }
        doacao.setId(generateNextId());
        doacaoList.add(doacao);
        DoacaoRepository.save(doacaoList);
    }

    public void removeDoacao(Doacao doacao) {
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

