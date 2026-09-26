package com.escola.cantina.model;

/**
 * Total agrupado por produtos.categoria dentro do mês de referência do
 * consolidado (Tela 4). Vem de um GROUP BY em itens_pedido/itens_venda_balcao.
 */
public class Categoria {
    private String nome;     // ex.: "Lanches"
    private int qtdItens;    // ex.: 21
    private double valorTotal;

    public Categoria(String nome, int qtdItens, double valorTotal) {
        this.nome = nome;
        this.qtdItens = qtdItens;
        this.valorTotal = valorTotal;
    }

    public String getNome() { return nome; }
    public int getQtdItens() { return qtdItens; }
    public double getValorTotal() { return valorTotal; }
}
