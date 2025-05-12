package com.app.foodguard.model;

public class Categoria {

    private int id;
    private String descricao;
    private String ativo; // "Sim" ou "Não"

    public Categoria(int id, String descricao, String ativo) {
        this.id = id;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public String toCSV() {
        return id + "," + descricao + "," + ativo;
    }

    public static Categoria fromCSV(String linha) {
        String[] partes = linha.split(",");
        if (partes.length >= 3) {
            int id = Integer.parseInt(partes[0]);
            String descricao = partes[1];
            String ativo = partes[2];
            return new Categoria(id, descricao, ativo);
        }
        return null;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
