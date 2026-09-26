package com.example.cantinavander.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cantinavander.R;
import com.example.cantinavander.model.AlunoResumo;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

 class AlunoResumoAdapter extends RecyclerView.Adapter<AlunoResumoAdapter.AlunoViewHolder> {

    public interface OnAlunoSelecionadoListener {
        void onAlunoSelecionado(AlunoResumo aluno);
    }

    private final List<AlunoResumo> alunos;
    private final OnAlunoSelecionadoListener listener;
    private final NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public AlunoResumoAdapter(List<AlunoResumo> alunos, OnAlunoSelecionadoListener listener) {
        this.alunos = alunos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlunoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resultado_aluno, parent, false);
        return new AlunoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlunoViewHolder holder, int position) {
        AlunoResumo aluno = alunos.get(position);

        holder.tvNomeAluno.setText(aluno.getNome());
        holder.tvTurmaCpf.setText(aluno.getTurmaECpf());

        String valorFormatado = formatoMoeda.format(Math.abs(aluno.getSaldoDisponivel()));
        if (aluno.isSaldoNegativo()) {
            holder.tvValorSaldo.setText("-" + valorFormatado);
            holder.tvValorSaldo.setTextColor(
                    holder.itemView.getContext().getColor(R.color.red_alert_text));
        } else {
            holder.tvValorSaldo.setText(valorFormatado);
            holder.tvValorSaldo.setTextColor(
                    holder.itemView.getContext().getColor(R.color.green_success_text));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAlunoSelecionado(aluno);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }

    static class AlunoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeAluno;
        TextView tvTurmaCpf;
        TextView tvValorSaldo;

        AlunoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeAluno = itemView.findViewById(R.id.tv_nome_aluno);
            tvTurmaCpf = itemView.findViewById(R.id.tv_turma_cpf);
            tvValorSaldo = itemView.findViewById(R.id.tv_valor_saldo);
        }
    }
}
