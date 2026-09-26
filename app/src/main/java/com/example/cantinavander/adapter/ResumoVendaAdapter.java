package com.example.cantinavander.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cantinavander.R;
import com.example.cantinavander.model.ItemCarrinho;
import com.example.cantinavander.model.Produto;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ResumoVendaAdapter extends RecyclerView.Adapter<ResumoVendaAdapter.ResumoViewHolder> {

    private final List<ItemCarrinho> itens;
    private final NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public ResumoVendaAdapter(List<ItemCarrinho> itens) {
        this.itens = itens;
    }

    @NonNull
    @Override
    public ResumoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resumo_venda, parent, false);
        return new ResumoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResumoViewHolder holder, int position) {
        ItemCarrinho item = itens.get(position);

        holder.tvQtdNomeProduto.setText(item.getQuantidade() + "× " + item.getProduto().getNome());
        holder.tvDetalheProduto.setText(formatoMoeda.format(item.getProduto().getPreco()) + " cada");
        holder.tvValorLinha.setText(formatoMoeda.format(item.getSubtotal()));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    static class ResumoViewHolder extends RecyclerView.ViewHolder {
        TextView tvQtdNomeProduto;
        TextView tvDetalheProduto;
        TextView tvValorLinha;

        ResumoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQtdNomeProduto = itemView.findViewById(R.id.tv_qtd_nome_produto);
            tvDetalheProduto = itemView.findViewById(R.id.tv_detalhe_produto);
            tvValorLinha = itemView.findViewById(R.id.tv_valor_linha);
        }
    }
}
