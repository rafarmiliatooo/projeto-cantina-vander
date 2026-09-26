package com.escola.cantina.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.escola.cantina.model.Aviso;

import java.util.List;

/**
 * Alimenta o RecyclerView "recyclerViewAvisosHoje" da Tela 6
 * (activity_notifications_center.xml) usando item_aviso_hoje.xml.
 * A cor/ícone do card muda conforme Aviso.Tipo (SALDO_BAIXO, LIMITE, CONTA).
 */
public class AvisoHojeAdapter extends RecyclerView.Adapter<AvisoHojeAdapter.AvisoViewHolder> {

    private final List<Aviso> avisos;
    private final OnItemClickListener<Aviso> listener;

    public AvisoHojeAdapter(List<Aviso> avisos, OnItemClickListener<Aviso> listener) {
        this.avisos = avisos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AvisoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_list_item, parent, false);
        return new AvisoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvisoViewHolder holder, int position) {
        Aviso aviso = avisos.get(position);
        holder.tvTitulo.setText(aviso.getTitulo());
        holder.tvDescricao.setText(aviso.getDescricao());

        switch (aviso.getTipo()) {
            case SALDO_BAIXO:
                holder.root.setBackgroundResource(R.drawable.bg_card_yellow);
                holder.ivIcone.setImageResource(R.drawable.ic_warning);
                break;
            case LIMITE:
                holder.root.setBackgroundResource(R.drawable.bg_card_yellow);
                holder.ivIcone.setImageResource(R.drawable.ic_trend);
                break;
            case CONTA:
                holder.root.setBackgroundResource(R.drawable.bg_card_purple_soft);
                holder.ivIcone.setImageResource(R.drawable.ic_receipt);
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(aviso);
        });
    }

    @Override
    public int getItemCount() {
        return avisos.size();
    }

    static class AvisoViewHolder extends RecyclerView.ViewHolder {
        View root;
        ImageView ivIcone;
        TextView tvTitulo, tvDescricao;

        AvisoViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.rootItemAvisoHoje);
            ivIcone = itemView.findViewById(R.id.ivIconeAvisoHoje);
            tvTitulo = itemView.findViewById(R.id.tvTituloAvisoHoje);
            tvDescricao = itemView.findViewById(R.id.tvDescricaoAvisoHoje);
        }
    }
}
