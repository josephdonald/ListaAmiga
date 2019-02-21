package com.container.listaamiga;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    private TextView textCadastro;
    private EditText edtLogin;
    private EditText edtSenha;
    private Button btnLogar;

    private String login, senha;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLogin = findViewById(R.id.edt_login_tela);
        edtSenha = findViewById(R.id.edt_senha_tela);
        btnLogar = findViewById(R.id.btn_logar_tela);
        textCadastro = findViewById(R.id.txt_cadastro_tela);

        /**
         * BOTAO PARA LOGAR NO APP
         * **/
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    login = edtLogin.getText().toString();
                    senha = edtSenha.getText().toString();

                    Log.i("testeLogin", "Login: " + login + " - Senha: " + senha );

                    loginUsuario(login, senha);

                } catch (Exception e){
                    Log.i("testeLogin", "Erro no login: " + e.getMessage() );
                }

            }
        });

        /**-----------------------------------------------**/

        /**
         * LINK PARA ABRIR TELA DE CADASTRO
         * **/
        textCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentCadastro = new Intent(Login.this, CadastroUsuario.class);

                startActivity( intentCadastro );

            }
        });

    }

    /**
     * MÉTODO PARA LOGIN DO USUÁRIO
     * **/

    private void loginUsuario(String loginExterno, String senhaExterna){

        String loginInterno = loginExterno;
        String senhaInterna = senhaExterna;

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(loginInterno, senhaInterna).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( !task.isSuccessful() ){

                    try {

                        throw task.getException();

                    } catch(FirebaseAuthWeakPasswordException e) {
                        Toast.makeText(Login.this, "A senha inserida é muito curta. O mínimo são 6 caracteres.", Toast.LENGTH_SHORT).show();
                        edtSenha.requestFocus();
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(Login.this, "O e-mail digitado é inválido.", Toast.LENGTH_SHORT).show();
                        edtLogin.requestFocus();
                    } catch(FirebaseAuthUserCollisionException e) {
                        Toast.makeText(Login.this, "O e-mail inserido já está cadastrado.", Toast.LENGTH_SHORT).show();
                        edtLogin.requestFocus();
                    }catch (FirebaseNoSignedInUserException e){
                        Toast.makeText(Login.this, "O e-mail inserido não pode ser vazio", Toast.LENGTH_SHORT).show();
                        edtLogin.requestFocus();
                    }catch(Exception e) {
                        Log.i("testeLogin", "Erro TRY:" + e.getMessage());
                    }

                } else {
                    Toast.makeText(Login.this, "Usuário logado com sucesso.", Toast.LENGTH_SHORT).show();

                    Intent intentLogin = new Intent(Login.this, MainActivity.class);
                    startActivity(intentLogin);
//                    Toast.makeText(Login.this, "Erro no login: " + task.getException(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}
