package com.app.foodguard.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    private int id;
    private String descricao;
    private String ativo; // "Sim" ou "Não"

    @Override
    public String toString() {
        return descricao;
    }
}
