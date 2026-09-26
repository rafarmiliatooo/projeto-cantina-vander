package com.escola.cantina.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.escola.cantina.R;
import com.escola.cantina.adapter.ExtratoAdapter;
import com.escola.cantina.data.CantinaRepository;
import com.escola.cantina.data.CantinaRepositoryJdbcImpl;
import com.escola.cantina.data.RepositoryCallback;
import com.escola.cantina.model.ExtratoItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ChildDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID_ALUNO = "EXTRA_ID_ALUNO";

    private final CantinaRepository repository = new CantinaRepositoryJdbcImpl();
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private int idAluno;
    private int mesSelecionado;
    private int anoSelecionado;

    private RecyclerView recyclerViewExtrato;
    private ExtratoAdapter extratoAdapter;
    private final List<ExtratoItem> listaExtrato = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_detail);

        idAluno = getIntent().getIntExtra(EXTRA_ID_ALUNO, -1);

        Calendar agora = Calendar.getInstance();
        mesSelecionado = agora.get(Calendar.MONTH) + 1;
        anoSelecionado = agora.get(Calendar.YEAR);

        configurarToolbar();
        configurarRecyclerView();
        configurarBotoesDeAcao();
        configurarBottomNav();
        carregarExtrato();

        // TODO: carregar também os dados do próprio filho (nome, turma, saldo, limite)
        // com repository.listarFilhos(...) filtrando pelo idAluno, ou criando um método
        // repository.buscarFilhoPorId(idAluno, callback) dedicado, e preencher:
        // tvNomeFilho, tvTurmaConta, tvSaldoAtual, tvUsadoNoMes, tvLimiteFilho, progressUsoMensal
    }

    private void configurarToolbar() {
        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        findViewById(R.id.btnSelecionarMes).setOnClickListener(v -> {
            // TODO: abrir um seletor de mês/ano (DatePickerDialog ou um BottomSheet custom),
            // atualizar mesSelecionado/anoSelecionado, atualizar tvMesSelecionado e chamar
            // carregarExtrato() novamente.
        });
    }

    private void configurarRecyclerView() {
        recyclerViewExtrato = findViewById(R.id.recyclerViewExtrato);
        recyclerViewExtrato.setLayoutManager(new LinearLayoutManager(this));
        extratoAdapter = new ExtratoAdapter(listaExtrato);
        recyclerViewExtrato.setAdapter(extratoAdapter);
    }

    private void carregarExtrato() {
        repository.listarExtrato(idAluno, mesSelecionado, anoSelecionado,
                new RepositoryCallback<List<ExtratoItem>>() {
            @Override
            public void onSuccess(List<ExtratoItem> resultado) {
                listaExtrato.clear();
                listaExtrato.addAll(resultado);
                extratoAdapter.notifyDataSetChanged();

                double usadoNoMes = 0;
                for (ExtratoItem item : resultado) {
                    if (item.getTipo() == ExtratoItem.Tipo.DEBITO) usadoNoMes += item.getValor();
                }
                TextView tvUsadoNoMes = findViewById(R.id.tvUsadoNoMes);
                tvUsadoNoMes.setText("Usado no mês: " + moeda.format(usadoNoMes));
            }

            @Override
            public void onError(Exception erro) {
                erro.printStackTrace();
            }
        });
    }

    private void configurarBotoesDeAcao() {
        findViewById(R.id.btnAdicionarCredito).setOnClickListener(v -> {
            Intent intent = new Intent(this, MovimentarSaldoActivity.class);
            intent.putExtra(ChildDetailActivity.EXTRA_ID_ALUNO, idAluno);
            intent.putExtra("EXTRA_MODO", "CREDITO");
            startActivity(intent);
        });

        findViewById(R.id.btnPagarConta).setOnClickListener(v -> {
            Intent intent = new Intent(this, MovimentarSaldoActivity.class);
            intent.putExtra(ChildDetailActivity.EXTRA_ID_ALUNO, idAluno);
            intent.putExtra("EXTRA_MODO", "PAGAR");
            startActivity(intent);
        });

        findViewById(R.id.ivAvatarPerfil).setOnClickListener(v -> {
            // TODO: abrir tela de "Definir limite do filho" (limite_fiado / limite_diario),
            // conforme observação no README (funcionalidade obrigatória do briefing).
        });
    }

    private void configurarBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.nav_familia);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_familia) {
                finish(); // volta para a lista de filhos
                return true;
            } else if (id == R.id.nav_avisos) {
                startActivity(new Intent(this, NotificationsCenterActivity.class));
                return true;
            }
            return true;
        });
    }
}
