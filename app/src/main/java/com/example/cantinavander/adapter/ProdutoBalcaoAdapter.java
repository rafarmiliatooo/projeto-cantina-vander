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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ProdutoBalcaoAdapter extends RecyclerView.Adapter<ProdutoBalcaoAdapter.ProdutoViewHolder> {

    public interface OnCarrinhoAlteradoListener {
        void onCarrinhoAlterado(List<ItemCarrinho> carrinhoAtual);
    }

    private final List<Produto> produtos;
    private final Map<Integer, Integer> quantidadesPorProdutoId = new LinkedHashMap<>();
    private final OnCarrinhoAlteradoListener listener;
    private final NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public ProdutoBalcaoAdapter(List<Produto> produtos, OnCarrinhoAlteradoListener listener) {
        this.produtos = produtos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_produto_balcao, parent, false);
        return new ProdutoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        Produto produto = produtos.get(position);
        int quantidade = quantidadesPorProdutoId.containsKey(produto.getId())
                ? quantidadesPorProdutoId.get(produto.getId()) : 0;

        holder.tvNomeProduto.setText(produto.getNome());
        holder.tvPrecoProduto.setText(formatoMoeda.format(produto.getPreco()));

        if (quantidade > 0) {
            holder.tvSubtituloProduto.setText(quantidade + " no carrinho");
            holder.tvBotaoAdd.setText(String.valueOf(quantidade));
            holder.rootItemProdutoBalcao.setBackgroundResource(R.drawable.bg_item_produto_carrinho);
        } else {
            holder.tvSubtituloProduto.setText("Toque para adicionar");
            holder.tvBotaoAdd.setText("+");
            holder.rootItemProdutoBalcao.setBackgroundResource(R.drawable.bg_card_rounded);
        }

        View.OnClickListener adicionar = v -> {
            int atual = quantidadesPorProdutoId.containsKey(produto.getId())
                    ? quantidadesPorProdutoId.get(produto.getId()) : 0;
            quantidadesPorProdutoId.put(produto.getId(), atual + 1);
            notifyItemChanged(holder.getBindingAdapterPosition());
            notificarCarrinhoAlterado();
        };

        holder.itemView.setOnClickListener(adicionar);
        holder.tvBotaoAdd.setOnClickListener(adicionar);
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public void filtrarPorCategoria(String categoria) {

        notifyDataSetChanged();
    }

    private void notificarCarrinhoAlterado() {
        if (listener == null) {
            return;
        }
        List<ItemCarrinho> carrinho = new ArrayList<>();
        for (Produto produto : produtos) {
            Integer qtd = quantidadesPorProdutoId.get(produto.getId());
            if (qtd != null && qtd > 0) {
                carrinho.add(new ItemCarrinho(produto, qtd));
            }
        }
        listener.onCarrinhoAlterado(carrinho);
    }

    static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        View rootItemProdutoBalcao;
        TextView tvNomeProduto;
        TextView tvSubtituloProduto;
        TextView tvPrecoProduto;
        TextView tvBotaoAdd;

        ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            rootItemProdutoBalcao = itemView.findViewById(R.id.root_item_produto_balcao);
            tvNomeProduto = itemView.findViewById(R.id.tv_nome_produto);
            tvSubtituloProduto = itemView.findViewById(R.id.tv_subtitulo_produto);
            tvPrecoProduto = itemView.findViewById(R.id.tv_preco_produto);
            tvBotaoAdd = itemView.findViewById(R.id.tv_botao_add);
        }

        public int getBindingAdapterPosition() {
            return 0;
        }
    }
}
