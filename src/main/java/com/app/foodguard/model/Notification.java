package com.app.foodguard.model;

public class Notification {
    private String mensagem;
    private boolean lido;

    public Notification(String mensagem, boolean lido) {
        this.mensagem = mensagem;
        this.lido = lido;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean isLido() {
        return lido;
    }

    public void setLido(boolean lido) {
        this.lido = lido;
    }
}