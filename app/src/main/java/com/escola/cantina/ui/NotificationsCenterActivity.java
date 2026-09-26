package com.escola.cantina.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.escola.cantina.R;
import com.escola.cantina.adapter.AvisoAnteriorAdapter;
import com.escola.cantina.adapter.AvisoHojeAdapter;
import com.escola.cantina.model.Aviso;
import com.escola.cantina.model.Filho;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Nesta tela os avisos ainda não vêm de uma tabela própria (ver README, item 7):
 * eles são MONTADOS EM MEMÓRIA comparando a lista de filhos (saldo x aviso
 * configurado, uso x limite_fiado) com os consolidados pendentes.
 * Quando a tabela "avisos" sugerida no README existir, troque
 * gerarAvisosDeExemplo()/gerarAnterioresDeExemplo() por chamadas reais ao
 * repositório (ex.: repository.listarAvisos(idResponsavel, callback)).
 */
public class NotificationsCenterActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAvisosHoje;
    private RecyclerView recyclerViewAvisosAnteriores;

    private AvisoHojeAdapter avisoHojeAdapter;
    private AvisoAnteriorAdapter avisoAnteriorAdapter;

    // Lista "mestre" (sem filtro) e a lista exibida (filtrada pelo Chip selecionado)
    private final List<Aviso> avisosHojeCompleto = new ArrayList<>();
    private final List<Aviso> avisosHojeFiltrados = new ArrayList<>();
    private final List<Aviso> avisosAnteriores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_center);

        configurarRecyclerViews();
        configurarFiltros();
        configurarMarcarLidos();
        configurarBottomNav();
        carregarAvisos();
    }

    private void configurarRecyclerViews() {
        recyclerViewAvisosHoje = findViewById(R.id.recyclerViewAvisosHoje);
        recyclerViewAvisosHoje.setLayoutManager(new LinearLayoutManager(this));
        avisoHojeAdapter = new AvisoHojeAdapter(avisosHojeFiltrados, aviso -> abrirDetalheDoAviso(aviso));
        recyclerViewAvisosHoje.setAdapter(avisoHojeAdapter);

        recyclerViewAvisosAnteriores = findViewById(R.id.recyclerViewAvisosAnteriores);
        recyclerViewAvisosAnteriores.setLayoutManager(new LinearLayoutManager(this));
        avisoAnteriorAdapter = new AvisoAnteriorAdapter(avisosAnteriores, aviso -> abrirDetalheDoAviso(aviso));
        recyclerViewAvisosAnteriores.setAdapter(avisoAnteriorAdapter);
    }

    private void abrirDetalheDoAviso(Aviso aviso) {
        if (aviso.getIdFilhoRelacionado() == null) return;
        Intent intent;
        if (aviso.getTipo() == Aviso.Tipo.CONTA) {
            intent = new Intent(this, MonthlyStatementActivity.class);
        } else {
            intent = new Intent(this, ChildDetailActivity.class);
        }
        intent.putExtra(ChildDetailActivity.EXTRA_ID_ALUNO, aviso.getIdFilhoRelacionado());
        startActivity(intent);
    }

    private void carregarAvisos() {
        // TODO: substituir pelas chamadas reais:
        // repository.listarFilhos(idResponsavel, ...) e, a partir do saldo/uso de cada
        // Filho, gerar os Avisos correspondentes (SALDO_BAIXO, LIMITE, CONTA).
        avisosHojeCompleto.clear();
        avisosHojeCompleto.add(new Aviso(1, Aviso.Tipo.SALDO_BAIXO,
                "Saldo baixo • Júlia",
                "O saldo chegou a R$ 9,20, abaixo do aviso de R$ 15,00.", false, 2));
        avisosHojeCompleto.add(new Aviso(2, Aviso.Tipo.LIMITE,
                "Limite próximo • Lucas",
                "83% do limite mensal de R$ 250,00 já foi utilizado.", false, 1));
        avisosHojeCompleto.add(new Aviso(3, Aviso.Tipo.CONTA,
                "Conta mensal disponível",
                "O consolidado de agosto foi fechado em R$ 326,80.", false, 1));

        avisosAnteriores.clear();
        avisosAnteriores.add(new Aviso(4, Aviso.Tipo.CONTA,
                "Crédito confirmado",
                "R$ 80,00 adicionados para Lucas • 28 ago", true, 1));
        avisosAnteriores.add(new Aviso(5, Aviso.Tipo.LIMITE,
                "Limite atualizado",
                "Júlia: R$ 180,00 por mês • 20 ago", true, 2));
        avisosAnteriores.add(new Aviso(6, Aviso.Tipo.CONTA,
                "Aluno vinculado",
                "Lucas Mendes • 04 fev", true, 1));

        aplicarFiltro(null); // "Todos" por padrão
        avisoAnteriorAdapter.notifyDataSetChanged();

        TextView tvQtdAvisos = findViewById(R.id.tvQtdAvisos);
        TextView tvBadgeNovos = findViewById(R.id.tvBadgeNovos);
        tvQtdAvisos.setText(avisosHojeCompleto.size() + " avisos para você");
        tvBadgeNovos.setText(avisosHojeCompleto.size() + " NOVOS");
    }

    private void configurarFiltros() {
        ChipGroup chipGroup = findViewById(R.id.cgFiltros);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int id = checkedIds.get(0);

            if (id == R.id.chipTodos) {
                aplicarFiltro(null);
            } else if (id == R.id.chipSaldo) {
                aplicarFiltro(Aviso.Tipo.SALDO_BAIXO);
            } else if (id == R.id.chipLimite) {
                aplicarFiltro(Aviso.Tipo.LIMITE);
            } else if (id == R.id.chipConta) {
                aplicarFiltro(Aviso.Tipo.CONTA);
            }
        });
    }

    /** Filtra a lista "Hoje" pelo tipo escolhido no Chip (null = sem filtro / "Todos"). */
    private void aplicarFiltro(Aviso.Tipo tipoFiltro) {
        avisosHojeFiltrados.clear();
        for (Aviso aviso : avisosHojeCompleto) {
            if (tipoFiltro == null || aviso.getTipo() == tipoFiltro) {
                avisosHojeFiltrados.add(aviso);
            }
        }
        avisoHojeAdapter.notifyDataSetChanged();
    }

    private void configurarMarcarLidos() {
        findViewById(R.id.tvMarcarLidos).setOnClickListener(v -> {
            // TODO: persistir "lido = true" na futura tabela "avisos" (UPDATE avisos SET lido = TRUE ...)
            avisosHojeCompleto.clear();
            avisosHojeFiltrados.clear();
            avisoHojeAdapter.notifyDataSetChanged();

            TextView tvQtdAvisos = findViewById(R.id.tvQtdAvisos);
            TextView tvBadgeNovos = findViewById(R.id.tvBadgeNovos);
            tvQtdAvisos.setText("0 avisos para você");
            tvBadgeNovos.setText("0 NOVOS");
        });
    }

    private void configurarBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.nav_avisos);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_familia) {
                startActivity(new Intent(this, FamilyHomeActivity.class));
                return true;
            } else if (id == R.id.nav_avisos) {
                return true; // já estamos aqui
            }
            return true;
        });
    }
}
