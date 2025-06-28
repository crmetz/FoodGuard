package com.app.foodguard.service;

import com.app.foodguard.model.Lote;
import com.app.foodguard.model.Notification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    public List<Notification> verificarValidade(List<Lote> lotes) {
        List<Notification> notificacoes = new ArrayList<>();
        LocalDate hoje = LocalDate.now();

        for (Lote lote : lotes) {
            if (lote.getDataValidade().isBefore(hoje.plusDays(2))) {
                String mensagem = String.format("O alimento %s está prestes a vencer, data de validade: %s",
                        lote.getCodigo(), lote.getDataValidade());
                notificacoes.add(new Notification(mensagem, false));
            }
        }

        return notificacoes;
    }
}