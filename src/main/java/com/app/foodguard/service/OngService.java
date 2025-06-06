package com.app.foodguard.service;

import com.app.foodguard.model.Ong;
import com.app.foodguard.repository.OngRepository;

import java.util.ArrayList;
import java.util.List;

public class OngService {
    private List<Ong> ongList;

    public OngService() {
        ongList = OngRepository.load(); // Carregar dados existentes
    }

    public List<Ong> getAllOngs() {
        return ongList;
    }

    public void addOng(Ong ong) {
        if (ongList == null) {
            ongList = new ArrayList<>();
        }
        ong.setId(generateNextId());
        ongList.add(ong);
        OngRepository.save(ongList);
    }

    public void removeOng(Ong ong) {
        ongList.removeIf(o -> o.getId() == ong.getId());
        OngRepository.save(ongList);
    }

    public void updateOng(Ong updatedOng) {
        for (int i = 0; i < ongList.size(); i++) {
            if (ongList.get(i).getId() == updatedOng.getId()) {
                ongList.set(i, updatedOng);
                break;
            }
        }
        OngRepository.save(ongList);
    }

    private int generateNextId() {
        return ongList.stream().mapToInt(Ong::getId).max().orElse(0) + 1;
    }
}