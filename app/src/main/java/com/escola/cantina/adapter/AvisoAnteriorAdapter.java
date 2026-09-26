package com.escola.cantina.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.escola.cantina.R;
import com.escola.cantina.model.Aviso;

import java.util.List;

public class AvisoAnteriorAdapter extends RecyclerView.Adapter<AvisoAnteriorAdapter.AvisoAnteriorViewHolder> {

    private final List<Aviso> avisos;
    private final OnItemClickListener<Aviso> listener;

    public AvisoAnteriorAdapter(List<Aviso> avisos, OnItemClickListener<Aviso> listener) {
        this.avisos = avisos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AvisoAnteriorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aviso_anterior, parent, false);
        return new AvisoAnteriorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvisoAnteriorViewHolder holder, int position) {
        Aviso aviso = avisos.get(position);
        holder.tvTitulo.setText(aviso.getTitulo());
        holder.tvDescricao.setText(aviso.getDescricao());

        switch (aviso.getTipo()) {
            case SALDO_BAIXO:
            case LIMITE:
                holder.ivIcone.setImageResource(R.drawable.ic_warning);
                break;
            case CONTA:
                holder.ivIcone.setImageResource(R.drawable.ic_check_circle);
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

    static class AvisoAnteriorViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcone;
        TextView tvTitulo, tvDescricao;

        AvisoAnteriorViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcone = itemView.findViewById(R.id.ivIconeAvisoAnterior);
            tvTitulo = itemView.findViewById(R.id.tvTituloAvisoAnterior);
            tvDescricao = itemView.findViewById(R.id.tvDescricaoAvisoAnterior);
        }
    }
}
