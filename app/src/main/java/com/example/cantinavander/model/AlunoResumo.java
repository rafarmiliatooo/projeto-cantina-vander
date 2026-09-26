package com.example.cantinavander.model;
public class AlunoResumo {

    private int id;
    private String nome;
    private String turma;
    private String cpfMascarado;
    private double saldoDisponivel;

    public AlunoResumo(int id, String nome, String turma, String cpfMascarado, double saldoDisponivel) {
        this.id = id;
        this.nome = nome;
        this.turma = turma;
        this.cpfMascarado = cpfMascarado;
        this.saldoDisponivel = saldoDisponivel;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTurma() {
        return turma;
    }

    public String getCpfMascarado() {
        return cpfMascarado;
    }

    public double getSaldoDisponivel() {
        return saldoDisponivel;
    }

    public boolean isSaldoNegativo() {
        return saldoDisponivel < 0;
    }

    public String getTurmaECpf() {
        return turma + " • CPF " + cpfMascarado;
    }

    public String getIniciais() {
        if (nome == null || nome.trim().isEmpty()) {
            return "?";
        }
        String[] partes = nome.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(partes[0].charAt(0)));
        if (partes.length > 1) {
            sb.append(Character.toUpperCase(partes[partes.length - 1].charAt(0)));
        }
        return sb.toString();
    }
}
