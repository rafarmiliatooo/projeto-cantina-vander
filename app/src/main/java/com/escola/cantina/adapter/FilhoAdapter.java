package com.escola.cantina.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.escola.cantina.R;
import com.escola.cantina.model.Filho;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FilhoAdapter extends RecyclerView.Adapter<FilhoAdapter.FilhoViewHolder> {

    private final List<Filho> filhos;
    private final OnItemClickListener<Filho> listener;
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public FilhoAdapter(List<Filho> filhos, OnItemClickListener<Filho> listener) {
        this.filhos = filhos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_filho, parent, false);
        return new FilhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilhoViewHolder holder, int position) {
        Filho filho = filhos.get(position);

        holder.tvIniciaisFilho.setText(filho.getIniciais());
        holder.tvNomeFilho.setText(filho.getNome());
        holder.tvTurmaSaldo.setText(
                String.format("%s • Saldo %s", filho.getTurma(), moeda.format(filho.getSaldo())));
        holder.progressSaldoFilho.setMax(100);
        holder.progressSaldoFilho.setProgress(filho.getPercentualDisponivel());
        holder.tvDetalheSaldoFilho.setText(
                String.format("%s disponíveis de %s",
                        moeda.format(filho.getSaldo()), moeda.format(filho.getLimiteFiado())));

        // Troca a cor do avatar e a barra quando o saldo está abaixo do aviso configurado
        if (filho.isSaldoBaixo()) {
            holder.avatarBg.setBackgroundResource(R.drawable.bg_avatar_circle_orange);
            holder.progressSaldoFilho.setProgressDrawable(
                    holder.itemView.getContext().getDrawable(R.drawable.progress_saldo_baixo));
        } else {
            holder.avatarBg.setBackgroundResource(R.drawable.bg_avatar_circle_green);
            holder.progressSaldoFilho.setProgressDrawable(
                    holder.itemView.getContext().getDrawable(R.drawable.progress_saldo));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(filho);
        });
    }

    @Override
    public int getItemCount() {
        return filhos.size();
    }

    static class FilhoViewHolder extends RecyclerView.ViewHolder {
        View avatarBg; // FrameLayout do avatar (primeiro filho do card)
        TextView tvIniciaisFilho, tvNomeFilho, tvTurmaSaldo, tvDetalheSaldoFilho;
        ProgressBar progressSaldoFilho;
        ImageView ivChevronFilho;

        FilhoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIniciaisFilho = itemView.findViewById(R.id.tvIniciaisFilho);
            tvNomeFilho = itemView.findViewById(R.id.tvNomeFilho);
            tvTurmaSaldo = itemView.findViewById(R.id.tvTurmaSaldo);
            progressSaldoFilho = itemView.findViewById(R.id.progressSaldoFilho);
            tvDetalheSaldoFilho = itemView.findViewById(R.id.tvDetalheSaldoFilho);
            ivChevronFilho = itemView.findViewById(R.id.ivChevronFilho);
        }
    }
}
