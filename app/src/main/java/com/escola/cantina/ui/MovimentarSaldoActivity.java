package com.escola.cantina.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.escola.cantina.R;
import com.escola.cantina.data.CantinaRepository;
import com.escola.cantina.data.CantinaRepositoryJdbcImpl;
import com.escola.cantina.data.RepositoryCallback;

import java.util.Locale;

/** Tela 3: não usa RecyclerView; incluída aqui só para o projeto compilar de ponta a ponta. */
public class MovimentarSaldoActivity extends AppCompatActivity {

    private final CantinaRepository repository = new CantinaRepositoryJdbcImpl();
    private int idAluno;
    private String modo; // "CREDITO" ou "PAGAR"
    private EditText etValorCredito;
    private TextView tvTextoBotaoConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimentar_saldo);

        idAluno = getIntent().getIntExtra(ChildDetailActivity.EXTRA_ID_ALUNO, -1);
        modo = getIntent().getStringExtra("EXTRA_MODO");

        etValorCredito = findViewById(R.id.etValorCredito);
        tvTextoBotaoConfirmar = findViewById(R.id.tvTextoBotaoConfirmar);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        findViewById(R.id.btnValor50).setOnClickListener(v -> etValorCredito.setText("50,00"));
        findViewById(R.id.btnValor100).setOnClickListener(v -> etValorCredito.setText("100,00"));
        findViewById(R.id.btnValor150).setOnClickListener(v -> etValorCredito.setText("150,00"));
        findViewById(R.id.btnValorOutro).setOnClickListener(v -> etValorCredito.setText(""));

        findViewById(R.id.btnConfirmarValor).setOnClickListener(v -> confirmarMovimentacao());
    }

    private void confirmarMovimentacao() {
        double valor;
        try {
            valor = Double.parseDouble(etValorCredito.getText().toString().replace(",", "."));
        } catch (NumberFormatException e) {
            return; // TODO: mostrar erro de validação ao usuário
        }

        RepositoryCallback<Void> callback = new RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void resultado) {
                finish();
            }

            @Override
            public void onError(Exception erro) {
                erro.printStackTrace();
            }
        };

        if ("PAGAR".equals(modo)) {
            repository.pagarConta(idAluno, valor, callback);
        } else {
            repository.adicionarCredito(idAluno, valor, callback);
        }
    }
}
