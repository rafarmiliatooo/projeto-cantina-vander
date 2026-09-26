package com.example.cantinavander.model;


public class ItemPedidoView {

    private String nomeProduto;  // produtos.nome
    private int quantidade;      // itens_pedido.quantidade
    private double precoUnitario; // itens_pedido.preco_unitario
    private String observacao;   // texto livre, ex: "Sem alterações" / "Restrição: sem açúcar"

    public ItemPedidoView(String nomeProduto, int quantidade, double precoUnitario, String observacao) {
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.observacao = observacao;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public String getObservacao() {
        return observacao;
    }

    /** Ex: "1× Sanduíche natural" */
    public String getDescricaoQuantidade() {
        return quantidade + "× " + nomeProduto;
    }

    public double getPrecoTotal() {
        return quantidade * precoUnitario;
    }
}
