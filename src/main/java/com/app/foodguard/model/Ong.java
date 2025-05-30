package com.app.foodguard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ong {
    private int id;
    private String nome;
    private String contato;
    private String endereco;
}