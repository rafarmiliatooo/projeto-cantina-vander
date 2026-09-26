package com.escola.cantina.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.escola.cantina.R;
import com.escola.cantina.adapter.FilhoAdapter;
import com.escola.cantina.data.CantinaRepository;
import com.escola.cantina.data.CantinaRepositoryJdbcImpl;
import com.escola.cantina.data.RepositoryCallback;
import com.escola.cantina.model.Filho;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class FamilyHomeActivity extends AppCompatActivity {

    // Troque por SharedPreferences/sessão real após o login do responsável.
    private static final int ID_RESPONSAVEL_LOGADO = 1;

    private final CantinaRepository repository = new CantinaRepositoryJdbcImpl();
    private RecyclerView recyclerViewFilhos;
    private FilhoAdapter filhoAdapter;
    private final List<Filho> listaFilhos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_home);

        configurarRecyclerView();
        configurarCliquesEstaticos();
        configurarBottomNav();
        carregarFilhos();
    }

    private void configurarRecyclerView() {
        recyclerViewFilhos = findViewById(R.id.recyclerViewFilhos);
        recyclerViewFilhos.setLayoutManager(new LinearLayoutManager(this));

        // O clique em um filho abre a Tela 2 (Detalhe do filho) passando o id do aluno
        filhoAdapter = new FilhoAdapter(listaFilhos, filho -> {
            Intent intent = new Intent(FamilyHomeActivity.this, ChildDetailActivity.class);
            intent.putExtra("EXTRA_ID_ALUNO", filho.getId());
            startActivity(intent);
        });
        recyclerViewFilhos.setAdapter(filhoAdapter);
    }

    private void carregarFilhos() {
        repository.listarFilhos(ID_RESPONSAVEL_LOGADO, new RepositoryCallback<List<Filho>>() {
            @Override
            public void onSuccess(List<Filho> resultado) {
                listaFilhos.clear();
                listaFilhos.addAll(resultado);
                filhoAdapter.notifyDataSetChanged();

                // Atualiza cabeçalho e alerta de saldo baixo com dados reais
                TextView tvBadge = findViewById(R.id.tvBadgeQtdFilhos);
                tvBadge.setText(resultado.size() + " FILHOS");
                atualizarAlertaSaldoBaixo(resultado);
            }

            @Override
            public void onError(Exception erro) {
                // TODO: mostrar um Toast/Snackbar amigável ao responsável
                erro.printStackTrace();
            }
        });
    }

    private void atualizarAlertaSaldoBaixo(List<Filho> filhos) {
        View card = findViewById(R.id.cardAlertaSaldo);
        for (Filho filho : filhos) {
            if (filho.isSaldoBaixo()) {
                TextView tvTitulo = findViewById(R.id.tvAlertaSaldoTitulo);
                tvTitulo.setText("Atenção ao saldo de " + filho.getNome().split(" ")[0]);
                card.setVisibility(View.VISIBLE);
                return; // mostra o primeiro encontrado; para vários, use outro RecyclerView
            }
        }
        card.setVisibility(View.GONE);
    }

    private void configurarCliquesEstaticos() {
        findViewById(R.id.btnCadastrarFilho).setOnClickListener(v ->
                startActivity(new Intent(this, LinkChildActivity.class)));

        findViewById(R.id.cardConsolidadoDisponivel).setOnClickListener(v -> {
            // Abre o consolidado do primeiro filho como exemplo; idealmente deixe o
            // usuário escolher, ou mostre um consolidado combinado de todos os filhos.
            if (!listaFilhos.isEmpty()) {
                Intent intent = new Intent(this, MonthlyStatementActivity.class);
                intent.putExtra("EXTRA_ID_ALUNO", listaFilhos.get(0).getId());
                startActivity(intent);
            }
        });

        findViewById(R.id.cardAlertaSaldo).setOnClickListener(v ->
                startActivity(new Intent(this, NotificationsCenterActivity.class)));
    }

    private void configurarBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.nav_familia);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_familia) {
                return true; // já estamos aqui
            } else if (id == R.id.nav_extrato) {
                // TODO: abrir uma tela de extrato consolidado de todos os filhos
                return true;
            } else if (id == R.id.nav_avisos) {
                startActivity(new Intent(this, NotificationsCenterActivity.class));
                return true;
            }
            return false;
        });
    }
}
