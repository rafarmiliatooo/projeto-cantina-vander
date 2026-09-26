package com.escola.cantina.adapter;

/** Interface genérica de clique usada por todos os adapters deste projeto. */
public interface OnItemClickListener<T> {
    void onItemClick(T item);
}
