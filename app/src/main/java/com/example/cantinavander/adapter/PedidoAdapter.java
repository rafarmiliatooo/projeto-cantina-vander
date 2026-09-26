package com.example.cantinavander.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cantinavander.R;
import com.example.cantinavander.model.PedidoResumo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    public interface OnPedidoClickListener {
        void onPedidoClick(PedidoResumo pedido);
    }

    private final List<PedidoResumo> listaCompleta;
    private final List<PedidoResumo> listaExibida;
    private final OnPedidoClickListener listener;

    public PedidoAdapter(List<PedidoResumo> pedidos, OnPedidoClickListener listener) {
        this.listaCompleta = new ArrayList<>(pedidos);
        this.listaExibida = new ArrayList<>(pedidos);
        this.listener = listener;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido_antecipado, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        PedidoResumo pedido = listaExibida.get(position);

        holder.tvAvatarIniciais.setText(pedido.getIniciais());
        holder.tvNomeAluno.setText(pedido.getNomeAluno());
        holder.tvTurmaAluno.setText(pedido.getTurma());
        holder.tvProdutoResumo.setText(pedido.getResumoItens());
        holder.tvCodigoPedido.setText(pedido.getCodigoRetirada());

        aplicarStatus(holder, pedido.getStatus());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPedidoClick(pedido);
            }
        });
    }

    private void aplicarStatus(PedidoViewHolder holder, String status) {
        String label;
        int corTexto;
        int corFundo;

        switch (status == null ? "" : status) {
            case "entregue":
                label = "Entregue";
                corTexto = R.color.green_success_text;
                corFundo = R.drawable.bg_badge_green;
                break;
            case "cancelado":
                label = "Cancelado";
                corTexto = R.color.red_alert_text;
                corFundo = R.drawable.bg_badge_red;
                break;
            case "pendente":
                label = "Pendente";
                corTexto = R.color.orange_warning_text;
                corFundo = R.drawable.bg_badge_orange;
                break;
            case "em_preparacao":
            default:
                label = "Preparando";
                corTexto = R.color.orange_warning_text;
                corFundo = R.drawable.bg_badge_orange;
                break;
        }

        holder.tvStatusPedido.setText(label);
        holder.tvStatusPedido.setTextColor(
                holder.itemView.getContext().getColor(corTexto));
        holder.tvStatusPedido.setBackgroundResource(corFundo);
    }

    @Override
    public int getItemCount() {
        return listaExibida.size();
    }
    public void atualizarLista(List<PedidoResumo> novaLista) {
        listaCompleta.clear();
        listaCompleta.addAll(novaLista);
        listaExibida.clear();
        listaExibida.addAll(novaLista);
        notifyDataSetChanged();
    }

    public void filtrar(String status, String textoBusca) {
        listaExibida.clear();
        String busca = textoBusca == null ? "" : textoBusca.trim().toLowerCase(Locale.getDefault());

        for (PedidoResumo pedido : listaCompleta) {
            boolean statusOk = (status == null) || status.equals(pedido.getStatus());
            boolean buscaOk = busca.isEmpty()
                    || pedido.getNomeAluno().toLowerCase(Locale.getDefault()).contains(busca)
                    || pedido.getCodigoRetirada().toLowerCase(Locale.getDefault()).contains(busca)
                    || (pedido.getTurma() != null
                        && pedido.getTurma().toLowerCase(Locale.getDefault()).contains(busca));

            if (statusOk && buscaOk) {
                listaExibida.add(pedido);
            }
        }
        notifyDataSetChanged();
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvAvatarIniciais;
        TextView tvNomeAluno;
        TextView tvTurmaAluno;
        TextView tvProdutoResumo;
        TextView tvCodigoPedido;
        TextView tvStatusPedido;

        PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAvatarIniciais = itemView.findViewById(R.id.tv_avatar_iniciais);
            tvNomeAluno = itemView.findViewById(R.id.tv_nome_aluno);
            tvTurmaAluno = itemView.findViewById(R.id.tv_turma_aluno);
            tvProdutoResumo = itemView.findViewById(R.id.tv_produto_resumo);
            tvCodigoPedido = itemView.findViewById(R.id.tv_codigo_pedido);
            tvStatusPedido = itemView.findViewById(R.id.tv_status_pedido);
        }
    }
}
