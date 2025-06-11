package com.app.foodguard.model;

import kotlin.text.UStringsKt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doacao {
    private int id;
    private Ong Ong;
    private Lote Lote;
    private float quantidade;
}
