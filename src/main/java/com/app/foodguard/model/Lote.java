package com.app.foodguard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lote {
    private int id;
    private int alimentoId;
    private float qtdInicial;
    private float qtdAtual;
    private LocalDate dataValidade;
    private LocalDate dataEntrada;
    private String codigo;
}
