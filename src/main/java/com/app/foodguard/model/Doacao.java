package com.app.foodguard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doacao {
    private int id;
    private int ongId;
    private int movimentacaoId;
}
