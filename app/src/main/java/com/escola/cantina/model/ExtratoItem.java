package com.escola.cantina.model;

/**
 * Uma linha do extrato do filho (Tela 2), vinda da união de "pedidos",
 * "vendas_balcao" (débitos) e lançamentos de crédito manual (créditos).
 */
public class ExtratoItem {

    public enum Tipo { DEBITO, CREDITO }

    private String descricao;   // ex.: "Cantina • Pedido A184" ou "Crédito adicionado"
    private String dataHora;    // ex.: "01 set • 09:00"
    private double valor;       // sempre positivo; o sinal é decidido pelo "tipo"
    private Tipo tipo;

    public ExtratoItem(String descricao, String dataHora, double valor, Tipo tipo) {
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.valor = valor;
        this.tipo = tipo;
    }

    public String getDescricao() { return descricao; }
    public String getDataHora() { return dataHora; }
    public double getValor() { return valor; }
    public Tipo getTipo() { return tipo; }
}
