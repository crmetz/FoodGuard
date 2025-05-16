package com.app.foodguard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alimento {
    private int id;
    private String nome;
    private LocalDate dataValidade;
    private float quantidade;
    private String unidadeMedida;
    private String marca;
    private int categoriaId;
    private String codigoDeBarras;
    private String observacoes;
    private String imagem;
}
