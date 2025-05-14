package com.app.foodguard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class Categoria {

    private int id;
    private String descricao;
    private String ativo; // "Sim" ou "Não"

    public Categoria(int id, String descricao, String ativo) {
        this.id = id;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
