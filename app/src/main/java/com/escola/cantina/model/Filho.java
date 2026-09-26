package com.escola.cantina.model;

public class Filho {
    private int id;              // usuarios.id
    private String nome;         // usuarios.nome
    private String turma;        // usuarios.turma
    private double saldo;        // usuarios.saldo
    private double limiteFiado;  // usuarios.limite_fiado
    private double avisoSaldoBaixo; // valor configurado pelo responsável (ex.: R$ 15,00)

    public Filho(int id, String nome, String turma, double saldo, double limiteFiado, double avisoSaldoBaixo) {
        this.id = id;
        this.nome = nome;
        this.turma = turma;
        this.saldo = saldo;
        this.limiteFiado = limiteFiado;
        this.avisoSaldoBaixo = avisoSaldoBaixo;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTurma() { return turma; }
    public double getSaldo() { return saldo; }
    public double getLimiteFiado() { return limiteFiado; }
    public double getAvisoSaldoBaixo() { return avisoSaldoBaixo; }

    /** Percentual do limite já utilizado (0-100), usado na ProgressBar do card. */
    public int getPercentualDisponivel() {
        if (limiteFiado <= 0) return 0;
        double percentual = (saldo / limiteFiado) * 100.0;
        return (int) Math.max(0, Math.min(100, percentual));
    }

    public boolean isSaldoBaixo() {
        return saldo < avisoSaldoBaixo;
    }

    /** Iniciais para o avatar, ex.: "Lucas Mendes" -> "LM". */
    public String getIniciais() {
        String[] partes = nome.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < partes.length && sb.length() < 2; i++) {
            if (!partes[i].isEmpty()) sb.append(Character.toUpperCase(partes[i].charAt(0)));
        }
        return sb.toString();
    }
}
