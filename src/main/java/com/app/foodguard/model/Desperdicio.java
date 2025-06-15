package com.app.foodguard.model;

import com.app.foodguard.controller.DTO.DesperdicioDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Desperdicio {

    public Desperdicio(DesperdicioDTO desperdicioDTO) {
        this.id = desperdicioDTO.getId();
        this.movimentacaoId = desperdicioDTO.getMovimentacaoId();
        this.motivo = desperdicioDTO.getMotivo();
        this.observacao = desperdicioDTO.getObservacao();
    }

    private int id;
    private int movimentacaoId;
    private String motivo;
    private String observacao;
}
