package com.escola.cantina.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.escola.cantina.R;
import com.escola.cantina.adapter.CategoriaAdapter;
import com.escola.cantina.data.CantinaRepository;
import com.escola.cantina.data.CantinaRepositoryJdbcImpl;
import com.escola.cantina.data.RepositoryCallback;
import com.escola.cantina.model.Categoria;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MonthlyStatementActivity extends AppCompatActivity {

    private final CantinaRepository repository = new CantinaRepositoryJdbcImpl();
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private int idAluno;
    private int mesReferencia; // mês FECHADO (mês anterior ao atual)
    private int anoReferencia;

    private RecyclerView recyclerViewCategorias;
    private CategoriaAdapter categoriaAdapter;
    private final List<Categoria> listaCategorias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_statement);

        idAluno = getIntent().getIntExtra(ChildDetailActivity.EXTRA_ID_ALUNO, -1);

        // O consolidado é sempre do mês anterior ao atual (fecha no dia 1º)
        Calendar mesAnterior = Calendar.getInstance();
        mesAnterior.add(Calendar.MONTH, -1);
        mesReferencia = mesAnterior.get(Calendar.MONTH) + 1;
        anoReferencia = mesAnterior.get(Calendar.YEAR);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        configurarRecyclerView();
        configurarBotaoPagar();
        carregarCategorias();
    }

    private void configurarRecyclerView() {
        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias);
        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this));
        categoriaAdapter = new CategoriaAdapter(listaCategorias);
        recyclerViewCategorias.setAdapter(categoriaAdapter);
    }

    private void carregarCategorias() {
        repository.listarCategorias(idAluno, mesReferencia, anoReferencia,
                new RepositoryCallback<List<Categoria>>() {
            @Override
            public void onSuccess(List<Categoria> resultado) {
                listaCategorias.clear();
                listaCategorias.addAll(resultado);
                categoriaAdapter.notifyDataSetChanged();

                double total = 0;
                for (Categoria categoria : resultado) total += categoria.getValorTotal();

                TextView tvTotalMes = findViewById(R.id.tvTotalMes);
                tvTotalMes.setText(moeda.format(total));
            }

            @Override
            public void onError(Exception erro) {
                erro.printStackTrace();
            }
        });
    }

    private void configurarBotaoPagar() {
        findViewById(R.id.btnPagarContaMes).setOnClickListener(v -> {
            Intent intent = new Intent(this, MovimentarSaldoActivity.class);
            intent.putExtra(ChildDetailActivity.EXTRA_ID_ALUNO, idAluno);
            intent.putExtra("EXTRA_MODO", "PAGAR");
            startActivity(intent);
        });
    }
}
