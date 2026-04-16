package com.example.despesaspessoais;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

EditText edtDescricao, edtValor, edtData;
Spinner spCategoria, spformaDePagamento;
RadioGroup rgStatus;
RadioButton rbPaga, rbNaoPaga;
Button btnSalvar;
ListView lvLista;
ArrayList<String> listaDespesa;
ArrayAdapter<String> listaAdapter;

BancoHelper db;

int despesaAtual = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarViews();
        configurarSpinners();
        configurarLista();

        db = new BancoHelper(this);

        btnSalvar.setOnClickListener(v -> salvarOuAtualizar());

        AdapterView<Adapter> lvDespesas = null;
        lvDespesas.setOnItemClickListener((p, v, pos, id) -> preencherCampos(pos));

        lvDespesas.setOnItemLongClickListener((p, v, pos, id) -> {
            removerDespesa(pos);
            return true;
        }
    ;}

    private void removerDespesa(int pos) {
        String item = listaDespesa.get(pos);
        int id = Integer.parseInt(item.split(" - ")[0]);

        db.excluirDespesa(id);
    }

    private void preencherCampos(int pos) {
        String item = listaDespesa.get(pos);
        despesaAtual = Integer.parseInt(item.split(" - ")[0]);

        Cursor cursor = db.listarDespesa();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (cursor.getInt(cursor.getColumnIndexOrThrow("id")) == despesaAtual) {

                    edtDescricao.setText(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                    edtValor.setText(cursor.getString(cursor.getColumnIndexOrThrow("valor")));
                    edtData.setText(cursor.getString(cursor.getColumnIndexOrThrow("data")));

                    spCategoria.setSelection(((ArrayAdapter<String>) spCategoria.getAdapter()).getPosition(categoria));

                    String st = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                    rbNaoPaga.setChecked(st.equals("Não Paga"));
                    rbPaga.setChecked(st.equals("Paga"));

                    break;
                }
            } while (cursor.moveToNext());
        }
    }

    private void salvarOuAtualizar() {
        String descricao = edtDescricao.getText().toString().trim();
        String valor = edtValor.getText().toString().trim();
        String data = edtData.getText().toString().trim();

        if (descricao.isEmpty() || valor.isEmpty() || data.isEmpty()) {
            Toast.makeText(this, "Campos obrigatórios vazios!", Toast.LENGTH_SHORT).show();
            return;
        }

        String categoria = spCategoria.getText().toString();
        String formaDePagamento = spformaDePagamento.getSelectedItem().toString();
        String status = rbNaoPaga.isChecked() ? "Não Paga" : "Paga";

        if (despesaAtual < 0) {
            db.inserirDespesa(descricao, valor, data, categoria, formaDePagamento, status);
        } else {
            db.atualizarDespesa(despesaAtual, descricao, valor, data, categoria, formaDePagamento, status);
        }

        resetarCampos();
    }

    private void resetarCampos() {
        edtDescricao.setText("");
        edtValor.setText("");
        edtData.setText("");

        rbNaoPaga.setChecked(true);
        spCategoria.setSelection(0);
        spformaDePagamento.setSelection(0);

        despesaAtual = -1;
    }

    private void configurarLista() {
        listaDespesa = new ArrayList<>();
        listaAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listaDespesa);
        lvLista.setAdapter(listaAdapter);
    }

    private void configurarSpinners() {
        String[] opCategoria = {"Alimentação", "Transporte", "Laser", "Saúde", "Contas"};
        ArrayAdapter<String> adpCategoria = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, opCategoria);
        adpCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adpCategoria);

        String[] opformaDePagamento = {"Dinheiro", "Pix", "Débito", "Crédito"};
        ArrayAdapter<String> adpformaDePagamento = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, opformaDePagamento);
        adpformaDePagamento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spformaDePagamento.setAdapter(adpformaDePagamento);
    }

    private void inicializarViews() {
        edtDescricao = findViewById(R.id.descricao);
        edtValor = findViewById(R.id.valor);
        edtData = findViewById(R.id.data);

        spCategoria = findViewById(R.id.categoria);
        spformaDePagamento = findViewById(R.id.formaDePagamento);

        rgStatus = findViewById(R.id.statusGroup);
        rbPaga = findViewById(R.id.paga);
        rbNaoPaga = findViewById(R.id.naoPaga);

        btnSalvar = findViewById(R.id.salvar);

        lvLista = findViewById(R.id.lista);
    }
}