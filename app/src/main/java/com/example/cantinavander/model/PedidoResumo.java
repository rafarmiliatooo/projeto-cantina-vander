package com.example.cantinavander.model;

public class PedidoResumo {

    private int id;
    private String codigoRetirada;
    private String nomeAluno;
    private String turma;
    private String resumoItens;
    private String status;

    public PedidoResumo(int id, String codigoRetirada, String nomeAluno,
                         String turma, String resumoItens, String status) {
        this.id = id;
        this.codigoRetirada = codigoRetirada;
        this.nomeAluno = nomeAluno;
        this.turma = turma;
        this.resumoItens = resumoItens;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getCodigoRetirada() {
        return codigoRetirada;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public String getTurma() {
        return turma;
    }

    public String getResumoItens() {
        return resumoItens;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIniciais() {
        if (nomeAluno == null || nomeAluno.trim().isEmpty()) {
            return "?";
        }
        String[] partes = nomeAluno.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(partes[0].charAt(0)));
        if (partes.length > 1) {
            sb.append(Character.toUpperCase(partes[partes.length - 1].charAt(0)));
        }
        return sb.toString();
    }
}
