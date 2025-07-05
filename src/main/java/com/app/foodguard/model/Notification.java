package com.app.foodguard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor

public class Notification {
    private Lote lote;
    private boolean lido;

    public Notification(Lote lote, boolean lido) {
        this.lote = lote;
        this.lido = lido;
    }
}
