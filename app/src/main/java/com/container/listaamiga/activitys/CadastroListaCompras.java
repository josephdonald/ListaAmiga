package com.container.listaamiga.activitys;

import android.app.DatePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.container.listaamiga.Classes.ListasCompras;
import com.container.listaamiga.R;
import com.container.listaamiga.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.Date;

public class CadastroListaCompras extends AppCompatActivity {

    private EditText edtNomeLista;
    private EditText edtDataLista;
    private Switch swtValorLimite;
    private EditText edtValorLimite;
    private Button btnCadastraLista;

    private Calendar calendario;


    /** Variaveis FIREBASE **/
    private DatabaseReference databaseReference;
    private FirebaseAuth contaUsuario = FirebaseAuth.getInstance();

//    private DatePickerDialog datePickerLista;

    private String dataLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_lista_compras);

        edtNomeLista = findViewById(R.id.edt_nome_lista_cad);
        edtDataLista = findViewById(R.id.edt_data_lista_cad);
        swtValorLimite = findViewById(R.id.swt_valor_limite_cad);
        edtValorLimite = findViewById(R.id.edt_valor_limite_cad);
        btnCadastraLista = findViewById(R.id.btn_cadastrar_lista);

        final ListasCompras listasCompras = new ListasCompras();

        edtDataLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendario = Calendar.getInstance();

                int ano = calendario.get(Calendar.YEAR);
                int mes = calendario.get(Calendar.MONTH);
                int dia = calendario.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int anoEscolhido, int mesEscolhido, int diaEscolhido) {

                        mesEscolhido ++;

                        edtDataLista.setText(diaEscolhido + "/" + mesEscolhido + "/" + anoEscolhido);
//                        editDataInicial.setText( anoEscolhido + "/" + (mesEscolhido + 1) +"/"+ diaEscolhido);

                        calendario.set(anoEscolhido, mesEscolhido, diaEscolhido);
//                        dataInicial = editDataInicial.getText().toString();
//                        listasCompras.setDataCompra( String.valueOf(anoEscolhido +"-"+ mesEscolhido +"-"+ diaEscolhido) );
                        dataLista = anoEscolhido +"-"+ mesEscolhido +"-"+ diaEscolhido;

                        Log.i("testeData", "Data: " + listasCompras.getDataCompra());

                    }
                }, ano, mes, dia);

                datePickerDialog.setTitle("Escolha a data: ");
                datePickerDialog.show();

            }
        });

        /** BOTAO PARA CADASTRAR A LISTA **/
        btnCadastraLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                listasCompras.setNomeLista( edtNomeLista.getText().toString() );
                listasCompras.setQuantItensSelecionado(0);
                listasCompras.setQuantItensTotal(0);
                listasCompras.setPrecoTotalLista(0);
                listasCompras.setDataCompra(dataLista);
                listasCompras.setFinalizada( false );

                if (swtValorLimite.isChecked()){
                    listasCompras.setLimiteValorTotal(Float.parseFloat(edtValorLimite.getText().toString()) );
//                    Log.i("testeCadastro", "Nome: " + listasCompras.getNomeLista() + " - " + "Data: " + listasCompras.getDataCompra() + " - " + "Valor limite: " + listasCompras.getLimiteValorTotal());
                } else {
                    listasCompras.setLimiteValorTotal(Float.parseFloat("0" ) );
//                    Log.i("testeCadastro", "Nome: " + listasCompras.getNomeLista() + " - " + "Data: " + listasCompras.getDataCompra() + " - " + "Valor limite: " + listasCompras.getLimiteValorTotal());
                }

                cadastraLista( listasCompras );

            }
        });

    }


    /** RECEBE OS DADOS CADASTRADOS E INSERE NO FIREBASE **/
    private void cadastraLista(ListasCompras listasComprasRecebida){

        databaseReference = ConfiguracaoFirebase.obterFirebase().child("usuarios").child(contaUsuario.getUid()).child("listasCadastradas").push();

        listasComprasRecebida.setIdLista( databaseReference.getKey() );

        try{
            databaseReference.child("idLista").setValue( listasComprasRecebida.getIdLista() );
            databaseReference.child("nome").setValue( listasComprasRecebida.getNomeLista() );
            databaseReference.child("quantItensSelecionado").setValue( listasComprasRecebida.getQuantItensSelecionado() );
            databaseReference.child("quantItensTotal").setValue( listasComprasRecebida.getQuantItensTotal() );
            databaseReference.child("precoTotal").setValue( listasComprasRecebida.getPrecoTotalLista() );
            databaseReference.child("dataCompra").setValue( listasComprasRecebida.getDataCompra() );
            databaseReference.child("limiteValorTotal").setValue( listasComprasRecebida.getLimiteValorTotal() );

        } catch (Exception e){
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
