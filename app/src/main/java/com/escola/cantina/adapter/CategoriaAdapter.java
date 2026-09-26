package com.escola.cantina.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.escola.cantina.R;
import com.escola.cantina.model.Categoria;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Alimenta o RecyclerView "recyclerViewCategorias" da Tela 4
 * (activity_monthly_statement.xml) usando o layout item_categoria.xml.
 */
public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private final List<Categoria> categorias;
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public CategoriaAdapter(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);
        holder.tvNomeCategoria.setText(categoria.getNome());
        holder.tvQtdItensCategoria.setText(categoria.getQtdItens() + " itens");
        holder.tvValorCategoria.setText(moeda.format(categoria.getValorTotal()));
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeCategoria, tvQtdItensCategoria, tvValorCategoria;

        CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeCategoria = itemView.findViewById(R.id.tvNomeCategoria);
            tvQtdItensCategoria = itemView.findViewById(R.id.tvQtdItensCategoria);
            tvValorCategoria = itemView.findViewById(R.id.tvValorCategoria);
        }
    }
}
