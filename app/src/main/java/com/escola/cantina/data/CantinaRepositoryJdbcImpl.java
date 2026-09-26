package com.escola.cantina.data;

import android.os.Handler;
import android.os.Looper;

import com.escola.cantina.model.Categoria;
import com.escola.cantina.model.ExtratoItem;
import com.escola.cantina.model.Filho;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ATENÇÃO: esta implementação conecta direto no MySQL usando JDBC.
 * É útil para testar os RecyclerViews rapidamente em um protótipo, mas
 * NÃO é recomendada em produção (exporia usuário/senha do banco dentro do
 * APK e não funciona bem em rede móvel/instável). O ideal é publicar uma
 * API REST (Node, Spring Boot, PHP etc.) na frente do MySQL e trocar esta
 * classe por uma implementação com Retrofit chamando essa API.
 *
 * Requer no build.gradle:
 *   implementation 'com.mysql:mysql-connector-j:8.3.0'
 * E permissão de internet no AndroidManifest:
 *   <uses-permission android:name="android.permission.INTERNET" />
 */
public class CantinaRepositoryJdbcImpl implements CantinaRepository {

    private static final String URL = "jdbc:mysql://SEU_HOST:3306/cantina_escolar_vanders";
    private static final String USER = "SEU_USUARIO";
    private static final String PASS = "SUA_SENHA";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private Connection abrirConexao() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }

    @Override
    public void listarFilhos(int idResponsavel, RepositoryCallback<List<Filho>> callback) {
        executor.execute(() -> {
            try (Connection conn = abrirConexao()) {
                String sql = "SELECT u.id, u.nome, u.turma, u.saldo, u.limite_fiado " +
                        "FROM aluno_responsavel ar " +
                        "JOIN usuarios u ON u.id = ar.id_aluno " +
                        "WHERE ar.id_responsavel = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idResponsavel);
                ResultSet rs = stmt.executeQuery();

                List<Filho> filhos = new ArrayList<>();
                while (rs.next()) {
                    filhos.add(new Filho(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("turma"),
                            rs.getDouble("saldo"),
                            rs.getDouble("limite_fiado"),
                            15.00 // TODO: buscar de uma futura coluna/tabela de preferências do responsável
                    ));
                }
                postSuccess(callback, filhos);
            } catch (Exception e) {
                postError(callback, e);
            }
        });
    }

    @Override
    public void listarExtrato(int idAluno, int mes, int ano, RepositoryCallback<List<ExtratoItem>> callback) {
        executor.execute(() -> {
            try (Connection conn = abrirConexao()) {
                String sql =
                        "SELECT codigo_retirada AS descricao, valor_total, criado_em AS data, 'DEBITO' AS tipo " +
                        "FROM pedidos WHERE id_aluno = ? AND MONTH(criado_em) = ? AND YEAR(criado_em) = ? " +
                        "UNION ALL " +
                        "SELECT 'Compra no balcão', valor_total, data_venda, 'DEBITO' " +
                        "FROM vendas_balcao WHERE id_aluno = ? AND MONTH(data_venda) = ? AND YEAR(data_venda) = ? " +
                        "ORDER BY data DESC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idAluno); stmt.setInt(2, mes); stmt.setInt(3, ano);
                stmt.setInt(4, idAluno); stmt.setInt(5, mes); stmt.setInt(6, ano);
                ResultSet rs = stmt.executeQuery();

                SimpleDateFormat formatoExibicao = new SimpleDateFormat("dd MMM • HH:mm", new Locale("pt", "BR"));
                List<ExtratoItem> itens = new ArrayList<>();
                while (rs.next()) {
                    String dataFormatada = formatoExibicao.format(rs.getTimestamp("data"));
                    ExtratoItem.Tipo tipo = "CREDITO".equals(rs.getString("tipo"))
                            ? ExtratoItem.Tipo.CREDITO : ExtratoItem.Tipo.DEBITO;
                    itens.add(new ExtratoItem(
                            rs.getString("descricao"),
                            dataFormatada,
                            rs.getDouble("valor_total"),
                            tipo
                    ));
                }
                postSuccess(callback, itens);
            } catch (Exception e) {
                postError(callback, e);
            }
        });
    }

    @Override
    public void listarCategorias(int idAluno, int mes, int ano, RepositoryCallback<List<Categoria>> callback) {
        executor.execute(() -> {
            try (Connection conn = abrirConexao()) {
                String sql =
                        "SELECT p.categoria, COUNT(*) AS qtd_itens, " +
                        "SUM(ip.quantidade * ip.preco_unitario) AS total " +
                        "FROM itens_pedido ip " +
                        "JOIN pedidos pd ON pd.id = ip.id_pedido " +
                        "JOIN produtos p ON p.id = ip.id_produto " +
                        "WHERE pd.id_aluno = ? AND MONTH(pd.data_retirada) = ? AND YEAR(pd.data_retirada) = ? " +
                        "GROUP BY p.categoria";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idAluno); stmt.setInt(2, mes); stmt.setInt(3, ano);
                ResultSet rs = stmt.executeQuery();

                List<Categoria> categorias = new ArrayList<>();
                while (rs.next()) {
                    categorias.add(new Categoria(
                            rs.getString("categoria"),
                            rs.getInt("qtd_itens"),
                            rs.getDouble("total")
                    ));
                }
                postSuccess(callback, categorias);
            } catch (Exception e) {
                postError(callback, e);
            }
        });
    }

    @Override
    public void adicionarCredito(int idAluno, double valor, RepositoryCallback<Void> callback) {
        executor.execute(() -> {
            try (Connection conn = abrirConexao()) {
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE usuarios SET saldo = saldo + ? WHERE id = ?");
                stmt.setDouble(1, valor);
                stmt.setInt(2, idAluno);
                stmt.executeUpdate();
                postSuccess(callback, null);
            } catch (Exception e) {
                postError(callback, e);
            }
        });
    }

    @Override
    public void pagarConta(int idAluno, double valor, RepositoryCallback<Void> callback) {
        executor.execute(() -> {
            try (Connection conn = abrirConexao()) {
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE usuarios SET saldo = saldo - ? WHERE id = ?");
                stmt.setDouble(1, valor);
                stmt.setInt(2, idAluno);
                stmt.executeUpdate();
                postSuccess(callback, null);
            } catch (Exception e) {
                postError(callback, e);
            }
        });
    }

    @Override
    public void vincularFilho(int idResponsavel, String nomeFilho, String cpfFilho, String senhaEscola,
                               RepositoryCallback<Boolean> callback) {
        executor.execute(() -> {
            try (Connection conn = abrirConexao()) {
                PreparedStatement busca = conn.prepareStatement(
                        "SELECT id FROM usuarios WHERE nome = ? AND cpf = ? AND senha = ? AND status_perfil = 2");
                busca.setString(1, nomeFilho);
                busca.setString(2, cpfFilho);
                busca.setString(3, senhaEscola); // TODO: comparar hash, nunca senha em texto puro em produção
                ResultSet rs = busca.executeQuery();

                if (rs.next()) {
                    int idAluno = rs.getInt("id");
                    PreparedStatement vincula = conn.prepareStatement(
                            "INSERT INTO aluno_responsavel (id_responsavel, id_aluno) VALUES (?, ?)");
                    vincula.setInt(1, idResponsavel);
                    vincula.setInt(2, idAluno);
                    vincula.executeUpdate();
                    postSuccess(callback, true);
                } else {
                    postSuccess(callback, false);
                }
            } catch (Exception e) {
                postError(callback, e);
            }
        });
    }

    @Override
    public void definirLimite(int idAluno, double limiteFiado, Double limiteDiario, RepositoryCallback<Void> callback) {
        executor.execute(() -> {
            try (Connection conn = abrirConexao()) {
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE usuarios SET limite_fiado = ?, limite_diario = ? WHERE id = ?");
                stmt.setDouble(1, limiteFiado);
                if (limiteDiario != null) stmt.setDouble(2, limiteDiario); else stmt.setNull(2, java.sql.Types.DECIMAL);
                stmt.setInt(3, idAluno);
                stmt.executeUpdate();
                postSuccess(callback, null);
            } catch (Exception e) {
                postError(callback, e);
            }
        });
    }

    private <T> void postSuccess(RepositoryCallback<T> callback, T resultado) {
        mainHandler.post(() -> callback.onSuccess(resultado));
    }

    private void postError(RepositoryCallback<?> callback, Exception e) {
        mainHandler.post(() -> callback.onError(e));
    }
}
