package com.app.foodguard.controller.DTO;

import com.app.foodguard.model.Desperdicio;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class DesperdicioDTO extends Desperdicio {
    private int loteId;
    private String alimento;
    private float quantidade;
    private String unidadeMedida;
    private LocalDate data;
}
