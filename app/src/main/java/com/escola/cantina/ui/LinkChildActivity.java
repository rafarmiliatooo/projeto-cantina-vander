package com.escola.cantina.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.escola.cantina.R;
import com.escola.cantina.data.CantinaRepository;
import com.escola.cantina.data.CantinaRepositoryJdbcImpl;
import com.escola.cantina.data.RepositoryCallback;

/** Tela 5: não usa RecyclerView; incluída aqui só para o projeto compilar de ponta a ponta. */
public class LinkChildActivity extends AppCompatActivity {

    private static final int ID_RESPONSAVEL_LOGADO = 1;

    private final CantinaRepository repository = new CantinaRepositoryJdbcImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_child);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
        findViewById(R.id.btnVincularFilho).setOnClickListener(v -> vincular());
    }

    private void vincular() {
        EditText etNome = findViewById(R.id.etNomeFilho);
        EditText etCpf = findViewById(R.id.etCpfFilho);
        EditText etSenha = findViewById(R.id.etSenhaEscola);

        repository.vincularFilho(ID_RESPONSAVEL_LOGADO,
                etNome.getText().toString(),
                etCpf.getText().toString(),
                etSenha.getText().toString(),
                new RepositoryCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean vinculado) {
                        if (vinculado) {
                            Toast.makeText(LinkChildActivity.this, "Filho vinculado com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(LinkChildActivity.this, "Dados não encontrados. Confira nome, CPF e senha.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Exception erro) {
                        erro.printStackTrace();
                        Toast.makeText(LinkChildActivity.this, "Erro ao vincular. Tente novamente.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
