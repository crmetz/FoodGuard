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
        food.setId(generateNextId());
        foodList.add(food);
        AlimentoRepository.save(foodList);
    }

    public void removeFood(Alimento food) {
        foodList.removeIf(f -> f.getId() == food.getId());
        AlimentoRepository.save(foodList);
    }

    public void updateFood(Alimento updatedFood) {
        for (int i = 0; i < foodList.size(); i++) {
            if (foodList.get(i).getId() == updatedFood.getId()) {
                foodList.set(i, updatedFood);
                break;
            }
        }
        AlimentoRepository.save(foodList);
    }

    private int generateNextId() {
        return foodList.stream().mapToInt(Alimento::getId).max().orElse(0) + 1;
    }
}
