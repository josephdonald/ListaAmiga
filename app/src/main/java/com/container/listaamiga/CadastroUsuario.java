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
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroUsuario extends AppCompatActivity {

    private TextView txtNomeCad, txtEmailCad, txtSenhaCad, txtConfirmaSenhaCad;
    private Button btnCadastrar;
    private Usuario usuario = new Usuario();
    private DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth autenticacaoContaUsuario = FirebaseAuth.getInstance();
    private String emailCadastrado, senhaCadastrada, confirmSenhaCadastrada;
    private FirebaseUser contaUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        txtNomeCad = findViewById(R.id.edt_nome_cad);
        txtEmailCad = findViewById(R.id.edt_email_cad);
        txtSenhaCad = findViewById(R.id.edt_senha_cad);
        txtConfirmaSenhaCad = findViewById(R.id.edt_confirm_senha_cad);
        btnCadastrar = findViewById(R.id.btn_cadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Usuario usuario = new Usuario();

                usuario.setNome(txtNomeCad.getText().toString());
                usuario.setEmail(txtEmailCad.getText().toString());
                usuario.setSenha(txtEmailCad.getText().toString());


                emailCadastrado = usuario.getEmail();
                senhaCadastrada = usuario.getSenha();
                confirmSenhaCadastrada = txtConfirmaSenhaCad.getText().toString();

                emailCadastrado = "applistaamiga@gmail.com";
                senhaCadastrada = "123456";
                confirmSenhaCadastrada = "123456";

                if ( verificaDados(emailCadastrado, senhaCadastrada, confirmSenhaCadastrada) ) {

                    cadastrarConta(usuario.getEmail(), usuario.getSenha());

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
                    usuario.setEmail( emailRecebido );
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


    /**
     * CADASTRA CONTA DE USUÁRIO
     * **/
    private void cadastrarConta(String email, String senha){

        final String emailRecebido = email;
        final String senhaRecebida = senha;

        Log.i("verificaDados", "Usuario: " + emailRecebido + " - Senha: " + senhaRecebida);

        try{

            autenticacaoContaUsuario.createUserWithEmailAndPassword(emailRecebido, senhaRecebida)
                    .addOnCompleteListener(CadastroUsuario.this, new OnCompleteListener<AuthResult>() {
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

                        FirebaseUser usuarioFirebase = task.getResult().getUser();

                        usuario.setIdUsuario( usuarioFirebase.getUid() );

                        //METODO PARA SALVAR OS DADOS DO USUARIO INCLUINDO O ID COMO "CHILD" NO FIREBASE
                        usuario.salvarUsuario();

                        contaUsuario = autenticacaoContaUsuario.getCurrentUser();

                        //APÓS O CADASTRO RETORNA PARA A PÁGINA DE LOGIN
                        Intent intentTelaPrincipal = new Intent(CadastroUsuario.this, Login.class);
                        startActivity(intentTelaPrincipal);

                        Toast.makeText(CadastroUsuario.this, "Usuário " + emailRecebido + " cadastrado com sucesso.", Toast.LENGTH_SHORT).show();

                        enviarVerificacaoEmail( contaUsuario );

                    }

                }
            });

        } catch (Exception e){

            Toast.makeText(this, "Por favor, verifique se há campos em branco.", Toast.LENGTH_SHORT).show();
            Log.i("verificaDados", "Erro: " + e.getMessage());

        }

    }

    private void enviarVerificacaoEmail(FirebaseUser contaUsuarioExterno){

        FirebaseUser contaUsuarioInterno = contaUsuarioExterno;

        Log.i("testeEmail", "E-mail de verificação enviado." );

        try{

            contaUsuarioInterno.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Log.i("testeEmail", "E-mail de verificação enviado. DENTRO DA TASK" );
                    Toast.makeText(CadastroUsuario.this, "E-mail de verificação enviado.", Toast.LENGTH_LONG).show();

                }
            });

        } catch (Exception e){

            Toast.makeText(this, "Erro ao enviar o e-mail de verificação.", Toast.LENGTH_SHORT).show();
            Log.i("testeEmail", "Erro ao enviar o e-mail de verificação: " + e.getMessage() );

        }

    }

}


/** AUTENTICAÇÃO POR E-MAIL (SEM SENHA) **/

//try{
//
//        FirebaseAuth autenticacao = FirebaseAuth.getInstance();
//        FirebaseUser usuario = autenticacao.getCurrentUser();
////            String url = "http://www.example.com/verify?uid=" + usuario.getUid();
//
//        //ENVIAR E-MAIL DE AUTENTICACAO
//        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
//        .setUrl( "https://www.example.com/finishSignUp?cartId=1234" )
//        .setHandleCodeInApp(true)
//        .setAndroidPackageName("com.container.listaamiga", true, "12")
//        .build();
//
//        //ENVIO DO LINK
//        autenticacao.sendSignInLinkToEmail(emailRecebido, actionCodeSettings).addOnCompleteListener(new OnCompleteListener<Void>() {
//@Override
//public void onComplete(@NonNull Task<Void> task) {
//
//        if(task.isSuccessful()){
//
//        Toast.makeText(CadastroUsuario.this, "E-mail para ativação da conta enviado para " + emailRecebido, Toast.LENGTH_LONG).show();
//        Log.i("testeEmail", "E-mail enviado!");
//
//        } else {
//
//        Log.i("testeEmail", "Falha no envio do e-mail." + task.getException() );
//
//        }
//
//        }
//        });
//
//
//        } catch (Exception e){
//
//        Toast.makeText(CadastroUsuario.this, "Erro na autenticação " + e.getMessage(), Toast.LENGTH_LONG).show();
//        Log.i("testeEmail", "Erro na autenticação " + e.getMessage());
//
//        }
