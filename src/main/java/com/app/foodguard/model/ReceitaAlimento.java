package com.app.foodguard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceitaAlimento {
    private int idReceita;
    private int idAlimento;
    private float quantidade;
}