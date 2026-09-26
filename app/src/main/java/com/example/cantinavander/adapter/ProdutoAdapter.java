package com.example.cantinavander.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cantinavander.R;
import com.example.cantinavander.model.Produto;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {
    public interface OnProdutoClickListener {
        void onProdutoClick(Produto produto);
    }

    private final List<Produto> listaCompleta;
    private final List<Produto> listaExibida;
    private final OnProdutoClickListener listener;
    private final NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public ProdutoAdapter(List<Produto> produtos, OnProdutoClickListener listener) {
        this.listaCompleta = new ArrayList<>(produtos);
        this.listaExibida = new ArrayList<>(produtos);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_produto, parent, false);
        return new ProdutoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        Produto produto = listaExibida.get(position);

        holder.tvNomeProduto.setText(produto.getNome());
        holder.tvPrecoProduto.setText(formatoMoeda.format(produto.getPreco()));

        if (produto.isEsgotado()) {
            holder.tvStatusDisponibilidade.setText("Esgotado • oculto para alunos");
            holder.tvBadgeStatus.setText("ESGOTADO");
            holder.tvBadgeStatus.setTextColor(
                    holder.itemView.getContext().getColor(R.color.red_alert_text));
            holder.tvBadgeStatus.setBackgroundResource(R.drawable.bg_badge_red);
            holder.rootItemProduto.setBackgroundResource(R.drawable.bg_item_produto_esgotado);
        } else {
            holder.tvStatusDisponibilidade.setText("Disponível • toque para editar");
            holder.tvBadgeStatus.setText("ATIVO");
            holder.tvBadgeStatus.setTextColor(
                    holder.itemView.getContext().getColor(R.color.green_success_text));
            holder.tvBadgeStatus.setBackgroundResource(R.drawable.bg_badge_green);
            holder.rootItemProduto.setBackgroundResource(R.drawable.bg_card_rounded);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProdutoClick(produto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaExibida.size();
    }

    public void atualizarLista(List<Produto> novaLista) {
        listaCompleta.clear();
        listaCompleta.addAll(novaLista);
        listaExibida.clear();
        listaExibida.addAll(novaLista);
        notifyDataSetChanged();
    }

    public void filtrar(String textoBusca) {
        listaExibida.clear();
        String busca = textoBusca == null ? "" : textoBusca.trim().toLowerCase(Locale.getDefault());

        for (Produto produto : listaCompleta) {
            if (busca.isEmpty() || produto.getNome().toLowerCase(Locale.getDefault()).contains(busca)) {
                listaExibida.add(produto);
            }
        }
        notifyDataSetChanged();
    }

    static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        View rootItemProduto;
        TextView tvNomeProduto;
        TextView tvStatusDisponibilidade;
        TextView tvPrecoProduto;
        TextView tvBadgeStatus;

        ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            rootItemProduto = itemView.findViewById(R.id.root_item_produto);
            tvNomeProduto = itemView.findViewById(R.id.tv_nome_produto);
            tvStatusDisponibilidade = itemView.findViewById(R.id.tv_status_disponibilidade);
            tvPrecoProduto = itemView.findViewById(R.id.tv_preco_produto);
            tvBadgeStatus = itemView.findViewById(R.id.tv_badge_status);
        }
    }
}
