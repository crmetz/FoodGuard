package com.app.foodguard.service;

import com.app.foodguard.model.Alimento;
import com.app.foodguard.repository.AlimentoRepository;

import java.util.ArrayList;
import java.util.List;

public class AlimentoService {
    private List<Alimento> foodList;

    public AlimentoService() {
        foodList = AlimentoRepository.load(); // Load existing data
    }

    public List<Alimento> getAllFoods() {
        return foodList;
    }

    public void addFood(Alimento food) {
        if (foodList == null) {
            foodList = new ArrayList<>();
        }
        foodList.add(food);
        AlimentoRepository.save(foodList);
    }

    public void removeFood(Alimento food) {
        foodList.remove(food);
        AlimentoRepository.save(foodList);
    }
}
