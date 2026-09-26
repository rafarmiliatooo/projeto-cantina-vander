package com.escola.cantina.data;

import com.escola.cantina.model.Categoria;
import com.escola.cantina.model.ExtratoItem;
import com.escola.cantina.model.Filho;

import java.util.List;

public interface CantinaRepository {

    void listarFilhos(int idResponsavel, RepositoryCallback<List<Filho>> callback);

    void listarExtrato(int idAluno, int mes, int ano, RepositoryCallback<List<ExtratoItem>> callback);

    void listarCategorias(int idAluno, int mes, int ano, RepositoryCallback<List<Categoria>> callback);

    void adicionarCredito(int idAluno, double valor, RepositoryCallback<Void> callback);

    void pagarConta(int idAluno, double valor, RepositoryCallback<Void> callback);

    void vincularFilho(int idResponsavel, String nomeFilho, String cpfFilho, String senhaEscola,
                        RepositoryCallback<Boolean> callback);

    void definirLimite(int idAluno, double limiteFiado, Double limiteDiario, RepositoryCallback<Void> callback);
}
