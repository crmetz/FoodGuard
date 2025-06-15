package com.app.foodguard.model;

import com.app.foodguard.controller.DTO.DesperdicioDTO;
import com.app.foodguard.model.enums.TipoSaida;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movimentacao {

    public Movimentacao (DesperdicioDTO desperdicioDTO) {
        this.loteId = desperdicioDTO.getLoteId();
        this.tipo = TipoSaida.DESPERDICIO;
        this.quantidade = desperdicioDTO.getQuantidade();
        this.data = LocalDate.now();
    }


    private int id;
    private int loteId;
    private TipoSaida tipo;
    private float quantidade;
    private LocalDate  data;
}
