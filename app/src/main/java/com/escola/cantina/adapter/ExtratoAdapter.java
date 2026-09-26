package com.escola.cantina.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.escola.cantina.R;
import com.escola.cantina.model.ExtratoItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Alimenta o RecyclerView "recyclerViewExtrato" da Tela 2 (activity_child_detail.xml)
 * usando o layout item_extrato.xml. Débitos aparecem em vermelho com sinal "-",
 * créditos em verde com sinal "+".
 */
public class ExtratoAdapter extends RecyclerView.Adapter<ExtratoAdapter.ExtratoViewHolder> {

    private final List<ExtratoItem> itens;
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public ExtratoAdapter(List<ExtratoItem> itens) {
        this.itens = itens;
    }

    @NonNull
    @Override
    public ExtratoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_extrato, parent, false);
        return new ExtratoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtratoViewHolder holder, int position) {
        ExtratoItem item = itens.get(position);

        holder.tvDescricaoExtrato.setText(item.getDescricao());
        holder.tvDataHoraExtrato.setText(item.getDataHora());

        boolean isCredito = item.getTipo() == ExtratoItem.Tipo.CREDITO;
        String sinal = isCredito ? "+" : "-";
        holder.tvValorExtrato.setText(sinal + moeda.format(item.getValor()));
        holder.tvValorExtrato.setTextColor(holder.itemView.getResources()
                .getColor(isCredito ? R.color.green_positive : R.color.red_negative));

        holder.ivIconeExtrato.setImageResource(
                isCredito ? R.drawable.ic_add : R.drawable.ic_receipt);
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    static class ExtratoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIconeExtrato;
        TextView tvDescricaoExtrato, tvDataHoraExtrato, tvValorExtrato;

        ExtratoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIconeExtrato = itemView.findViewById(R.id.ivIconeExtrato);
            tvDescricaoExtrato = itemView.findViewById(R.id.tvDescricaoExtrato);
            tvDataHoraExtrato = itemView.findViewById(R.id.tvDataHoraExtrato);
            tvValorExtrato = itemView.findViewById(R.id.tvValorExtrato);
        }
    }
}
