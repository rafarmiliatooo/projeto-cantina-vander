package com.example.cantinavander.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cantinavander.R;
import com.example.cantinavander.model.ItemPedidoView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ItemPedidoAdapter extends RecyclerView.Adapter<ItemPedidoAdapter.ItemViewHolder> {

    private final List<ItemPedidoView> itens;
    private final NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public ItemPedidoAdapter(List<ItemPedidoView> itens) {
        this.itens = itens;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_item_pedido, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemPedidoView item = itens.get(position);

        holder.tvQtdProduto.setText(item.getDescricaoQuantidade());

        String obs = item.getObservacao();
        holder.tvObsItem.setText((obs == null || obs.trim().isEmpty()) ? "Sem alterações" : obs);

        holder.tvPrecoItem.setText(formatoMoeda.format(item.getPrecoTotal()));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvQtdProduto;
        TextView tvObsItem;
        TextView tvPrecoItem;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQtdProduto = itemView.findViewById(R.id.tv_qtd_produto);
            tvObsItem = itemView.findViewById(R.id.tv_obs_item);
            tvPrecoItem = itemView.findViewById(R.id.tv_preco_item);
        }
    }
}
