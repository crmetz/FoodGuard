package com.app.foodguard.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alimento {
    private String name;
    private int quantity;
    private String category;
}
