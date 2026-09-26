package com.escola.cantina.model;

/**
 * Representa um aviso na Central de Avisos (Tela 6), tanto os de "Hoje"
 * (não lidos) quanto os "Anteriores" (já lidos). Ver observação no README
 * sobre criar uma tabela "avisos" própria para persistir isso; enquanto ela
 * não existir, esses objetos podem ser gerados em memória comparando
 * usuarios.saldo / uso do mês com os limites configurados.
 */
public class Aviso {

    public enum Tipo { SALDO_BAIXO, LIMITE, CONTA }

    private long id;
    private Tipo tipo;
    private String titulo;
    private String descricao;
    private boolean lido;
    private Integer idFilhoRelacionado; // usuarios.id (nullable)

    public Aviso(long id, Tipo tipo, String titulo, String descricao, boolean lido, Integer idFilhoRelacionado) {
        this.id = id;
        this.tipo = tipo;
        this.titulo = titulo;
        this.descricao = descricao;
        this.lido = lido;
        this.idFilhoRelacionado = idFilhoRelacionado;
    }

    public long getId() { return id; }
    public Tipo getTipo() { return tipo; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public boolean isLido() { return lido; }
    public void setLido(boolean lido) { this.lido = lido; }
    public Integer getIdFilhoRelacionado() { return idFilhoRelacionado; }
}
