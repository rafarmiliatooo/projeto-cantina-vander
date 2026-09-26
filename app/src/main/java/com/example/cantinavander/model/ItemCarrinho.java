package com.example.cantinavander.model;

public class ItemCarrinho {

    private final Produto produto;
    private int quantidade;

    public ItemCarrinho(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void incrementar() {
        this.quantidade++;
    }

    public void decrementar() {
        if (this.quantidade > 0) {
            this.quantidade--;
        }
    }

    public double getSubtotal() {
        return produto.getPreco() * quantidade;
    }
}
