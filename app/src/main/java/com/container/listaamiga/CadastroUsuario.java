package com.container.listaamiga;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.container.listaamiga.Classes.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroUsuario extends AppCompatActivity {

    private TextView txtEmailCad, txtSenhaCad, txtConfirmaSenhaCad;
    private Button btnCadastrar;
    private Usuario usuario = new Usuario();
    private DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth criarContaUsuario = FirebaseAuth.getInstance();
    private String emailCadastrado, senhaCadastrada, confirmSenhaCadastrada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        txtEmailCad = findViewById(R.id.edt_email_cad);
        txtSenhaCad = findViewById(R.id.edt_senha_cad);
        txtConfirmaSenhaCad = findViewById(R.id.edt_confirm_senha_cad);
        btnCadastrar = findViewById(R.id.btn_cadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                emailCadastrado = txtEmailCad.getText().toString();
//                senhaCadastrada = txtSenhaCad.getText().toString();
//                confirmSenhaCadastrada = txtConfirmaSenhaCad.getText().toString();

                emailCadastrado = "teste@teste.com";
                senhaCadastrada = "123456";
                confirmSenhaCadastrada = "123456";

                if ( verificaDados(emailCadastrado, senhaCadastrada, confirmSenhaCadastrada) ) {

                    cadastrarConta(usuario.getUsuario(), usuario.getSenha());

                } else {
                    Log.i("verificaDados", "ERRO!!");
                }

            }
        });

    }

    /**
     * MÉTODO QUE VERIFICA OS DADOS INSERIDOS
     * **/
    public Boolean verificaDados(String email, String senha, String confirmacaoDeSenha){

        String emailRecebido = email;
        String senhaRecebida = senha;
        String confirmaSenhaRecebida = confirmacaoDeSenha;

        Boolean dadosOk = false;

        try{

            if ( ( ( senhaRecebida.isEmpty() ) || ( confirmaSenhaRecebida.isEmpty() ) ) ){

                Toast.makeText(this, "Insira todos os dados", Toast.LENGTH_SHORT).show();

            } else {

                if ( senhaRecebida.equals( confirmaSenhaRecebida ) ){

                    dadosOk = true;
                    usuario.setUsuario( emailRecebido );
                    usuario.setSenha( senhaRecebida );

                } else{

                    Toast.makeText(this, "As senha não conferem. Por favor, insira novamente.", Toast.LENGTH_LONG).show();
                    txtSenhaCad.requestFocus();

                }

            }

        }catch (Exception e){

                Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("verificaDados", "Erro: " + e.getMessage() );

        }

        return dadosOk;

    }

//    private Boolean verificarEmail(String email){
//
//        Boolean emailOk = false;
//
//        String emailRecebido = email;
//
////        if ( emailRecebido.indexOf('@') > 0){
//
//            emailOk = true;
//            usuario.setUsuario( emailRecebido );
//
////        }   else {
////            Toast.makeText(this, "E-mail inválido. Verifique o endereço informado.", Toast.LENGTH_LONG).show();
////        }
//
//        return emailOk;
//
//    }

    private Boolean verificaSenha(String senha, String confirmSenha){

        Boolean senhaOk = false;

        String senhaRecebida = senha;
        String confirmSenhaRecebida = confirmSenha;

        if ( senhaRecebida.equals( confirmSenhaRecebida ) ){

            senhaOk = true;
            usuario.setSenha( senhaRecebida );

        } else{

            Toast.makeText(this, "As senha não conferem. Por favor, insira novamente.", Toast.LENGTH_LONG).show();

        }

        return senhaOk;

    }

    /**
     * CADASTRA CONTA DE USUÁRIO
     * **/
    private void cadastrarConta(String email, String senha){

        final String emailRecebido = email;
        final String senhaRecebida = senha;

        Log.i("verificaDados", "Usuario: " + emailRecebido + " - Senha: " + senhaRecebida);

        try{

            criarContaUsuario.createUserWithEmailAndPassword(emailRecebido, senhaRecebida).addOnCompleteListener(CadastroUsuario.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if ( !task.isSuccessful() ){

                        try {
                            throw task.getException();
                        } catch(FirebaseAuthWeakPasswordException e) {
                            Toast.makeText(CadastroUsuario.this, "A senha inserida é muito curta. O mínimo são 6 caracteres.", Toast.LENGTH_SHORT).show();
                            txtSenhaCad.requestFocus();
                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(CadastroUsuario.this, "O e-mail digitado é inválido.", Toast.LENGTH_SHORT).show();
                            txtEmailCad.requestFocus();
                        } catch(FirebaseAuthUserCollisionException e) {
                            Toast.makeText(CadastroUsuario.this, "O e-mail inserido já está cadastrado.", Toast.LENGTH_SHORT).show();
                            txtEmailCad.requestFocus();
                        }catch (FirebaseException e){
                            Toast.makeText(CadastroUsuario.this, "O e-mail inserido não pode ser vazio", Toast.LENGTH_SHORT).show();
                            txtEmailCad.requestFocus();
                        }catch(Exception e) {
                            Log.i("ErroCad", "Erro TRY:" + e.getMessage());
                        }

                    } else {

//                        Toast.makeText(CadastroUsuario.this, "Erro ao cadastrar o usuário " + usuarioRecebido + ". Erro: " + task.getException(), Toast.LENGTH_SHORT).show();
//                        Log.i("verificaDados", "Erro: " + task.getException() );
                        //APÓS O CADASTRO RETORNA PARA A PÁGINA DE LOGIN
                        Intent intentTelaPrincipal = new Intent(CadastroUsuario.this, Login.class);
                        startActivity(intentTelaPrincipal);

                        Toast.makeText(CadastroUsuario.this, "Usuário " + emailRecebido + " cadastrado com sucesso.", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        } catch (Exception e){

            Toast.makeText(this, "Por favor, verifique se há campos em branco.", Toast.LENGTH_SHORT).show();
            Log.i("verificaDados", "Erro: " + e.getMessage());

        }

    }

}