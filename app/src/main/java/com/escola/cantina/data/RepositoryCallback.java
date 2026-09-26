package com.escola.cantina.data;

/** Callback genérico usado pelo CantinaRepository para devolver resultado na Main Thread. */
public interface RepositoryCallback<T> {
    void onSuccess(T resultado);
    void onError(Exception erro);
}
